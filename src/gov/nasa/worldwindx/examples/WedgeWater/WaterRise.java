package gov.nasa.worldwindx.examples.WedgeWater;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.joda.time.DateTime;




//import agi.foundation.time.JulianDate;
//import agi.foundation.time.TimeStandard;
import cesiumlanguagewriter.Cartographic;
import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumStreamWriter;
import cesiumlanguagewriter.JulianDate;
import cesiumlanguagewriter.PacketCesiumWriter;
import cesiumlanguagewriter.SolidColorMaterialCesiumWriter;

public class WaterRise {
	public void addDynamicRise(CesiumOutputStream output){
		CesiumStreamWriter stream = new CesiumStreamWriter();
		
		Calendar startDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		startDate.set(2012, 8, 4, 0, 0, 0);
		JulianDate startJD = new JulianDate(new DateTime(startDate.getTime()));
		
		startDate.add(Calendar.HOUR, 1);
		JulianDate endJD = new JulianDate(new DateTime(startDate.getTime()));
		
		for(int i=1 ; i<=100 ; i++){
			//Water level rise:
			float h_meters=(5*111319)/100; //increment value
			float h = i*h_meters;
			float hDegree = h/111319;
			//Create the packet
			PacketCesiumWriter packet = stream.openPacket(output);
			//ID
			packet.writeId("id"+i);
			
			//Time availability for 1 hr.
			packet.writeAvailability(startJD, endJD);
			
			startJD = endJD;
			endJD = endJD.addSeconds(3600);
			
			ArrayList<Cartographic> arr = new ArrayList<Cartographic>();
			arr.add(new Cartographic(0,0,h));
			arr.add(new Cartographic(hDegree,0,h));
			arr.add(new Cartographic(hDegree,5,h));
			arr.add(new Cartographic(0,5,h));
			arr.add(new Cartographic(0,0,h));
			
			//Create the polygon
			cesiumlanguagewriter.PolygonCesiumWriter polygon = packet.openPolygonProperty();
			polygon.writePositionsPropertyCartographicDegrees(arr);
			
			//Material Property
			cesiumlanguagewriter.MaterialCesiumWriter materialWriter = polygon.openMaterialProperty();
			SolidColorMaterialCesiumWriter solidColorWriter = materialWriter.openSolidColorProperty();
			cesiumlanguagewriter.ColorCesiumWriter colorWriter = solidColorWriter.openColorProperty();
			colorWriter.writeRgba(0, 0, 255, 255);
			colorWriter.close();
			solidColorWriter.close();
			materialWriter.close();
			
			polygon.writeOutlineProperty(true);
			polygon.writePerPositionHeightProperty(true);
			polygon.writeShowProperty(true);
			
			polygon.close();
			
			packet.close();
		}
	}
	/*public static void main(String args[]){
		Calendar startDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		startDate.set(2012, 8, 4, 0, 0, 0);
		
		JulianDate startJD = new JulianDate(new DateTime(startDate.getTime()));
		System.out.println(startJD);
		
		startDate.add(Calendar.HOUR, 1);
		JulianDate endJD = new JulianDate(new DateTime(startDate.getTime()));
		
	}*/
}
