package com.muke.crusoe;

import com.muke.crusoe.gpsfile.WayPoint;

public class StatWpt {
    private WayPoint wpt;//waypoint name
    private float eta=0;//tiempo estimado de arribo, tiempo de viaje si llegue
    private float dist=0;//distancia al punto, o distancia recorrida si ya llegue
    private boolean terminado=false;//indica si llegue al wpt

    public String getName() {
    	if(wpt==null)
    		return "EMPTY";
        return wpt.getName();
    }
    public WayPoint getWpt()
    {
    	return wpt;
    }
    public void setWpt(WayPoint w) {
        wpt = w;
    }

    public float getEta() {
        return eta;
    }
    public void Terminado(boolean t)
    {
    	terminado = t;
    }
    public boolean Terminado()
    {
    	return terminado;    	
    }

    public void setEta(float t) {
        eta = t;
    }

    public float getDist() {
        return dist;
    }

    public void setDist(float d) {
        dist = d;
    }
    public void addDist(float d) {
        dist += d;
    }

    public StatWpt(WayPoint w) {
        wpt = w;
        eta = 0;
        dist = 0;
    }
    public StatWpt() {
        wpt = null;
        eta = 0;
        dist = 0;
    }
}

