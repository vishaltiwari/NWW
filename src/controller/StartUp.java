package controller;

import java.util.List;

import citygmlModel.Buildings;
import citygmlModel.BuildingsClass;
import citygmlModel.CoordinateClass;
import citygmlModel.MultipleBuildingsFileClass;
import citygmlModel.PolygonClass;
import citygmlModel.SurfaceMember;
import render.AppFrame.RenderFrame;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

public class StartUp {
	//The control starts from here
	public static MultipleBuildingsFileClass obj;
	public static void main(String argv[]){
		
        /*Configuration.setValue(AVKey.INITIAL_LATITUDE, 76.51134570525976);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, 9.019376924014613e-5);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10);*/
		String filePath = "/home/vishal/NWW/sampleData/CityGML_British_Ordnance_Survey_v1.0.0.xml";
        MultipleBuildingsFileClass obj1 = new MultipleBuildingsFileClass();
        
        double latitude=0,  longitude =0 , height=100;
        try{
        	obj1.IterateGMLFile(filePath);
        	List<BuildingsClass> buildingsList = obj1.getBuildingsList();
        	BuildingsClass building  = buildingsList.get(0);
        	
        	List<SurfaceMember> roofs = building.getRoofs();
    		List<SurfaceMember> solids = building.getSolid();
    		CrsConverterGDAL convert = new CrsConverterGDAL();
    		
    		SurfaceMember surface =null;
    		
    		if(roofs.size()>0){
    			surface = roofs.get(0);
    			
    		}
    		else if(solids.size()>0){
    			surface = solids.get(0);
    		}
    		
    		PolygonClass polygon = surface.getPolygon();
			List<CoordinateClass> coords = polygon.getPolygon();
			CoordinateClass coord = coords.get(0);
			double[] arr = convert.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", coord.getCoords());
			latitude = arr[1];
			longitude = arr[0];
			//height = arr[2];
			
        	obj = obj1;
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        
		Configuration.setValue(AVKey.INITIAL_LATITUDE, latitude);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, longitude);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, height);
        
        ApplicationTemplate.start("Getting Started with NASA World Wind", RenderFrame.class);
	}
}