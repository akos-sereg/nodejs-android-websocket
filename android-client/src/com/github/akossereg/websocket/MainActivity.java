package com.github.akossereg.websocket;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager sm;
    private long lastSendTime;
    
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mOrientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    
    private static final String TAG = "de.tavendo.test1";
    private WebSocketConnection mConnection;
    
    private void start() {

        final String wsuri = "ws://akosslgstorage.no-ip.org:3000";

        try {
        	mConnection = new WebSocketConnection();
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
		
		if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
		
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            //Log.i("OrientationTestActivity", String.format("Orientation: %f, %f, %f", mOrientation[0], mOrientation[1], mOrientation[2]));
        }
		
		if (event.sensor.getType() != Sensor.TYPE_GYROSCOPE && mLastAccelerometerSet && mLastMagnetometerSet) {
			
			TextView axisXtextbox = (TextView)this.findViewById(R.id.axis_x);
			TextView axisYtextbox = (TextView)this.findViewById(R.id.axis_y);
			TextView axisZtextbox = (TextView)this.findViewById(R.id.axis_z);
			TextView console = (TextView)this.findViewById(R.id.console);
			
			float axisX = event.values[0];
		    float axisY = event.values[1];
		    float axisZ = event.values[2];
	        
		    axisXtextbox.setText(String.valueOf(mOrientation[0]));
		    axisYtextbox.setText(String.valueOf(mOrientation[1]));
		    axisZtextbox.setText(String.valueOf(mOrientation[2]));
	        
	        long millisec = 200; // Expect messages to be sent in every "millisec" interval
	        
	        if (lastSendTime == 0) {
	        	lastSendTime = event.timestamp;
	        }
	        
	        if ((event.timestamp - lastSendTime) > (100*millisec) && mConnection != null && mConnection.isConnected()) {
	        	try {
	        		this.mConnection.sendTextMessage(String.format("{ \"axis_x\": \"%f\", \"axis_y\": \"%f\", \"axis_z\": \"%f\", \"orientation_x\": \"%f\", \"orientation_y\": \"%f\", \"orientation_z\": \"%f\"  }", 
	        				axisX, axisY, axisZ, mOrientation[0], mOrientation[1], mOrientation[2]));
	        	}
	        	catch (Exception err) {
	        		StringWriter sw = new StringWriter();
	        		PrintWriter pw = new PrintWriter(sw);
	        		err.printStackTrace(pw);
	        		console.setText("SendMessage Ex: " + event.timestamp + ", " + err.getClass().toString() + ", " + sw.toString());
	        	}
	        	
	        	lastSendTime = event.timestamp;
	        }
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
		
		mAccelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
        // Start WebSocket client
        this.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			mConnection = null;
			this.start();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (sm.getSensorList(Sensor.TYPE_GYROSCOPE).size() != 0) {
			Sensor s = sm.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
			sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
		}
		
		mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;
        sm.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
        sm.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
