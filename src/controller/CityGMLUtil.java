package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nasa.worldwind.geom.LatLon;

public class CityGMLUtil {
	
	private double[] center;
	private String EPSG;
	private String layername;
	
	public String getSRS(String line){
		
		if(!line.contains("srsName")){
			return "";
		}
		String EPSGCode="";
		String[] subs = line.split("srsName=");
		//System.out.println(subs[0] + "\n"+subs[1]);
		if(subs.length < 2){
			return "";
		}
		else{
			String code = subs[1];
			
			for(int i=0 ; i<code.length() ; i++){
				if(code.charAt(i)=='"'){
					i++;
					while(code.charAt(i)!='"' && i<code.length()){
						EPSGCode += code.charAt(i);
						i++;
					}
					break;
				}
			}
		}
		if(EPSGCode.contains("EPSG")){
			String[] s = EPSGCode.split("EPSG:");
			return s[1];
		}
		return EPSGCode;
	}
	
	public double[] getCoordinate(String line){
		//String[] strs = line.split("")
		String pattern = "[>].*[<]";
		
		Pattern r = Pattern.compile(pattern);
		
		Matcher m = r.matcher(line);
		if(m.find()){
		//	System.out.println("middle part"+m.group(0));
			pattern = "[^>].*[^<]";
			r = Pattern.compile(pattern);
			m = r.matcher(m.group(0));
			if(m.find()){
				System.out.println(m.group(0));
			}
		}
		
		String newString = m.group(0);
		String[] coods = newString.split(" ");
		
		
		double[] coords = new double[3];
		coords[0] = Double.parseDouble(coods[0]);
		coords[1] = Double.parseDouble(coods[1]);
		coords[2] = Double.parseDouble(coods[2]);
		
		//System.out.println(coords[0]);
		//System.out.println(coords[1]);
		//System.out.println(coords[2]);
		return coords;
	}
	
	public double[] getCenter(ArrayList<double[]> list){
		
		double[] lowPos = list.get(0);
		double[] highPos = list.get(1);
		
		double[] center = new double[3];
		center[0] = (lowPos[0]+highPos[0])/2;
		center[1] = (lowPos[1]+highPos[1])/2;
		center[2] = (lowPos[2]+highPos[2])/2;
		return center;
		
	}
	
	public String getFileName(String filename){
		String[] parts = filename.split("/");
		//System.out.println(parts[parts.length-1]);
		return parts[parts.length-1];
	}
	@SuppressWarnings("resource")
	public CityGMLUtil(String filename) throws IOException{
		
		BufferedReader br = null;
		String sCurrentLine="";

		this.layername = getFileName(filename);
		br = new BufferedReader(new FileReader(filename));
		
		while((sCurrentLine = br.readLine()) != null){
			//System.out.println(sCurrentLine);
			if(sCurrentLine.contains("<gml:Envelope")){
				//System.out.println("srsName Line : "+sCurrentLine);
				this.EPSG = getSRS(sCurrentLine);
				System.out.println(this.EPSG);
				
				//get the next two <gml:pos> tag lines:
				int count=0;
				ArrayList<double[]> extend = new ArrayList<double[]>();
				while((sCurrentLine = br.readLine()) != null){
					if(count>=2) break;
					if(sCurrentLine.equals("")){
						continue;
					}
					
					extend.add(getCoordinate(sCurrentLine));
					
					++count;
				}
				this.center = getCenter(extend);
				break;
			}
		}
	}
	/**
	 * Setter and getter for mid-coordinate and the EPSG code 
	 * **/
	public double[] getCenter() {
		return center;
	}

	public void setCenter(double[] center) {
		this.center = center;
	}

	public String getEPSG() {
		return EPSG;
	}

	public void setEPSG(String ePSG) {
		EPSG = ePSG;
	}
	
	public String getLayername() {
		return layername;
	}

	public void setLayername(String layername) {
		this.layername = layername;
	}
	
	public static void main(String argv[]){
		try {
			CityGMLUtil obj = new CityGMLUtil("/home/vishal/NWW/sampleData/waldbruecke_v1.0.0.gml");
			//obj.getLocation("/home/vishal/NWW/sampleData/waldbruecke_v1.0.0.gml");
			double[] center = obj.getCenter();
			System.out.println(obj.getEPSG() + "," + center[0] + ","+center[1]+","+center[2]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
