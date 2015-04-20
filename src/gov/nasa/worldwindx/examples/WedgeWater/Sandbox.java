package gov.nasa.worldwindx.examples.WedgeWater;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;

import cesiumlanguagewriter.BillboardCesiumWriter;
import cesiumlanguagewriter.Cartographic;
import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumResourceBehavior;
import cesiumlanguagewriter.CesiumStreamWriter;
import cesiumlanguagewriter.PacketCesiumWriter;
import cesiumlanguagewriter.PositionCesiumWriter;

public class Sandbox {
	public void go() throws IOException {
		StringWriter sw = new StringWriter();
		CesiumOutputStream output = new CesiumOutputStream(sw);
		output.setPrettyFormatting(true);
		CesiumStreamWriter stream = new CesiumStreamWriter();
		PacketCesiumWriter packet = stream.openPacket(output);
		packet.writeId("Test");
		BillboardCesiumWriter billboard = packet.openBillboardProperty();
		billboard.writeColorProperty(123, 67, 0, 255);
		//billboard.writeImageProperty(URI.create("http://cesium.agi.com/images/CesiumHeaderLogo.png"), CesiumResourceBehavior.EMBED);
		billboard.close();
		PositionCesiumWriter position = packet.openPositionProperty();
		position.writeCartographicDegrees(new Cartographic(-75.0, 45.0, 100.0));
		position.close();
		packet.close();
		sw.close();
		System.out.println(sw.toString());
	}
}
