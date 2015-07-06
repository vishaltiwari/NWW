package render;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

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
import gui.MenuBar;
import gui.TimeSlider;
import gui.ToolBox;

public class AppFrame extends ApplicationTemplate {
	
	public static class RenderFrame extends ApplicationTemplate.AppFrame
    {
        public RenderFrame()
        {
            super(true, true, false);

            //Create the UI for extracting the elevation
            //new ElevationExtractor(this);
            //this.getWwd().addSelectListener(new BasicDragger(this.getWwd()));
            
            //Menu Bar
            //new MenuBar(this);
            //add the Toolbox
            
            //new ToolBox(this);
            //create layer for a polygon
            
            //Time slider
            //new TimeSlider(this);
            
            RenderableLayer layer = new RenderableLayer();
            
            //RenderingBuildingSurface
            /*try {
    			//obj.IterateGMLFile(filePath);
    			List<BuildingsClass> buildingsList = StartUp.obj.getBuildingsList();
    			//Each element in BuildingList is a new citygml file, so each will be a separate layer:
    			for(BuildingsClass building : buildingsList){
    				RenderObjects renderFile = new RenderObjects(StartUp.obj.getCrs());
    				RenderableLayer buildingsLayer = renderFile.startRenderingBuildings(building);
    				insertBeforeCompass(getWwd(),buildingsLayer);
    			}
    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}*/
            
            //Add the waterSurface
            //RenderAnalyticSurface waterSurface = new RenderAnalyticSurface("/home/vishal/NWW/sampleData/floodPolygon2.tif");
            //RenderAnalyticSurface waterSurface = new RenderAnalyticSurface("/home/vishal/Desktop/Grass_Output/01a_07_15.tif",200,1);
            //RenderableLayer waterlayer = waterSurface.renderWaterSurface("/home/vishal/Desktop/Grass_Output/images10");
            
            //insertBeforePlacenames(this.getWwd(), waterlayer);
            
            /*RenderAnalyticSurface waterSurface2 = new RenderAnalyticSurface("/home/vishal/Desktop/Grass_Output/testElevationLive.tif");
            RenderableLayer waterlayer2 = waterSurface2.renderWaterSurface();
            
            insertBeforePlacenames(this.getWwd(), waterlayer2);*/
            
            this.getLayerPanel().update(this.getWwd());
            // Update layer
            //this.getLayerPanel().update(this.getWwd());
        }
    }
}