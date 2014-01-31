package com.rnav.navapp;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.*;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.util.*;
import org.achartengine.renderer.*;


import android.content.Context;
import android.graphics.Color;


public class Linegraph {
	
	private GraphicalView view;
	
	private TimeSeries dataset = new TimeSeries("Rain Fall");
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	
	private XYSeriesRenderer renderer = new XYSeriesRenderer();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	
	public Linegraph()
	{
		
		renderer.setColor(Color.WHITE);
		renderer.setPointStyle(PointStyle.SQUARE);
		renderer.setFillPoints(true);
		
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXTitle("samples");
		mRenderer.setYTitle("Gyroscopic Data");
		
		mRenderer.addSeriesRenderer(renderer);
		
		
	}

	public GraphicalView getView(Context context)
	{
		view = ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return view;
	}
	
	public void addNewPoints(point p)
	{
		dataset.add(p.getX(), p.getY());
	}
	}

