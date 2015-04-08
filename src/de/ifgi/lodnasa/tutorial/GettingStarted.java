package de.ifgi.lodnasa.tutorial;

import java.util.ArrayList;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWind;
//import gov.nasa.worldwind.layers.LatLonGraticuleLayer;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.ExtrudedPolygon;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.BasicDragger;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;


public class GettingStarted extends ApplicationTemplate{
	 public static class AppFrame extends ApplicationTemplate.AppFrame
	    {
	        public AppFrame()
	        {
	            super(true, true, false);

	            //this.getWwd().addSelectListener(new BasicDragger(this.getWwd()));
	            
	            //create layer for a polygon
	            RenderableLayer layer = new RenderableLayer();
	            
	            //set the basic attributes for the polygon
	            ShapeAttributes normalAttributes = new BasicShapeAttributes();
	            
	            normalAttributes.setInteriorMaterial(Material.YELLOW);
	            normalAttributes.setOutlineWidth(2);
	            normalAttributes.setOutlineOpacity(0.5);
	            normalAttributes.setDrawInterior(true);
	            normalAttributes.setDrawInterior(true);
	            
	            //define the coordinates position
	            ArrayList<Position> positions = new ArrayList<Position>();
	            positions.add(Position.fromDegrees(52, 10, 5e4));
	            positions.add(Position.fromDegrees(55, 11, 5e4));
	            positions.add(Position.fromDegrees(55, 11, 5e4));
	            positions.add(Position.fromDegrees(52, 14, 5e4));
	            positions.add(Position.fromDegrees(52, 10, 5e4));
	            
	            //Polygon poly = new Polygon(positions);
	            ExtrudedPolygon poly = new ExtrudedPolygon(positions);
	            poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
	            
	            poly.setAttributes(normalAttributes);
	            poly.setValue(AVKey.DISPLAY_NAME, "My extruded first polygon");
	            
	            //Added the renderables to the layer
	            layer.addRenderable(poly);
	            
	            //add the layer to the model
	            insertBeforeCompass(getWwd(),layer);
	            
	            //update the layer panel
	            //this.getLayerPanel().update(this.getWwd());
	            
	            // Add graticule
	            //insertBeforePlacenames(this.getWwd(), new LatLonGraticuleLayer());

	            // Update layer
	            //this.getLayerPanel().update(this.getWwd());
	        }
	    }

	    public static void main(String[] args)
	    {
	    	Configuration.setValue(AVKey.INITIAL_LATITUDE, 54);
	        Configuration.setValue(AVKey.INITIAL_LONGITUDE, 13);
	        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 190e4);
	        ApplicationTemplate.start("Getting Started with NASA World Wind", AppFrame.class);
	    }
}