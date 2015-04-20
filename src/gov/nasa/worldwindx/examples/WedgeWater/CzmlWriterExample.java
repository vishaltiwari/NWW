package gov.nasa.worldwindx.examples.WedgeWater;

import java.io.IOException;
import java.io.StringWriter;

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
		packet.writeId("Test");
		packet.close();
		sw.close();
		
		System.out.println(sw.toString());
		
		
	}
}
