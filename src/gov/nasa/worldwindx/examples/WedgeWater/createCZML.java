package gov.nasa.worldwindx.examples.WedgeWater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.text.html.parser.Parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class createCZML{
	
	public static JSONArray createPolygon(int i){
		JSONArray list = new JSONArray();
		
		float h_meters=5566;
		float h = i*h_meters;
		float hDegree = h/111319;
		
		list.add(0);
		list.add(0);
		list.add(h);
		
		list.add(hDegree);
		list.add(0);
		list.add(h);
		
		list.add(hDegree);
		list.add(5);
		list.add(h);
		
		list.add(0);
		list.add(5);
		list.add(h);
		
		return list;		
	}
	public static void main(String args[]) throws ParseException
	{
		//dummy simulation, with a hypothetical water rise on a wedge
		JSONParser parser = new JSONParser();
		try{
			
			//Head of the doc.
			String filepath = "/home/vishal/NWW/czmlFiles/staticWedge.czml";
			File file = new File("/home/vishal/NWW/czmlFiles/outfile.czml");
			if(!file.exists()){
				file.createNewFile();
			}
			//Reading the Wedge from the czml file:
			JSONArray czmlObj = (JSONArray) parser.parse(new FileReader(filepath));
			
			/*DateFormat gmtFormat = new SimpleDateFormat();
			TimeZone gmtTime = TimeZone.getTimeZone("GMT");
			gmtFormat.setTimeZone(gmtTime);*/
			
			Calendar startDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			startDate.set(2012, 8, 4, 0, 0, 0);
			
			//System.out.println(startDate.getTime());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			String ISOStartTime = df.format(startDate.getTime());
			//System.out.println(ISOdate);
			startDate.add(Calendar.HOUR, 1);
			String ISOEndTime = df.format(startDate.getTime());
			for(int i=1 ; i<=100 ; i++){
				//JSONObject obj = new JSONObject();
				LinkedHashMap<String,Object> obj = new LinkedHashMap<String,Object>();
				String timeStamp = ISOStartTime+"/"+ISOEndTime;
				//Update the time by 1 hr.
				ISOStartTime = ISOEndTime;
				startDate.add(Calendar.HOUR, 1);
				ISOEndTime = df.format(startDate.getTime());
				
				obj.put("id", "id"+i);
				obj.put("availability", timeStamp);
				
				JSONArray cartographicDegreesArr = createPolygon(i);
				
				//Add the polygon created
				JSONObject position = new JSONObject();
				position.put("referenceFrame", "FIXED");
				position.put("cartographicDegrees", cartographicDegreesArr);
				
				obj.put("polygon",position);
				
				
				//Color for the Polygon
				JSONArray rgbaColor = new JSONArray();
				rgbaColor.add(0);
				rgbaColor.add(0);
				rgbaColor.add(255);
				rgbaColor.add(255);
				
				
				JSONObject rgba = new JSONObject();
				rgba.put("rgba",rgbaColor);
				
				JSONObject color = new JSONObject();
				color.put("color", rgba);
				
				JSONObject solidColor = new JSONObject();
				solidColor.put("solidColor", color);
				
				JSONObject material = new JSONObject();
				obj.put("material", solidColor);
				
				//Outline
				JSONObject bool = new JSONObject();
				bool.put("boolean", true);
				JSONArray outlineArr = new JSONArray();
				outlineArr.add(bool);
				obj.put("outline", outlineArr);
				
				//perpositionHeight
				JSONObject bool2 = new JSONObject();
				bool2.put("boolean", true);
				JSONArray perpositionHeightArr = new JSONArray();
				perpositionHeightArr.add(bool2);
				obj.put("perPositionHeight", perpositionHeightArr);
				
				//show
				JSONObject bool3 = new JSONObject();
				bool3.put("boolean", true);
				JSONArray showArr = new JSONArray();
				showArr.add(bool3);
				obj.put("show", showArr);
				
				//Convert the hashmap to JSONObject
				JSONObject orderedJson = new JSONObject(obj);
				
				czmlObj.add(orderedJson);
			}
			//Testing the sandbox
			Sandbox test = new Sandbox();
			test.go();
			
			//Write it in  ISO8601 interval format
			/*int year = startDate.getTime().getYear();
			int month = startDate.getTime().getMonth();
			int date = startDate.getTime().getDate();
			int hour = startDate.getTime().getHours();
			int mins = startDate.getTime().getMinutes();
			int secs = startDate.getTime().getSeconds();
			
			String StartTimeStamp = year+"-"+month+"-"+date+"T"+hour+":"+mins+":"+secs+"Z";
			System.out.println(StartTimeStamp);*/
			
			//A rise of 5566 meters/hr, 
						
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
