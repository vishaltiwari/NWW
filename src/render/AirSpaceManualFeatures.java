package render;

import java.awt.Color;
import java.util.Arrays;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.AirspaceLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.airspaces.Airspace;
import gov.nasa.worldwind.render.airspaces.Polygon;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

public class AirSpaceManualFeatures extends ApplicationTemplate{

	
	public static class AppFrame extends ApplicationTemplate.AppFrame{
		protected AirspaceLayer aglAirspaces;
		public AppFrame(){
			super(true, true, false);
			
			this.aglAirspaces = new AirspaceLayer();
			this.aglAirspaces.setName("My first aiSpace");
			
			// Polygon over the Sierra Nevada mountains.
            Polygon poly = new Polygon();
            poly.setLocations(Arrays.asList(
                LatLon.fromDegrees(40.1323, -122.0911),
                LatLon.fromDegrees(38.0062, -120.7711),
                LatLon.fromDegrees(37.0562, -119.6226),
                LatLon.fromDegrees(36.9231, -118.1829),
                LatLon.fromDegrees(37.8211, -118.8557),
                LatLon.fromDegrees(39.0906, -120.0304),
                LatLon.fromDegrees(40.2609, -120.8295)));
            poly.setAltitudes(0, 5);
            poly.setAltitudeDatum(AVKey.ABOVE_GROUND_LEVEL, AVKey.ABOVE_GROUND_REFERENCE);
            poly.setValue(AVKey.DISPLAY_NAME, "Polygon over the Sierra Nevada mountains.");
            this.setupDefaultMaterial(poly, Color.LIGHT_GRAY);
            //airspaces.add(poly);

			this.aglAirspaces.addAirspace(poly);
			
			insertBeforePlacenames(this.getWwd(), this.aglAirspaces);
		}
		protected void setupDefaultMaterial(Airspace a, Color color)
        {
            a.getAttributes().setDrawOutline(true);
            a.getAttributes().setMaterial(new Material(color));
            a.getAttributes().setOutlineMaterial(new Material(WWUtil.makeColorBrighter(color)));
            a.getAttributes().setOpacity(0.8);
            a.getAttributes().setOutlineOpacity(0.9);
            a.getAttributes().setOutlineWidth(3.0);
        }

	}
	public static void main(String argv[]){
		start("World Wind Airspaces", AppFrame.class);
	}
}
