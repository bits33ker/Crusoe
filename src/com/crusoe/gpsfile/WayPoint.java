package com.crusoe.gpsfile;

import com.crusoe.nav.R;

import android.location.Location;

public class WayPoint extends Location {
	String name;
	String description;
	int symbol;//indica el simbolo asociado
	public WayPoint(String n, String d, String provider) {
		super(provider);
		// TODO Auto-generated constructor stub
		name = n;
		description="";
		symbol = R.drawable.starblue32;//valor por default
	}
	public WayPoint(String n, String d, Location l)
	{
		super(l);
		name = n;
		description=d;
	}
	public WayPoint(String n)
	{
		super("");
		name = n;
		description="";
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
	public void setSymbol(int s)
	{
		symbol = s;
	}
	public int getSymbol()
	{
		return symbol;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String d)
	{
		description = d;
	}
	public String getLatDMS()
	{
		double l=this.getLatitude();
		String res = "";
		if(l<0)
		{
			l=-l;
			res="S ";
		}
		else
			res = "N ";
		int grados=(int) l;
		double min = (l-grados)*60.0;
		int seg = (int) ((min - (int)min)*60);
		res= res + String.format("%d°%d\'%d\"", grados, (int)min, seg);
		return res;
	}
	public String getLongDMS()
	{
		double l=this.getLatitude();
		String res = "";
		if(l<0)
		{
			l=-l;
			res="W ";
		}
		else
			res = "E ";
		int grados=(int) l;
		double min = (l-grados)*60.0;
		int seg = (int) ((min - (int)min)*60);
		res= res + String.format("%d°%d\'%d\"", grados, (int)min, seg);
		return res;
	}
}
