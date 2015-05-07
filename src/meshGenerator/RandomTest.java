package meshGenerator;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RandomTest {
	//Read an image
	private static int width;
	private static int height;
	
	private int[][] loadHeightMap(String filename){
		int[][] HeightMap=null;
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			Raster image_raster = image.getData();
			
			width = image_raster.getWidth();
			height = image_raster.getHeight();
			
			HeightMap = new int[width][height];
			
			int[] pixel = new int[1];
			int[] buffer = new int[1];
			
			for(int i=0 ; i<width ; i++){
				for(int j=0 ; j<height ; j++){
					
					pixel = image_raster.getPixel(i, j, buffer);
					HeightMap[i][j] = pixel[0];
					//System.out.print(HeightMap[i][j] + " ");
				}
				//System.out.println("");
			}
			//System.out.println("width" + width + "\nHeight"+height);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return HeightMap;
	}
	public static void main(String argv[]){
		int[][] HeightMap = null;
		String filename = "/home/vishal/NWW/sampleData/Createdheightmap.png";
		RandomTest obj = new RandomTest();
		HeightMap = obj.loadHeightMap(filename);
		
		for(int i=0 ; i<width ; i++){
			for(int j=0 ; j<height ; j++){
				System.out.print(HeightMap[i][j]+" ");
			}
			System.out.println("");
		}
		
	}
}
