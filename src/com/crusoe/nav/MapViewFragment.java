package com.crusoe.nav;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.crusoe.gpsfile.FileExtFilter;
import com.crusoe.gpsfile.GpxFileContentHandler;
import com.crusoe.gpsfile.RoutePoint;
import com.crusoe.gpsfile.TrackPoint;
import com.crusoe.gpsfile.TrackPoint.TrackSegment;
import com.crusoe.gpsfile.WayPoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class MapViewFragment extends CrusoeNavFragments{
	//private MyLocationOverlay locOverlay = null;
	private CrusoeOverlay locOverlay = null;
	private MapView myOpenMapView;
	private MapListener mapListener = null;
	//private ImageButton btnLayer = null;
	boolean unlocked = false;//cada vez que intento un scroll en la pantalla para ver mas del mapa pongo unlocked a true
	int zoom = 15;

	public static final String CRUSOE_LOC_MESSAGE = "com.crusoe.nav.loc.message";
	private final IntentFilter msgFilter = new IntentFilter(CRUSOE_LOC_MESSAGE);
	private final CrusoeLocMessageReceiver msgReceiver = new CrusoeLocMessageReceiver();
	private boolean msgRegistered=false;
	//recibe mensajes de otras actividades y fragments
	private class CrusoeLocMessageReceiver extends BroadcastReceiver{
		//recibe mensajes como nuevo goto, ruta, ...
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
    		Log.i("TAG", "MapViewFragment.CrusoeLocMessageReceiver.onReceive");
    		unlocked = false;
			Log.i("TAG", "CrusoeNavActivity.MSG");
			try{
	           	int select = intent.getIntExtra("VIEW", CrusoeNavActivity.NotDefined);
	           	if(select==CrusoeNavActivity.Active)
	           	{
	           		CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
	           		String n = intent.getStringExtra("NAME");
	           		File a =  new File(Environment.getExternalStorageDirectory() + "/Crusoe/Tracks/"+ n +".gpx");
	            
	           		//leo trackpoints
	           		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
	           		GpxFileContentHandler gpxFileContentHandler = new GpxFileContentHandler();
	           		xmlReader.setContentHandler(gpxFileContentHandler);
	           		FileReader fileReader = new FileReader(a);
	           		InputSource inputSource = new InputSource(fileReader);          
	           		xmlReader.parse(inputSource);
        		
	           		if(gpxFileContentHandler.getTrackPoint()!=null)
	           		{
	           			ResourceProxy mResourceProxy = new DefaultResourceProxyImpl(getActivity().getApplicationContext());
	           			//ArrayList<OverlayItem> anotherOverlayItemArray;
	           			//anotherOverlayItemArray = new ArrayList<OverlayItem>();
	           			PathOverlay path = new PathOverlay(R.color.path_color, mResourceProxy);
	           			GeoPoint i = null; 
	           			for(TrackSegment s : gpxFileContentHandler.getTrackPoint().Segments())
	           			{
	           				for(Location l : s.Locations())
	           				{
	           					GeoPoint p = new GeoPoint(l);
	           					if(i==null)
	           						i=p;
	           					path.addPoint(p);
	           					//anotherOverlayItemArray.add(new OverlayItem("", "", p));	
	           				}
	           			}
	           			//CrusoeItemizedOverlay<OverlayItem> items = new CrusoeItemizedOverlay<OverlayItem>(anotherOverlayItemArray, getResources().getDrawable(R.drawable.starblue32), mResourceProxy);
	           			//myOpenMapView.getOverlays().add(items);	
	           			myOpenMapView.getOverlays().add(path);
 
	           			if(i!=null)
	           			{
	           				MapController myMapController= (MapController) myOpenMapView.getController();
	           				myMapController.setCenter(i); //This point is in Enschede, Netherlands. You should select a point in your map or get it from user's location.
	           				myMapController.setZoom(zoom);
	           				myOpenMapView.invalidate();//llama a onDraw	
	           			}	
	           		}
	           		fileReader.close();
	           	}
	           	if(select==CrusoeNavActivity.Delete)
	           	{
	           		CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
	           		String n = intent.getStringExtra("NAME");
	           		File a =  new File(Environment.getExternalStorageDirectory() + "/Crusoe/Tracks/"+ n +".gpx");
	           		if(!a.delete())
	           		{
	                    Toast.makeText(getActivity().getBaseContext(), 
	                    		"MapViewFragment.LocMessage.onReceived: No se puede borrar " + n + ".gpx", 
	                            Toast.LENGTH_SHORT).show();
	           			
	           		}
	           		else
	           		{
	           	    	Intent t = new Intent();
	           	    	t.setAction(TracksFragment.TRACKS_MESSAGE);
	           			getActivity().sendBroadcast(t);
	           			
	           		}
	           	}
			}
			catch(Exception e)
			{
                Toast.makeText(getActivity().getBaseContext(), 
                		"MapViewFragment.LocMessage.onReceived: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
			}
		}
	};
	@Override
	public void onResume()
	{
		super.onResume();

		if(!msgRegistered)
		{
			if(getActivity().registerReceiver(msgReceiver, msgFilter)!=null)
				msgRegistered = true;
			else
				Log.i("ERROR", "MapViewFragment.onResume.CrusoeLocMessageReceiver");			
		}
	}
	@Override
	public void onPause()
	{
		super.onPause();
		if(msgRegistered)
			getActivity().unregisterReceiver(msgReceiver);
		msgRegistered = false;
	}

	@Override
	void UpdateMapView() {
		// TODO Auto-generated method stubB
		if(unlocked) 
			return;
		MapController myMapController= (MapController) myOpenMapView.getController();
        myMapController.setCenter(new GeoPoint(mLocation)); //This point is in Enschede, Netherlands. You should select a point in your map or get it from user's location.
        myMapController.setZoom(zoom);
        myOpenMapView.invalidate();//llama a onDraw	
        //this.getView().invalidate();
	}

	/** Called when the activity is first created. */
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewroot = null;
		try{
			viewroot = inflater.inflate(R.layout.mapview, container, false);
			unlocked = false;
			Log.i("TAG", "CrusoeNavActivity.onCreate");
			myOpenMapView = (MapView)viewroot.findViewById(R.id.openmapview);
			myOpenMapView.setBuiltInZoomControls(true);
			myOpenMapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
			mapListener = new MapListener() {
				@Override
				public boolean onZoom(ZoomEvent z) {
					// TODO Auto-generated method stub
					if(isVisible()==false)
						return false;
					zoom = z.getZoomLevel();
					return false;
				}
				
				@Override
				public boolean onScroll(ScrollEvent s) {
					// TODO Auto-generated method stub
					if(isVisible()==false)
						return false;
					unlocked = true;
					return false;
				}
			};

			CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
			myOpenMapView.setMapListener(mapListener);
			if(app.cfg_maps.contentEquals("Offline"))
				myOpenMapView.setUseDataConnection(false);
			else
				myOpenMapView.setUseDataConnection(true);

			MapController myMapController= (MapController) myOpenMapView.getController();
			myMapController.setCenter(new GeoPoint(-34.50955, -58.3105833)); //This point is in Enschede, Netherlands. You should select a point in your map or get it from user's location.B
			myMapController.setZoom(zoom);
        
			ResourceProxy mResourceProxy = new DefaultResourceProxyImpl(this.getActivity().getApplicationContext());
			//locOverlay = new MyLocationOverlay(this.getActivity().getApplicationContext(), myOpenMapView);
			locOverlay = new CrusoeOverlay(this.getActivity().getApplicationContext(), myOpenMapView, mResourceProxy);
			myOpenMapView.getOverlays().add(locOverlay);
			
			if(app.ruta_seguir!=null && app.ruta_seguir.size()!=0)
			{
				ArrayList<OverlayItem> anotherOverlayItemArray;
				anotherOverlayItemArray = new ArrayList<OverlayItem>();
				PathOverlay path = new PathOverlay(R.color.path_color, mResourceProxy);
				for(StatWpt w : app.ruta_seguir)
				{
					GeoPoint p = new GeoPoint(w.getWpt());
					path.addPoint(p);
					OverlayItem o  = new OverlayItem(w.getWpt().getName(), w.getWpt().getDescription(), p);
					o.setMarker(getResources().getDrawable(w.getWpt().getSymbol()));
					anotherOverlayItemArray.add(o);					
				}
				RoutePoint waypoints = app.getRoute("waypoints");
				for(WayPoint w : waypoints.Locations())
				{
					if(w.getSymbol()!=R.drawable.starblue32)
					{
						GeoPoint p = new GeoPoint(w);
						OverlayItem o  = new OverlayItem(w.getName(), w.getDescription(), p);
						o.setMarker(getResources().getDrawable(w.getSymbol()));
						anotherOverlayItemArray.add(o);
					}
				}

				//ItemizedOverlayWithFocus<OverlayItem> anotherItemizedIconOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, anotherOverlayItemArray, myOnItemGestureListener);
		        //MyItemizedIconOverlay anotherItemizedIconOverlay = new MyItemizedIconOverlay(anotherOverlayItemArray, myOnItemGestureListener, mResourceProxy);
				CrusoeItemizedOverlay<OverlayItem> items = new CrusoeItemizedOverlay<OverlayItem>(anotherOverlayItemArray, getResources().getDrawable(R.drawable.starblue32), mResourceProxy);
				myOpenMapView.getOverlays().add(items);	
				myOpenMapView.getOverlays().add(path);
				}
			
			locOverlay.enableMyLocation();
			//locOverlay.disableCompass();
			locOverlay.enableFollowLocation();
			myOpenMapView.invalidate();//llama a onDraw
        
        }catch(Exception e)
        {
        	Log.i("ERROR", e.getMessage());
        }
		return viewroot;
    }
}
