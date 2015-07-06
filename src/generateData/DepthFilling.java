package generateData;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.data.ByteBufferRaster;
import gov.nasa.worldwind.formats.tiff.GeotiffWriter;
import gov.nasa.worldwind.geom.Sector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.ListIterator;
import java.util.Map;

import waterSurfaceModel.SurfaceModelClass;

public class DepthFilling {
	
	public static class Pair{
		public int x;
		public int y;
		public int width;
		public Pair(int x,int y,int width){
			this.x = x;
			this.y = y;
			this.width = width;
		}
		public int hashCode(){
			int hashCode=0;
			hashCode = y*width+x;
			//System.out.println("HashCode:"+hashCode);
			return hashCode;
		}
		public boolean equals(Object obj){
			if(obj instanceof Pair){
				Pair p = (Pair)obj;
				if(p.x==x && p.y==y)
					return true;
				return false;
			}
			else{
				return false;
			}
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		
	}
	//1 min, values in seconds 
	private static int timeinterval=60;
	private static double Rate=0.007;
	
	public static void levelHeightMap(SurfaceModelClass surface){
		float[] height = surface.getHeightMap();
		int size = surface.getHeight() * surface.getWidth();
		for(int i=0 ; i<size ;i++){
			height[i] = height[i]/10;
		}
		surface.setHeightMap(height);
		surface.setMaxVal(surface.getMaxVal()/10);
		surface.setMinVal(surface.getMinVal()/10);
	}
	
	public static void writeElevationsToFile(Sector sector, int width, int height, double[] elevations, File gtFile)
			throws IOException
	{
		// These parameters are required for writeElevation
		AVList elev32 = new AVListImpl();

		elev32.setValue(AVKey.SECTOR, sector);
		elev32.setValue(AVKey.WIDTH, width);
		elev32.setValue(AVKey.HEIGHT, height);
		elev32.setValue(AVKey.COORDINATE_SYSTEM, AVKey.COORDINATE_SYSTEM_GEOGRAPHIC);
		elev32.setValue(AVKey.PIXEL_FORMAT, AVKey.ELEVATION);
		elev32.setValue(AVKey.DATA_TYPE, AVKey.FLOAT32);
		elev32.setValue(AVKey.ELEVATION_UNIT, AVKey.UNIT_METER);
		elev32.setValue(AVKey.BYTE_ORDER, AVKey.BIG_ENDIAN);
		elev32.setValue(AVKey.MISSING_DATA_SIGNAL, 0.0);
		//elev32.setValue(AVKey.PIXEL_HEIGHT, 10.0);
		//elev32.setValue(AVKey.PIXEL_WIDTH, 10.0);


		ByteBufferRaster raster = (ByteBufferRaster) ByteBufferRaster.createGeoreferencedRaster(elev32);
		// copy elevation values to the elevation raster
		int i = 0;
		for (int y = 0; y <height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				raster.setDoubleAtPosition(y, x, elevations[i++]);
				//System.out.print(elevations[i-1]+" ");
			}
			//System.out.println("");
		}

		GeotiffWriter writer = new GeotiffWriter(gtFile);
		try
		{
			writer.write(raster);
		}
		finally
		{
			writer.close();
		}
	}
	
	public static int check(int a,int b,int width,int height,LinkedHashSet<Pair> list, LinkedHashSet<Pair> blue){
		int flag=1;
		if(a>=0 && b>=0 && a<width && b<height){
			//check if not a blue pixel
			//LinkedHashSet
			Pair p = new Pair(a,b,width);
			if(blue.contains(p)==true || list.contains(p)){
				flag=0;
			}
			//arrayList
			/*for(int i=0 ; i<blue.size() ; i++){
				Pair p = blue.get(i);
				if(p.x == a && p.y==b){
					flag = 0;
					break;
				}
			}*/
		}
		else
			flag=0;
		/*if(flag==1){
			System.out.println("Added to neigbhours, x:"+a+" b:"+b);
		}*/
		//System.out.println("value of flag:"+flag);
		return flag;
	}
	public static void checkNeighbours(int x,int y,int width,int height, LinkedHashSet<Pair> list,LinkedHashSet<Pair> blue){
		int a = x-1;
		int b = y-1;
		int count=0;
		if(check(a,b,width,height,list,blue)==1){
			list.add(new Pair(a,b,width));
			++count;
		}
		
		a = x;
		b = y-1;
		if(check(a,b,width,height,list,blue)==1){
			list.add(new Pair(a,b,width));
			++count;
		}
		
		a = x+1;
		b = y-1;
		if(check(a,b,width,height,list,blue)==1){
			list.add(new Pair(a,b,width));
			++count;
		}
		
		a = x-1;
		b = y;
		if(check(a,b,width,height,list,blue)==1){
			list.add(new Pair(a,b,width));
			++count;
		}
		
		a = x+1;
		b = y;
		if(check(a,b,width,height,list,blue)==1){
			list.add(new Pair(a,b,width));
			++count;
		}
		
		a = x-1;
		b = y+1;
		if(check(a,b,width,height,list,blue)==1){
			list.add(new Pair(a,b,width));
			++count;
		}
		
		a = x;
		b = y+1;
		if(check(a,b,width,height,list,blue)==1){
			list.add(new Pair(a,b,width));
			++count;
		}
		
		a = x+1;
		b = y+1;
		if(check(a,b,width,height,list,blue)==1){
			list.add(new Pair(a,b,width));
			++count;
		}
		//System.out.println("Number of neighbours added" + count);
	}
	
