
package com.rnav.navapp;

import java.sql.Timestamp;
import java.util.Date;
import java.lang.Math;

import org.achartengine.GraphicalView;

import android.hardware.Sensor;
import android.os.Bundle;
import android.app.Activity;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener{

	Sensor accelerometer;
	Sensor compass;
	Sensor gyrometer;
	SensorManager SensorMan;
	TextView acceleration;
	TextView gyroscope;
	TextView compassDisp;
	
	double[] acc = new double[3];
	double a[][];
	Timestamp timestamp;
	int i =0;
	double sx=0,sy=0,sz=0;
	double vx=0,vy=0,vz=0;
	
	double avgx = 0,avgy = 0,avgz = 0;
	private static final float NS2S = 1.0f / 1000000000.0f;
	private final double[] deltaRotationVector = new double[4];

	
	/* Called When the activity is first created */
	
	private static GraphicalView view;
	private Linegraph line = new Linegraph();
	double[] test = new double[100];
	
	//private point p  = new point();
	private static Thread thread;
	//private point p = new point(double x = 0, double y = 0);	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SensorMan=(SensorManager)getSystemService(SENSOR_SERVICE);
		
		accelerometer = SensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gyrometer = SensorMan.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		compass = SensorMan.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	
		SensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		SensorMan.registerListener(this, gyrometer, SensorManager.SENSOR_DELAY_NORMAL);
		SensorMan.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);
		
		acceleration = (TextView)findViewById(R.id.acceleration);
		gyroscope = (TextView)findViewById(R.id.gyroscope);
		compassDisp = (TextView)findViewById(R.id.compass);
		
		
		timestamp= new Timestamp(new Date().getTime());
		thread = new Thread(){
			public void run()
			{
			for(int i=0; i < 10; i++)
			{
				try {
					Thread.sleep(1000);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
				point p = Data.getDataFromReceiver(i);
				line.addNewPoints(p);
				view.repaint();
			}		
		   }
		};
		thread.start();
	}
	
	

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	//
	}
	 
	

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		int sensorType = event.sensor.getType();
		double samp_T  = 0.1;
		int N = 100;
		final double[] Rxest = new double[N];
		double[] Ryest = new double[N];
		double[] Rzest = new double[N];
		double[] Rxacc = new double[N];
		double[] Ryacc = new double[N];
		double[] Rzacc = new double[N];
		double[] Axz = new double[N];
		double[] Ayz = new double[N];
		double[] Axy = new double[N];
		double[] RateAxz = new double[N];
		double[] RateAxy = new double[N];
		double[] RateAyz = new double[N];
		
		RateAxz[0] = 0;
		RateAyz[0] = 0;
		RateAxy[0] = 0;
		
		for(int k = 1; k<N; k++) {
			//k = (k-1)*N;
			 sensorType = Sensor.TYPE_ACCELEROMETER; 
				//	Timestamp newtimestamp = new Timestamp(new Date().getTime());
					//double timediff = newtimestamp.getTime()-timestamp.getTime();
			
			/* Print timediff
			TextView tvTimeDiff =(TextView) findViewById(R.id.timediff);
			timediff=timediff/(1000000);
			tvTimeDiff.setText("Timediff: "+timediff);*/
			for (int z=0; z<3; z++){
				acc[z] = event.values[z];
			}
			
			//timestamp=newtimestamp;
			double R = Math.sqrt(acc[0]*acc[0]+acc[1]*acc[1]+acc[2]*acc[2]);
			Rxest[k-1] = acc[0];
			Ryest[k-1] = acc[1];
			Rzest[k-1] = acc[2];
			
			Rxacc[k-1] = (Rxest[k-1]/R)*Math.cos(Math.acos(Rxest[k-1]/R));
			Ryacc[k-1] = (Ryest[k-1]/R)*Math.cos(Math.acos(Ryest[k-1]/R));
			Rzacc[k-1] = (Rzest[k-1]/R)*Math.cos(Math.acos(Rzest[k-1]/R));
			
			double Racc = (Rxacc[k-1]*Rxacc[k-1]+Ryacc[k-1]*Ryacc[k-1]+Rzacc[k-1]*Rzacc[k-1]);
			
			Rxacc[k-1] = Rxacc[k-1]/Racc;
			Ryacc[k-1] = Ryacc[k-1]/Racc;
			Rzacc[k-1] = Rzacc[k-1]/Racc;
			
			Axz[k-1] = Math.atan2(Rxest[k-1], Rzest[k-1]);
			Ayz[k-1] = Math.atan2(Ryest[k-1], Rzest[k-1]);
			Axy[k-1] = Math.atan2(Rxest[k-1], Ryest[k-1]);
			
			
			
			sensorType = Sensor.TYPE_GYROSCOPE;
				float gyro[] = new float[] {0, 0, 0} ;
				
				//Timestamp newtimestamp = new Timestamp(new Date().getTime());
				//float dT = event.timestamp-timestamp.getTime();
				
				for(int m=0;m<3;m++){
					gyro[m] = event.values[m];
				}	
				
			RateAxz[k] = gyro[0];
			RateAyz[k] = gyro[1];
			RateAxy[k] = gyro[2];
			
			double Rxgyro = gyro[0];
			double Rygyro = gyro[1];
			double Rzgyro = gyro[2];
			
			double RateAxzAvg = (RateAxz[k]+RateAxz[k-1])/2;
			
			Axz[k] = Axz[k-1] + RateAxzAvg*samp_T;
			Ayz[k] = Axz[k-1] + RateAxzAvg*samp_T;
			Axy[k] = Axz[k-1] + RateAxzAvg*samp_T;
			
			double cotxz = 1/(Math.tan(Axz[k]));
			double cotyz = 1/(Math.tan(Ayz[k]));
			double cotxy = 1/(Math.tan(Axy[k]));
			
			double secxz = 1/(Math.cos(Axz[k]));
			double secyz = 1/(Math.cos(Ayz[k]));
			double secxy = 1/(Math.cos(Axy[k]));
			
			Rxgyro = 1/(Math.sqrt(1+(cotxz)*(cotxz)*(secxz)*(secxz)));
			Rygyro = 1/(Math.sqrt(1+(cotyz)*(cotyz)*(secyz)*(secyz)));
			Rzgyro = 1/(Math.sqrt(1+(cotxy)*(cotxy)*(secxy)*(secxy)));
			
			double Rgyro = Math.sqrt(Rxgyro*Rxgyro + Rygyro*Rygyro + Rzgyro*Rzgyro);
									
			int w = 15;
			
			Rxest[k] = (Rxacc[k-1]+Rxgyro*w)/(1+w);
			Ryest[k] = (Ryacc[k-1]+Rygyro*w)/(1+w);
			Rzest[k] = (Rzacc[k-1]+Rzgyro*w)/(1+w);
			
			double R1 = Math.sqrt(Rxest[k]*Rxest[k] + Ryest[k]*Ryest[k] + Rzest[k]*Rzest[k]);
			
			Rxest[k] = Rxest[k]/R1;
			Ryest[k] = Ryest[k]/R1;
			Rzest[k] = Rzest[k]/R1;
			test[k] = (Rxest[k]);
			
			gyroscope.setText("ax: "+ Rxest[k] +"\nay: "+Ryest[k]+"\naz: "+Rzest[k] );
			
			System.out.format("%-15s%-15s%n", "x-acc");
		}
		
			for(int i=0; i<test.length; i++) {
				System.out.format("%-15s%-15s%n", i+1, test[i]);
			}
	}
	
	public double[] getAccx() {
		return test;
	}

	public void setAccx(double[] test) {
		this.test = test;
	}
	@Override
	protected void onStart() {
		super.onStart();
		view = line.getView(this);
		setContentView(this);
	}

	private void setContentView(MainActivity mainActivity) {
		// TODO Auto-generated method stub
		
	}
}
			/*
			
			
			double[] Axr = new double[2];
			double[] Ayr = new double[2];
			double[] Azr = new double[2];
			
			Axr[0] = Math.acos(Rx/R);
			Ayr[0] = Math.acos(Ry/R);
		    Azr[0] = Math.acos(Rz/R);
			*/
			//double T  = 0.01;//Sampling time
			
			
			//double Racc = Math.sqrt(Rxacc*Rxacc+Ryacc*Ryacc+Rzacc*Rzacc);
			//Rxacc = Rxacc/Racc;
			//Ryacc = Ryacc/Racc;
			//Rzacc = Rzacc/Racc;
																
		
		/*
		if (sensorType == Sensor.TYPE_GYROSCOPE){
			float gyro[] = new float[] {0, 0, 0} ;
			
		//	Timestamp newtimestamp = new Timestamp(new Date().getTime());
			float dT = event.timestamp-timestamp.getTime();
			
			for(int z=0;z<3;z++){
				gyro[z] = event.values[z];
			}
			
			double omegaMagnitude = (gyro[0]*gyro[0]+gyro[1]*gyro[1]+gyro[2]*gyro[2]);
			omegaMagnitude = Math.sqrt(omegaMagnitude);
			
			if(omegaMagnitude>1){
				for(int z=0;z<3;z++){
					gyro[z] /= omegaMagnitude;
				}
			}
			
			double theta = omegaMagnitude*dT/2.0f;
			double sinx =  Math.sin(theta);
			double cosx = Math.cos(theta);
			double [] trig_func = new double[4];
			trig_func[0] = sinx;
			trig_func[1] = sinx;
			trig_func[2] = sinx;
			trig_func[3] = cosx;
			
			
			for(int z=0;z<3;z++){
				deltaRotationVector[z] = trig_func[z]*gyro[z];
			}
			
				deltaRotationVector[3] = cosx;
						
			gyroscope.setText("Qx: " + Double.toString(deltaRotationVector[0]) +"\nQy: " + Double.toString(deltaRotationVector[1]) + "\nQz: " + Double.toString(deltaRotationVector[2]));
			
		}
			}
	
		
		if (sensorType == Sensor.TYPE_MAGNETIC_FIELD){
			float compass_readings[] = new float[] {0, 0, 0};
			
			for (int z=0; z<3; z++){
				compass_readings[z] = event.values[z];				
			}
	
			compassDisp.setText("Cx: "+ compass_readings[0] + "\nCy: " + compass_readings[1] + "\nCz:" + compass_readings[2]);
			
			
		}
	}
}

		
	

	
	//private float cos(float theta) {
		// TODO Auto-generated method stub
		//return 0;
	//}

	//private float sin(float theta) {
		// TODO Auto-generated method stub
		//return 0;
	//}

	//private float sqrt(float omegaMagnitude) {
		// TODO Auto-generated method stub
		//return 0;
	//}
//}*/