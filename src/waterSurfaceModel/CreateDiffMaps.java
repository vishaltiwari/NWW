package waterSurfaceModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CreateDiffMaps {

	public static class Pair{
		public int i;
		public int j;
		public Pair(int i,int j){
			this.i = i;
			this.j = j;
		}
	}
	
	private String dir;
	private ArrayList<Map<Pair,Double> > timeStampedDiff;
	private String cachePath;
	
	//Time interval of snapshots, for interpolation (in ms):
	private int TimeIntervalSimulation;
	private int TimeInterval;
	private int NumofSnapShots;
	
	private String interpolationFile;
	
	private SurfaceModelClass[] surfaceModel;
	
	//0 means use created cache if any, 1: override the cache file and do fresh computation
	private int overRideCacheFlag;

	/**
	 * Constructors
	 * **/
	public CreateDiffMaps(String dir){
		init(dir);
		this.overRideCacheFlag = 0;
		
	}
	
	public CreateDiffMaps(String dir,int overRideCacheFlag){
		init(dir);
		this.overRideCacheFlag = overRideCacheFlag;
	}
	
	public void init(String dir){
		this.dir = dir;
		this.timeStampedDiff = new ArrayList<Map<Pair,Double> >();
		this.cachePath = this.dir + "/cache.txt";
		this.interpolationFile = this.dir + "/interpolation.txt";
		
		this.TimeIntervalSimulation = 120000;
		this.TimeInterval = 2000;
		this.NumofSnapShots = this.TimeIntervalSimulation / this.TimeInterval;
		
		
	}
	/**
	 * util methods, which should be removed later
	 * **/
	public static void printHashMap(Map<Pair,Double> map){
		Iterator<Entry<Pair, Double>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Pair, Double> mapEntry = (Map.Entry<Pair, Double>)it.next();
			System.out.println("("+mapEntry.getKey().i + ","+mapEntry.getKey().j + ") -> " + mapEntry.getValue().doubleValue());
		}
	}
	
	/**
	 * Saving the surfaceModel data as an arrayList
	 * **/
	public void saveSurfaceModel(){
		File[] fileList = getFiles();

		Arrays.sort(fileList,FileComparator);
		
		this.surfaceModel = new SurfaceModelClass[fileList.length];
		
		for(int i=0 ; i<fileList.length ; i++){
			File file = fileList[i];
			SurfaceModelClass surface = new SurfaceModelClass(file.getAbsolutePath());
			this.surfaceModel[i] = surface;
		}
	}
	
	/***
	 * Method for interpolating data between two snapshots:
	 * @throws IOException 
	 * 
	 * */
	public void interpolateData() throws IOException{
		
		File gtFile = new File(this.interpolationFile);
		
		if(!gtFile.exists()){
			gtFile.createNewFile();
		}
		FileWriter fw = new FileWriter(gtFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		this.NumofSnapShots = this.TimeIntervalSimulation/TimeInterval;
		
		File[] fileList = getFiles();

		Arrays.sort(fileList,FileComparator);
		
		//first iteration from 0 to 1
		File first = fileList[0];
		SurfaceModelClass surfaceFirst = new SurfaceModelClass(first.getAbsolutePath());
		
		System.out.println(first.getName());
		
		int width = surfaceFirst.getWidth();
		int height = surfaceFirst.getHeight();
		
		System.out.println("Going from zero to first map, snapshots:"+this.NumofSnapShots);
		for(int t=0 ; t<=this.NumofSnapShots ; t++){
			System.out.println("t:"+t);
			//Map<Pair,Double> map = new HashMap<Pair,Double>();
			double time = t*TimeInterval;
			double t_i = 0;
			for(int y=0 ; y<height ; y++){
				for(int x=0 ; x<width ; x++){
					Pair p = new Pair(x,y);
					
					if((surfaceFirst.getHeight(x, y) - 0 < 0.0005))
							continue;
					//bw.write("val at x,y"+surfaceFirst.getHeight(x, y)+"\n");		
					double k = (((t_i * (surfaceFirst.getHeight(x, y)))/this.TimeIntervalSimulation)) - surfaceFirst.getHeight(x, y);
					double m = ((surfaceFirst.getHeight(x, y) - 0) * time) / this.TimeIntervalSimulation;

					//bw.write("k:"+k+"m"+m);
					double deltaVal = m - k;
					//System.out.println(deltaVal);
					//System.out.println("Coords "+p.j+","+p.i+"->" + deltaVal);
					//bw.write(p.j+","+p.i+","+Double.toString(deltaVal)+"\n");
					//map.put(p, new Double(deltaVal));
				}
			}
			//bw.write("\n");
			//this.timeStampedDiff.add(map);
		}
		
		for(int i=1 ; i<fileList.length ; i++){
			File fPrev = fileList[i-1];
			File fCurr = fileList[i];

			System.out.println(fCurr.getName());
			SurfaceModelClass surfacePrev = new SurfaceModelClass(fPrev.getAbsolutePath());
			SurfaceModelClass surfaceCurr = new SurfaceModelClass(fCurr.getAbsolutePath());
			
			width = surfaceCurr.getWidth();
			height = surfaceCurr.getHeight();
			if(width!=surfacePrev.getWidth() || height!=surfacePrev.getHeight()){
				System.out.println("ERRR!!Dimensions not Equal");
				return;
			}
			
			for(int t=0 ; t<=this.NumofSnapShots ; t++){
				//Map<Pair,Double> map = new HashMap<Pair,Double>();
				System.out.println("t:" + t);
				double time = (i-1)*this.TimeIntervalSimulation + t*TimeInterval;
				double t_i = (i-1)*this.TimeIntervalSimulation;
				for(int y=0 ; y<height ; y++){
					for(int x=0 ; x<width ; x++){
						Pair p = new Pair(x,y);
						double k = ((t_i * ((surfaceCurr.getHeight(x, y) - surfacePrev.getHeight(x, y)))/this.TimeIntervalSimulation)) - surfacePrev.getHeight(x, y);
						double m = ((surfaceCurr.getHeight(x, y) - surfacePrev.getHeight(x, y)) * time) / this.TimeIntervalSimulation;

						double deltaVal = m - k;
						//System.out.println(p.i+","+p.j+"->" + deltaVal);
						//bw.write(p.j+","+p.i+","+Double.toString(deltaVal)+"\n");
					//	map.put(p, new Double(deltaVal));
					}
				}
				//bw.write("\n");
				//this.timeStampedDiff.add(map);
			}
		}
		bw.close();
		/*
		try {
			createCache();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	/**
	 * Class Methods for creating the diff map:
	 * @throws IOException 
	 * **/
	public void start(){
		if(this.overRideCacheFlag == 0){
			//look for cacheFile
			File gtFile = new File(this.cachePath);
			System.out.println(this.cachePath);
			if(gtFile.exists()){
				//Fill the timeStampDiff from cache file
				System.out.println("File exists, yay no need for re-doing computation");
				try {
					readCache();
					//System.out.println("creating a cache from the read cache, just to check if they match");
					//createCache();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				//Run createDiffMap method and create Cache:
				this.createDiffMaps();
				
				try {
					System.out.println("Creating cache");
					this.createCache();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else{
			this.createDiffMaps();
			try {
				this.createCache();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void readCache() throws IOException{
		
		FileReader gtFile = new FileReader(this.cachePath);
		BufferedReader br = new BufferedReader(gtFile);
		
		String currLine;
		Map<Pair,Double> map = new HashMap<Pair,Double>();
		while((currLine = br.readLine()) != null){
			if(currLine.equals("")){
				//push the hashmap to arrayList and reset hashMap
				this.timeStampedDiff.add(map);
				map = new HashMap<Pair,Double>();
				continue;
			}
			//parse the line
			String[] parsedStr = currLine.split(",");
			Pair p = new Pair(Integer.parseInt(parsedStr[0]),Integer.parseInt(parsedStr[1]));
			Double val = Double.valueOf(parsedStr[2]);
			//System.out.println(p.i+" "+p.j+ " "+val);
			map.put(p, val);
		}
		//System.out.println("timeStampedDiff size"+timeStampedDiff.size());
		br.close();
		
	}
	public void createCache() throws IOException{
		
		File gtFile = new File(this.cachePath);
		
		if(!gtFile.exists()){
			gtFile.createNewFile();
		}
		FileWriter fw = new FileWriter(gtFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		int size  = this.timeStampedDiff.size();
		for(int i=0 ; i<size ; i++){
			Map<Pair,Double> map = timeStampedDiff.get(i);
			Iterator<Entry<Pair, Double>> it = map.entrySet().iterator();
			while(it.hasNext()){
				
				Map.Entry<Pair, Double> mapEntry = (Map.Entry<Pair, Double>)it.next();
				Pair p = mapEntry.getKey();
				double val = mapEntry.getValue().doubleValue();
				
				bw.write(p.i+","+p.j+","+val+"\n");
			}
			bw.write("\n");//blank line
		}
		bw.close();
	}
	
	private Map<Pair,Double> getDiffMap(SurfaceModelClass curr,SurfaceModelClass prev){
		Map<Pair,Double> map = new HashMap<Pair,Double>();
		int width = curr.getWidth();
		int height = curr.getHeight();
		if(width!=prev.getWidth() || height!=prev.getHeight()){
			System.out.println("ERRR!!Dimensions not Equal");
			return null;
		}
		else{
			for(int i=0 ; i<height ; i++){
				for(int j=0 ; j<width ; j++){
					double diff = curr.getHeight(j, i) - prev.getHeight(j, i); 
					if(diff != 0){
						Pair p = new Pair(j,i);
						Double deltaChange = new Double(diff);
						map.put(p, deltaChange);
					}
				}
			}
		}
		
		return map;
	}
	public File[] getFiles(){
		File folder = new File(this.dir);
		
		FilenameFilter filter = new FilenameFilter(){
			public boolean accept(File dir, String filename) {
				if(filename.endsWith(".tif"))
					return true;
				else
					return false;
			}
		};
		
		File[] fileList = folder.listFiles(filter);
		
		return fileList;
	}
	public void createDiffMaps(){
		//get the list of files:
		File[] fileList = getFiles();
		
		Arrays.sort(fileList,FileComparator);
		/*for(int i=0 ; i<fileList.length ; i++){
			System.out.println(fileList[i].getName());
		}*/
		
		for(int i=1 ; i<fileList.length ; i++){
			File fPrev = fileList[i-1];
			File fCurr = fileList[i];
			
			System.out.println(fCurr.getName());
			SurfaceModelClass surfacePrev = new SurfaceModelClass(fPrev.getAbsolutePath());
			SurfaceModelClass surfaceCurr = new SurfaceModelClass(fCurr.getAbsolutePath());
			
			Map<Pair,Double> map = getDiffMap(surfaceCurr,surfacePrev);
			//printHashMap(map);
			this.timeStampedDiff.add(map);
		}
	}
	
	private static int extractFileNumber(String str){
		String no="";
		for(int i=0 ; str.charAt(i)!='\0' ; i++){
			if(str.charAt(i)=='.'){
				break;
			}
			no += str.charAt(i);
		}
		//String no = num[0];
		//System.out.println("no "+no);
		return Integer.parseInt(no);
	}
	public static Comparator<File> FileComparator = new Comparator<File>(){
		public int compare(File file1,File file2){
			
			String file1Name = file1.getName();
			String file2Name = file2.getName();
			
			int fileNo1 = extractFileNumber(file1Name);
			int fileNo2 = extractFileNumber(file2Name);
			
			//System.out.println("fileNo1::" + fileNo1 + " fileNo2::"+fileNo2);
			return fileNo1 - fileNo2;
			
		}
	};

	
	/**
	 * Getter and Setter
	 * **/
	public ArrayList<Map<Pair, Double>> getTimeStampedDiff() {
		return timeStampedDiff;
	}

	public void setTimeStampedDiff(ArrayList<Map<Pair, Double>> timeStampedDiff) {
		this.timeStampedDiff = timeStampedDiff;
	}
	
	
	public int getTimeIntervalSimulation() {
		return TimeIntervalSimulation;
	}

	public void setTimeIntervalSimulation(int timeIntervalSimulation) {
		TimeIntervalSimulation = timeIntervalSimulation;
	}

	public int getTimeInterval() {
		return TimeInterval;
	}

	public void setTimeInterval(int timeInterval) {
		TimeInterval = timeInterval;
	}

	public int getNumofSnapShots() {
		return NumofSnapShots;
	}

	public void setNumofSnapShots(int numofSnapShots) {
		NumofSnapShots = numofSnapShots;
	}

	public SurfaceModelClass[] getSurfaceModel() {
		return surfaceModel;
	}

	public void setSurfaceModel(SurfaceModelClass[] surfaceModel) {
		this.surfaceModel = surfaceModel;
	}

	/**
	 * Main method just to test other methods
	 * **/
	
	public static void main(String argv[]){
		long startTime = System.currentTimeMillis();
		
		CreateDiffMaps obj = new CreateDiffMaps("/home/vishal/Desktop/Grass_Output/images4",0);
		/*try {
			obj.interpolateData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		obj.saveSurfaceModel();
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Start Time: "+ startTime + "endTime: "+endTime+" Total Time taken:"+totalTime);
		
		//obj.start();
		
	}
}