	public static void createDepthMaps(SurfaceModelClass surface,String filePath){
		
		float[] heightMap = surface.getHeightMap();
		int width =surface.getWidth();
		int height = surface.getHeight();
		double minVal = surface.getMinVal();
		System.out.println("width:"+width+" height:"+height+" minVal:"+minVal);
		
		//ArrayList<Pair> bluePixels= new ArrayList<Pair>();
		LinkedHashSet<Pair> blue = new LinkedHashSet<Pair>();
		
		//Look of the x,y of minVal;
		Pair start = new Pair(1,1,width);
		int flag=0;
		for(int y=0 ; y<height ; y++){
			for(int x=0 ; x<width ; x++){
				//System.out.println(surface.getHeight(x, y));
				//if(Double.compare(surface.getHeight(x, y),minVal)==0){
				if(Double.compare(surface.getHeight(x, y),minVal)< 0.0001){
					//start.x = x;
					//start.y = y;
					blue.add(new Pair(x,y,width));
					//flag=1;
					//break;
				}
			}
			if(flag==1) break;
		}
	//	System.out.println("starting spread from"+start.x+","+start.y);
		
		//bluePixels.add(start);
//		blue.add(start);
		System.out.println("No. of blue pixels"+blue.size());
		//System.out.println("It contains::::::::::::"+blue.contains(new Pair(1,1,width)));
		
		
		double maxVal = surface.getMaxVal();
		double currHeight=minVal;
		
		float[] newHeightMap = heightMap.clone();
		//ArrayList<Pair> neighborList = new ArrayList<Pair>();
		LinkedHashSet<Pair> neighborList = new LinkedHashSet<Pair>(); 
		//add the neighbors of blue
		
		//Iterator<Pair> it = bluePixels.iterator();
		Iterator<Pair> it = blue.iterator();
		
		//ListIterator<Pair> it2 = neighborList.listIterator();
		while(it.hasNext()){
			Pair p = it.next();
			int x = p.x;
			int y = p.y;
			
			checkNeighbours(x,y,width,height,neighborList,blue);
		}
		//System.out.println("neighborList size:"+neighborList.size());
		
		int size = width*height;
		Iterator<Pair> it1;
		int count=1;
		while(Double.compare((currHeight),maxVal)<=0){
			String filename = filePath+"/"+count+".tif";
			count++;
			//part (a)
			System.out.println("currHeight:"+currHeight+ "iteration no:"+(count-1) + " maxVal:"+maxVal);
			currHeight += Rate;
			
			//add Rate to every blue pixel
			//it1 = bluePixels.listIterator();
			Iterator<Pair> it3 = blue.iterator();
			
			while(it3.hasNext()){
				//Map.Entry<Pair,Boolean> mapEntry = (Map.Entry<Pair,Boolean>) it1.next();
				Pair p = it3.next();
				newHeightMap[p.y*width+p.x] += Rate;
			}
			//part (b)
			//Search for the neighbor of blue pixels:
			it1 = neighborList.iterator();
			System.out.println("blue pixel count:"+blue.size());
			System.out.println("neigbours count"+neighborList.size());
			LinkedHashSet<Pair> newNeighborList = new LinkedHashSet<Pair>();
			while(it1.hasNext()){
				Pair p = it1.next();
				//System.out.print("Height at:"+p.x+","+p.y+": ");
				//System.out.println(surface.getHeight(p.x, p.y));
				if(surface.getHeight(p.x, p.y) <= currHeight){
					//System.out.println("This value will be added"+ p.x+ " "+p.y);
					newHeightMap[p.y*width+p.x] = (float) currHeight;
					//bluePixels.add(p);
					blue.add(p);
					//it1.remove();
					//checkNeighbours(p.x,p.y,width,height,it1,bluePixels);
					//This also adds the new neighbors to the list
					checkNeighbours(p.x,p.y,width,height,newNeighborList,blue);
					continue;
				}
				newNeighborList.add(p);
			}
			neighborList = newNeighborList;
			
			//part(c)
			//Calculate the height map
			double[] depthMap = new double[size];
			for(int i=0 ; i<size ; i++){
				depthMap[i] = newHeightMap[i] - heightMap[i];
				//if(depthMap[i]!=0)
					//System.out.print(depthMap[i]+",");
			}
			
			//part(d)
			//Save it to File
			//System.out.println(filename);
			try {
				writeElevationsToFile(surface.getSector(),width,height,depthMap,new File(filename));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public static void main(String argv[]){
		SurfaceModelClass surface = new SurfaceModelClass("/home/vishal/Desktop/Grass_Output/largerAreaDEM.tif");
		//flat the surface
		levelHeightMap(surface);
		
		//NOTE: don't use a last back slash
		createDepthMaps(surface,"/home/vishal/Desktop/Grass_Output/images11");
		
		
	}
}
