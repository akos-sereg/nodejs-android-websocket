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
    
    private static final String TAG = "de.tavendo.test1";
    private final WebSocketConnection mConnection = new WebSocketConnection();
    
    private void start() {

        final String wsuri = "ws://192.168.1.100:3000";

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
		
		TextView axisXtextbox = (TextView)this.findViewById(R.id.axis_x);
		TextView axisYtextbox = (TextView)this.findViewById(R.id.axis_y);
		TextView axisZtextbox = (TextView)this.findViewById(R.id.axis_z);
		TextView console = (TextView)this.findViewById(R.id.console);
		
		float axisX = event.values[0];
	    float axisY = event.values[1];
	    float axisZ = event.values[2];
        
	    axisXtextbox.setText(String.valueOf(axisX));
	    axisYtextbox.setText(String.valueOf(axisY));
	    axisZtextbox.setText(String.valueOf(axisZ));
        
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
			return true;
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
	}

	@Override
	protected void onPause() {

		sm.unregisterListener(this);
		super.onStop();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
