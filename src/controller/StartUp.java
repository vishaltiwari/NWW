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
		
		//Modeling the data
		//String filePath = "/home/vishal/NWW/sampleData/LOD2_Buildings_v100.gml";
		/*Buildings building = new Buildings();
		try {
			building.IterateGMLFile(filePath);
			//print the Footprint
			List<double[]> footPolygon = building.getSurfacePolygon();
			for(int i=0 ; i<footPolygon.size() ; i++){
				double[] polygon = footPolygon.get(i);
				System.out.println(polygon[0]+" "+polygon[1]+" "+polygon[2]);
			}
			//print the wallpolygon
			System.out.println("\nWall polygons\n");
			List<List<double[]>> wallPolygons = building.getWallPolygons();
			for(int i=0 ; i<wallPolygons.size() ; i++){
				List<double[]> walls = wallPolygons.get(i);
				for(int j=0 ; j<walls.size() ; j++){
					double[] wallPolygon = walls.get(j);
					System.out.println(wallPolygon[0]+" "+wallPolygon[1]+" "+wallPolygon[2]);
				}
			}
			//print the Roof
			System.out.println("\nRoof polygons\n");
			List<List<double[]>> roofPolygons = building.getRoofPolygons();
			for(int i=0 ; i<roofPolygons.size() ; i++){
				List<double[]> roofs = roofPolygons.get(i);
				for(int j=0 ; j<roofs.size() ; j++){
					double[] roofPolygon = roofs.get(j);
					System.out.println(roofPolygon[0]+" "+roofPolygon[1]+" "+roofPolygon[2]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("OOPS! Something went wrong in extracting from citygml file");
		}*/
		//Rendering the data
		Configuration.setValue(AVKey.INITIAL_LATITUDE, 76.51134570525976);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, 9.019376924014613e-5);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10);
        ApplicationTemplate.start("Getting Started with NASA World Wind", RenderFrame.class);
	}
}
