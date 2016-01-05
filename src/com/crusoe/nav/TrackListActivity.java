package com.crusoe.nav;

import java.io.File;
import java.util.ArrayList;

import com.crusoe.gpsfile.FileExtFilter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TrackListActivity extends ListActivity{

	ArrayList<String> tracks = new ArrayList<String>();	//nombre de los waypoints
    //ArrayList<StatWpt> wptlist = new ArrayList<StatWpt>();
	//String [] nombres ={"Luis", "Eugenio", "Voss"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);//para que lo ponga en el stack cuando llama a una activity
		try{
			Log.i("TAG", "TracksFragment.onCreateView");
		
			File CrusoeDir =  new File(Environment.getExternalStorageDirectory() + getBaseContext().getString(R.string.track_dir));
			File []archivos = CrusoeDir.listFiles(new FileExtFilter("gpx"));
			for(File a : archivos)
			{
				tracks.add(a.getName().replace(".gpx", ""));			        	
			}
			
			setListAdapter(new ArrayAdapter<String>(TrackListActivity.this,
					R.layout.goto_view, tracks));
		}
		catch(Exception e)
		{
			Log.i("ERROR", "StatFragment.onCreateView " + e.getMessage());			
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}
	@Override
	public void onPause()
	{
		super.onPause();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_CANCELED)
		{
			return;
		}
		//CrusoeApplication app = ((CrusoeApplication)getApplication());
    	this.setResult(Activity.RESULT_OK,data);//reenvio los datos a CrusoeNavActivity		
		//por ahora solo el Add, Edit waypoints y Mark.
		finish();
    }

	int pos_selected=-1;
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		// TODO Auto-generated method stub
        //Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
		pos_selected = pos;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(tracks.get(pos));
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
               	sendBroadcast(t);
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
		
	}

}
