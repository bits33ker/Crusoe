/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.crusoe.gpsfile;

import android.os.Build;


//import java.io.OutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.crusoe.gpsfile.TrackPoint.TrackSegment;

import android.location.Location;

/**
 * Log of one track.
 *
 * @author Sandor Dornbush
 */
/*
 * <gpx creator="GPS Visualizer http://www.gpsvisualizer.com/" version="1.0">
  <wpt lat="45.44283" lon="-121.72904"><ele>1374</ele><name>Vista Ridge Trailhead</name><sym>Trail Head</sym></wpt>
  <wpt lat="45.41000" lon="-121.71349"><ele>1777</ele><name>Wy'East Basin</name></wpt>
  <wpt lat="45.41124" lon="-121.70404"><ele>1823</ele><name>Dollar Lake</name></wpt>
  <wpt lat="45.39260" lon="-121.69937"><ele>2394</ele><name>Barrett Spur</name><sym>Summit</sym></wpt>
  <trk>
    <name>Barrett Spur 1</name>
    <trkseg>
      <trkpt lat="45.4431641" lon="-121.7295456"></trkpt>
      <trkpt lat="45.4428615" lon="-121.7290800"></trkpt>
      <trkpt lat="45.4425697" lon="-121.7279085"></trkpt>
      <trkpt lat="45.4424274" lon="-121.7267360"></trkpt>
      <trkpt lat="45.4422017" lon="-121.7260429"></trkpt>
      <trkpt lat="45.4416576" lon="-121.7252347"></trkpt>
      <trkpt lat="45.4406144" lon="-121.7241181"></trkpt>
      <trkpt lat="45.4398193" lon="-121.7224890"></trkpt>
      <trkpt lat="45.4387649" lon="-121.7226112"></trkpt>
      <trkpt lat="45.4383933" lon="-121.7224328"></trkpt>
      <trkpt lat="45.4377850" lon="-121.7224159"></trkpt>
      <trkpt lat="45.4372204" lon="-121.7226603"></trkpt>
    </trkseg>
    <trkseg>
      <trkpt lat="45.4099792" lon="-121.7134610"></trkpt>
      <trkpt lat="45.4091489" lon="-121.7134937"></trkpt>
      <trkpt lat="45.4086133" lon="-121.7132504"></trkpt>
      <trkpt lat="45.4080616" lon="-121.7127670"></trkpt>
      <trkpt lat="45.4076426" lon="-121.7126047"></trkpt>
      <trkpt lat="45.4075043" lon="-121.7122301"></trkpt>
      <trkpt lat="45.4070652" lon="-121.7118980"></trkpt>
      <trkpt lat="45.4068712" lon="-121.7114766"></trkpt>
      <trkpt lat="45.4067987" lon="-121.7108634"></trkpt>
      <trkpt lat="45.4064528" lon="-121.7106934"></trkpt>
      <trkpt lat="45.4057286" lon="-121.7110326"></trkpt>
      <trkpt lat="45.4056813" lon="-121.7108280"></trkpt>
    </trkseg>
  </trk>
  <trk>
    <name>Barrett Spur 2</name>
    <trkseg>
      <trkpt lat="45.3928201" lon="-121.6995658"></trkpt>
      <trkpt lat="45.3935449" lon="-121.6998805"></trkpt>
      <trkpt lat="45.3937897" lon="-121.6997710"></trkpt>
      <trkpt lat="45.3941789" lon="-121.6999492"></trkpt>
      <trkpt lat="45.3942372" lon="-121.7001375"></trkpt>
      <trkpt lat="45.3946353" lon="-121.6999452"></trkpt>
      <trkpt lat="45.3953599" lon="-121.7005823"></trkpt>
      <trkpt lat="45.3957081" lon="-121.7006533"></trkpt>
      <trkpt lat="45.3964324" lon="-121.7016813"></trkpt>
    </trkseg>
    <trkseg>
      <trkpt lat="45.4055556" lon="-121.7058619"></trkpt>
      <trkpt lat="45.4057016" lon="-121.7055424"></trkpt>
      <trkpt lat="45.4064672" lon="-121.7058247"></trkpt>
      <trkpt lat="45.4065550" lon="-121.7056490"></trkpt>
      <trkpt lat="45.4081392" lon="-121.7055042"></trkpt>
      <trkpt lat="45.4084785" lon="-121.7052201"></trkpt>
      <trkpt lat="45.4089125" lon="-121.7053029"></trkpt>
      <trkpt lat="45.4097597" lon="-121.7050730"></trkpt>
      <trkpt lat="45.4098359" lon="-121.7049047"></trkpt>
      <trkpt lat="45.4101859" lon="-121.7049419"></trkpt>
    </trkseg>
   </trk>
</gpx>
*/
public class GpxTrackWriter implements TrackFormatWriter {
  private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

  private final NumberFormat elevationFormatter;
  private final NumberFormat coordinateFormatter;
  private final SimpleDateFormat timestampFormatter;
  private PrintWriter pw = null;
  //private Track track;

