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

public class WayPointsListActivity extends ListActivity{
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

		Log.i("TAG", "WayPointsListActivity.onCreate");
		//setContentView(R.layout.goto_view);
		Intent intent = this.getIntent();
		String p = intent.getStringExtra("NAMES");
		//type = intent.getIntExtra("TYPE", NotDefined);
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
		setListAdapter(new ArrayAdapter<String>(WayPointsListActivity.this,
				R.layout.goto_view, names));

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_CANCELED)
		{
			return;
		}
		//CrusoeApplication app = ((CrusoeApplication)getApplication());
    	setResult(RESULT_OK,data);//reenvio los datos a CrusoeNavActivity		
		//por ahora solo el Add, Edit waypoints y Mark.
		finish();
    }
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		pos_selected = pos;
		v.setSelected(true);
		String s = names.get(pos_selected);
       	if(action==Add)
       	{
           	Intent returnIntent = new Intent();
           	returnIntent.putExtra("RESULT",s);
           	returnIntent.putExtra("ACTION", Add);
           	setResult(RESULT_OK,returnIntent);
        	finish();
        	return;
       	}
       	if(pos_selected==0 && s.compareTo(getBaseContext().getString(R.string.action_Agregar))==0)
       	{
       		//agrego waypoint a la lista
			Intent wpi = new Intent(this, WayPointActivity.class);
			wpi.putExtra("NAME", "NEW");
			//gotoIntent.putExtra("TYPE", WptRC);
			wpi.putExtra("ACTION", Add);
			this.startActivityForResult(wpi, CrusoeNavActivity.WptRC);
       		return;
       	}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(s);
		builder.setItems(R.array.GoTo_menues, new DialogInterface.OnClickListener() {            
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
               			select = Delete;
               			break;
               		case 2:
               		{
               			CrusoeApplication app = (CrusoeApplication)getApplication();
               			WayPoint p = app.getWayPoint(names.get(pos_selected));
               			//select = Edit;
                		Intent wptIntent = new Intent(WayPointsListActivity.this, WayPointActivity.class);
                		wptIntent.putExtra("LATITUD", p.getLatitude());
                		wptIntent.putExtra("LONGITUD", p.getLongitude());
                		wptIntent.putExtra("ACCURACY", p.getAccuracy());
                		wptIntent.putExtra("NAME", p.getName());
                		wptIntent.putExtra("ACTION", Edit);
                		startActivityForResult(wptIntent, CrusoeNavActivity.WptRC);
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
