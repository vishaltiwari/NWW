package gov.nasa.worldwindx.examples.WedgeWater;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import agi.internal.agi.foundation.cesium.agi.foundation.cesium.ColorCesiumWriter;
import agi.internal.agi.foundation.cesium.agi.foundation.cesium.MaterialCesiumWriter;
import agi.internal.agi.foundation.cesium.agi.foundation.cesium.PolygonCesiumWriter;
import cesiumlanguagewriter.Cartographic;
import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumStreamWriter;
import cesiumlanguagewriter.PacketCesiumWriter;
import cesiumlanguagewriter.SolidColorMaterialCesiumWriter;

public class CzmlWriterExample {
	public static void main(String args[]) throws IOException{
		
		StringWriter sw = new StringWriter();
		CesiumOutputStream output = new CesiumOutputStream(sw);
		output.setPrettyFormatting(true);
		
		//Write a polygon packet[Make the Static geometry(i.e. Wedge)]:
		CreateWedge wedge = new CreateWedge();
		wedge.create(output);
		
		//Add the dynamic polygons to czml file
		WaterRise water = new WaterRise();
		water.addDynamicRise(output);
		
		sw.close();
		
		System.out.println(sw.toString());
		try{
			File file = new File("/home/vishal/NWW/czmlFiles/outfile.czml");
			if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile()) ;
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("[\n\t"+sw.toString()+"\n]");
			bw.close();
		}
		catch (IOException e){
			System.out.println("Error while writing");
		}
	}
}