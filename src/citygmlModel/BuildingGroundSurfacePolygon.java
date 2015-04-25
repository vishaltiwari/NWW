package citygmlModel;

import java.util.ArrayList;
import java.util.List;

public class BuildingGroundSurfacePolygon {
	List<double[]> surfacePolygon;
	public BuildingGroundSurfacePolygon(){
		surfacePolygon = new ArrayList<double[]>();
	}
	
	public List<double[]> getSurfacePolygon() {
		return surfacePolygon;
	}
	public void setSurfacePolygon(List<double[]> surfacePolygon) {
		this.surfacePolygon = surfacePolygon;
	}	
}
