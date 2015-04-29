package com.example.obdtracking;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Locale;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager sm;
	private WindowManager mWindowManager;
    private long lastSendTime;
    
    private static final String TAG = "de.tavendo.test1";
    private final WebSocketConnection mConnection = new WebSocketConnection();
    
    private void start() {

        final String wsuri = "ws://akosslgstorage.no-ip.org:3000";

        try {
           mConnection.connect(wsuri, new WebSocketHandler() {

              @Override
              public void onOpen() {
                 Log.d(TAG, "Status: Connected to " + wsuri);
                 mConnection.sendTextMessage("Hello, world!");
              }

              @Override
              public void onTextMessage(String payload) {
                 Log.d(TAG, "Got echo: " + payload);
              }

              @Override
              public void onClose(int code, String reason) {
                 Log.d(TAG, "Connection lost.");
              }
           });
        } catch (WebSocketException e) {

           Log.d(TAG, e.toString());
        }
     }

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_GYROSCOPE)
            return;
		
		TextView textview = (TextView)this.findViewById(R.id.textView1);
		TextView textview2 = (TextView)this.findViewById(R.id.textView2);
		TextView textview3 = (TextView)this.findViewById(R.id.textView3);
		TextView console = (TextView)this.findViewById(R.id.console);
		
		float axisX = event.values[0];
	    float axisY = event.values[1];
	    float axisZ = event.values[2];
        
        textview.setText(String.valueOf(axisX));
        textview2.setText(String.valueOf(axisY));
        textview3.setText(String.valueOf(axisZ));
        
        long millisec = 1000;
        
        if (lastSendTime == 0) {
        	lastSendTime = event.timestamp;
        }
        
        if ((event.timestamp - lastSendTime) > (100*millisec)) {
        	
        	try {
        		this.mConnection.sendTextMessage(String.format("{ \"axis_x\": \"%f\", \"axis_y\": \"%f\", \"axis_z\": \"%f\" }", axisX, axisY, axisZ));
        	}
        	catch (Exception err) {
        		StringWriter sw = new StringWriter();
        		PrintWriter pw = new PrintWriter(sw);
        		err.printStackTrace(pw);
        		
        		console.setText("SendMessage Ex: " + event.timestamp + ", " + err.getClass().toString() + ", " + sw.toString());
        	}
        	
        	lastSendTime = event.timestamp;
        	//console.setText("SendMessage: " + event.timestamp);
        }
        else {
        	//console.setText("TS: " + event.timestamp);
        }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get an instance of the SensorManager
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (sm.getSensorList(Sensor.TYPE_GYROSCOPE).size() != 0) {
			Sensor s = sm.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
			sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
		}
		
		// Get an instance of the WindowManager
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        // Start WebSocket client
        this.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	@Override
	protected void onPause() {

		sm.unregisterListener(this);
		super.onStop();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
