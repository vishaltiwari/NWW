package citygmlModel;

import java.util.ArrayList;
import java.util.List;

import org.citygml4j.model.gml.geometry.primitives.Envelope;

public class BuildingsClass {
	private String id;
	private String name;
	private String yearOfConstruction;
	private String yearOfDemolition;
	private String roofType;
	private double measuredHeight;
	private int storeysAboveGround;
	private int storeysBelowGround;
	private double storeysHeightsAboveGround;
	private double storeysHeightsBelowGround;
	private String srsName;
	
	private Envelope envolope;
	private List<SurfaceMember> surfacePolygon;
	private List<SurfaceMember> walls;
	private List<SurfaceMember> roofs;
	private List<SurfaceMember> solid;
	private boolean solidFlag;
	
	public BuildingsClass(){
		this.envolope = new Envelope();
		this.surfacePolygon = new ArrayList<SurfaceMember>();
		this.walls = new ArrayList<SurfaceMember>();
		this.roofs = new ArrayList<SurfaceMember>();
		this.solid = new ArrayList<SurfaceMember>();
		this.solidFlag = false;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getYearOfConstruction() {
		return yearOfConstruction;
	}
	public void setYearOfConstruction(String yearOfConstruction) {
		this.yearOfConstruction = yearOfConstruction;
	}
	public String getYearOfDemolition() {
		return yearOfDemolition;
	}
	public void setYearOfDemolition(String yearOfDemolition) {
		this.yearOfDemolition = yearOfDemolition;
	}
	public String getRoofType() {
		return roofType;
	}
	public void setRoofType(String roofType) {
		this.roofType = roofType;
	}
	public double getMeasuredHeight() {
		return measuredHeight;
	}
	public void setMeasuredHeight(double measuredHeight) {
		this.measuredHeight = measuredHeight;
	}
	public int getStoreysAboveGround() {
		return storeysAboveGround;
	}
	public void setStoreysAboveGround(int storeysAboveGround) {
		this.storeysAboveGround = storeysAboveGround;
	}
	public int getStoreysBelowGround() {
		return storeysBelowGround;
	}
	public void setStoreysBelowGround(int storeysBelowGround) {
		this.storeysBelowGround = storeysBelowGround;
	}
	public double getStoreysHeightsAboveGround() {
		return storeysHeightsAboveGround;
	}
	public void setStoreysHeightsAboveGround(double storeysHeightsAboveGround) {
		this.storeysHeightsAboveGround = storeysHeightsAboveGround;
	}
	public double getStoreysHeightsBelowGround() {
		return storeysHeightsBelowGround;
	}
	public void setStoreysHeightsBelowGround(double storeysHeightsBelowGround) {
		this.storeysHeightsBelowGround = storeysHeightsBelowGround;
	}
	public String getSrsName() {
		return srsName;
	}
	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}
	public Envelope getEnvolope() {
		return envolope;
	}
	public void setEnvolope(Envelope envolope) {
		this.envolope = envolope;
	}
	public List<SurfaceMember> getSurfacePolygon() {
		return surfacePolygon;
	}
	public void setSurfacePolygon(List<SurfaceMember> surfacePolygon) {
		this.surfacePolygon = surfacePolygon;
	}
	public List<SurfaceMember> getWalls() {
		return walls;
	}
	public void setWalls(List<SurfaceMember> walls) {
		this.walls = walls;
	}
	public List<SurfaceMember> getRoofs() {
		return roofs;
	}
	public void setRoofs(List<SurfaceMember> roofs) {
		this.roofs = roofs;
	}
	public List<SurfaceMember> getSolid() {
		return solid;
	}
	public void setSolid(List<SurfaceMember> solid) {
		this.solid = solid;
	}
	public boolean isSolidFlag() {
		return solidFlag;
	}
	public void setSolidFlag(boolean solidFlag) {
		this.solidFlag = solidFlag;
	}
	
}
