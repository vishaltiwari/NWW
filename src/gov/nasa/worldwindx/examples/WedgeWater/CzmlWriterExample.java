package gov.nasa.worldwindx.examples.WedgeWater;

import java.awt.List;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import agi.internal.agi.foundation.cesium.agi.foundation.cesium.PolygonCesiumWriter;
import cesiumlanguagewriter.Cartographic;
import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumStreamWriter;
import cesiumlanguagewriter.PacketCesiumWriter;

public class CzmlWriterExample {
	public static void main(String args[]) throws IOException{
		
		StringWriter sw = new StringWriter();
		CesiumOutputStream output = new CesiumOutputStream(sw);
		output.setPrettyFormatting(true);
		CesiumStreamWriter stream = new CesiumStreamWriter();
		PacketCesiumWriter packet = stream.openPacket(output);
		//Write the document packet
		packet.writeId("document");
		packet.writeVersion("1.0");
		
		packet.close();
		//Write a polygon packet:
		PacketCesiumWriter packet2 = stream.openPacket(output);
		packet2.writeId("Wedge");
		cesiumlanguagewriter.PolygonCesiumWriter polygon = packet2.openPolygonProperty();
		
		//List<cesiumlanguagewriter.CartographicDegrees> positions = new ArrayList<cesiumlanguagewriter.CartographicDegrees>();
		float h = 5*111319;
		Cartographic object1 = new Cartographic(0, 0, 0);
		Cartographic object2 = new Cartographic(0, 5.0, 0);
		Cartographic object3 = new Cartographic(5, 5, h);
		Cartographic object4 = new Cartographic(5, 0, h);
		Cartographic object5 = new Cartographic(0, 0, 0);
		
		
		ArrayList<Cartographic> arr = new ArrayList<Cartographic>();
		arr.add(object1);
		arr.add(object2);
		arr.add(object3);
		arr.add(object4);
		arr.add(object5);
		
		polygon.writePositionsPropertyCartographicDegrees(arr);
		//polygon.writePositionsPropertyReferences(references);
		
		packet2.close();
				
		sw.close();
		
		System.out.println(sw.toString());
		
		
	}
}
