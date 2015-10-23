package com.crusoe.nav;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.crusoe.nav.R;
import com.crusoe.nav.R.array;
import com.crusoe.nav.R.id;
import com.crusoe.nav.R.layout;
import com.crusoe.gpsfile.WayPoint;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

public class StatFragment extends CrusoeNavFragments implements OnItemClickListener{
	WayPoint mLocAnterior = null;
	//double speed = 0;
	//String spd_unit;
	float distto=0;//distancia al wpt
	StatWptAdapter wsa = null;

	public static final String CRUSOE_STAT_MESSAGE = "com.crusoe.nav.stat.message";
	private final IntentFilter msgFilter = new IntentFilter(CRUSOE_STAT_MESSAGE);
	private final CrusoeMessageReceiver msgReceiver = new CrusoeMessageReceiver();
	private boolean msgRegistered=false;
	//recibe mensajes de otras actividades y fragments
	private class CrusoeMessageReceiver extends BroadcastReceiver{
		//recibe mensajes como nuevo goto, ruta, ...
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
    		Log.i("TAG", "StatFragment.CrusoeMessageReceiver.onReceive");
			try{
				CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
				wptlist.clear();//names.clear();
				if(app.ruta_seguir!=null && app.ruta_seguir.size()!=0)
				{
					wptlist.addAll(app.ruta_seguir);
				}
				//else
				//	wptlist.add(new StatWpt(null, "00:00:00", "0 KM"));//names.add("STATS");

				if(wsa!=null)
				{
					wsa.UpdateList(wptlist);
				}
		}
			catch(Exception e)
			{
                Toast.makeText(getActivity().getBaseContext(), 
                		"StatFragment.CrusoeMessageReceiver: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
			}
		}
	};
	@Override
	protected void UpdateMapView() {
		// TODO Auto-generated method stub
		try
		{
			if(mLocAnterior!=null)
			{
				float rec = mLocAnterior.distanceTo(mLocation); 
				if(rec>=25.0)
				{
					//setDataText(intent);
					if(wsa!=null)
						wsa.notifyDataSetChanged();
					//wsa.UpdateList();
					mLocAnterior = mLocation;
				}
			}
			else
			{
				mLocAnterior=mLocation;
				if(wsa!=null)
					wsa.notifyDataSetChanged();
			}
		}
		catch(Exception e)
		{
            Toast.makeText(getActivity().getBaseContext(), 
            		"StatFragment.CrusoeLocationReceiver: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
			
		}
		
	}

    public class StatWptAdapter extends ArrayAdapter<StatWpt> {
        private ArrayList<StatWpt> items;
        private StatWptViewHolder wptHolder;

        private class StatWptViewHolder {
            TextView wpt;
            TextView eta; 
            TextView dist; 
        }

        public StatWptAdapter(Context context, int tvResId, ArrayList<StatWpt> items) {
            super(context, tvResId, items);
            this.items = items;
        }
        public void UpdateList(ArrayList<StatWpt> s)
        {
        	items = s;
        	this.notifyDataSetChanged();
        }
        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View v = convertView;
        	try {
        	if(pos<0 && pos >= items.size())
        		return v;
            if (v == null) {
        		Log.e("GPS", "StatFragment.getView NULL");
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.stat_wpt, null);
                wptHolder = new StatWptViewHolder();
                wptHolder.wpt = (TextView)v.findViewById(R.id.wpt_name);
                wptHolder.eta = (TextView)v.findViewById(R.id.wpt_eta);
                wptHolder.dist = (TextView)v.findViewById(R.id.wpt_dist);
                v.setTag(wptHolder);
            } 
            else 
            {    		
            	wptHolder = (StatWptViewHolder)v.getTag(); 
            	if(wptHolder==null)
            	{
            		Log.e("GPS", "StatFragment. Holder==NULL");
            		return v;
            	}
            }
            
            StatWpt sw = items.get(pos);

            if (sw != null && sw.getWpt()!=null) {
            	wptHolder.wpt.setText(sw.getName());
        		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        		CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
            	if(mLocation!=null && sw.Terminado()==false)
            	{
            		if(pos<app.ruta_offset)
            		{
            			float d = sw.getDist();
        				wptHolder.dist.setText(app.Distance(d));//(sw.getDist());
               			wptHolder.eta.setText(sdf.format(new Date((long)sw.getEta()*1000)));
            		}
            		if(pos==app.ruta_offset)
            		{
            			distto = mLocation.distanceTo(sw.getWpt());
        				wptHolder.dist.setText(app.Distance(distto));//(sw.getDist());
                		if(getSpeed()!=0)
                		{
                			float eta = (float) ((distto*3.6)/getSpeed());
                			//wptHolder.eta.setText(sdf.format(new Date((long)eta*1000)));
                			wptHolder.eta.setText(CrusoeNavActivity.convMilliSec((long)(1000*eta)));
                		}
                		//else
                		//	wptHolder.eta.setText(sdf.format(new Date((long)sw.getEta()*1000)));
            		}
            		if(pos>app.ruta_offset)
                	{
                        StatWpt sw2 = items.get(pos-1);
            			distto += sw2.getWpt().distanceTo(sw.getWpt());
        				wptHolder.dist.setText(app.Distance(distto));//(sw.getDist());
                		if(getSpeed()!=0)
                		{
                			float eta = (float) ((distto*3.6)/getSpeed());
                			wptHolder.eta.setText(CrusoeNavActivity.convMilliSec((long)(1000*eta)));
                		}
                		//else
                		//	wptHolder.eta.setText(sdf.format(new Date((long)sw.getEta()*1000)));
                	}
            	}
            	else
        		{
            		Log.e("GPS", "StatFragment item==NULL");
        			wptHolder.eta.setText(CrusoeNavActivity.convMilliSec((long)(1000*sw.getEta())));
    				wptHolder.dist.setText(app.Distance(sw.getDist()));//(sw.getDist());
        		}
            }
        	}
        	catch(Exception e)
        	{
        		Toast.makeText(getContext(), "StatFragment: " + e.getMessage(), Toast.LENGTH_LONG).show();
        	}

            return v;
        }
    }

	//ArrayList<String> names = new ArrayList<String>();	//nombre de los waypoints
    ArrayList<StatWpt> wptlist = new ArrayList<StatWpt>();
	//String [] nombres ={"Luis", "Eugenio", "Voss"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		try{
		Log.i("TAG", "StatFragment.onCreateView");
		
		CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
		wptlist.clear();//names.clear();
		if(app.ruta_seguir!=null && app.ruta_seguir.size()!=0)
		{
			wptlist.addAll(app.ruta_seguir);
		}
		else
		{
			if(app.gotoWpt!=null)
				wptlist.add(app.gotoWpt);
		//else
		//	wptlist.add(new StatWpt());//names.add("STATS");
		}
			
		rootView = inflater.inflate(R.layout.stat_view, container, false);
		if(rootView==null)
		{
			Log.i("ERROR", "StatFragment.onCreateView View==NULL");
			return rootView;
		}
		ListView lv = (ListView)rootView.findViewById(R.id.statlist);
		//lv.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.stat_view, R.id.emptylist, names));
		wsa = new StatWptAdapter(getActivity().getBaseContext(), R.layout.stat_view, wptlist);
		lv.setAdapter(wsa);
		lv.setOnItemClickListener(this); 
		/*
		if(!locRegistered)
		{
			if(getActivity().registerReceiver(locReceiver, locFilter)!=null)
				locRegistered = true;
			else
				Log.i("ERROR", "StatFragment.onCreateView.CrusoeLocationReceiver");			
		}
		if(!msgRegistered)
		{
			if(getActivity().registerReceiver(msgReceiver, msgFilter)!=null)				
				msgRegistered = true;
			else
				Log.i("ERROR", "StatFragment.onCreateView.CrusoeMessageReceiver");			
		}*/
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
        builder.setTitle(wptlist.get(position).getName());
		builder.setItems(R.array.GoTo_menues, new DialogInterface.OnClickListener() {            
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                //mDoneButton.setText(items[item]);
            	Intent t = new Intent();
            	t.setAction(CrusoeNavActivity.CRUSOE_MESSAGE);
               	int select = CrusoeNavActivity.NotDefined;
               	t.putExtra("NAME",wptlist.get(pos_selected).getName());
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
               	t.putExtra("ACTION",select);
               	getActivity().sendBroadcast(t);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
		
	}

}
