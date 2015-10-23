package com.crusoe.nav;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.crusoe.gpsfile.RoutePoint;
import com.crusoe.gpsfile.WayPoint;

public class EditRouteActivity extends ListActivity{
	/*
	 * utiliza goto_view.xml como resource.
	 * dentro de goto_view.xml se instancia bg_key.xml que indica el color que usamos cuando seleccionamos 
	 * un elemento de la lista. Tambien indica que el mismo permanece seleccionado.
	 * El color de seleccion se define en colors.xml
	 * 
	 * Muestra la lista de WayPoints de una Ruta.
	 */
	
	//int action = NotDefined;//indica la accion a realizar
	int pos_selected=-1;
	int action_selected=CrusoeNavActivity.NotDefined;
	ArrayList<String> names = new ArrayList<String>();	//nombre de los waypoints
	String route_name="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);//para que lo ponga en el stack cuando llama a una activity

		Log.i("TAG", "EditRouteActivity.onCreate");
		//setContentView(R.layout.goto_view);
		Intent intent = this.getIntent();
		String p = intent.getStringExtra("NAMES");
		route_name = intent.getStringExtra("ROUTE");
		//action = intent.getIntExtra("ACTION", NotDefined);
		//si action==RouteEdit muestra los waypoints de la ruta en cuestion
		//sino muestra el listado de rutas.
		if(p==null)
		{
			//aunque sea tiene que tener una lista de 
	    	Intent returnIntent = new Intent();
	    	setResult(RESULT_CANCELED,returnIntent);
			finish();
			return;
		}
		String[] lista = p.split(";");
		int i=0;
		while(i<lista.length)
		{
			names.add(lista[i]);
			i++;
		};
		setListAdapter(new ArrayAdapter<String>(EditRouteActivity.this,
				R.layout.goto_view, names));

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_CANCELED)
		{
			return;
		}
		if(requestCode == CrusoeNavActivity.WptRC)
		{
			//esta seleccionando waypoints
	    	//Intent returnIntent = new Intent();
	    	//returnIntent.putExtra("RESULT", "ADD");
	    	//returnIntent.putExtra("NAME", data.getStringExtra("RESULT"));
	    	//setResult(RESULT_OK,returnIntent);
			//finish();
			String n = data.getStringExtra("RESULT");
			if(action_selected == CrusoeNavActivity.Add)
				names.add(n);
			if(action_selected == CrusoeNavActivity.Insert)
				names.add(pos_selected+1, n);
   			CrusoeApplication app = (CrusoeApplication)getApplication();
			WayPoint p = null;
			for(RoutePoint r: app.routes)
			{
				for(WayPoint w: r.Locations())
				{
					if(n.compareTo(w.getName())==0)
					{
						p = w;
						break;
					}
				}
			}
			if(p!=null)
			{
       			RoutePoint rp = app.getRoute(route_name);
       			if(rp!=null)
       			{
       				if(action_selected == CrusoeNavActivity.Add)
       					rp.addWayPoint(p);
       				if(action_selected == CrusoeNavActivity.Insert)
       					rp.insWayPoint(pos_selected+1, p);
       				CrusoeNavActivity.SaveRoute(rp, Environment.getExternalStorageDirectory() + getBaseContext().getString(R.string.wpt_dir), rp.getName() + ".gpx");
       				//((CrusoeNavActivity)getParent()).SaveRoute(rp, rp.getName() + ".gpx");
       			}
			}
			setListAdapter(new ArrayAdapter<String>(EditRouteActivity.this,
					R.layout.goto_view, names));
			return;
		}
    }
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		pos_selected = pos;
		v.setSelected(true);
		String s = names.get(pos_selected);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(s);
		if(pos==0)
		{
			//selecciono waypoint a agregar
			CrusoeApplication app = (CrusoeApplication)getApplication();
			
   			String param=app.getLocationNames();
			Intent gotoIntent = new Intent(EditRouteActivity.this, WayPointsListActivity.class);
			gotoIntent.putExtra("NAMES", param);
			action_selected = CrusoeNavActivity.Add;
			gotoIntent.putExtra("ACTION", CrusoeNavActivity.Add);
			this.startActivityForResult(gotoIntent, CrusoeNavActivity.WptRC);
			
			return;
		}
		//si llega por aca es para editar algun waypoint de la ruta
		//MENU: INSERT, DELETE, SAVE, MOVEUP, MOVEDOWN.
		builder.setItems(R.array.Route_Edit_menues, new DialogInterface.OnClickListener() {            
	        //builder.setItems(items, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
	                // Do something with the selection
	                //mDoneButton.setText(items[item]);
	               	Intent returnIntent = new Intent();
	               	returnIntent.putExtra("RESULT",names.get(pos_selected));
	               	action_selected = CrusoeNavActivity.NotDefined;
       				CrusoeApplication app = (CrusoeApplication)getApplication();
           			RoutePoint rp = app.getRoute(route_name);
	       			switch(item)
	       			{
	       			case 0://inserta debajo de la posicion actual
	       			{
	       				action_selected = CrusoeNavActivity.Insert;
	       				//selecciono waypoint a agregar
	       				
	       	   			String param=app.getLocationNames();
	       				Intent gotoIntent = new Intent(EditRouteActivity.this, WayPointsListActivity.class);
	       				gotoIntent.putExtra("NAMES", param);
	       				gotoIntent.putExtra("ACTION", CrusoeNavActivity.Insert);
	       				startActivityForResult(gotoIntent, CrusoeNavActivity.WptRC);
	       				
	       				return;
	       			}
	               	case 1:
	               	{
	               		action_selected = CrusoeNavActivity.Delete;
	           			rp.delWayPoint(pos_selected-1);
	           			names.remove(pos_selected);
	               	}
	               		break;
	               	case 2:
	               	{
	               		action_selected = CrusoeNavActivity.MoveUp;
	               		if(pos_selected<=1)//agregar y la primera pos no se cuenta
	               			break;
	           			WayPoint p = rp.getWayPoint(pos_selected-1);
	           			rp.delWayPoint(pos_selected-1);
	           			rp.insWayPoint(pos_selected-2, p);
	           			names.remove(pos_selected);
	           			names.add(pos_selected -1, p.getName());
	               	}
	               		break;
	               	case 3:
	               	{
	               		action_selected = CrusoeNavActivity.MoveDown;
	               		if(pos_selected==(names.size()-2))
	               			break;
	           			WayPoint p = rp.getWayPoint(pos_selected-1);
	           			rp.delWayPoint(pos_selected-1);
	           			rp.insWayPoint(pos_selected, p);
	           			names.remove(pos_selected);
	           			names.add(pos_selected +1, p.getName());
	               	}
	               		break;
	               	}
       				CrusoeNavActivity.SaveRoute(rp, Environment.getExternalStorageDirectory() + getBaseContext().getString(R.string.wpt_dir), rp.getName() + ".gpx");
	       			//((CrusoeNavActivity)app.getBaseContext()).SaveRoute(rp, rp.getName() + ".gpx");
       				setListAdapter(new ArrayAdapter<String>(EditRouteActivity.this,	R.layout.goto_view, names));
	            }
	        });
	        AlertDialog alert = builder.create();
	        alert.show();
			
    }
}
