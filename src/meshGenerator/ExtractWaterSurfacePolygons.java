package meshGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import meshGenerator.MeshSurface.AppFrame;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

public class ExtractWaterSurfacePolygons extends ApplicationTemplate{
	private String DSMString;
	private String floodMapString;
	
	public ExtractWaterSurfacePolygons(){
		
	}
	public ExtractWaterSurfacePolygons(String DSMString,String floodMapString){
		this.DSMString = DSMString;
		this.floodMapString = floodMapString;
	}
	
	protected MeshSurface CalculateWaterArea(MeshSurface DSM,MeshSurface floodMap){
		if(DSM.getHeight() != floodMap.getHeight() || DSM.getWidth()!=floodMap.getWidth()){
			System.out.println("They have different dimensions");
			return null;
		}
		int width = DSM.getWidth();
		int height = DSM.getHeight();
		System.out.println("DSM width"+width + " DSM height"+height);
		//calculate the diff of the two heightmaps
		int[][] DSMHeightMap = DSM.getHeightMap();
		int[][] floodHeightMap = floodMap.getHeightMap();
		
		//Diff result
		int[][] floodArea = new int[height][width];
		
		try{
			File file = new File("/home/vishal/NWW/sampleData/out2.txt");
			
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for(int i=0 ; i<height ; i++){
				for(int j=0 ; j<width ; j++){

					if((floodHeightMap[i][j] - DSMHeightMap[i][j]) >0){
						floodArea[i][j] = floodHeightMap[i][j];
					}
					else{
						floodArea[i][j] = 0;
					}
						

					bw.write(floodArea[i][j]+" ");
					//System.out.print(floodArea[i][j]+" ");
				}
				bw.write("\n");
				//System.out.println("");
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}

		MeshSurface floodMesh = new MeshSurface(floodArea,width,height);
		System.out.println(floodMesh.getHeight());
		return floodMesh;
	}
	
	public MeshSurface extractWaterSurfaces(){
		MeshSurface DSM = new MeshSurface(this.DSMString);
		MeshSurface floodMap = new MeshSurface(this.floodMapString);
		
		return CalculateWaterArea(DSM,floodMap);
	}
	
		
	private void print(MeshSurface mesh){
		System.out.println("Hello world");
		int[][] heightmap = mesh.getHeightMap();
		int width = mesh.getWidth();
		int height = mesh.getHeight();
		System.out.println("Width:"+width + " Height"+height);
		for(int i=0 ; i<height ; i++){
			for(int j=0 ; j<width ; j++){
				System.out.print(heightmap[i][j]+" ");
			}
			System.out.println("");
		}
	}
	
	protected static class AppFrame extends ApplicationTemplate.AppFrame
    {   
        public AppFrame()
        {
            super(true, false, false);

            RenderableLayer layer = new RenderableLayer();
            //Cube cube = new Cube(Position.fromDegrees(35.0, -120.0, 3000), 1000);
            String filename = "/home/vishal/NWW/sampleData/DSM.png";
            MeshSurface DSM = new MeshSurface(filename);
            MeshSurface floodMap = new MeshSurface("/home/vishal/NWW/sampleData/floodPolygon.png");
            ExtractWaterSurfacePolygons pol = new ExtractWaterSurfacePolygons();
            MeshSurface floodarea = pol.CalculateWaterArea(DSM, floodMap);
            
            layer.addRenderable(floodarea);
            //layer.addRenderable(DSM);
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
