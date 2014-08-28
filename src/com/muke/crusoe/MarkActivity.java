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
import android.content.Context;
import android.location.*;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.muke.crusoe.gpsfile.*;
import com.muke.crusoe.CrusoeApplication.*;

import android.content.Intent;
import android.content.IntentFilter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class MarkActivity extends Activity{

	TextView txtLat;
	TextView txtLong;
	TextView txtAcc;
	EditText editWpt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			Log.i("TAG", "MarkActivity.onCreate");
			setContentView(R.layout.mark);
			InitializeUI();
			Intent intent = this.getIntent();
			txtLat.setText("Lat: " + intent.getDoubleExtra("LATITUD", 0.0));
			txtLong.setText("Long " + intent.getDoubleExtra("LONGITUD", 0.0));
			txtAcc.setText("Acc " + intent.getFloatExtra("ACCURACY", (float) 0.0));		
		}
		catch(Exception e)
		{
			Log.i("ERROR", e.getMessage());
		}
	}
	/*
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    switch (keyCode) {
	        case KeyEvent.KEYCODE_ENTER:
	        {
	            //your Action code
	        	Intent returnIntent = new Intent();
	        	returnIntent.putExtra("RESULT",editWpt.getText().toString());
	        	Log.i("MARK", editWpt.getText().toString());
	        	setResult(RESULT_OK,returnIntent);
	        	finish();	            
	        	return true;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}*/
	void InitializeUI()
	{
		txtLat = (TextView) findViewById(R.id.txtLat);
		txtLong = (TextView) findViewById(R.id.txtLong);
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
		                    //addCourseFromTextBox();
		    	        	Intent returnIntent = new Intent();
		    	        	returnIntent.putExtra("RESULT",editWpt.getText().toString());
		    	        	Log.i("MARK", editWpt.getText().toString());
		    	        	setResult(RESULT_OK,returnIntent);
		    	        	finish();	            
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
