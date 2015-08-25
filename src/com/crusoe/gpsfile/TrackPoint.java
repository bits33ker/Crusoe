package com.crusoe.gpsfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.location.*;

public class TrackPoint {
	ArrayList<WayPoint>waypoints = null;
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
		public void AddLocation(Location l)
		{
			locations.add(l);
		}
	}
	ArrayList<TrackSegment> segments=null;
	public TrackPoint()
	{
		segments = new ArrayList<TrackPoint.TrackSegment>();
		waypoints = new ArrayList<WayPoint>();
	}
	public void AddWayPoint(WayPoint l)
	{
		waypoints.add(l);
	}
	public void AddWayPoints(Collection<WayPoint> l)
	{
		waypoints.addAll(l);
	}
	public void StartSegment()
	{
		segments.add(new TrackSegment());
	}
	public Location FirstLocation()
	{
		if(segments!=null)
		{
			if(segments.get(0).Locations().size()==0)
				return null;
			return (Location)segments.get(0).Locations().toArray()[0];
		}
		return null;
	}
	public void StopSegment()
	{
		
	}
	public Collection<TrackSegment>Segments()
	{
		return segments;
	}
	public Collection<WayPoint>WayPoints()
	{
		return waypoints;
	}
	public TrackSegment LastSegment()
	{
		if(segments.size()==0)
			StartSegment();
		TrackSegment s = segments.get(segments.size()-1);
		return s;	
	}
}
