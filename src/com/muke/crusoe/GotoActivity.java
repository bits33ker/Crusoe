package com.muke.crusoe;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GotoActivity extends ListActivity{
	ArrayList<String> waypoints = new ArrayList<String>();	//nombre de los waypoints
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("TAG", "GotoActivity.onCreate");
		//setContentView(R.layout.goto_view);
		Intent intent = this.getIntent();
		String p = intent.getStringExtra("WAYPOINTS");
		String[] lista = p.split(";");
		int i=0;
		while(i<lista.length)
		{
			waypoints.add(lista[i]);
			i++;
		};
		setListAdapter(new ArrayAdapter<String>(GotoActivity.this,
				R.layout.goto_view, waypoints));

	}
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		String res = waypoints.get(pos);
    	Intent returnIntent = new Intent();
    	returnIntent.putExtra("RESULT",res);
    	Log.i("GOTO", res);
    	setResult(RESULT_OK,returnIntent);
		finish();
	}
}
