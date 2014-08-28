package com.muke.crusoe.gpsfile;

import android.location.Location;

public class WayPoint extends Location {
	String name;
	public WayPoint(String n, String provider) {
		super(provider);
		// TODO Auto-generated constructor stub
		name = n;
	}
	public WayPoint(String n, Location l)
	{
		super(l);
		name = n;
	}
	public WayPoint(Location l)
	{
		super(l);
		name = "";
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
		return "";
	}
}
