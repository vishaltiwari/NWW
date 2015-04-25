package citygmlModel;

import java.util.ArrayList;
import java.util.List;

public class BuildingWallSurfacePolygons {
	private List<List<double[]>> wallPolygons;
	
	public BuildingWallSurfacePolygons(){
		wallPolygons = new ArrayList<List<double[]>>();
	}

	public List<List<double[]>> getWallPolygons() {
		return wallPolygons;
	}

	public void setWallPolygons(List<List<double[]>> wallPolygons) {
		this.wallPolygons = wallPolygons;
	}
	
}
