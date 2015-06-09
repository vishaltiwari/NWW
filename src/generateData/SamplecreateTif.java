package generateData;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SamplecreateTif {
	
	public void writeTIF(String filename){
		File file  = new File(filename);
		try {
			
			BufferedImage img  = ImageIO.read(file);
			
			ColorModel cm = img.getColorModel();
			boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
			WritableRaster raster = img.copyData(null);
			
			BufferedImage newImg = new BufferedImage(cm,raster,isAlphaPremultiplied,null);
			ImageIO.write(newImg,"tif",new File("/home/vishal/Desktop/newoutimage.tif"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String argv[]){
		String filename = "/home/vishal/NWW/sampleData/floodPolygon2.tif";
		SamplecreateTif obj = new SamplecreateTif();
		obj.writeTIF(filename);
	}
	
}