  public GpxTrackWriter(File f) throws FileNotFoundException {
	  
    // GPX readers expect to see fractional numbers with US-style punctuation.
    // That is, they want periods for decimal points, rather than commas.
	  //try{
	    pw = new PrintWriter(f);
	  //}catch(FileNotFoundException e)
	  //{
	//	    pw = null;
	  //}
    elevationFormatter = NumberFormat.getInstance(Locale.US);
    elevationFormatter.setMaximumFractionDigits(1);
    elevationFormatter.setGroupingUsed(false);

    coordinateFormatter = NumberFormat.getInstance(Locale.US);
    coordinateFormatter.setMaximumFractionDigits(5);
    coordinateFormatter.setMaximumIntegerDigits(3);
    coordinateFormatter.setGroupingUsed(false);

    timestampFormatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
    timestampFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	  
  }

  private String formatLocation(Location l) {
    return "lat=\"" + coordinateFormatter.format(l.getLatitude())
      + "\" lon=\"" + coordinateFormatter.format(l.getLongitude()) + "\"";
  }
  /*
  @SuppressWarnings("hiding")
  @Override
  
  public void prepare(Track track, OutputStream out) {
    this.track = track;
    this.pw = new PrintWriter(out);
  }*/

  @Override
  public String getExtension() {
    return "gpx";//;TrackFileFormat.GPX.getExtension();
  }

  public void writeHeader() {
    if (pw != null) {
      pw.format("<?xml version=\"1.0\" encoding=\"%s\" standalone=\"yes\"?>\n",
          Charset.defaultCharset().name());
      pw.println("<?xml-stylesheet type=\"text/xsl\" href=\"details.xsl\"?>");
      pw.println("<gpx");
      pw.println(" version=\"1.1\"");
      pw.format(" creator=\"Crusoe %s\"\n", Build.MODEL);
      pw.println(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
      pw.println(" xmlns=\"http://www.topografix.com/GPX/1/1\"");
      pw.print(" xmlns:topografix=\"http://www.topografix.com/GPX/Private/"
          + "TopoGrafix/0/1\"");
      pw.print(" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 ");
      pw.print("http://www.topografix.com/GPX/1/1/gpx.xsd ");
      pw.print("http://www.topografix.com/GPX/Private/TopoGrafix/0/1 ");
      pw.println("http://www.topografix.com/GPX/Private/TopoGrafix/0/1/"
          + "topografix.xsd\">");
      // TODO: Author etc.
    }
  }

  public void writeFooter() {
    if (pw != null) {
      pw.println("</gpx>");
    }
  }
  public void writeBeginTrack(String name) {
	  if(pw!=null){
	      pw.println("<trk>");
	      pw.println("<name>" + name  + "</name>");
	  }
  }
  /*
  @Override
  public void writeBeginTrack(Location firstPoint) {
    if (pw != null) {
      pw.println("<trk>");
      pw.println("<name>" + StringUtils.stringAsCData(track.getName())
          + "</name>");
      pw.println("<desc>" + StringUtils.stringAsCData(track.getDescription())
          + "</desc>");
      pw.println("<number>" + track.getId() + "</number>");
      pw.println("<extensions><topografix:color>c0c0c0</topografix:color></extensions>");
    }
  }*/

  public void writeEndTrack() 
  {
    if (pw != null) {
      pw.println("</trk>");
    }
  }
  public int writeSegment(TrackSegment s)
  {
	  if(pw!=null)
	  {
		  writeOpenSegment();
		  for(int i=0; i<s.locations.size(); i++)
		  {
			  Location l = s.locations.get(i);
			  writeLocation(l);
		  }
		  writeCloseSegment();
	  }
	  return s.locations.size();
  }
	
  public void writeOpenSegment() {
    pw.println("<trkseg>");
  }

  public void writeCloseSegment() {
    pw.println("</trkseg>");
  }

  @Override
  public void writeTrack(TrackPoint t, String name)
 {
    if (pw != null) {
    	writeHeader();
    	writeBeginTrack(name);
    	int s;
    	for(s=0; s<t.segments.size(); s++)
    		writeSegment(t.segments.get(s));
    	writeEndTrack();
    	writeFooter();
    }
  }

  @Override
  public void close() {
    if (pw != null) {
      pw.close();
      pw = null;
    }
  }

  public void writeWaypoint(WayPoint waypoint) {
    if (pw != null) {
      if (waypoint != null) {
        pw.println("<wpt " + formatLocation(waypoint) + ">");
        //pw.println("<ele>" + elevationFormatter.format(waypoint.getAltitude()) + "</ele>");
        //pw.println("<time>" + timestampFormatter.format(waypoint.getTime()) + "</time>");
        pw.println("<name>" + waypoint.getName()
            + "</name>");
        //pw.println("<desc>" + waypoint.getDescription() + "</desc>");
        pw.println("</wpt>");
      }
    }
  }
  public void writeLocation(Location location) {
	    if (pw != null) {
	      if (location != null) {
	        pw.println("<trkpt " + formatLocation(location) + ">");
	        pw.println("<ele>" + elevationFormatter.format(location.getAltitude()) + "</ele>");
	        pw.println("<time>" + timestampFormatter.format(location.getTime()) + "</time>");
	        //pw.println("<desc>" + waypoint.getDescription() + "</desc>");
	        pw.println("</trkpt>");
	      }
	    }
	  }
}
