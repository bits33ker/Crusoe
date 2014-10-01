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
	ArrayList<String> names = new ArrayList<String>();	//nombre de los waypoints
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("TAG", "GotoActivity.onCreate");
		//setContentView(R.layout.goto_view);
		Intent intent = this.getIntent();
		String p = intent.getStringExtra("NAMES");
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
		String res = names.get(pos);
    	Intent returnIntent = new Intent();
    	returnIntent.putExtra("RESULT",res);
    	Log.i("GOTO", res);
    	setResult(RESULT_OK,returnIntent);
		finish();
	}
}
