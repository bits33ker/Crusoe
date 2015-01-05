package com.muke.crusoe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.muke.crusoe.gpsfile.WayPoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StatFragment extends Fragment implements OnItemClickListener{
	WayPoint mLocation = null;
	double speed = 0;
	private final IntentFilter intentFilter = new IntentFilter(CrusoeNavActivity.CRUSOE_LOCATION_VIEW_INTENT);
	private final CrusoeLocationReceiver mReceiver = new CrusoeLocationReceiver();
	private boolean registered=false;
	  
	private class CrusoeLocationReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
			
	        //handler.sendEmptyMessage(1000);//envia mensaje. Atiende handler
			try{
				mLocation = new WayPoint("", "", intent.getExtras().getString("PROVIDER"));
				mLocation.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
				mLocation.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
				mLocation.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));
				String s = intent.getStringExtra("SPEED");
				speed = Double.parseDouble(s);
					//setDataText(intent);
			}
			catch(Exception e)
			{
                Toast.makeText(getActivity().getBaseContext(), 
                		"StatFragment.onReceived: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
			}
		}
	};

    class StatWpt {
        private WayPoint wpt;//waypoint name
        private String eta;//tiempo estimado de arribo
        private String dist;//distancia al punto

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

        public String getEta() {
            return eta;
        }

        public void setEta(String t) {
            eta = t;
        }

        public String getDist() {
            return dist;
        }

        public void setDist(String d) {
            dist = d;
        }

        public StatWpt(WayPoint w, String t, String d) {
            wpt = w;
            eta = t;
            dist = d;
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

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View v = convertView;
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

            if (sw != null) {
            	wptHolder.wpt.setText(sw.getName());
            	if(mLocation!=null)
            	{
            		float distto = mLocation.distanceTo(sw.getWpt());
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
                    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                		wptHolder.eta.setText(sdf.format(new Date((long)eta*1000)));
                	}
                	else
                		wptHolder.eta.setText(sw.getEta());
            	}
            	else
            	{
            		wptHolder.eta.setText(sw.getEta());
        			wptHolder.dist.setText(sw.getDist());
            	}
            		
		
            }

            return v;
        }
    }

	//ArrayList<String> names = new ArrayList<String>();	//nombre de los waypoints
    ArrayList<StatWpt> wptlist = new ArrayList<StatWpt>();
	String [] nombres ={"Luis", "Eugenio", "Voss"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i("TAG", "StatFragment.onCreate");
		//setContentView(R.layout.goto_view);
		
		CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
		wptlist.clear();//names.clear();
		if(app.ruta_seguir!=null)
		{
			for(WayPoint w : app.ruta_seguir.Locations())
			{
				wptlist.add(new StatWpt(w, "00:00:00", "0 KM"));//names.add(w.getName());
			};
		}
		else
			wptlist.add(new StatWpt(null, "00:00:00", "0 KM"));//names.add("STATS");
			
		View rootView = null;
		rootView = inflater.inflate(R.layout.stat_view, container, false);
		ListView lv = (ListView)rootView.findViewById(R.id.statlist);
		//lv.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.stat_view, R.id.emptylist, names));
		lv.setAdapter(new StatWptAdapter(getActivity().getBaseContext(), R.layout.stat_view, wptlist));
		lv.setOnItemClickListener(this); 
		return rootView;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if(!registered)
		{
			getActivity().registerReceiver(mReceiver, intentFilter);
			registered = true;
		}
	}
	@Override
	public void onPause()
	{
		super.onPause();
		if(registered)
			getActivity().unregisterReceiver(mReceiver);
		registered = false;
	}

	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT)
        .show();
		
	}
}
