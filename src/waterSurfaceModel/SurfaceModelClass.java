package waterSurfaceModel;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gov.nasa.worldwind.geom.Sector;

public class SurfaceModelClass {
	private String filePath;
	private float heightMap[];
	private int width;
	private int height;
	private Sector sector;
	
	/**
	 * Constructor
	 * **/
	public SurfaceModelClass(String filePath){
		this.filePath = filePath;
		this.setProjection();
	}
	/**
	 * Methods
	 * **/
	
	public void setProjection(){
		this.sector = ExtractBoundry.extractBoundry(this.filePath);
		System.out.println(sector.getMaxLatitude().toString());
		System.out.println(sector.getMinLatitude().toString());
		
		System.out.println(sector.getMaxLongitude().toString());
		System.out.println(sector.getMinLongitude().toString());
	}
	
	public float[] loadHeightMap(){
		float[] HeightMap=null;
		int count=0;
		try {
			BufferedImage image = ImageIO.read(new File(this.filePath));
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
	/**
	 * Setters and getters of the Model
	 * **/
	
	public int getWidth() {
		return width;
	}
	public float[] getHeightMap() {
		return heightMap;
	}
	public void setHeightMap(float[] heightMap) {
		this.heightMap = heightMap;
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
	public Sector getSector() {
		return sector;
	}
	public void setSector(Sector sector) {
		this.sector = sector;
	}
	
	public static void main(String argv[]){
		SurfaceModelClass obj = new SurfaceModelClass("/home/vishal/Desktop/Grass_Output/images/20.tif");
		obj.loadHeightMap();
		System.out.println("width:"+obj.getWidth() + " height:"+obj.getHeight());
	}
	
}
