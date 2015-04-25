package citygmlModel;

import java.util.ArrayList;
import java.util.List;

public class BuildingRoofPolygons {
	List<List<double[]>> roofPolygon;
	public BuildingRoofPolygons(){
		roofPolygon = new ArrayList<List<double[]>>();
	}
	public List<List<double[]>> getRoofPolygon() {
		return roofPolygon;
	}
	public void setRoofPolygon(List<List<double[]>> roofPolygon) {
		this.roofPolygon = roofPolygon;
	}
	
}
