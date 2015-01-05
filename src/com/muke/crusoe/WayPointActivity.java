package com.muke.crusoe;
/*
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
*/
import android.app.Activity;
import android.os.*;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;

public class WayPointActivity extends Activity{
	//esta clase es para la creacion y edicion de los WayPoints

	TextView editLat;
	TextView editLong;
	TextView txtAcc;
	EditText editWpt;
	Button btnAdd;
	String old_name="";
	int action;//tipo de edicion

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			Log.i("TAG", "MarkActivity.onCreate");
			setContentView(R.layout.waypoint_view);
			InitializeUI();
			Intent intent = this.getIntent();
			String n = intent.getStringExtra("NAME");
			action = intent.getIntExtra("ACTION", CrusoeListActivity.NotDefined);
			if(n!=null && n!="")
			{
				old_name = n;
				if(action!=WayPointsListActivity.Add)
					editWpt.setText(n);
			}
			Double l = intent.getDoubleExtra("LATITUD", 0.0);
			editLat.setText(l.toString());
			l = intent.getDoubleExtra("LONGITUD", 0.0);
			editLong.setText(l.toString());
			txtAcc.setText("ACC " + intent.getFloatExtra("ACCURACY", (float) 0.0));		
		}
		catch(Exception e)
		{
			Log.i("ERROR", e.getMessage());
		}
	}
	void InitializeUI()
	{
		//txtLat = (TextView) findViewById(R.id.txtLat);
		//txtLong = (TextView) findViewById(R.id.txtLong);
		editLat = (TextView) findViewById(R.id.editLat);
		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//llamar a actividad para agregar WayPoints
                Intent returnIntent = new Intent();
	        	returnIntent.putExtra("RESULT",editWpt.getText().toString());
	        	returnIntent.putExtra("NAME", old_name);
	        	double l = Double.parseDouble(editLat.getText().toString());
	        	returnIntent.putExtra("LATITUD", l);
	        	l = Double.parseDouble(editLong.getText().toString());
	        	returnIntent.putExtra("LONGITUD", l);
	        	returnIntent.putExtra("ACTION", action);//para saber porque fue llamado
	        	Log.i("WPT", editWpt.getText().toString());
	        	setResult(RESULT_OK,returnIntent);
	        	finish();
			}
		});
		editLat.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
		        if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                {
		                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		                    imm.hideSoftInputFromWindow(editLat.getWindowToken(), 0);
		                    return true;
		                }
		                default:
		                    break;
		            }
		        }
		        return false;
		    }
		});
		editLong = (TextView) findViewById(R.id.editLong);
		editLong.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
		        if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                {
		                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		                    imm.hideSoftInputFromWindow(editLong.getWindowToken(), 0);
		                    return true;
		                }
		                default:
		                    break;
		            }
		        }
		        return false;
		    }
		});
		txtAcc = (TextView) findViewById(R.id.txtAcc);
		editWpt = (EditText)findViewById(R.id.editWpt);
		editWpt.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
		        if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                {
		                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		                    imm.hideSoftInputFromWindow(editWpt.getWindowToken(), 0);

		                    return true;
		                }
		                default:
		                    break;
		            }
		        }
		        return false;
		    }
		});
	}
}
