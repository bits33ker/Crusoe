package com.muke.crusoe;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.muke.crusoe.WayPointActivity;
import com.muke.crusoe.gpsfile.RoutePoint;
import com.muke.crusoe.gpsfile.WayPoint;

public class CrusoeListActivity extends ListActivity{
	/*
	 * utiliza goto_view.xml como resource.
	 * dentro de goto_view.xml se instancia bg_key.xml que indica el color que usamos cuando seleccionamos 
	 * un elemento de la lista. Tambien indica que el mismo permanece seleccionado.
	 * El color de seleccion se define en colors.xml
	 */
	public static final int NotDefined=0;
	public static final int Active=1;
	public static final int Invert=2;
	public static final int Delete=3;
	public static final int Add=4;
	public static final int Edit=5;
	
	int action = NotDefined;//indica la accion a realizar
	int pos_selected=-1;
	ArrayList<String> names = new ArrayList<String>();	//nombre de los waypoints
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);//para que lo ponga en el stack cuando llama a una activity

		Log.i("TAG", "GotoActivity.onCreate");
		//setContentView(R.layout.goto_view);
		Intent intent = this.getIntent();
		String p = intent.getStringExtra("NAMES");
		action = intent.getIntExtra("ACTION", NotDefined);
		if(p==null)
		{
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
		setListAdapter(new ArrayAdapter<String>(CrusoeListActivity.this,
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
	    	Intent returnIntent = new Intent();
	    	returnIntent.putExtra("RESULT", "ADD");
	    	returnIntent.putExtra("NAME", data.getStringExtra("RESULT"));
	    	setResult(RESULT_OK,returnIntent);
			finish();
			return;
		}
		if(action == CrusoeListActivity.Edit)
		{
	    	data.putExtra("ROUTE", names.get(pos_selected));
		}
		//CrusoeApplication app = ((CrusoeApplication)getApplication());
    	setResult(RESULT_OK,data);//reenvio los datos a CrusoeNavActivity		
		//por ahora solo el edit waypoints y Mark.
		finish();
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
			if(action!=Edit)
			{
				Intent gotoIntent = new Intent(CrusoeListActivity.this, AddRouteActivity.class);
				this.startActivityForResult(gotoIntent, CrusoeNavActivity.RouteRC);
			}
			else
			{	//selecciono waypoint a agregar
				CrusoeApplication app = (CrusoeApplication)getApplication();
				
    			String param="";
    			for(RoutePoint r : app.routes)
    			{
    				for(WayPoint w: r.Locations())
    				{
    					param = param + w.getName() + ";";
    				}
    			}
				Intent gotoIntent = new Intent(CrusoeListActivity.this, WayPointsListActivity.class);
				gotoIntent.putExtra("NAMES", param);
				gotoIntent.putExtra("ACTION", Add);
				this.startActivityForResult(gotoIntent, CrusoeNavActivity.WptRC);
			}
			return;
		}
		builder.setItems(R.array.Route_menues, new DialogInterface.OnClickListener() {            
        //builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                //mDoneButton.setText(items[item]);
               	Intent returnIntent = new Intent();
               	returnIntent.putExtra("RESULT",names.get(pos_selected));
               	int select = NotDefined;
       			switch(item)
       			{
          		case 0:
               			select = Active;
               			break;
               	case 1:
               			select = Invert;
               			break;
               	case 2:
               			select = Delete;
               			break;
               	case 3:
               		{
               			//select = Edit;
               			//aca debo mostrar todos los waypoints de la ruta en cuestion
               			CrusoeApplication app = (CrusoeApplication)getApplication();
               			RoutePoint rp = app.getRoute(names.get(pos_selected));
               			if(rp==null)
               			{
                            Toast.makeText(getBaseContext(), 
                                    R.string.RouteNotExist, 
                                    Toast.LENGTH_SHORT).show();
                            return;
               			}
               			String param =getResources().getString(R.string.action_Agregar) + ";";
    	    			for(WayPoint w: rp.Locations())
    	    			{
    	    				param = param + w.getName() + ";";
    	    			}
    	    			action = CrusoeListActivity.Edit;
						Intent routeIntent = new Intent(CrusoeListActivity.this, CrusoeListActivity.class);
						routeIntent.putExtra("NAMES", param);
						routeIntent.putExtra("ACTION", action);
						startActivityForResult(routeIntent, CrusoeNavActivity.RouteEdit);
               		}
       				return;
               	}
               	returnIntent.putExtra("ACTION", select);
               	setResult(RESULT_OK,returnIntent);
            	finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
		
    }
}
