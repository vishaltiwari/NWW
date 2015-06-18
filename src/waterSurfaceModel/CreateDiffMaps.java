package waterSurfaceModel;

import java.io.File;
import java.io.FilenameFilter;
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

	/**
	 * Constructors
	 * **/
	public CreateDiffMaps(String dir){
		this.dir = dir;
		this.timeStampedDiff = new ArrayList<Map<Pair,Double> >();
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
	 * Class Methods for creating the diff map:
	 * **/
	
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
					double diff = prev.getHeight(i, j) - curr.getHeight(i, j); 
					if(diff != 0){
						Pair p = new Pair(i,j);
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
	
	public static void main(String argv[]){
		CreateDiffMaps obj = new CreateDiffMaps("/home/vishal/Desktop/Grass_Output/images3");
		
		obj.createDiffMaps();
		
	}
}
