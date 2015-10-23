package com.crusoe.nav;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.crusoe.nav.StatWpt;
import com.crusoe.gpsfile.FileExtFilter;
import com.crusoe.gpsfile.WayPoint;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TracksFragment extends CrusoeNavFragments implements OnItemClickListener{
	WayPoint mLocAnterior = null;
	//double speed = 0;
	//String spd_unit;
	float distto=0;//distancia al wpt
	TracksAdapter wsa = null;

	public static final String TRACKS_MESSAGE = "com.crusoe.nav.track.message";
	private final IntentFilter msgFilter = new IntentFilter(TRACKS_MESSAGE);
	private final CrusoeMessageReceiver msgReceiver = new CrusoeMessageReceiver();
	private boolean msgRegistered=false;
	//recibe mensajes de otras actividades y fragments
	private class CrusoeMessageReceiver extends BroadcastReceiver{
		//recibe mensajes como nuevo goto, ruta, ...
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
    		Log.i("TAG", "TracksFragment.CrusoeMessageReceiver.onReceive");
    		try{
    			Log.i("TAG", "TracksFragment.onCreateView");
    			tracks.clear();
    			wsa.clear();
    			File CrusoeDir =  new File(Environment.getExternalStorageDirectory() + getActivity().getBaseContext().getString(R.string.track_dir));
    			File []archivos = CrusoeDir.listFiles(new FileExtFilter("gpx"));
    			for(File a : archivos)
    			{
    				tracks.add(a.getName().replace(".gpx", ""));			        	
    			}
    			wsa.UpdateList(tracks);
    			
     		}
    		catch(Exception e)
    		{
    			Log.i("ERROR", "StatFragment.onCreateView " + e.getMessage());			
    		}
		}
	};
	@Override
	protected void UpdateMapView() 
	{
	}

    public class TracksAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;
        private TrackViewHolder wptHolder;

        private class TrackViewHolder {
            TextView wpt;
        }

        public TracksAdapter(Context context, int tvResId, ArrayList<String> items) {
            super(context, tvResId, items);
            this.items = items;
        }
        public void UpdateList(ArrayList<String> s)
        {
        	items = s;
        	this.notifyDataSetChanged();
        }
        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View v = convertView;
    		Log.i("TAG", "StatFragment.getView");
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.track_wpt, null);
                wptHolder = new TrackViewHolder();
                wptHolder.wpt = (TextView)v.findViewById(R.id.track_name);
                 v.setTag(wptHolder);
            } else wptHolder = (TrackViewHolder)v.getTag(); 

            String sw = items.get(pos);

            if (sw != null) {
            	wptHolder.wpt.setText(sw);
            }

            return v;
        }
    }

	ArrayList<String> tracks = new ArrayList<String>();	//nombre de los waypoints
    //ArrayList<StatWpt> wptlist = new ArrayList<StatWpt>();
	//String [] nombres ={"Luis", "Eugenio", "Voss"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		try{
			Log.i("TAG", "TracksFragment.onCreateView");
		
			File CrusoeDir =  new File(Environment.getExternalStorageDirectory() + getActivity().getBaseContext().getString(R.string.track_dir));
			File []archivos = CrusoeDir.listFiles(new FileExtFilter("gpx"));
			for(File a : archivos)
			{
				tracks.add(a.getName().replace(".gpx", ""));			        	
			}
			
			rootView = inflater.inflate(R.layout.stat_view, container, false);
			if(rootView==null)
			{
				Log.i("ERROR", "StatFragment.onCreateView View==NULL");
				return rootView;
			}
			ListView lv = (ListView)rootView.findViewById(R.id.statlist);
			//lv.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.stat_view, R.id.emptylist, names));
			wsa = new TracksAdapter(getActivity().getBaseContext(), R.layout.track_view, tracks);
			lv.setAdapter(wsa);
			lv.setOnItemClickListener(this); 
		}
		catch(Exception e)
		{
			Log.i("ERROR", "StatFragment.onCreateView " + e.getMessage());			
		}
		return rootView;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		/*
		if(!locRegistered)
		{
			if(getActivity().registerReceiver(locReceiver, locFilter)!=null)
				locRegistered = true;
			else
				Log.i("ERROR", "StatFragment.onResume.CrusoeLocationReceiver");			
		}*/
		if(!msgRegistered)
		{
			if(getActivity().registerReceiver(msgReceiver, msgFilter)!=null)
				msgRegistered = true;
			else
				Log.i("ERROR", "StatFragment.onResume.CrusoeMessageReceiver");			
		}
	}
	@Override
	public void onPause()
	{
		super.onPause();
		//if(locRegistered)
			//getActivity().unregisterReceiver(locReceiver);
		//locRegistered = false;
		if(msgRegistered)
			getActivity().unregisterReceiver(msgReceiver);
		msgRegistered = false;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_CANCELED)
		{
			return;
		}
		//CrusoeApplication app = ((CrusoeApplication)getApplication());
    	this.getActivity().setResult(Activity.RESULT_OK,data);//reenvio los datos a CrusoeNavActivity		
		//por ahora solo el Add, Edit waypoints y Mark.
		this.getActivity().finish();
    }

	int pos_selected=-1;
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
        //Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
		pos_selected = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(tracks.get(position));
		builder.setItems(R.array.GoTo_menues, new DialogInterface.OnClickListener() {            
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                //mDoneButton.setText(items[item]);
            	Intent t = new Intent();
            	t.setAction(MapViewFragment.CRUSOE_LOC_MESSAGE);
               	int select = CrusoeNavActivity.NotDefined;
               	t.putExtra("NAME",tracks.get(pos_selected));
               	switch(item)
               	{
               		case 0:
               			select = CrusoeNavActivity.Active;
               			break;
               		case 1:
               			select = CrusoeNavActivity.Delete;
               			break;
               		case 2://edit
               			return;
               	}
               	t.putExtra("VIEW",select);
               	getActivity().sendBroadcast(t);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
		
	}

}
