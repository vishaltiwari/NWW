package randomTest;

import gov.nasa.worldwind.BasicFactory;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.event.BulkRetrievalEvent;
import gov.nasa.worldwind.event.BulkRetrievalListener;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.retrieve.BulkRetrievable;
import gov.nasa.worldwind.retrieve.BulkRetrievalThread;
import gov.nasa.worldwind.terrain.CompoundElevationModel;

public class WMSHeight{

	
	public void downloadData(Sector sector){
		BulkRetrievable layer;
		BulkRetrievalThread thread;
		AVListImpl params = new AVListImpl();
		
		params.setValue(AVKey.NUM_LEVELS, 9);
		CompoundElevationModel cem = (CompoundElevationModel) BasicFactory.create(AVKey.ELEVATION_MODEL_FACTORY, "/home/vishal/worldwind-2.0.0/src/config/Earth/myElevation.xml");
		layer = (BulkRetrievable) cem.getElevationModels().get(0);
		System.out.println(layer.getName());
		
		thread = layer.makeLocal(sector, 0, new BulkRetrievalListener()
        {
            @Override
            public void eventOccurred(BulkRetrievalEvent event)
            {
                System.out.println(event.getItem());
            }
        });
        try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch blockcd Here 
			e.printStackTrace();
		}

		
	}
	public static void main(String argv[]){
		WMSHeight wms = new WMSHeight();
		wms.downloadData(Sector.fromDegrees(52.3235382489081, 52.33008046162245, 13.03095442220654, 13.044403199503318));
	}
	
}
