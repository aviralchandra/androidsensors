
package com.rnav.navapp;

import java.sql.Timestamp;
import java.util.Date;

import android.hardware.Sensor;
import android.os.Bundle;
import android.app.Activity;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;


public class CopyOfMainActivity extends Activity implements SensorEventListener{

	Sensor accelerometer;
	Sensor compass;
	Sensor gyrometer;
	SensorManager sm;
	TextView acceleration;
	TextView gyroscope;
	
	double a[][];
	Timestamp timestamp;
	int i =0;
	double sx=0,sy=0,sz=0;
	double vx=0,vy=0,vz=0;
	
	double avgx = 0,avgy = 0,avgz = 0;

	
	/* Called When the activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sm=(SensorManager)getSystemService(SENSOR_SERVICE);

		accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		gyrometer = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		
		//compass = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	
		sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		acceleration = (TextView)findViewById(R.id.acceleration);
		gyroscope = (TextView)findViewById(R.id.gyroscope);
		
		
		a = new double[10][3];
		timestamp= new Timestamp(new Date().getTime());
	
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	//
	}
	 
	

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		int sensorType = event.sensor.getType();
		
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			
			Timestamp newtimestamp = new Timestamp(new Date().getTime());
			double timediff = newtimestamp.getTime()-timestamp.getTime();
			
			// Print timediff
			TextView tvTimeDiff =(TextView) findViewById(R.id.timediff);
			timediff=timediff/(1000000);
			tvTimeDiff.setText("Timediff: "+timediff);
			
			
			
			for(int z=0;z<3;z++){
				a[i][z]=event.values[z];
			}
			i++;
			if(i==10){
				
				double avgx=0;
				double avgy=0;
				double avgz=0;
				for(int j=0;j<10;j++){
					avgx+=a[j][0];
					avgy+=a[j][1];
					avgz+=a[j][2];
				}
				
				avgx=avgx/10;
				avgy=avgy/10;
				avgz=avgz/10;
				acceleration.setText("ax: "+  avgx +
						"\nay: "+ avgy +
						"\naz: "+ avgz);
				i=0;
				
				// Set Text Gyroscope
		
				
				// sx sy sz
				sx = vx * timediff * 10 + 0.5 * avgx * (timediff * timediff * 100);
				sy = vy * timediff * 10 + 0.5 * avgy * (timediff * timediff * 100);
				sz = vz * timediff * 10 + 0.5 * avgz * (timediff * timediff * 100);
				
				// Update velocity
				vx  = vx + avgx * timediff * 10;
				vy  = vy + avgy * timediff * 10;
				vz  = vz + avgz * timediff * 10;
				
				TextView tvDisp =(TextView) findViewById(R.id.displacement);
				tvDisp.setText("Sx: "+ sx + "\n" 
								+ "Sy: " +sy+ "\n"
								+ "Sz: " + sz);
				
			}
			
			timestamp=newtimestamp;
			
		}
		
		if (sensorType == Sensor.TYPE_GYROSCOPE){
			float gyro[] = new float[] {0, 0, 0} ;
	
			
			for(int z=0;z<3;z++){
				gyro[z] = event.values[z];
			}	
			
			gyroscope.setText("Gx: " + gyro[0] +"Gy: " + gyro[1] + "Gz: " + gyro[2]);
			
		}
		

		
	}
}