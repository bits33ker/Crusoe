package com.crusoe.nav;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

public class CrusoeItemizedOverlay<Item extends OverlayItem> extends ItemizedOverlay<Item> {
	 
	private final ArrayList<Item> mList;//lista a dibujar sobre el mapa
	//protected final Drawable mDefaultMarker;
	
	public CrusoeItemizedOverlay(
            List<Item> pList,
            Drawable d,
            ResourceProxy pResourceProxy) {
 
        //super(pList, pOnItemGestureListener, pResourceProxy);
    	super(d, pResourceProxy);
        // TODO Auto-generated constructor stub
    	mList = (ArrayList<Item>) pList;
    	populate();//carga lista en mInternalList
    	//mDefaultMarker = d;
 
    }
 /*
    @Override
	public boolean onScroll(final MotionEvent pEvent1, final MotionEvent pEvent2,
			final float pDistanceX, final float pDistanceY, final MapView pMapView) {
    	unlocked = true;
    	super.onScroll(pEvent1, pEvent2, pDistanceX, pDistanceY, pMapView);
		return true;
	}*/
    @Override
    public boolean onSingleTapUp(MotionEvent event, MapView mapView) {
        // TODO Auto-generated method stub
        // return super.onSingleTapUp(event, mapView);
        return true;
    }
	@Override
	public boolean onSnapToItem(int arg0, int arg1, Point arg2,
			IMapView arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected Item createItem(int index) {
		// TODO Auto-generated method stub
		return mList.get(index);
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mList.size();
	}
};
