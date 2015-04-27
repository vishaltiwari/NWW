package citygmlModel;

public class SurfaceMember {
	private PolygonClass polygon;
	private String id;
	
	public SurfaceMember(){
		this.polygon = new PolygonClass();
		this.id="";
	}
	public PolygonClass getPolygon() {
		return polygon;
	}
	public void setPolygon(PolygonClass polygon) {
		this.polygon = polygon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
