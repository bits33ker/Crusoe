package com.muke.crusoe.gpsfile;

import android.location.Location;

public class WayPoint extends Location {
	String name;
	String description;
	public WayPoint(String n, String d, String provider) {
		super(provider);
		// TODO Auto-generated constructor stub
		name = n;
		description="";
	}
	public WayPoint(String n, String d, Location l)
	{
		super(l);
		name = n;
		description=d;
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
