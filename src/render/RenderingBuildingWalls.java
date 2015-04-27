package render;

import java.util.ArrayList;
import java.util.List;

import controller.CrsConverterGDAL;
import citygmlModel.BuildingWallSurfacePolygons;
import citygmlModel.Buildings;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;

public class RenderingBuildingWalls {
	public RenderableLayer renderWalls(Buildings buildings){
		RenderableLayer layer = new RenderableLayer();
		
		//Define the material property of the polygons(foot polygons)
		ShapeAttributes wallsNormalAttributes = new BasicShapeAttributes();

		wallsNormalAttributes.setInteriorMaterial(Material.CYAN);
		wallsNormalAttributes.setOutlineWidth(2);
		wallsNormalAttributes.setOutlineOpacity(0.5);
		wallsNormalAttributes.setDrawInterior(true);
		wallsNormalAttributes.setDrawInterior(true);
		//Fix this Code, walls/building are not being created.
		List<BuildingWallSurfacePolygons> buildingWallSurfacePolygons = buildings.getWallPolygons();
		System.out.println("Total Buildings:"+buildingWallSurfacePolygons.size());
		CrsConverterGDAL convert = new CrsConverterGDAL();
		for(int i=0 ; i<buildingWallSurfacePolygons.size() ; i++){
			System.out.println("Building no"+i);
			BuildingWallSurfacePolygons wallSurfacePolygons = buildingWallSurfacePolygons.get(i);
			List<List<double[]>> buildingWalls = wallSurfacePolygons.getWallPolygons();
			System.out.println("No of walls in this building:"+buildingWalls.size());
			
			ArrayList<Position> wallPositions = new ArrayList<Position>();
			
			for(int j=0 ; j<buildingWalls.size() ; j++){
				List<double[]> wall = buildingWalls.get(j);
				System.out.println("Coordinates are as follows:");
				for(int k=0 ; k<wall.size() ; k++){
					double[] coordinate = wall.get(k);
					coordinate = convert.convertCoordinate("not using", "not using", coordinate);
					System.out.println(coordinate[0]+" "+coordinate[1]+" "+coordinate[2]);
					wallPositions.add(Position.fromDegrees(coordinate[0], coordinate[1], coordinate[2]));
				}
				
				System.out.println("\n");
			}
			Polygon poly = new Polygon(wallPositions);
			poly.setAttributes(wallsNormalAttributes);
			poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			layer.addRenderable(poly);
			System.out.println("\nNew Building:");
		}
		
		return layer;
	}
}
