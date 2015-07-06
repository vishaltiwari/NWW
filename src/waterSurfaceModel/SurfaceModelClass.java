package waterSurfaceModel;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.gdalconst.gdalconstConstants;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;

import gov.nasa.worldwind.formats.tiff.GeotiffReader;
import gov.nasa.worldwind.geom.Sector;

public class SurfaceModelClass {
	private String filePath;
	private float heightMap[];
	private int width;
	private int height;
	private Sector sector;
	
	private double minVal;
	private double maxVal;
	
	/**
	 * Constructor
	 * **/
	public SurfaceModelClass(int width,int height){
		this.filePath = null;
		this.sector = null;
		//this.setProjection();
		this.width = width;
		this.height = height;
		heightMap = new float[width*height];
		Arrays.fill(heightMap, 0);
	}
	public SurfaceModelClass(String filePath){
		this.filePath = filePath;
		this.setProjection();
		this.heightMap = this.loadHeightMapGDAL();
	}
	/**
	 * Methods
	 * **/
	
	public void setProjection(){
		this.sector = ExtractBoundry.extractBoundry(this.filePath);
		/*System.out.println(sector.getMaxLatitude().toString());
		System.out.println(sector.getMinLatitude().toString());
		
		System.out.println(sector.getMaxLongitude().toString());
		System.out.println(sector.getMinLongitude().toString());*/
	}
	
	public static float[] loadHeightMapGeoTiffReader(String filename){
		int width,height;
		float[] HeightMap;
		try {
			FileSeekableStream stream = new FileSeekableStream(filename);
			TIFFDecodeParam decodeParam = new TIFFDecodeParam();
			decodeParam.setDecodePaletteAsShorts(true);
			ParameterBlock params = new ParameterBlock();
			params.add(stream);
			RenderedOp image1 = JAI.create("tiff", params);

			BufferedImage image = image1.getAsBufferedImage();
			
			Raster image_raster = image.getData();
			
			width = image_raster.getWidth();
			height = image_raster.getHeight();
			
			
			
			HeightMap = new float[height * width];
			
			int[] pixel = new int[1];
			int[] buffer = new int[1];
			int count=0;
			for(int y=0 ; y<height ; y++){
				for(int x=0 ; x<width; x++){
					
					pixel = image_raster.getPixel(x, y, buffer);
					System.out.print(pixel[0]+" ");
					HeightMap[count] = pixel[0];
					++count;
					//System.out.print(HeightMap[i][j] + " ");
				}
				System.out.println("");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public float[] loadHeightMapGDAL(){
		
		int numBands;
		Dataset hDataset;
		gdal.AllRegister();
		
		hDataset = gdal.Open(this.filePath,gdalconstConstants.GA_ReadOnly);
		numBands = hDataset.getRasterCount();
		
		//System.out.println(numBands);
		
		Band band = hDataset.GetRasterBand(1);
		
		width = band.getXSize();
		height = band.getYSize();
		
		//System.out.println("xSize:"+width+" ySize:"+height);
		
		float[] heightMap = new float[width*height];
		
		band.ReadRaster(0,0,width,height,gdalconst.GDT_Float32,heightMap);
		
		double[] minMaxVal = new double[2];
		
		//2nd arg set to 1, if faster approx is okay.
		band.ComputeRasterMinMax(minMaxVal, 0);
		
		this.minVal = minMaxVal[0];
		this.maxVal = minMaxVal[1];
		
		/*for(int y=0 ; y<height*width ; y++){
			System.out.println(heightMap[y]+" ");
		}*/
		
		return heightMap;
	}
	
	public float[] loadHeightMap(){
		float[] HeightMap=null;
		int count=0;
		try {
			BufferedImage image = ImageIO.read(new File(this.filePath));
			Raster image_raster = image.getData();
			
			this.width = image_raster.getWidth();
			this.height = image_raster.getHeight();
			
			HeightMap = new float[this.height * this.width];
			
			int[] pixel = new int[1];
			int[] buffer = new int[1];
			
			for(int y=0 ; y<this.height ; y++){
				for(int x=0 ; x<this.width; x++){
					
					pixel = image_raster.getPixel(x, y, buffer);
					//System.out.print(pixel[0]+" ");
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
    
    public float getHeight(int x,int y){
		return heightMap[y*width+x];
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
	public double getMinVal() {
		return minVal;
	}
	public void setMinVal(double minVal) {
		this.minVal = minVal;
	}
	public double getMaxVal() {
		return maxVal;
	}
	public void setMaxVal(double maxVal) {
		this.maxVal = maxVal;
	}
	
	public static void main(String argv[]){
		SurfaceModelClass obj = new SurfaceModelClass("/home/vishal/Desktop/Grass_Output/images7/1.tif");
		//obj.loadHeightMap();
		//float[] heightMap = obj.getHeightMap();
		//if(heightMap == null)
		//	System.out.println("returned array is ponting to null");
		//System.out.println("width:"+obj.getWidth() + " height:"+obj.getHeight());
		//obj.loadHeightMapGDAL("/home/vishal/Desktop/Grass_Output/extractedElevation.tif");
		//loadHeightMapGeoTiffReader("/home/vishal/Desktop/Grass_Output/geotiffElevation.tif");	
		
		System.out.println("minVal:"+obj.getMinVal()+" maxVal:"+obj.getMaxVal());
	}
	
}
