package com.muke.crusoe.gpsfile;

import java.util.ArrayList;
import java.util.Collection;

public class RoutePoint {
	String name;
	ArrayList<WayPoint> waypoints=null;
	public RoutePoint(String n)
	{
		name = n;
		waypoints = new ArrayList<WayPoint>();
	}
	public Collection<WayPoint> Locations()
	{
		return waypoints;
	}
	public void addWayPoint(WayPoint p)
	{
		waypoints.add(p);
	}
	public void insWayPoint(int pos, WayPoint p)
	{
		waypoints.add(pos,  p);
	}
	public WayPoint getWayPoint(int i)
	{
		return waypoints.get(i);
	}
	public void addAll(Collection<WayPoint> w)
	{
		waypoints.addAll(w);
	}
	public String getName()
	{
		return name;
	}
}
