package timeDynamicSurface;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WaterSurfaceModel {
	private float heightMap[];
	private int width;
	private int height;
	
	
	public float[] getHeightMap() {
		return heightMap;
	}
	public void setHeightMap(float[] heightMap) {
		this.heightMap = heightMap;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public WaterSurfaceModel(String filename){
		this.heightMap = loadHeightMap(filename);
	}
	public float[] loadHeightMap(String filename){
		float[] HeightMap=null;
		int count=0;
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			Raster image_raster = image.getData();
			
			this.width = image_raster.getWidth();
			this.height = image_raster.getHeight();
			
			HeightMap = new float[this.height*this.width];
			
			int[] pixel = new int[1];
			int[] buffer = new int[1];
			
			for(int i=0 ; i<this.width ; i++){
				for(int j=0 ; j<this.height; j++){
					
					pixel = image_raster.getPixel(i, j, buffer);
					HeightMap[count] = pixel[0];
					++count;
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
    
    public float getHeight(int i,int j){
		return heightMap[i*width+j];
	}
}
