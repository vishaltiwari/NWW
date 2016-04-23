package floodTesselator;

import java.io.IOException;

import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.terrain.LocalElevationModel;
import gov.nasa.worldwind.terrain.RectangularTessellator;

public class TerrainReader {
	public static void readElevationFile() throws IOException{
		LocalElevationModel localElevationModel = new LocalElevationModel();
		String filepath = "/home/vishal/NWW/sampleData/gt30w180n40.tif";
		localElevationModel.addElevations(filepath);
		
		System.out.println("Max Elevation:"+localElevationModel.getMaxElevation());
		System.out.println("Min Elevation:"+localElevationModel.getMinElevation());
		
		Sector sector = localElevationModel.getSector();
		double[] sec = sector.asDegreesArray();
		System.out.println(sec.length +" "+sec[0]+" "+sec[1]+" "+sec[2]+" "+sec[3]);
		
		//Create the RectangularTessellator.RenderInfo::(contains the vertices, texCoords, referanceCenter)
		RectangularTessellator tessellator = new RectangularTessellator();
		/*DrawContext dc = null;
		dc.addPickPointFrustum();
		tessellator.tessellate(dc);*/
		
	}
	public static void main(String argv[]){
		try {
			readElevationFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
