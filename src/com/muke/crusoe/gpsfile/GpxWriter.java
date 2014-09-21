package com.muke.crusoe.gpsfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.location.Location;
import android.os.Build;

public class GpxWriter implements FormatWriter {

	  private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	  private final NumberFormat elevationFormatter;
	  private final NumberFormat coordinateFormatter;
	  private final SimpleDateFormat timestampFormatter;
	  private PrintWriter pw = null;
	  //private Track track;

	  public GpxWriter(File f) throws FileNotFoundException {
		  
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

	  @Override
	  public String getExtension() {
	    return "gpx";//;TrackFileFormat.GPX.getExtension();
	  }

	  @Override
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

	  @Override
	  public void writeFooter() {
	    if (pw != null) {
	      pw.println("</gpx>");
	    }
	  }

	  @Override
	  public void close() {
	    if (pw != null) {
	      pw.close();
	      pw = null;
	    }
	  }

	  @Override
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
}
