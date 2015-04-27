package controller;

import java.util.List;

import citygmlModel.Buildings;
import render.AppFrame.RenderFrame;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

public class StartUp {
	//The control starts from here
	public static void main(String argv[]){
		
        Configuration.setValue(AVKey.INITIAL_LATITUDE, 76.51134570525976);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, 9.019376924014613e-5);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10);
		/*Configuration.setValue(AVKey.INITIAL_LATITUDE,-8.97704839361097);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE,  42.919814512930095);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10);*/
        
        ApplicationTemplate.start("Getting Started with NASA World Wind", RenderFrame.class);
	}
}
