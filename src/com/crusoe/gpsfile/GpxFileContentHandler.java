package com.crusoe.gpsfile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.crusoe.nav.R;

import android.location.Location;

public class GpxFileContentHandler implements ContentHandler {
    private String currentValue;
    private WayPoint location;
    private RoutePoint ruta;
    private TrackPoint track;
    private List<WayPoint> locationList;
    private List<RoutePoint>routeList;
    private int GetId(String name)
    {
    	if(name.equalsIgnoreCase("maxspeed"))
    		return R.drawable.maxspeed;
    	if(name.equalsIgnoreCase("maxspeed40"))
    		return R.drawable.maxspeed40;
    	if(name.equalsIgnoreCase("maxspeed60"))
    		return R.drawable.maxspeed60;
    	if(name.equalsIgnoreCase("maxspeed100"))
    		return R.drawable.maxspeed100;
    	if(name.equalsIgnoreCase("maxspeed120"))
    		return R.drawable.maxspeed120;
    	if(name.equalsIgnoreCase("maxspeed130"))
    		return R.drawable.maxspeed130;
    	if(name.equalsIgnoreCase("sailboat"))
    		return R.drawable.sailboat32;
    	if(name.equalsIgnoreCase("pilote"))
    		return R.drawable.pilote32;
    	if(name.equalsIgnoreCase("sink"))
    		return R.drawable.sink32;
    	if(name.equalsIgnoreCase("boya"))
    		return R.drawable.boya;
    	return R.drawable.starblue32;
    }
    private final SimpleDateFormat GPXTIME_SIMPLEDATEFORMAT = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss'Z'");

    public GpxFileContentHandler() {
        locationList = new ArrayList<WayPoint>();
        routeList = new ArrayList<RoutePoint>();
    }

    public List<WayPoint> getLocationList() {
        return locationList;
    }
    public List<RoutePoint> getRouteList() {
        return routeList;
    }

    public TrackPoint getTrackPoint() {
        return track;
    }
    
    @Override
    public void startElement(String uri, String localName, String qName,
        Attributes atts) throws SAXException {

    	if(localName.equalsIgnoreCase("trkseg")){
    		track.StartSegment();
    	}
        if (localName.equalsIgnoreCase("trkpt")) {
            location = new WayPoint("", "", "gpxImport");
            location.setLatitude(Double.parseDouble(atts.getValue("lat").trim()));
            location.setLongitude(Double.parseDouble(atts.getValue("lon").trim()));
        }
        if (localName.equalsIgnoreCase("wpt")) {
            location = new WayPoint("", "", "gpxImport");//name, provider
            location.setLatitude(Double.parseDouble(atts.getValue("lat").trim()));         
            location.setLongitude(Double.parseDouble(atts.getValue("lon").trim()));
        }
        if (localName.equalsIgnoreCase("rtept")) {//waypoint de una ruta
            location = new WayPoint("", "", "gpxImport");//name, provider
            location.setLatitude(Double.parseDouble(atts.getValue("lat").trim()));         
            location.setLongitude(Double.parseDouble(atts.getValue("lon").trim()));
        }
        if(localName.equalsIgnoreCase("rte"))
        {
        	ruta = new RoutePoint();
        }
        if(localName.equalsIgnoreCase("trk"))
        {
        	track = new TrackPoint();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
    	if(localName.equalsIgnoreCase("trkseg")){
    		track.StopSegment();
    	}
        if (localName.equalsIgnoreCase("ele")) {
            location.setAltitude(Double.parseDouble(currentValue.trim()));
        }

        if (localName.equalsIgnoreCase("time")) {
            try {
            Date date = GPXTIME_SIMPLEDATEFORMAT.parse(currentValue.trim());
            Long time = date.getTime();
            location.setTime(time);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(localName.equalsIgnoreCase("name"))
        {
        	if(location!=null)
        		location.setName(currentValue.trim());
        	else
        	{
        		if(ruta!=null)
        			ruta.setName(currentValue.trim());
        	}
        }
        if(localName.equalsIgnoreCase("desc"))
        {
        	if(location!=null)
        		location.setDescription(currentValue.trim());
        	else
        	{
        		if(ruta!=null)
        			ruta.setDescription(currentValue.trim());
        	}
        }
        if(localName.equalsIgnoreCase("sym"))
        {
        	if(location!=null)
        	{
        		location.setSymbol(GetId(currentValue.trim()));
        	}
        }
        if (localName.equalsIgnoreCase("trkpt")) {
        	track.LastSegment().AddLocation(location);
        	location = null;
        }
        if (localName.equalsIgnoreCase("wpt")) {
        	locationList.add(location);
        	location = null;
        }
        if (localName.equalsIgnoreCase("rtept")) {
        	ruta.addWayPoint(location);
        	location = null;
        }
        if (localName.equalsIgnoreCase("rte")) {
        	routeList.add(ruta);
        	ruta = null;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        currentValue = new String(ch, start, length);
    }

    @Override
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub
    }

    @Override
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        // TODO Auto-generated method stub
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length)
        throws SAXException {
        // TODO Auto-generated method stub
    }

    @Override
    public void processingInstruction(String target, String data)
        throws SAXException {
        // TODO Auto-generated method stub
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        // TODO Auto-generated method stub
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        // TODO Auto-generated method stub
    }

    @Override
        public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
            // TODO Auto-generated method stub
    }

}