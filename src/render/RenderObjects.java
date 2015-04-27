package render;

import java.util.ArrayList;
import java.util.List;

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

public class RenderObjects {
	private ShapeAttributes shapeAttribute;
	CrsConverterGDAL convert = new CrsConverterGDAL();
	
	
	public RenderObjects(){
		this.shapeAttribute = new BasicShapeAttributes();
	}
	public RenderableLayer startRenderingBuildings(BuildingsClass buildings){
		RenderableLayer layer = new RenderableLayer();
		
		List<SurfaceMember> surfaceList = buildings.getSurfacePolygon();
		List<SurfaceMember> walls = buildings.getWalls();
		List<SurfaceMember> roofs = buildings.getRoofs();

		renderGroundSurface(surfaceList,layer);
		renderWalls(walls,layer);
		renderRoofs(roofs,layer);

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
				double[] arr = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", coord.getCoords());
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
        
        normalAttributes.setInteriorMaterial(Material.CYAN);
        normalAttributes.setOutlineWidth(2);
        normalAttributes.setOutlineOpacity(0.5);
        normalAttributes.setDrawInterior(true);
        normalAttributes.setDrawInterior(true);
        
		for(SurfaceMember wall : wallList){
			PolygonClass polygon = wall.getPolygon();
			List<CoordinateClass> coords = polygon.getPolygon();
			ArrayList<Position> Positions = new ArrayList<Position>();
			for(CoordinateClass coord : coords){
				double[] arr = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", coord.getCoords());
		//		System.out.println(arr[0]+" "+arr[1]+" "+arr[2]);
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
        
        normalAttributes.setInteriorMaterial(Material.RED);
        normalAttributes.setOutlineWidth(2);
        normalAttributes.setOutlineOpacity(0.5);
        normalAttributes.setDrawInterior(true);
        normalAttributes.setDrawInterior(true);
        
		for(SurfaceMember roof : roofList){
			PolygonClass polygon = roof.getPolygon();
			List<CoordinateClass> coords = polygon.getPolygon();
			ArrayList<Position> Positions = new ArrayList<Position>();
			for(CoordinateClass coord : coords){
				double[] arr = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", coord.getCoords());
				System.out.println(arr[0]+" "+arr[1]+" "+arr[2]);
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
