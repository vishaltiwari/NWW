package randomTest;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RandomDEM {
	private int height = 512;
	private int width = 512;
	private double[] elevation;
	private double maxval;
	private double minval;
	
	public RandomDEM(int height,int width,int maxval,int minval){
		this.height = height;
		this.width = width;
		this.maxval = maxval;
		this.minval = minval;
		elevation = new double[width*height];
	}
	
	public void fillElevation(){
		int i=0;
		for(int y=0 ; y<height ; y++){
			for(int x=0 ; x<width ; x++){
				//select min or max:
				elevation[i++] = Math.random()*maxval+minval;
				//elevation[i++] = 0;
			}
		}
	}
	public void copyFromDEM(String src,String dest){
		File file  = new File(src);
		
		try{
			BufferedImage img  = ImageIO.read(file);
			
			Raster inputRaster = img.getRaster();
			ColorModel cm = img.getColorModel();
			boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
			
			BufferedImage im = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
			
			WritableRaster raster = im.copyData(null);
			
			double[] buffer = new double[1];
			double[] val = new double[1];
			int[] newval = new int[1];
			for (int y = 0; y <img.getHeight(); y++)
            {
                for (int x = 0; x < img.getWidth(); x++)
                {
                    //raster.setDoubleAtPosition(y, x, elevations[i++]);
                	val = inputRaster.getPixel(x, y, buffer);
                	
                	newval[0] = (int) val[0];
                    raster.setPixel(x, y, newval);
                    //System.out.print(elevations[i-1]+" ");
                }
                //System.out.println("");
            }
			
			BufferedImage newImg = new BufferedImage(cm,raster,isAlphaPremultiplied,null);
			//write to file
			ImageIO.write(newImg,"tif",new File(dest));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	public void writeToFile(File gtFile){
		File file  = new File("/home/vishal/NWW/sampleData/floodPolygon2.tif");
		
		try{
			BufferedImage img  = ImageIO.read(file);
			ColorModel cm = img.getColorModel();
			boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
			
			BufferedImage im = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
			
			WritableRaster raster = im.copyData(null);
			
			int[] newval = new int[1];
			int i=0;
			for (int y = 0; y <height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    //raster.setDoubleAtPosition(y, x, elevations[i++]);
                	newval[0] = (int) elevation[i++];
                    raster.setPixel(x, y, newval);
                    //System.out.print(elevations[i-1]+" ");
                }
                //System.out.println("");
            }
			
			BufferedImage newImg = new BufferedImage(cm,raster,isAlphaPremultiplied,null);
			//write to file
			ImageIO.write(newImg,"tif",gtFile);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String argv[]){
		
		//File gtFile = new File("/home/vishal/Desktop/Grass_Output/randomDEM.tif");
		
		RandomDEM dem = new RandomDEM(512,512,10,50);
		//dem.fillElevation();
		dem.copyFromDEM("/home/vishal/Desktop/Grass_Output/Heightmap.tif","/home/vishal/Desktop/Grass_Output/randomDEM.tif");
		//dem.writeToFile(gtFile);
	}
	
}
