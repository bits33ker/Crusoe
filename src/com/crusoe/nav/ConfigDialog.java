package com.crusoe.nav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ConfigDialog extends DialogFragment{
	public interface ConfigDialogListener {
        void onFinishConfigDialog(String res);
    }
	
	String version;
	RadioGroup metrics;//millas o metros
	RadioGroup maps;//offline u online
	public ConfigDialog(String v) {
        // Empty constructor required for DialogFragment
		version = v;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = null;
        try{
    		CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
        	view = inflater.inflate(R.layout.cfgdlg, container);
        	TextView n = (TextView)view.findViewById(R.id.txtVersion);
        	n.setText(version);
        	metrics = (RadioGroup) view.findViewById(R.id.crusoe_metrics);
            getDialog().setTitle(R.string.Cfg);
            if(app.cfg_metric.contentEquals("KM"))
                ((RadioButton)metrics.findViewById(R.id.radio_metrics)).setChecked(true);
            else
                ((RadioButton)metrics.findViewById(R.id.radio_miles)).setChecked(true);

                        /* Attach CheckedChangeListener to radio group */
                        metrics.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                                // Is the button now checked?
                                boolean checked = rb.isChecked();
                            	String select = "";
                                
                                // Check which radio button was clicked
                                switch(checkedId) {
                                    case R.id.radio_metrics:
                                        if (checked)
                                        {
                                            // Pirates are the best
                                        	select = getActivity().getBaseContext().getString(R.string.cfg_km);
                                        }
                                        break;
                                    case R.id.radio_miles:
                                        if (checked)
                                        {
                                            // Ninjas rule
                                        	select = getActivity().getBaseContext().getString(R.string.cfg_nautic);
                                        }
                                        break;
                                }
                                ConfigDialogListener activity = (ConfigDialogListener) getActivity();
                                activity.onFinishConfigDialog(select);
                                dismiss();                            
                                }
                        });

                    	maps = (RadioGroup) view.findViewById(R.id.MapsGroup);
                    	getDialog().setTitle(R.string.Cfg);
                        if(app.cfg_maps.contentEquals("Offline"))
                            ((RadioButton)maps.findViewById(R.id.OfflineButton)).setChecked(true);
                        else
                            ((RadioButton)maps.findViewById(R.id.OnlineButton)).setChecked(true);
                        /* Attach CheckedChangeListener to radio group */
                        maps.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                                // Is the button now checked?
                                boolean checked = rb.isChecked();
                            	String select = "";
                                
                                // Check which radio button was clicked
                                switch(checkedId) {
                                    case R.id.OfflineButton:
                                        if (checked)
                                        {
                                            // Pirates are the best
                                        	select = getActivity().getBaseContext().getString(R.string.Offline);
                                        }
                                        break;
                                    case R.id.OnlineButton :
                                        if (checked)
                                        {
                                            // Ninjas rule
                                        	select = getActivity().getBaseContext().getString(R.string.Online);
                                        }
                                        break;
                                }
                                ConfigDialogListener activity = (ConfigDialogListener) getActivity();
                                activity.onFinishConfigDialog(select);
                                dismiss();                            
                                }
                        });
        }
        catch(InflateException ie)
        {
        	Log.i("ERROR", ie.getMessage());
        }
        return view;
    }
	
    /*
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_metrics:
                if (checked)
                    // Pirates are the best
                break;
            case R.id.radio_miles:
                if (checked)
                    // Ninjas rule
                break;
        }
    }*/
}