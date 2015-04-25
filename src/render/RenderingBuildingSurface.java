package render;

import java.util.ArrayList;
import java.util.List;

import controller.CrsConverterGDAL;
import citygmlModel.BuildingGroundSurfacePolygon;
import citygmlModel.Buildings;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;

public class RenderingBuildingSurface {
	public RenderableLayer renderBuildings(Buildings buildings){
		RenderableLayer layer = new RenderableLayer();
		
		//Define the material property of the polygons(foot polygons)
		ShapeAttributes surfaceNormalAttributes = new BasicShapeAttributes();
        
        surfaceNormalAttributes.setInteriorMaterial(Material.YELLOW);
        surfaceNormalAttributes.setOutlineWidth(2);
        surfaceNormalAttributes.setOutlineOpacity(0.5);
        surfaceNormalAttributes.setDrawInterior(true);
        surfaceNormalAttributes.setDrawInterior(true);
        
		//Get the groundSurface Polygon
		List<BuildingGroundSurfacePolygon> surfacePolygon = buildings.getSurfacePolygons();
		for(int i=0 ; i<surfacePolygon.size() ; i++){
			
			CrsConverterGDAL convert = new CrsConverterGDAL();
			
			BuildingGroundSurfacePolygon buildingSurfacePolygon = surfacePolygon.get(i);
			List<double[]> polygons = buildingSurfacePolygon.getSurfacePolygon();
			
			
			ArrayList<Position> surfacePositions = new ArrayList<Position>();
			for(int j=0 ; j<polygons.size() ; j++){
				double[] polygon = polygons.get(j);
				convert.convertCoordinate("not using", "not using", polygon);

				//System.out.println(polygon[0]+" "+polygon[1]+" "+polygon[2]);
				surfacePositions.add(Position.fromDegrees(polygon[0], polygon[1], polygon[2]));
			}
			
			System.out.println("");
			Polygon poly = new Polygon(surfacePositions);
			poly.setAttributes(surfaceNormalAttributes);
			//poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			poly.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
			layer.addRenderable(poly);
			//positions.add(Position.fromDegrees(polygon[0], polygon[1], polygon[2]));
		}
		return layer;
	}
}