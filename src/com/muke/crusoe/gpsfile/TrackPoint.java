package com.muke.crusoe.gpsfile;
import java.util.ArrayList;
import java.util.Collection;

import android.location.*;

public class TrackPoint {
	public class TrackSegment {
		ArrayList<Location> locations=null;
		public TrackSegment()
		{
			locations = new ArrayList<Location>();
		}
		public Collection<Location> Locations()
		{
			return locations;
		}
	}
	ArrayList<TrackSegment> segments=null;
	public TrackPoint()
	{
		segments = new ArrayList<TrackPoint.TrackSegment>();
	}
	public void StartSegment()
	{
		segments.add(new TrackSegment());
	}
	public void AddLocation(Location l)
	{
		if(segments.size()==0)
			StartSegment();
		TrackSegment s = segments.get(segments.size()-1);
		s.locations.add(l);
	}
	public void StopSegment()
	{
		
	}
	public Collection<TrackSegment>Segments()
	{
		return segments;
	}
}