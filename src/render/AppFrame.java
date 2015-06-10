package render;

import java.util.ArrayList;
import java.util.List;

import controller.ElevationExtractor;
import controller.StartUp;
import citygmlModel.BuildingsClass;
import citygmlModel.Buildings;
import citygmlModel.CoordinateClass;
import citygmlModel.MultipleBuildingsFileClass;
import citygmlModel.PolygonClass;
import citygmlModel.SurfaceMember;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.ExtrudedPolygon;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

public class AppFrame extends ApplicationTemplate {
	
	public static class RenderFrame extends ApplicationTemplate.AppFrame
    {
        public RenderFrame()
        {
            super(true, true, false);

            //Create the UI for extracting the elevation
            new ElevationExtractor(this);
            //this.getWwd().addSelectListener(new BasicDragger(this.getWwd()));
            
            //create layer for a polygon
            RenderableLayer layer = new RenderableLayer();
            
            //RenderingBuildingSurface
            try {
    			//obj.IterateGMLFile(filePath);
    			List<BuildingsClass> buildingsList = StartUp.obj.getBuildingsList();
    			//Each element in BuildingList is a new citygml file, so each will be a separate layer:
    			for(BuildingsClass building : buildingsList){
    				RenderObjects renderFile = new RenderObjects();
    				RenderableLayer buildingsLayer = renderFile.startRenderingBuildings(building);
    				insertBeforeCompass(getWwd(),buildingsLayer);
    			}
    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            
            // Update layer
            //this.getLayerPanel().update(this.getWwd());
        }
    }
}
