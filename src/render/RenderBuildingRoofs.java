package render;

import java.util.ArrayList;
import java.util.List;

import controller.CrsConverterGDAL;
import citygmlModel.BuildingGroundSurfacePolygon;
import citygmlModel.BuildingRoofPolygons;
import citygmlModel.BuildingWallSurfacePolygons;
import citygmlModel.Buildings;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;

public class RenderBuildingRoofs {
	public RenderableLayer renderRoofs(Buildings buildings){
		RenderableLayer layer = new RenderableLayer();
		//Define the material property of the polygons(foot polygons)
		ShapeAttributes roofNormalAttributes = new BasicShapeAttributes();

		roofNormalAttributes.setInteriorMaterial(Material.RED);
		roofNormalAttributes.setOutlineWidth(2);
		roofNormalAttributes.setOutlineOpacity(0.5);
		roofNormalAttributes.setDrawInterior(true);
		roofNormalAttributes.setDrawInterior(true);

		//Get the groundSurface Polygon
		//Fix this Code, walls/building are not being created.
		List<BuildingRoofPolygons> buildingRoofPolygons = buildings.getRoofPolygons();
		//System.out.println("Total Buildings:"+buildingWallSurfacePolygons.size());
		CrsConverterGDAL convert = new CrsConverterGDAL();
		for(int i=0 ; i<buildingRoofPolygons.size() ; i++){
			System.out.println("Building no"+i);
			BuildingRoofPolygons roofPolygons = buildingRoofPolygons.get(i);
			List<List<double[]>> buildingRoofs = roofPolygons.getRoofPolygon();
			//System.out.println("No of walls in this building:"+buildingRoofs.size());

			ArrayList<Position> roofPositions = new ArrayList<Position>();

			for(int j=0 ; j<buildingRoofs.size() ; j++){
				List<double[]> roof = buildingRoofs.get(j);
				System.out.println("Coordinates are as follows:");
				for(int k=0 ; k<roof.size() ; k++){
					double[] coordinate = roof.get(k);
					convert.convertCoordinate("not using", "not using", coordinate);
					//System.out.println(coordinate[0]+" "+coordinate[1]+" "+coordinate[2]);
					roofPositions.add(Position.fromDegrees(coordinate[0], coordinate[1], coordinate[2]));
				}

				System.out.println("\n");
			}
			Polygon poly = new Polygon(roofPositions);
			poly.setAttributes(roofNormalAttributes);
			poly.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			layer.addRenderable(poly);
			System.out.println("\nNew Building:");
		}
		return layer;
	}
}
