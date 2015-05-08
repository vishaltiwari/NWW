package meshGenerator;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import meshGenerator.ExtractWaterSurfacePolygons.AppFrame;

public class WaterMeshRender extends ApplicationTemplate{
	
	//Need to mofiy this for the DEM from WMS server
	//This class is only for representation, not to be used in income code. We won't draw buildings like this. 
	/*protected MeshClass getBuildings(MeshClass terrain,MeshClass DSM){
		int width = DSM.getWidth();
		int height = DSM.getHeight();
		
		if(DSM.getHeight() != terrain.getHeight() || DSM.getWidth()!=terrain.getWidth()){
			System.out.println("They have different dimensions");
			return null;
		}
		
		float[] buildingArea = new float[height*width];
		
		MeshClass buildings=null;
		return buildings;
	}*/
	
	protected MeshClass CalculateWaterArea(MeshClass DSM,MeshClass floodMap,float[] color,String tag){
		
		int width = DSM.getWidth();
		int height = DSM.getHeight();
		
		if(DSM.getHeight() != floodMap.getHeight() || DSM.getWidth()!=floodMap.getWidth()){
			System.out.println("They have different dimensions");
			return null;
		}
		
		System.out.println("DSM width"+width + " DSM height"+height);
		//calculate the diff of the two heightmaps
		
		//Diff result
		float[] floodArea = new float[height*width];
		
		try{
			File file = new File("/home/vishal/NWW/sampleData/out.txt");
			
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			int cnt=0;
			for(int i=0 ; i<height ; i++){
				for(int j=0 ; j<width ; j++){

					if((floodMap.getHeight(i, j) - DSM.getHeight(i, j)) >0){
						if(tag=="building")
							floodArea[cnt++] = floodMap.getHeight(i, j)*2;
						else
							floodArea[cnt++] = floodMap.getHeight(i, j);
					}
					else{
						floodArea[cnt++] = 0;
					}
					bw.write(floodArea[cnt-1]+" ");
					//System.out.print(floodArea[i][j]+" ");
				}
				bw.write("\n");
				//System.out.println("");
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}

		
		MeshClass floodMesh = new MeshClass(floodArea,width,height,floodMap.getPosition(),color);
		System.out.println(floodMesh.getHeight());
		return floodMesh;
	}
		
	protected static class AppFrame extends ApplicationTemplate.AppFrame
    {   
        public AppFrame()
        {
            super(true, false, false);

            RenderableLayer layer = new RenderableLayer();
            //Cube cube = new Cube(Position.fromDegrees(35.0, -120.0, 3000), 1000);
            Position pos = new Position(Angle.fromDegrees(0),Angle.fromDegrees(0),100);
            String filename = "/home/vishal/NWW/sampleData/DSM.png";
            float[] DSMColor = {1,1,1};
            float[] floodColor = {0,0,1};
            float[] terrainColor = {0,1,0};
            float[] buildingCoor = {0.5f,0.3f,0.0f};
            MeshClass terrain = new MeshClass("/home/vishal/NWW/sampleData/Createdheightmap.png",pos,terrainColor);
            
            MeshClass DSM = new MeshClass(filename,pos,DSMColor);
            MeshClass floodMap = new MeshClass("/home/vishal/NWW/sampleData/floodPolygon2.png",pos,floodColor);
            WaterMeshRender pol = new WaterMeshRender();
            
            MeshClass buildings = pol.CalculateWaterArea(terrain, DSM,buildingCoor,"building");
            MeshClass floodarea = pol.CalculateWaterArea(DSM, floodMap,floodMap.getColorArr(),"flood");
            System.out.println(floodarea.getWidth() + " "+floodarea.getHeight());
            
            System.out.println("coordCount "+floodarea.getCoordCount() + " indx count:"+floodarea.getIndexCount()+ " count");
            layer.addRenderable(buildings);
            layer.addRenderable(DSM);
            layer.addRenderable(floodarea);
            
            //pol.print(floodarea);

            getWwd().getModel().getLayers().add(layer);
        }
    }   
	
	public static void main(String argv[]){
		Configuration.setValue(AVKey.INITIAL_LATITUDE, 0);
		Configuration.setValue(AVKey.INITIAL_LONGITUDE, 0);
		Configuration.setValue(AVKey.INITIAL_ALTITUDE, 15500);
		Configuration.setValue(AVKey.INITIAL_PITCH, 45);
		Configuration.setValue(AVKey.INITIAL_HEADING, 45);

		ApplicationTemplate.start("World Wind Custom Renderable Tutorial", AppFrame.class);
	}
}
