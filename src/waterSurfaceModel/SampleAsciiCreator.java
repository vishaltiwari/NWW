package waterSurfaceModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import controller.CrsConverterGDAL;

public class SampleAsciiCreator {
	private double north;
	private double south;
	private double east;
	private double west;
	
	private int res;
	private int size;
	
	private int minHeight;
	
	private double[][] height;
	
	public SampleAsciiCreator(int res,int size,int minHeight){
		
		this.res = res;
		this.size = size;
		this.minHeight = minHeight;
		
		this.height = new double[size][size];
		
		this.setDirections();
	}
	
	private void setDirections(){
		CrsConverterGDAL obj = new CrsConverterGDAL();
		double[] coord1 = new double[3];
		coord1[0] = 0;
		coord1[1] = 0;
		coord1[2] = 0;
		
		double[] coord2 = new double[3];
		coord2[0] = size*res;
		coord2[1] = size*res;
		coord2[2] = 0;
		
		coord1 = obj.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", coord1);

		System.out.println("west:"+coord1[0]+" south:"+coord1[1]);
		this.west = coord1[0];
		this.south = coord1[1];
		
		coord2 = obj.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", coord2);
		
		System.out.println("east:"+coord2[0]+" north:"+coord2[1]);
		this.east = coord2[0];
		this.north = coord2[1];
	}
	
	public void createAsciiElevation(File gtFile) throws IOException{
		if(!gtFile.exists()){
			gtFile.createNewFile();
		}
		FileWriter fw = new FileWriter(gtFile.getAbsolutePath());
		BufferedWriter bw = new BufferedWriter(fw);
		
		double val = this.minHeight;
		for(int i=0 ; i<this.size ; i++){
			for(int a=0 ; a<=i ; a++){
				height[a][i] = val;
				height[i][a] = val;
			}
			++val;
		}
		System.out.println("north:  "+ this.north);
		System.out.println("south:  "+ this.south);
		System.out.println("east:   "+ this.east);
		System.out.println("west:   "+ this.west);
		
		System.out.println("rows:   "+this.size);
		System.out.println("cols:   "+this.size);
		System.out.println("null:   "+"-9999\n");
		
		for(int i=0 ; i<this.size ; i++){
			for(int j=0 ; j<this.size ; j++){
				System.out.print(height[i][j]+" ");
			}
			System.out.println();
		}
		
		bw.close();
		
	}
	public static void main(String argv[]){
		File file = new File("/home/vishal/Desktop/Grass_Output/elevation.acsii");
		SampleAsciiCreator obj = new SampleAsciiCreator(10,100,500);
		
		try {
			obj.createAsciiElevation(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
