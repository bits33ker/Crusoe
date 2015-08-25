package com.crusoe.nav;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddRouteActivity extends Activity {
	EditText editRoute;
	Button btnAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			Log.i("TAG", "AddRouteActivity.onCreate");
			setContentView(R.layout.add_route);
			InitializeUI();
		}
		catch(Exception e)
		{
			Log.i("ERROR", e.getMessage());
		}
	}
	void InitializeUI()
	{
		editRoute = (EditText)findViewById(R.id.editRoute);
		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//llamar a actividad para agregar WayPoints
	        	Intent returnIntent = new Intent();
	        	returnIntent.putExtra("RESULT","NEW");
	        	returnIntent.putExtra("NAME", editRoute.getText().toString());
	        	Log.i("ADD ROUTE", editRoute.getText().toString());
	        	setResult(RESULT_OK,returnIntent);
	        	finish();
			}
		});
		
		editRoute.setOnKeyListener(new OnKeyListener()
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
		                    imm.hideSoftInputFromWindow(editRoute.getWindowToken(), 0);
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
