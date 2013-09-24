package com.example.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

	Sensor accelerometer;
	SensorManager sm;
	TextView acceleration;
		
	Sensor linearaccel;
	//Sensor gravity;
	
	TextView Linearaccel;
	SensorManager sm1;
	SensorManager sm2;
	//private float[] gravity;
	
	
		
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Accelerometer
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        acceleration=(TextView)findViewById(R.id.acceleration);
        
        //Linear acceleration
        sm1 = (SensorManager)getSystemService(SENSOR_SERVICE);
        linearaccel=sm1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //gravity=sm2.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sm1.registerListener(this, linearaccel, SensorManager.SENSOR_DELAY_NORMAL);
        Linearaccel=(TextView)findViewById(R.id.Linearaccel);
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		final float alpha = (float)0.8;
		acceleration.setText("X: "+event.values[0]+
				"\nY: "+event.values[1]+
				"\nZ: "+event.values[2]);
		
		
		float[] gravity = new float[3];
		gravity[0] = SensorManager.GRAVITY_EARTH;
		gravity[1] = SensorManager.GRAVITY_EARTH;
		gravity[2] = SensorManager.GRAVITY_EARTH;

		gravity[0]=alpha*gravity[0] + (1-alpha)*event.values[0];
		gravity[1]=alpha*gravity[1] + (1-alpha)*event.values[1];
		gravity[2]=alpha*gravity[2] + (1-alpha)*event.values[2];
		
		Linearaccel.setText("X1: "+(-gravity[0]+event.values[0])+
				"\nY1: "+(-gravity[1]+event.values[1])+
				"\nZ1: "+(-gravity[2]+event.values[2]));
	}
    
}
