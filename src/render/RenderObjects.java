package render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import controller.CrsConverter;
import controller.CrsConverterGDAL;
import citygmlModel.BuildingsClass;
import citygmlModel.CoordinateClass;
import citygmlModel.PolygonClass;
import citygmlModel.SurfaceMember;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.WWUtil;

public class RenderObjects {
	private ShapeAttributes shapeAttribute;
	private String crs;
	//CrsConverterGDAL convert = new CrsConverterGDAL();
	
	
	public RenderObjects(String crs){
		this.shapeAttribute = new BasicShapeAttributes();
		this.crs = crs;
	}
	public RenderableLayer startRenderingBuildings(BuildingsClass buildings){
		RenderableLayer layer = new RenderableLayer();
		
		List<SurfaceMember> surfaceList = buildings.getSurfacePolygon();
		List<SurfaceMember> walls = buildings.getWalls();
		List<SurfaceMember> roofs = buildings.getRoofs();
		List<SurfaceMember> solids = buildings.getSolid();
		
		/*ManualFeatures features = new ManualFeatures();
		
		//Road
		features.renderRoad(layer, new Color(0,0,0));
		//features.renderBoundingWall(layer);
		Color color = new Color(0, 15, 35);
		
		
		features.renderExtrudedSemiCircle(layer, 150, 20, 6, color);
		
		features.renderExtrudedArc(layer, 150, 200, 20, 4.5, new Color(0, 31, 76));
		features.renderExtrudedArc(layer, 200, 250, 20, 4.4, new Color(0, 31, 86));
		features.renderExtrudedArc(layer, 250, 300, 20, 4.1, new Color(0, 31, 96));
		features.renderExtrudedArc(layer, 300, 350, 20, 3.9, new Color(0, 31, 106));
		features.renderExtrudedArc(layer, 350, 400, 20, 3.6, new Color(0, 31, 116));
		features.renderExtrudedArc(layer, 400, 450, 20, 3.3, new Color(0, 31, 126));
		features.renderExtrudedArc(layer, 450, 500, 20, 3.1, new Color(0, 31, 136));
		features.renderExtrudedArc(layer, 500, 550, 20, 3, new Color(0, 31, 146));
		features.renderExtrudedArc(layer, 550, 600, 20, 2.9, new Color(0, 31, 156));
		features.renderExtrudedArc(layer, 600, 650, 20, 2.8, new Color(0, 31, 166));
		features.renderExtrudedArc(layer, 650, 700, 20, 2.8, new Color(0, 31, 176));
		features.renderExtrudedArc(layer, 700, 750, 20, 2.6, new Color(0, 31, 186));*/
		//features.renderExtrudedArc(layer, 750, 800, 20, 2.4, new Color(0, 31, 196));
		
		/*
		//System.out.println("r "+color.getRed()+ " g "+ color.getGreen() + " b "+color.getBlue());
		features.renderExtrudedSemiCircle(layer, 680, 20, 15, color);
		color = color.brighter().brighter();
		features.renderExtrudedArc(layer, 880, 980, 20, 10, new Color(0,0,100));*/
		/*features.renderExtrudedSemiCircle(layer, 780, 20, 14.5,color.brighter());
		features.renderExtrudedSemiCircle(layer, 880, 20, 14,color.brighter().brighter());
		features.renderExtrudedSemiCircle(layer, 980, 20, 13.5,color.brighter().brighter().brighter());*/
		
		if(surfaceList.size() > 0)
			renderGroundSurface(surfaceList,layer);
		if(walls.size() > 0)
			renderWalls(walls,layer);
		if(roofs.size() > 0)
			renderRoofs(roofs,layer);
		if(solids.size() > 0)
			renderSolids(solids,layer);
		
		
		
		return layer;
	}
	private void renderGroundSurface(List<SurfaceMember> surfaceList,RenderableLayer layer){
		System.out.println("Rendering GroundSurface");
		
		ShapeAttributes normalAttributes = new BasicShapeAttributes();
		
		normalAttributes.setInteriorMaterial(Material.YELLOW);
        normalAttributes.setOutlineWidth(2);
        normalAttributes.setOutlineOpacity(0.5);
        normalAttributes.setDrawInterior(true);
        normalAttributes.setDrawInterior(true);
        
		for(SurfaceMember surface : surfaceList){
			PolygonClass polygon = surface.getPolygon();
			List<CoordinateClass> coords = polygon.getPolygon();
			ArrayList<Position> Positions = new ArrayList<Position>();
			for(CoordinateClass coord : coords){
				//System.out.println("Inside the coords class");
				double[] arr = CrsConverter.convertCoordinate(this.crs, "WGS84", coord.getCoords());
				//System.out.println(arr[0]+" "+arr[1]+" "+arr[2]);
				Positions.add(Position.fromDegrees(arr[1],arr[0],arr[2]));
				
			}
			if(Positions.size() < 3) continue;
			Polygon poly = new Polygon(Positions);
			poly.setAttributes(normalAttributes);
			poly.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
			layer.addRenderable(poly);
		}
	}
	
