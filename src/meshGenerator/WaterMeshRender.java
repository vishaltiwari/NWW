package meshGenerator;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import meshGenerator.ExtractWaterSurfacePolygons.AppFrame;

public class WaterMeshRender extends ApplicationTemplate{
	
	//Need to mofiy this for the DEM from WMS server
	protected MeshSurfaceVBO CalculateWaterArea(MeshSurfaceVBO DSM,MeshSurfaceVBO floodMap){
		
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

		MeshSurfaceVBO floodMesh = new MeshSurfaceVBO(floodArea,width,height);
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
            String filename = "/home/vishal/NWW/sampleData/DSM.png";
            MeshSurfaceVBO DSM = new MeshSurfaceVBO(filename);
            MeshSurfaceVBO floodMap = new MeshSurfaceVBO("/home/vishal/NWW/sampleData/floodPolygon.png");
            WaterMeshRender pol = new WaterMeshRender();
            MeshSurfaceVBO floodarea = pol.CalculateWaterArea(DSM, floodMap);
            System.out.println(floodarea.getWidth() + " "+floodarea.getHeight());
            
            System.out.println("coordCount "+floodarea.getCoordCount() + " indx count:"+floodarea.getIndexCount()+ " count");
            layer.addRenderable(floodarea);
            layer.addRenderable(DSM);
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
