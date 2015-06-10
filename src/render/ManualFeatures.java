package render;

import java.awt.Color;
import java.util.ArrayList;

import controller.CrsConverterGDAL;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.ExtrudedPolygon;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Polyline;
import gov.nasa.worldwind.render.ShapeAttributes;

public class ManualFeatures{
	CrsConverterGDAL convert = new CrsConverterGDAL();
	
	public void renderRoad(RenderableLayer layer,Color color){
		ShapeAttributes sideAttributes = new BasicShapeAttributes();
		
		Material mat = new Material(color);
		
        sideAttributes.setInteriorMaterial(mat);
        
        sideAttributes.setOutlineOpacity(0.5);
        sideAttributes.setInteriorOpacity(0.5);
        sideAttributes.setOutlineMaterial(mat);
        sideAttributes.setOutlineWidth(2);
        sideAttributes.setDrawOutline(true);
        sideAttributes.setDrawInterior(true);
        sideAttributes.setEnableLighting(true);
        
        //RoadPolyline 1
        ArrayList<Position> linePos= new ArrayList<Position>();
        linePos.add(Position.fromDegrees(52.3292, 13.0390));
        linePos.add(Position.fromDegrees(52.3268, 13.0387));
        linePos.add(Position.fromDegrees(52.3271, 13.0369));
        
        linePos.add(Position.fromDegrees(52.3273, 13.0354));
        linePos.add(Position.fromDegrees(52.3272, 13.0327));
        linePos.add(Position.fromDegrees(52.3231, 13.0332));
        linePos.add(Position.fromDegrees(52.3232, 13.0386));
        linePos.add(Position.fromDegrees(52.3268, 13.0387));
        linePos.add(Position.fromDegrees(52.3267, 13.0405));
        
        Polyline line = new Polyline(linePos);
        line.setColor(color);
        line.setLineWidth(5);
        line.setFollowTerrain(true);
        line.setTerrainConformance(50);
        
        layer.addRenderable(line);
        
        //RoadPolyline 2
        linePos= new ArrayList<Position>();
        linePos.add(Position.fromDegrees(52.3272, 13.0327));
        linePos.add(Position.fromDegrees(52.3295, 13.0329));
        linePos.add(Position.fromDegrees(52.3292, 13.0390));
        linePos.add(Position.fromDegrees(52.3297, 13.0418));
        linePos.add(Position.fromDegrees(52.3262, 13.0423));
        linePos.add(Position.fromDegrees(52.3260, 13.0387));
        
        line = new Polyline(linePos);
        line.setColor(color);
        line.setLineWidth(5);
        line.setFollowTerrain(true);
        line.setTerrainConformance(50);
        
        layer.addRenderable(line);
        
        //RoadPolyline 3
        linePos= new ArrayList<Position>();
        linePos.add(Position.fromDegrees(52.3245, 13.0386));
        linePos.add(Position.fromDegrees(52.3243, 13.0331));
        
        line = new Polyline(linePos);
        line.setColor(color);
        line.setLineWidth(5);
        line.setFollowTerrain(true);
        line.setTerrainConformance(50);
        
        layer.addRenderable(line);
        
        //Road 4
        linePos= new ArrayList<Position>();
        linePos.add(Position.fromDegrees(52.3244, 13.0355));
        linePos.add(Position.fromDegrees(52.3273, 13.0351));
        
        line = new Polyline(linePos);
        line.setColor(color);
        line.setLineWidth(10);
        line.setFollowTerrain(true);
        line.setTerrainConformance(50);
        
        layer.addRenderable(line);
        
	}
	public void renderExtrudedArc(RenderableLayer layer,double R1,double R2, int noPoints,double height,Color color){
		ShapeAttributes sideAttributes = new BasicShapeAttributes();
		
		Material mat = new Material(color);
		
        sideAttributes.setInteriorMaterial(mat);
        
        sideAttributes.setOutlineOpacity(0.8);
        sideAttributes.setInteriorOpacity(0.8);
        sideAttributes.setOutlineMaterial(mat);
        sideAttributes.setOutlineWidth(0);
        sideAttributes.setDrawOutline(false);
        sideAttributes.setDrawInterior(true);
        sideAttributes.setEnableLighting(true);
        
        double xo = -538.04;
        double yo = -503.6;
        ArrayList<Position> curveSurface = new ArrayList<Position>();
        double[] posList = new double[3];
        
        double[] pos = new double[3];
        //pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
    	//curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));
        
    	//Smaller arc points
        double multiplier = 90/noPoints;
        for(int i=0 ; i<noPoints ; i++){
        	posList[0] = xo + R1 * Math.cos(i*multiplier * Math.PI / 180);
        	posList[1] = yo + R1 * Math.sin(i*multiplier * Math.PI / 180);
        	posList[2] = height;
        	
        	pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
        	curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));
        }
        
        posList[0] = xo + 0;
        posList[1] = yo + R1;
        posList[2] = height;
        
        pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
    	curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));
    	
    	posList[0] = xo + 0;
        posList[1] = yo + R2;
        posList[2] = height;
        
        pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
    	curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));
    	
        //Larger arc points
        for(int i=noPoints-1 ; i>=0 ; i--){
        	posList[0] = xo + R2 * Math.cos(i*multiplier * Math.PI / 180);
        	posList[1] = yo + R2 * Math.sin(i*multiplier * Math.PI / 180);
        	posList[2] = height;
        	
        	pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
        	curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));
        }
        // Last point
        /*posList[0] = xo + R1;
        posList[1] = 0;
        posList[2] = height;
        
        pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
    	curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));*/
        
    	//add the first point to curveSurface
    	ExtrudedPolygon poly = new ExtrudedPolygon(curveSurface);
        poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        
        poly.setAttributes(sideAttributes);
        
        //poly.setCapAttributes(capAttributes);
        layer.addRenderable(poly);
        
	}
	public void renderExtrudedSemiCircle(RenderableLayer layer,double R, int noPoints,double height,Color color){
		ShapeAttributes sideAttributes = new BasicShapeAttributes();
		
		Material mat = new Material(color);
		
        sideAttributes.setInteriorMaterial(mat);
        
        sideAttributes.setOutlineOpacity(1);
        sideAttributes.setInteriorOpacity(1);
        sideAttributes.setOutlineMaterial(mat);
        sideAttributes.setOutlineWidth(2);
        sideAttributes.setDrawOutline(true);
        sideAttributes.setDrawInterior(true);
        sideAttributes.setEnableLighting(true);
        
        /*ShapeAttributes capAttributes = new BasicShapeAttributes(sideAttributes);
        capAttributes.setInteriorMaterial(Material.YELLOW);
        capAttributes.setInteriorOpacity(0.25);
        capAttributes.setDrawInterior(true);*/
        //draw the curve
        double xo = -538.04;
        double yo = -503.6;
        ArrayList<Position> curveSurface = new ArrayList<Position>();
        double[] posList = new double[3];
        
        posList[0] = xo + 0;
        posList[1] = yo + R;
        posList[2] = height;
        
        double[] pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
    	curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));

    	//origin
    	posList[0] = xo + 0;
        posList[1] = yo + 0;
        posList[2] = height;
        
        pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
    	curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));
    	
    	/*posList[0] = xo + R;
        posList[1] = yo + 0;
        posList[2] = height;
        
        pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
    	curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));*/
        
        double multiplier = 90/noPoints;
        for(int i=0 ; i<noPoints ; i++){
        	posList[0] = xo + R * Math.cos(i*multiplier * Math.PI / 180);
        	posList[1] = yo + R * Math.sin(i*multiplier * Math.PI / 180);
        	posList[2] = height;
        	
        	pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
        	curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));
        }
        
        posList[0] = xo + 0;
        posList[1] = yo + R;
        posList[2] = height;
        
        pos = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", posList);
    	curveSurface.add(Position.fromDegrees(pos[1],pos[0],pos[2]));
        
    	//add the first point to curveSurface
    	ExtrudedPolygon poly = new ExtrudedPolygon(curveSurface);
        poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        
        poly.setAttributes(sideAttributes);
        
        //poly.setCapAttributes(capAttributes);
        layer.addRenderable(poly);
	}
	
	public void renderBoundingWall(RenderableLayer layer){
		 // Create and set an attribute bundle.
        ShapeAttributes sideAttributes = new BasicShapeAttributes();
        sideAttributes.setInteriorMaterial(Material.MAGENTA);
        sideAttributes.setOutlineOpacity(0.5);
        sideAttributes.setInteriorOpacity(0.5);
        sideAttributes.setOutlineMaterial(Material.GREEN);
        sideAttributes.setOutlineWidth(2);
        sideAttributes.setDrawOutline(true);
        sideAttributes.setDrawInterior(true);
        sideAttributes.setEnableLighting(true);
        
        ShapeAttributes capAttributes = new BasicShapeAttributes(sideAttributes);
        capAttributes.setInteriorMaterial(Material.YELLOW);
        capAttributes.setInteriorOpacity(0.25);
        capAttributes.setDrawInterior(true);

        double[] lowerCorner = {-648.04,-563.6 ,50.0};
        double[] upperCorner = {270.42, 301.74,50.0};
        double[] leftCorner = {-648.04 , 301.74, 50.0};
        double[] rightCorner = {270.42, -563.6,50.0};

        ArrayList<Position> boundry = new ArrayList<Position>();

        double[] points = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", upperCorner);
        boundry.add(Position.fromDegrees(points[1],points[0],points[2]));
        
        points = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", leftCorner);
        boundry.add(Position.fromDegrees(points[1],points[0],points[2]));
        
        points = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", lowerCorner);
        boundry.add(Position.fromDegrees(points[1],points[0],points[2]));
        
        points = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", rightCorner);
        boundry.add(Position.fromDegrees(points[1],points[0],points[2]));

        points = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", upperCorner);
        boundry.add(Position.fromDegrees(points[1],points[0],points[2]));
        
        ExtrudedPolygon poly = new ExtrudedPolygon(boundry);
        poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        
        poly.setAttributes(sideAttributes);
        
        poly.setCapAttributes(capAttributes);
        layer.addRenderable(poly);
	}
}
