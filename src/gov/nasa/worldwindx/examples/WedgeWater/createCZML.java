package gov.nasa.worldwindx.examples.WedgeWater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.text.html.parser.Parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class createCZML{
	
	public static void main(String args[]) throws ParseException
	{
		//dummy simulation, with a hypothetical water rise on a wedge
		JSONParser parser = new JSONParser();
		try{
			
			//Head of the doc.
			String filepath = "/home/vishal/NWW/czmlFiles/sample1.czml";
			File file = new File("/home/vishal/NWW/czmlFiles/outfile.czml");
			if(!file.exists()){
				file.createNewFile();
			}
			//Reading the Wedge from the czml file:
			JSONArray czmlObj = (JSONArray) parser.parse(new FileReader(filepath));
						
			//writing to file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			fw.write(czmlObj.toJSONString());
			fw.flush();
			fw.close();
			
			System.out.println("Done writing to File");
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		//int rainfall = 10;
		//10 mm/hr
		
		//**Define the structure**
		//making the wedge x-y=0
		//have a length of 10 meters
		
		//making the inclosing sides z=5, z=-5, x=0;
		//draw the scene as a czml object.
		
		//coordinates of the water rise:
		//(0,h,5) , (0,h,-5) , (h,h,-5) , (h,h,5)
		
		//calculating the value of h:
		//5*h*h is the volume of water over an area of 100 X 10 sq.meter.
		
		//run a loop and store the height vaules for every hour in the czml file.
		//use the czml java library writer to write to czml file
		
	}
}
