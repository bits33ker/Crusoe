package com.muke.crusoe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.muke.crusoe.StatWpt;
import com.muke.crusoe.gpsfile.WayPoint;

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

public class StatFragment extends Fragment implements OnItemClickListener{
	WayPoint mLocation = null;
	double speed = 0;
	float distto=0;//distancia al wpt
	StatWptAdapter wsa = null;

	public static final String CRUSOE_STAT_MESSAGE = "com.muke.crusoe.stat.message";
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

	private final IntentFilter locFilter = new IntentFilter(CrusoeNavActivity.CRUSOE_LOCATION_VIEW_INTENT);
	private final CrusoeLocationReceiver locReceiver = new CrusoeLocationReceiver();
	private boolean locRegistered=false;
	//recibe mensajes de cambio de posicion
	private class CrusoeLocationReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
			
	        //handler.sendEmptyMessage(1000);//envia mensaje. Atiende handler
			try{
	    		//Log.i("TAG", "StatFragment.CrusoeLocationReceiver.onReceive");
				WayPoint loc = new WayPoint("", "", intent.getExtras().getString("PROVIDER"));
				loc.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
				loc.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
				loc.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));
				String s = intent.getStringExtra("SPEED");
				speed = Double.parseDouble(s);
				if(mLocation!=null)
				{
					float rec = mLocation.distanceTo(loc); 
					if(rec>=25.0)
					{
						//setDataText(intent);
						if(wsa!=null)
							wsa.notifyDataSetChanged();
							//wsa.UpdateList();
						mLocation = loc;
					}
				}
				else
				{
					mLocation=loc;
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
	};

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
    		Log.i("TAG", "StatFragment.getView");
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.stat_wpt, null);
                wptHolder = new StatWptViewHolder();
                wptHolder.wpt = (TextView)v.findViewById(R.id.wpt_name);
                wptHolder.eta = (TextView)v.findViewById(R.id.wpt_eta);
                wptHolder.dist = (TextView)v.findViewById(R.id.wpt_dist);
                v.setTag(wptHolder);
            } else wptHolder = (StatWptViewHolder)v.getTag(); 

            StatWpt sw = items.get(pos);

            if (sw != null && sw.getWpt()!=null) {
            	wptHolder.wpt.setText(sw.getName());
        		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            	if(mLocation!=null && sw.Terminado()==false)
            	{
            		CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
            		if(pos<app.ruta_offset)
            		{
            			float d = sw.getDist();
            			if(d>1000)
            			{
            				wptHolder.dist.setText(String.format("%.2f KM", d/1000));//(sw.getDist());
            			}
            			else
            			{
            				wptHolder.dist.setText(String.format("%d mts", (int)d));//(sw.getDist());
            			}
               			wptHolder.eta.setText(sdf.format(new Date((long)sw.getEta()*1000)));
            		}
            		if(pos==app.ruta_offset)
            		{
            			distto = mLocation.distanceTo(sw.getWpt());
            			if(distto>1000)
            			{
            				wptHolder.dist.setText(String.format("%.2f KM", distto/1000));//(sw.getDist());
            			}
            			else
            			{
            				wptHolder.dist.setText(String.format("%d mts", (int)distto));//(sw.getDist());
            			}
                		if(speed!=0)
                		{
                			float eta = (float) ((distto*3.6)/speed);
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
            			if(distto>1000)
            			{
            				wptHolder.dist.setText(String.format("%.2f KM", distto/1000));//(sw.getDist());
            			}
            			else
            			{
            				wptHolder.dist.setText(String.format("%d mts", (int)distto));//(sw.getDist());
            			}
                		if(speed!=0)
                		{
                			float eta = (float) ((distto*3.6)/speed);
                			wptHolder.eta.setText(CrusoeNavActivity.convMilliSec((long)(1000*eta)));
                		}
                		//else
                		//	wptHolder.eta.setText(sdf.format(new Date((long)sw.getEta()*1000)));
                	}
            	}
            	else
        		{
        			wptHolder.eta.setText(CrusoeNavActivity.convMilliSec((long)(1000*sw.getEta())));
        			if(sw.getDist()>1000)
        			{
        				wptHolder.dist.setText(String.format("%.2f KM", sw.getDist()/1000));//(sw.getDist());
        			}
        			else
        			{
        				wptHolder.dist.setText(String.format("%d mts", (int)sw.getDist()));//(sw.getDist());
        			}
        		}
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
		//else
		//	wptlist.add(new StatWpt());//names.add("STATS");
			
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
		}
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
		if(!locRegistered)
		{
			if(getActivity().registerReceiver(locReceiver, locFilter)!=null)
				locRegistered = true;
			else
				Log.i("ERROR", "StatFragment.onResume.CrusoeLocationReceiver");			
		}
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
		if(locRegistered)
			getActivity().unregisterReceiver(locReceiver);
		locRegistered = false;
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
