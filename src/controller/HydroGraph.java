package controller;

import org.jfree.data.xy.XYSeries;

import render.RenderAnalyticSurface;
import waterSurfaceModel.CreateDiffMaps;
import waterSurfaceModel.SurfaceModelClass;

public class HydroGraph {
	public static XYSeries getXYData(int x,int y,double lat,double lon){
		
		//Get this from RenderAnalyticSurface
		
		SurfaceModelClass[] timeData = RenderAnalyticSurface.obj.getSurfaceModel();
		//String label = "("+x+","+y+")";
		String Lat = (String.format("%.6g%n", new Double(lat)).toString());
		String Lon = (String.format("%.6g%n", new Double(lon)).toString());
		
		String label = "("+Lat+","+Lon+")";
		XYSeries series= new XYSeries(label);
		for(int i=0 ; i<timeData.length ; i++){
			series.add((double)(i*RenderAnalyticSurface.Simulationinterval)/60, (double)timeData[i].getHeight(x, y));
		}
		return series;
	}
}
