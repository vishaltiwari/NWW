package meshGenerator;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

public class RenderMesh extends ApplicationTemplate{
	protected static class AppFrame extends ApplicationTemplate.AppFrame
    {   
        public AppFrame()
        {
            super(true, false, false);

            RenderableLayer layer = new RenderableLayer();
            //Cube cube = new Cube(Position.fromDegrees(35.0, -120.0, 3000), 1000);
            String filename = "/home/vishal/NWW/sampleData/DSM.png";
            MeshSurfaceVBO waterSurface = new MeshSurfaceVBO(filename);
            ShapeAttributes normalAttributes = new BasicShapeAttributes();
            
            normalAttributes.setInteriorMaterial(Material.YELLOW);
            normalAttributes.setOutlineWidth(2);
            normalAttributes.setOutlineOpacity(0.5);
            normalAttributes.setDrawInterior(true);
            normalAttributes.setDrawOutline(true);
            
            waterSurface.setAttributes(normalAttributes);
            
            layer.addRenderable(waterSurface);

            getWwd().getModel().getLayers().add(layer);
        }
    }   

	public static void main(String[] args)
	{   
		Configuration.setValue(AVKey.INITIAL_LATITUDE, 0);
		Configuration.setValue(AVKey.INITIAL_LONGITUDE, 0);
		Configuration.setValue(AVKey.INITIAL_ALTITUDE, 15500);
		Configuration.setValue(AVKey.INITIAL_PITCH, 45);
		Configuration.setValue(AVKey.INITIAL_HEADING, 45);

		ApplicationTemplate.start("World Wind Custom Renderable Tutorial", AppFrame.class);
	}
}
