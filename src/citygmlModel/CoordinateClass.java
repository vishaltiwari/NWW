package citygmlModel;

public class CoordinateClass {
	private double[] coords;
	private double x;
	private double y;
	private double z;
	
	public CoordinateClass(double x,double y,double z){
		this.x = x;
		this.y = y;
		this.z = z;
		coords[0] = x;
		coords[1] = y;
		coords[2] = z;
	}
	public CoordinateClass(double[] coords){
		this.coords = coords;
		this.x = coords[0];
		this.y = coords[1];
		this.z = coords[2];
	}
	public double[] getCoords() {
		return coords;
	}
	public void setCoords(double[] coords) {
		this.coords = coords;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	
}
