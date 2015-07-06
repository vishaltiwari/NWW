package render;

import java.util.ArrayList;

import controller.StartUpGUI;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Cylinder;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;

public class CylinderPole {
	private String name;
	private double Lat;
	private double Lon;
	RenderableLayer layer;
	public CylinderPole(double Lat,Double Lon,int count,RenderableLayer layer){
		this.name = Integer.toString(count);
		this.Lat = Lat;
		this.Lon = Lon;
		this.layer = layer;
	}
	public void createPole(){
		ShapeAttributes normalAttributes = new BasicShapeAttributes();
		ShapeAttributes normalAttributes2 = new BasicShapeAttributes();
        
        normalAttributes.setInteriorMaterial(Material.RED);
        normalAttributes.setOutlineWidth(0);
        normalAttributes.setOutlineOpacity(0);
        normalAttributes.setDrawInterior(true);
        //normalAttributes.setDrawInterior(true);
        normalAttributes.setInteriorOpacity(1);
        
        /*normalAttributes2.setInteriorMaterial(Material.RED);
        normalAttributes2.setOutlineWidth(0);
        normalAttributes2.setOutlineOpacity(1);
        normalAttributes2.setDrawInterior(true);
        //normalAttributes2.setDrawInterior(true);
        normalAttributes2.setInteriorOpacity(1);*/
        
        Cylinder pole = new Cylinder(Position.fromDegrees(Lat, Lon),10,0.15);
        
        /*ArrayList<Position> positions = new ArrayList<Position>();
        positions.add(Position.fromDegrees(Lat, Lon, 0));
        positions.add(Position.fromDegrees(Lat, Lon, 2));
        positions.add(Position.fromDegrees(Lat+0.0001, Lon+0.0001, 1));*/
        
        /*Polygon poly = new Polygon(positions);
        poly.setAttributes(normalAttributes2);
        poly.setAltitudeMode(10);
        poly.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
        */
        pole.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
        pole.setAttributes(normalAttributes);
        
        this.layer.addRenderable(pole);
        /*this.layer.addRenderable(poly);
        */
	}
}
