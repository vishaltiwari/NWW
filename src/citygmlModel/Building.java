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

public class Building {
	private String id;
	private String description;
	private double measuredHeightMeters;
	private int storiesAboveGround;
	private int storiesBelowGround;
	private List<double[]> surfacePolygon;
	private List<List<double[]>> wallPolygons;
	private List<List<double[]>> roofPolygons;
	
	public Building(){
		id = "";
		description = "";
		measuredHeightMeters = 0;
		storiesAboveGround = 0;
		storiesBelowGround = 0;
		surfacePolygon = new ArrayList<double[]>();
		wallPolygons = new ArrayList<List<double[]>>();
		roofPolygons = new ArrayList<List<double[]>>();
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
										System.out.println("inside the linearRing visitor");
										if(linearRing.isSetPosList()){
											DirectPositionList posList = linearRing.getPosList();
											List<Double> points = posList.toList3d();
											
											for(int i=0 ; i<points.size() ;i+=3){
												double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
												//System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
												surfacePolygon.add(vals);
											}
										}
									}
								};
								groundSurface.accept(footPolygonWalker);					
							}
						};
						//Visit the WallSurface to get its geometry
						FeatureWalker wallWalker = new FeatureWalker(){
							public void visit(WallSurface wallSurface){
								//Get the geometry of the walls
								GMLWalker wallWalk = new GMLWalker(){
									public void visit(LinearRing linearRing){
										DirectPositionList posList = linearRing.getPosList();
										List<Double> points = posList.toList3d();
										List<double[]> wallPolygon = new ArrayList<double[]>();
										for(int i=0 ; i<points.size() ;i+=3){
											double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
											//System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
											wallPolygon.add(vals);
										}
										wallPolygons.add(wallPolygon);
									}
								};
								wallSurface.accept(wallWalk);
							}
						};
						//Visit roofSurface to get its geometry:
						FeatureWalker roofWalker = new FeatureWalker(){
							public void visit(RoofSurface roofSurface){
								//Get the geometry of the roof
								GMLWalker roofWalk = new GMLWalker(){
									public void visit(LinearRing linearRing){
										DirectPositionList posList = linearRing.getPosList();
										List<Double> points = posList.toList3d();
										
										List<double[]> roofPolygon = new ArrayList<double[]>();
										for(int i=0 ; i<points.size() ;i+=3){
											double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
											//System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
											roofPolygon.add(vals);
										}
										roofPolygons.add(roofPolygon);
									}
								};
								roofSurface.accept(roofWalk);
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
	public List<double[]> getSurfacePolygon() {
		return surfacePolygon;
	}
	public void setSurfacePolygon(List<double[]> surfacePolygon) {
		this.surfacePolygon = surfacePolygon;
	}
	public List<List<double[]>> getWallPolygons() {
		return wallPolygons;
	}
	public void setWallPolygons(List<List<double[]>> wallPolygons) {
		this.wallPolygons = wallPolygons;
	}
	public List<List<double[]>> getRoofPolygons() {
		return roofPolygons;
	}
	public void setRoofPolygons(List<List<double[]>> roofPolygons) {
		this.roofPolygons = roofPolygons;
	}
	public void setMeasuredHeightMeters(double measuredHeightMeters) {
		this.measuredHeightMeters = measuredHeightMeters;
	}
	
	
}
