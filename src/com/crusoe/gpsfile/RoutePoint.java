package com.crusoe.gpsfile;

import java.util.ArrayList;
import java.util.Collection;

public class RoutePoint {
	String name;
	String description;
	ArrayList<WayPoint> waypoints=null;
	public RoutePoint(String n)
	{
		name = n;
		waypoints = new ArrayList<WayPoint>();
		description = "";
	}
	public RoutePoint()
	{
		description = name = "";
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
	public void delWayPoint(int pos)
	{
		waypoints.remove(pos);
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
	public void setName(String n)
	{
		name = n;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String d)
	{
		description = d;
	}
}
