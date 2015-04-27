package citygmlModel;

import java.util.ArrayList;
import java.util.List;

public class PolygonClass {
	private List<CoordinateClass> polygon;
	
	public PolygonClass(){
		this.polygon = new ArrayList<CoordinateClass>();
	}
	public PolygonClass(List<CoordinateClass> coordList){
		this.polygon = new ArrayList<CoordinateClass>();
		this.polygon = coordList;
	}

	public List<CoordinateClass> getPolygon() {
		return polygon;
	}

	public void setPolygon(List<CoordinateClass> polygon) {
		this.polygon = polygon;
	}
	
	
}
