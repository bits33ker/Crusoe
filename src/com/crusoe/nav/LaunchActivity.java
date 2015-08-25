package com.crusoe.nav;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class LaunchActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("TAG", "LaunchActivity.onCreate");
		CrusoeApplication app = ((CrusoeApplication)getApplication());
		
		if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
			app.CargoWayPoints();
		}

		setContentView(R.layout.launch_activity);
		Configuration cfg = getResources().getConfiguration();
		int size = cfg.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		if(size == Configuration.SCREENLAYOUT_SIZE_SMALL)
		{
			Intent intent = new Intent(this, CrusoeSmallActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);		
        }
		else
		{
			Intent intent = new Intent(this, CrusoeDefaultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);		
		}

	}
}
