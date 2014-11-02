package com.muke.crusoe;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GotoActivity extends ListActivity{
	/*
	 * utiliza goto_view.xml como resource.
	 * dentro de goto_view.xml se instancia bg_key.xml que indica el color que usamos cuando seleccionamos 
	 * un elemento de la lista. Tambien indica que el mismo permanece seleccionado.
	 * El color de seleccion se define en colors.xml
	 */
	String type="";
	int pos_selected=-1;
	ArrayList<String> names = new ArrayList<String>();	//nombre de los waypoints
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("TAG", "GotoActivity.onCreate");
		//setContentView(R.layout.goto_view);
		Intent intent = this.getIntent();
		String p = intent.getStringExtra("NAMES");
		type = intent.getStringExtra("TYPE");
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
		setListAdapter(new ArrayAdapter<String>(GotoActivity.this,
				R.layout.goto_view, names));

	}
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		/*
		String res = names.get(pos);
    	Intent returnIntent = new Intent();
    	returnIntent.putExtra("RESULT",res);
    	Log.i("GOTO", res);
    	setResult(RESULT_OK,returnIntent);
		finish();*/
		pos_selected = pos;
		v.setSelected(true);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.i("TAG", "GotoActivity.onCreateOptionsMenu");
		if(type=="GOTO")
			getMenuInflater().inflate(R.menu.goto_menu, menu);
		else//type==ROUTE
			getMenuInflater().inflate(R.menu.route_menu, menu);
			
		return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
   		if(pos_selected<0 || pos_selected>=names.size())
   		{
           	Intent returnIntent = new Intent();
           	returnIntent.putExtra("RESULT","");
           	Log.i(type, "Select Canceled");
           	setResult(RESULT_CANCELED,returnIntent);
           	finish();
           	return true;
   		}
   		String res = names.get(pos_selected);//pos=item seleccionado
       	Intent returnIntent = new Intent();
       	returnIntent.putExtra("RESULT",res);
       	setResult(RESULT_OK,returnIntent);
    	switch (item.getItemId()) {
    	case R.id.action_goto:
           	Log.i(type, res);
       		returnIntent.putExtra("ACTION","ACTIVE");
       		returnIntent.putExtra("INVERT","NO");
       		finish();
       		return true;
    	case R.id.action_invertir:
       		returnIntent.putExtra("ACTION","ACTIVE");
       		returnIntent.putExtra("INVERT", "YES");
           	Log.i(type, res + " INVERT");
           	finish();
    		return true;
    	case R.id.action_borrar:
       		returnIntent.putExtra("ACTION","DELETE");
           	Log.i(type, res + " DELETE");
           	finish();
    		return true;
    	case R.id.action_agregar:
    		break;
    	case R.id.action_editar:
    		break;
    	case R.id.action_distancia:
    	}
    	return true;
    }
	
}
