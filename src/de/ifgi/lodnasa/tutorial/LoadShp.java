package de.ifgi.lodnasa.tutorial;

//import de.ifgi.lodnasa.tutorial.GettingStarted.AppFrame;
import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.formats.shapefile.Shapefile;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecord;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.ExtrudedPolygon;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.VecBuffer;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

public class LoadShp extends ApplicationTemplate{
	public static class AppFrame  extends ApplicationTemplate.AppFrame{
		
		public AppFrame()
		{
			super(true,true,false);
			
			File file = new File("/home/vishal/cesium/Apps/SampleData/Vector_data_buildings/IIIT_campus_shapefile.shp");
			Shapefile shapefile = new Shapefile(file);
			
			RenderableLayer layer = new RenderableLayer();
			//set the basic attributes for the polygon
            ShapeAttributes normalAttributes = new BasicShapeAttributes();
            
            normalAttributes.setInteriorMaterial(Material.YELLOW);
            normalAttributes.setOutlineWidth(2);
            normalAttributes.setOutlineOpacity(0.2);
            normalAttributes.setDrawInterior(true);
            normalAttributes.setDrawInterior(true);
            normalAttributes.setInteriorOpacity(0.2);
            
            AVList list;
            while(shapefile.hasNext()){
            	ShapefileRecord record = shapefile.nextRecord();
            	//Read the height value 
            	
            	System.out.println("I am here");
            	Set<Map.Entry<String,Object>> attr = record.getAttributes().getEntries();
            	Object Arr[] = attr.toArray();
            	//System.out.println("Is this priting??"+((Map.Entry<String,Object>)Arr[0]).getValue());
            	String v = (String)(((Map.Entry<String,Object>)Arr[0]).toString());
            	String[] new_str = v.split("=");
            	//System.out.println();
            	//Object storiesObj = ((Map.Entry<String,Object>)Arr[0]).getValue();
            	int stories = Integer.parseInt(new_str[1]);
            	double height = 3.5*stories;
            	VecBuffer vectorBuffer = record.getPointBuffer(0);
            
            	ExtrudedPolygon poly = new ExtrudedPolygon();
            	poly.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
                poly.setOuterBoundary(vectorBuffer.getLocations(),height);
            	poly.setAttributes(normalAttributes);
            	
            	layer.addRenderable(poly);
            }
            shapefile.close();
            
            insertBeforeCompass(getWwd(),layer);
            
            this.getLayerPanel().update(this.getWwd());
		}
	}
	
	public static void main(String[] args)
    {
    	Configuration.setValue(AVKey.INITIAL_LATITUDE, 17.4450237);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, 78.3454232);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 2900);
        ApplicationTemplate.start("Getting Started with NASA World Wind", AppFrame.class);
    }
}
