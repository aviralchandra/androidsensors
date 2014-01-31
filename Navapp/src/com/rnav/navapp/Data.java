package com.rnav.navapp;

import java.util.*;

public class Data {

	public static double[] st = new double[100];
	
	public final static point getDataFromReceiver(int x)
	{
		return new point(x, fetchData(x));
}

	

	private static double fetchData(int x) {
		//Random random = new Random();
		MainActivity main = new MainActivity();
		
		for(int i=0; i<100; i++) {
		st[i] = main.getAccx()[i];
		//return st[i];
		}
		//return random.nextFloat();
		// TODO Auto-generated method stub
		return st[x];
		
	}
}