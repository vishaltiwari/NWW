package citygmlModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.CityGMLBuilder;
import org.citygml4j.model.citygml.CityGML;
import org.citygml4j.model.citygml.CityGMLClass;
import org.citygml4j.model.citygml.building.GroundSurface;
import org.citygml4j.model.citygml.building.RoofSurface;
import org.citygml4j.model.citygml.building.WallSurface;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.gml.geometry.primitives.DirectPositionList;
import org.citygml4j.model.gml.geometry.primitives.LinearRing;
import org.citygml4j.util.walker.FeatureWalker;
import org.citygml4j.util.walker.GMLWalker;
import org.citygml4j.xml.io.CityGMLInputFactory;
import org.citygml4j.xml.io.reader.CityGMLReader;

public class Buildings {
	private String id;
	private String description;
	private double measuredHeightMeters;
	private int storiesAboveGround;
	private int storiesBelowGround;
	private List<BuildingGroundSurfacePolygon> surfacePolygons;
	private List<BuildingWallSurfacePolygons> listBuildingWalls;
	private List<BuildingRoofPolygons> listroofPolygons;
	
	public static List<List<double[]>> wallsPolygon;
	public static List<List<double[]>> roofPolygons;
	
	public Buildings(){
		id = "";
		description = "";
		measuredHeightMeters = 0;
		storiesAboveGround = 0;
		storiesBelowGround = 0;
		surfacePolygons = new ArrayList<BuildingGroundSurfacePolygon>();
		listBuildingWalls = new ArrayList<BuildingWallSurfacePolygons>();
		listroofPolygons = new ArrayList<BuildingRoofPolygons>();
		
		roofPolygons = new ArrayList<List<double[]>>();
		wallsPolygon = new ArrayList<List<double[]>>();
	}
	public void IterateGMLFile(String filePath) throws Exception{
		CityGMLContext ctx = new CityGMLContext();
		CityGMLBuilder builder = ctx.createCityGMLBuilder();
		
		CityGMLInputFactory in = builder.createCityGMLInputFactory();
		CityGMLReader reader = in.createCityGMLReader(new File(filePath));
		
		while(reader.hasNext())
		{
			CityGML citygml = reader.nextFeature();
			//System.out.println("Found class:" + citygml.getCityGMLClass() + "\nVersion"+citygml.getCityGMLModule().getVersion());
			if(citygml.getCityGMLClass() == CityGMLClass.CITY_MODEL)
			{
				CityModel cityModel = (CityModel)citygml;
				
				//Walk through the buildings
				FeatureWalker buildingwalker = new FeatureWalker(){
					public void visit(org.citygml4j.model.citygml.building.Building building){
						id = building.getId();
						measuredHeightMeters = building.getMeasuredHeight().getValue();
						FeatureWalker surfacewalker = new FeatureWalker(){
							public void visit(GroundSurface groundSurface){
								//System.out.println("GroundSurface ID"+groundSurface.getId());
								//Get the Coordinates of the groundSurface polygon:
								GMLWalker footPolygonWalker = new GMLWalker(){
									public void visit(LinearRing linearRing)
									{
										if(linearRing.isSetPosList()){
											DirectPositionList posList = linearRing.getPosList();
											List<Double> points = posList.toList3d();
											BuildingGroundSurfacePolygon buildingSurfacePolygon = new BuildingGroundSurfacePolygon();
											List<double[]> polygonfloor = new ArrayList<double[]>();
											for(int i=0 ; i<points.size() ;i+=3){
												double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
												//System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
												polygonfloor.add(vals);
											}
											
											buildingSurfacePolygon.setSurfacePolygon(polygonfloor);
											surfacePolygons.add(buildingSurfacePolygon);
										}
									}
								};
								groundSurface.accept(footPolygonWalker);					
							}
						};
						//Visit the WallSurface to get its geometry
						System.out.println("BuildingId "+building.getId());
						FeatureWalker wallWalker = new FeatureWalker(){
							public void visit(WallSurface wallSurface){
								//Get the geometry of the walls
								BuildingWallSurfacePolygons buildingWallSurfacepolygons = new BuildingWallSurfacePolygons();
								//wallsPolygon = new ArrayList<List<double[]>>();
								System.out.println("wallID"+wallSurface.getId());
								wallsPolygon = new ArrayList<List<double[]>>();
								GMLWalker wallWalk = new GMLWalker(){	
									public void visit(LinearRing linearRing){
										
										System.out.println("inside the linearRing visitor");
										
										DirectPositionList posList = linearRing.getPosList();
										List<Double> points = posList.toList3d();
										
										List<double[]> wallPolygon = new ArrayList<double[]>();
										
										for(int i=0 ; i<points.size() ;i+=3){
											double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
											//System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
											wallPolygon.add(vals);
										}
										
										//System.out.println(wallPolygon.size());
										wallsPolygon.add(wallPolygon);
										
										//System.out.println(wallsPolygon.size());
										/*for(int i=0 ; i<wallPolygon.size(); i++){
											List<double[]> a = wallsPolygon.get(i);
											for(int j=0 ; j<a.size() ; j++){
												double[] b = a.get(j);
												System.out.println(b[0]+" "+b[1]+" "+b[2]);
											}
											System.out.println();
										}*/
										//System.out.println(wallsPolygon.get(wallsPolygon.size()-1)+"\n");
									}
								};
								wallSurface.accept(wallWalk);
								//TODO::If this is outside, then it might probably work:
								buildingWallSurfacepolygons.setWallPolygons(wallsPolygon);
								//wallsPolygon.clear();
								listBuildingWalls.add(buildingWallSurfacepolygons);
								//System.out.println("Number of buildings"+listBuildingWalls.size());
							}
						};
						//Visit roofSurface to get its geometry:
						FeatureWalker roofWalker = new FeatureWalker(){
							public void visit(RoofSurface roofSurface){
								//Get the geometry of the roof
								BuildingRoofPolygons buildingRoofPolygons = new BuildingRoofPolygons();
								GMLWalker roofWalk = new GMLWalker(){
									public void visit(LinearRing linearRing){
										DirectPositionList posList = linearRing.getPosList();
										List<Double> points = posList.toList3d();
										
										
										List<double[]> roofPolygon = new ArrayList<double[]>();
										roofPolygons = new ArrayList<List<double[]>>();
										
										for(int i=0 ; i<points.size() ;i+=3){
											double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
											//System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
											roofPolygon.add(vals);
										}
										roofPolygons.add(roofPolygon);
									}
								};
								roofSurface.accept(roofWalk);
								buildingRoofPolygons.setRoofPolygon(roofPolygons);
								listroofPolygons.add(buildingRoofPolygons);
							}
						};
						building.accept(surfacewalker);
						building.accept(wallWalker);
						building.accept(roofWalker);
					}
				};
				cityModel.accept(buildingwalker);
			}
			else{
				System.out.println("Its not a CityModel, Its:"+citygml.getCityGMLClass().toString());
			}
		}
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getMeasuredHeightMeters() {
		return measuredHeightMeters;
	}

	public void setMeasuredHeightMeters(int measuredHeightMeters) {
		this.measuredHeightMeters = measuredHeightMeters;
	}

	public int getStoriesAboveGround() {
		return storiesAboveGround;
	}

	public void setStoriesAboveGround(int storiesAboveGround) {
		this.storiesAboveGround = storiesAboveGround;
	}

	public int getStoriesBelowGround() {
		return storiesBelowGround;
	}

	public void setStoriesBelowGround(int storiesBelowGround) {
		this.storiesBelowGround = storiesBelowGround;
	}
	public List<BuildingGroundSurfacePolygon> getSurfacePolygons() {
		return surfacePolygons;
	}
	public void setSurfacePolygons(List<BuildingGroundSurfacePolygon> surfacePolygon) {
		this.surfacePolygons = surfacePolygon;
	}
	public List<BuildingWallSurfacePolygons> getWallPolygons() {
		return listBuildingWalls;
	}
	public void setWallPolygons(List<BuildingWallSurfacePolygons> wallPolygons) {
		this.listBuildingWalls = wallPolygons;
	}
	public List<BuildingRoofPolygons> getRoofPolygons() {
		return listroofPolygons;
	}
	public void setRoofPolygons(List<BuildingRoofPolygons> roofPolygons) {
		this.listroofPolygons = roofPolygons;
	}
	public void setMeasuredHeightMeters(double measuredHeightMeters) {
		this.measuredHeightMeters = measuredHeightMeters;
	}
	
	
}
