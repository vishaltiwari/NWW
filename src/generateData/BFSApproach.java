package generateData;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

public class BFSApproach {
	static public class Pair{
		public int x;
		public int y;
		public Pair(int i,int j){
			this.x = i;
			this.y = j;
		}
	}
	
	private Pair startPoint;
	private int width;
	private int height;
	// true: visited , false: not visited
	private boolean[][] visited;
	
	//private int[][] rasterMap;
	private BufferedImage img;
	
	public BFSApproach(String filename,Pair start){
		//read the file and save it a 2D array
		this.startPoint = start;
		
		File file  = new File(filename);
		try{
			this.img = ImageIO.read(file);
			this.height = img.getHeight();
			this.width = img.getWidth();
			visited = new boolean[height][width];
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			for(boolean[] arr : visited)
				Arrays.fill(arr, false);
		}
	}
	
	public boolean check(int x,int y){
		if(x<0 || y<0 || x>=width || y>=height) return false;
		return true;
	}
	
	public void checkNAdd(Pair curr, Queue<Pair> myqueue,Raster image_raster){
		int[] heightValue = new int[1];
		int[] currValue = new int[1];
		int[] buffer = new int[1];
		
		int x = curr.x;
		int y = curr.y;
		
		currValue = image_raster.getPixel(x,y,buffer);
		//add the lower elevation pixels to myqueue
		int[] xit = {-1,0,1};
		int[] yit = {-1,0,1};
		
		for(int i=0 ; i<3 ; i++){
			for(int j=0 ; j<3 ; j++){
				
				if(xit[i] == 0 && yit[j]==0) continue;
				
				//validity of D8 neighbours
				if(check(x+xit[i],y+yit[j])){
					
					if(visited[y+yit[j]][x+xit[i]]==false){
						
						heightValue = image_raster.getPixel(x+xit[i], y+yit[j], buffer);
						if((currValue[0]-heightValue[0]) >= 0){
							myqueue.add(new Pair(x+xit[i],y+yit[j]));
							visited[y+yit[j]][x+xit[i]]=true;
						}
					}
				}
			}
		}
	}
	
	public void saveHeightMap(int imageCount) throws IOException{
		
		int[] currValue = new int[1];
		int[] buffer = new int[1];
		
		String filename = "/home/vishal/Desktop/Grass_Output/images/"+imageCount+".tif";
		
		//create the data that you want to copy
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		
		//modify this writable raster
		WritableRaster raster = img.copyData(null);
		
		int addHeight = 2;
		int[] newHeight = new int[1];
		for(int y=0 ; y<this.height ; y++){
			for(int x=0 ; x < this.width ; x++){
				if(visited[y][x]==true){
					currValue = raster.getPixel(x, y, buffer);
					newHeight[0] = currValue[0] + addHeight;
					raster.setPixel(x, y, newHeight);
				}
			}
		}
		
		BufferedImage newImg = new BufferedImage(cm,raster,isAlphaPremultiplied,null);
		//write to file
		ImageIO.write(newImg,"tif",new File(filename));
	}
	
	public void printHeightMap(int imageCount){
		
		try{
			File outFile = new File("/home/vishal/Desktop/Grass_Output/output/output"+imageCount+".txt");
			System.out.println(outFile);
			
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			FileWriter fw = new FileWriter(outFile.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);
			//write using bw to file
			System.out.println(this.height + " " + this.width);
			for(int y=0 ; y<this.height ; y++){
				for(int x=0 ; x<this.width ; x++){
					if(visited[y][x]==true)
						bw.write("1 ");
					else
						bw.write("0 ");
				}
				bw.write("\n");
			}
			bw.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void generateByBFS(){
		Queue<Pair> myqueue = new LinkedList<Pair>();
		
		Raster image_raster = img.getData();
		
		int saveAfterIter = 200;
		int count=0;
		
		int imageCount=1;
		
		visited[this.startPoint.y][this.startPoint.x] = true;
		myqueue.add(this.startPoint);
		
		while(!myqueue.isEmpty()){
			Pair curr = myqueue.remove();
			// i ,j is the position of the
			if(curr!=null){
				checkNAdd(curr,myqueue,image_raster);
				Pair p=null;
				myqueue.add(p);
				++count;
			}
			else{
				//Take a snapshot of the visited height map
				if(count%saveAfterIter==0){
					//print the image
					//printHeightMap(imageCount);
					
					//save the image:
					try {
						saveHeightMap(imageCount);
					} catch (IOException e) {
						e.printStackTrace();
					}
					++imageCount;
				}
			}
		}
	}
	
	public static void main(String argv[]){
		int x=100; int y=10;
		Pair pair = new Pair(x,y);
		//String filename = "/home/vishal/NWW/sampleData/floodPolygon2.tif";
		String filename = "/home/vishal/Desktop/Grass_Output/testElevationLive.tif";
		BFSApproach obj = new BFSApproach(filename,pair);
		obj.generateByBFS();
	}
}