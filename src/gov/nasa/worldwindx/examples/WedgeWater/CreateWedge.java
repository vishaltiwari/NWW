package gov.nasa.worldwindx.examples.WedgeWater;

import java.util.ArrayList;

import cesiumlanguagewriter.Cartographic;
import cesiumlanguagewriter.CesiumOutputStream;
import cesiumlanguagewriter.CesiumStreamWriter;
import cesiumlanguagewriter.PacketCesiumWriter;
import cesiumlanguagewriter.SolidColorMaterialCesiumWriter;

public class CreateWedge {
	public void create(CesiumOutputStream output){
		CesiumStreamWriter stream = new CesiumStreamWriter();
		
		PacketCesiumWriter packet0 = stream.openPacket(output);
		//Write the document packet
		packet0.writeId("document");
		packet0.writeVersion("1.0");
				
		packet0.close();

		//Create the Wedge:
		String namesId[] = new String[10];
		
		namesId[0] = "Wedge";
		namesId[1] = "Side1TriangleLower";
		namesId[2] = "Side1TriangleUpper";
		namesId[3] = "side2";
		namesId[4] = "side3";
		
		ArrayList<ArrayList<Cartographic>> listofPositions = new ArrayList<ArrayList<Cartographic>>();
		
		float h = 5*111319;
		
		ArrayList<Cartographic> arr1 = new ArrayList<Cartographic>();
		//Wedge slant plane:
		arr1.add(new Cartographic(0, 0, 0));
		arr1.add(new Cartographic(0, 5.0, 0));
		arr1.add(new Cartographic(5, 5, h));
		arr1.add(new Cartographic(5, 0, h));
		arr1.add(new Cartographic(0, 0, 0));
		listofPositions.add(arr1);
		
		//Lower Triangle Side
		arr1 = new ArrayList<Cartographic>();
		arr1.add(new Cartographic(0, 0, 0));
		arr1.add(new Cartographic(0, 5.0, 0));
		arr1.add(new Cartographic(0, 5, h));
		arr1.add(new Cartographic(0, 0, 0));
		
		listofPositions.add(arr1);
		
		//Upper Triangle Side
		arr1 = new ArrayList<Cartographic>();
		arr1.add(new Cartographic(0, 0, 0));
		arr1.add(new Cartographic(0, 0, h));
		arr1.add(new Cartographic(0, 5, h));
		arr1.add(new Cartographic(0, 0, 0));

		listofPositions.add(arr1);
		
		//Side 2
		arr1 = new ArrayList<Cartographic>();
		arr1.add(new Cartographic(0, 0, 0));
		arr1.add(new Cartographic(0, 0, h));
		arr1.add(new Cartographic(5, 0, h));
		arr1.add(new Cartographic(0, 0, 0));

		listofPositions.add(arr1);
		
		//Side 3
		arr1 = new ArrayList<Cartographic>();
		arr1.add(new Cartographic(0, 5, 0));
		arr1.add(new Cartographic(0, 5, h));
		arr1.add(new Cartographic(5, 5, h));
		arr1.add(new Cartographic(0, 5, 0));

		listofPositions.add(arr1);

		for(int i=0 ; i<listofPositions.size() ; i++){
			PacketCesiumWriter packet = stream.openPacket(output);
			packet.writeId(namesId[i]);
			cesiumlanguagewriter.PolygonCesiumWriter polygon = packet.openPolygonProperty();
			
			//List<cesiumlanguagewriter.CartographicDegrees> positions = new ArrayList<cesiumlanguagewriter.CartographicDegrees>();

			
			polygon.writePositionsPropertyCartographicDegrees(listofPositions.get(i));
			//polygon.writePositionsPropertyReferences(references);
			//Material Property
			cesiumlanguagewriter.MaterialCesiumWriter materialWriter = polygon.openMaterialProperty();
			SolidColorMaterialCesiumWriter solidColorWriter = materialWriter.openSolidColorProperty();
			cesiumlanguagewriter.ColorCesiumWriter colorWriter = solidColorWriter.openColorProperty();
			colorWriter.writeRgba(255, 255, 0, 100);
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
}