	private void renderWalls(List<SurfaceMember> wallList,RenderableLayer layer){
		System.out.println("Rendering Wall List");
		
		ShapeAttributes normalAttributes = new BasicShapeAttributes();
        
        normalAttributes.setInteriorMaterial(new Material(new Color(255,255,196)));
        normalAttributes.setOutlineWidth(2);
        normalAttributes.setOutlineOpacity(0.5);
        normalAttributes.setDrawInterior(true);
        normalAttributes.setDrawInterior(true);
        
		for(SurfaceMember wall : wallList){
			PolygonClass polygon = wall.getPolygon();
			List<CoordinateClass> coords = polygon.getPolygon();
			ArrayList<Position> Positions = new ArrayList<Position>();
			for(CoordinateClass coord : coords){
				double[] arr = CrsConverter.convertCoordinate(this.crs, "WGS84", coord.getCoords());
				//System.out.println(arr[0]+" "+arr[1]+" "+arr[2]);
				Positions.add(Position.fromDegrees(arr[1],arr[0],arr[2]));
			}
			if(Positions.size() < 3) continue;
			Polygon poly = new Polygon(Positions);
			poly.setAttributes(normalAttributes);
			poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			layer.addRenderable(poly);
		}
	}
	
	private void renderRoofs(List<SurfaceMember> roofList,RenderableLayer layer){
		System.out.println("Rendering Roof List");
		ShapeAttributes normalAttributes = new BasicShapeAttributes();
        
        normalAttributes.setInteriorMaterial(new Material(new Color(209,25,25)));
        normalAttributes.setOutlineWidth(2);
        normalAttributes.setOutlineOpacity(0.5);
        normalAttributes.setDrawInterior(true);
        normalAttributes.setDrawInterior(true);
        
		for(SurfaceMember roof : roofList){
			PolygonClass polygon = roof.getPolygon();
			List<CoordinateClass> coords = polygon.getPolygon();
			ArrayList<Position> Positions = new ArrayList<Position>();
			for(CoordinateClass coord : coords){
				double[] arr = CrsConverter.convertCoordinate(this.crs, "WGS84", coord.getCoords());
				//System.out.println(arr[0]+" "+arr[1]+" "+arr[2]);
				Positions.add(Position.fromDegrees(arr[1],arr[0],arr[2]));
			}
			if(Positions.size() < 3) continue;
			Polygon poly = new Polygon(Positions);
			poly.setAttributes(normalAttributes);
			poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			layer.addRenderable(poly);
		}
	}
	private void renderSolids(List<SurfaceMember> solids,RenderableLayer layer){
		System.out.println("Rendering Solids");
		
		ShapeAttributes normalAttributes = new BasicShapeAttributes();
        
        normalAttributes.setInteriorMaterial(new Material(new Color(255,255,196)));
        normalAttributes.setOutlineWidth(2);
        normalAttributes.setOutlineOpacity(0.5);
        normalAttributes.setDrawInterior(true);
        normalAttributes.setDrawInterior(true);
        
        for(SurfaceMember solid : solids){
        	PolygonClass polygon = solid.getPolygon();
        	List<CoordinateClass> coords = polygon.getPolygon();
        	ArrayList<Position> Positions = new ArrayList<Position>();
			for(CoordinateClass coord : coords){
				double[] arr = CrsConverter.convertCoordinate(this.crs, "WGS84", coord.getCoords());
				//System.out.println(arr[0]+" "+arr[1]+" "+arr[2]);
				Positions.add(Position.fromDegrees(arr[1],arr[0],arr[2]));
			}
			if(Positions.size() < 3) continue;
			Polygon poly = new Polygon(Positions);
			poly.setAttributes(normalAttributes);
			poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			layer.addRenderable(poly);
        }
        
	}
	
	/*public RenderableLayer startRendering(List<SurfaceMember> surfaces){
		RenderableLayer layer = new RenderableLayer();
		CrsConverterGDAL convert = new CrsConverterGDAL();
		
		for(SurfaceMember surface : surfaces){
			PolygonClass polygon = surface.getPolygon();
			List<CoordinateClass> coords = polygon.getPolygon();
			ArrayList<Position> Positions = new ArrayList<Position>();
			for(CoordinateClass coord : coords){
				double[] arr = convert.convertCoordinate("something", "crap", coord.getCoords());
				System.out.println(arr[0]+" "+arr[1]+" "+arr[2]);
				Positions.add(Position.fromDegrees(arr[0],arr[1],arr[2]));
			}
			Polygon poly = new Polygon(Positions);
			poly.setAttributes(this.shapeAttribute);
			poly.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
			layer.addRenderable(poly);
		}
		return layer;
	}*/
	public ShapeAttributes getShapeAttribute() {
		return shapeAttribute;
	}
	public void setShapeAttribute(ShapeAttributes shapeAttribute) {
		this.shapeAttribute = shapeAttribute;
	}
	
}
