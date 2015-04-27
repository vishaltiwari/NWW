package citygmlModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.CityGMLBuilder;
import org.citygml4j.model.citygml.CityGML;
import org.citygml4j.model.citygml.CityGMLClass;
import org.citygml4j.model.citygml.building.Building;
import org.citygml4j.model.citygml.building.GroundSurface;
import org.citygml4j.model.citygml.building.RoofSurface;
import org.citygml4j.model.citygml.building.WallSurface;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.gml.geometry.primitives.Coord;
import org.citygml4j.model.gml.geometry.primitives.DirectPosition;
import org.citygml4j.model.gml.geometry.primitives.DirectPositionList;
import org.citygml4j.model.gml.geometry.primitives.LinearRing;
import org.citygml4j.model.gml.geometry.primitives.PosOrPointPropertyOrPointRep;
import org.citygml4j.model.gml.geometry.primitives.Solid;
import org.citygml4j.util.walker.FeatureWalker;
import org.citygml4j.util.walker.GMLWalker;
import org.citygml4j.xml.io.CityGMLInputFactory;
import org.citygml4j.xml.io.reader.CityGMLReader;

public class MultipleBuildingsFileClass {
	private List<BuildingsClass> buildingsList;
	SurfaceMember surfaceMember = new SurfaceMember();
	List<SurfaceMember> surfaceMemberList = new ArrayList<SurfaceMember>();
	List<SurfaceMember> wallList = new ArrayList<SurfaceMember>();
	List<SurfaceMember> roofList = new ArrayList<SurfaceMember>();
	
	String typeFlag;
	

	public MultipleBuildingsFileClass(){
		this.buildingsList = new ArrayList<BuildingsClass>();
	}
	public void IterateGMLFile(String filepath) throws Exception{
		
		CityGMLContext ctx = new CityGMLContext();
		CityGMLBuilder builder = ctx.createCityGMLBuilder();
		
		CityGMLInputFactory in = builder.createCityGMLInputFactory();
		try{
			CityGMLReader reader = in.createCityGMLReader(new File(filepath));
			while(reader.hasNext()){
				CityGML citygml = reader.nextFeature();
				//System.out.println("Found class:" + citygml.getCityGMLClass() + "\nVersion"+citygml.getCityGMLModule().getVersion());
				if(citygml.getCityGMLClass() == CityGMLClass.CITY_MODEL)
				{
					CityModel cityModel = (CityModel)citygml;
					
					//FeatureWalker buildingWalker = IterateBuildings();
					//cityModel.accept(buildingWalker);
					
					//A visitor Iterates over all the element in the entire file 
					BuildingsClass building = new BuildingsClass();
					this.typeFlag="groundSurface";
					FeatureWalker groundWalker = IterateGroundSurface(building);
					cityModel.accept(groundWalker);
					
					this.typeFlag="walls";
					FeatureWalker wallWalker = IterateWall(building);
					cityModel.accept(wallWalker);
					
					this.typeFlag="roofs";
					FeatureWalker roofWalker = IterateRoof(building);
					cityModel.accept(roofWalker);
					this.buildingsList.add(building);
					
				}
				else
				{
					System.out.println("The gml file doesn't have a CITY_MODEL");
				}
			}
		}
		catch(Exception e){
			System.out.println("Error in Reading the CityGML file");
			e.printStackTrace();
		}
	}
	private FeatureWalker IterateBuildings(){
		FeatureWalker buildingWalker = new FeatureWalker(){
			public void visit(Building building){
				BuildingsClass singleBuilding = new BuildingsClass();
				
				//TODO::Set other building properties
				if(building.isSetId())
					singleBuilding.setId(building.getId());
				if(building.isSetName()){
					//building.getN
					//singlebuilding.setName(building.getName());
				}
				if(building.isSetYearOfConstruction())
					singleBuilding.setYearOfConstruction(building.getYearOfConstruction().getTime().toString());
				if(building.isSetYearOfDemolition())
					singleBuilding.setYearOfDemolition(building.getYearOfDemolition().getTime().toString());
				if(building.isSetMeasuredHeight()){
					//singleBuilding.setMeasuredHeight(Double.parseDouble(building.getMeasuredHeight().toString()));
				}
				/////////////////////////////////////
				//Walk through wallSurface, wallSurface, roofSurface, soild
				
				//FeatureWalker groundWalker = IterateGroundSurface(singleBuilding);
				
				//FeatureWalker wallWalker = IterateWall(singleBuilding);
				
				/*surfaceMemberList = new ArrayList<SurfaceMember>();
				FeatureWalker roofWalker = IterateRoof(singleBuilding);*/
				
				//surfaceMemberList = new ArrayList<SurfaceMember>();
				//FeatureWalker soildWalker = IterateSolid(singleBuilding);
				
				
				//building.accept(groundWalker);
				//building.accept(wallWalker);
				//building.accept(roofWalker);
				//building.accept(soildWalker);
				////////////////////////////////////
				buildingsList.add(singleBuilding);
				
			}
		};
		return buildingWalker;
	}

	private FeatureWalker IterateGroundSurface(BuildingsClass singleBuilding){
		
		FeatureWalker groundWalker = new FeatureWalker(){
			
			public void visit(GroundSurface groundSurface){
				GMLWalker gmlWalker = new GMLWalker(){
					public void visit(LinearRing linearRing){
						
						/*if(linearRing.isSetPosList()){
							DirectPositionList posList = linearRing.getPosList();
							List<Double> points = posList.toList3d();
							
							List<CoordinateClass> polygonfloor = new ArrayList<CoordinateClass>();
							PolygonClass poly = new PolygonClass();
							for(int i=0 ; i<points.size() ;i+=3){
								double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
								//System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
								CoordinateClass coord = new CoordinateClass(vals);
								polygonfloor.add(coord);
							}
							poly.setPolygon(polygonfloor);
							surfaceMember.setPolygon(poly);
							surfaceMemberList.add(surfaceMember);
							surfaceMember = new SurfaceMember();
							
						}*/
						visitMethod(linearRing);
						//VisitMethod2(linearRing);
					}
				};
				groundSurface.accept(gmlWalker);
			}
		};
		singleBuilding.setSurfacePolygon(surfaceMemberList);
		
		return groundWalker;
	}
	
	private FeatureWalker IterateWall(BuildingsClass singleBuilding){
		
		FeatureWalker wallWalker = new FeatureWalker(){
			
			public void visit(WallSurface wall){
				GMLWalker gmlWalker = new GMLWalker(){
					public void visit(LinearRing linearRing){
						visitMethod(linearRing);
					}
				};
				wall.accept(gmlWalker);
			}
		};
		singleBuilding.setWalls(wallList);
		//wallList = new ArrayList<SurfaceMember>();
		return wallWalker;
	}
	
	private FeatureWalker IterateRoof(BuildingsClass singleBuilding){
		
		FeatureWalker roofWalker = new FeatureWalker(){
			
			public void visit(RoofSurface roof){
				GMLWalker gmlWalker = new GMLWalker(){
					public void visit(LinearRing linearRing){
						visitMethod(linearRing);
					}
				};
				roof.accept(gmlWalker);
			}
		};
		singleBuilding.setRoofs(roofList);
		return roofWalker;
	}
	
	private void visitMethod(LinearRing linearRing){
		if(linearRing.isSetPosList()){
			DirectPositionList posList = linearRing.getPosList();
			List<Double> points = posList.toList3d();
			
			List<CoordinateClass> polygonfloor = new ArrayList<CoordinateClass>();
			PolygonClass poly = new PolygonClass();
			for(int i=0 ; i<points.size() ;i+=3){
				double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
				//System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
				CoordinateClass coord = new CoordinateClass(vals);
				polygonfloor.add(coord);
			}
			poly.setPolygon(polygonfloor);
			
			surfaceMember.setPolygon(poly);
			if(this.typeFlag=="groundSurface")
				surfaceMemberList.add(surfaceMember);
			else if(this.typeFlag=="walls")
				wallList.add(surfaceMember);
			else if(this.typeFlag=="roofs")
				roofList.add(surfaceMember);
			surfaceMember = new SurfaceMember();
			//singleBuilding.setSurfacePolygon(surfaceMember);
			
			//surfacePolygons.add(buildingSurfacePolygon);
		}
		else{
			
			List<PosOrPointPropertyOrPointRep> posList = linearRing.getPosOrPointPropertyOrPointRep();
			for(PosOrPointPropertyOrPointRep position : posList){
				DirectPosition pos = position.getPos();
				List<Double> points = pos.getValue();
				
				List<CoordinateClass> polygonfloor = new ArrayList<CoordinateClass>();
				PolygonClass poly = new PolygonClass();
				for(int i=0 ; i<points.size() ;i+=3){
					double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
					//System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
					
					CoordinateClass coord = new CoordinateClass(vals);
					polygonfloor.add(coord);
				}
				poly.setPolygon(polygonfloor);
				
				surfaceMember.setPolygon(poly);
				if(this.typeFlag=="groundSurface")
					surfaceMemberList.add(surfaceMember);
				else if(this.typeFlag=="walls")
					wallList.add(surfaceMember);
				else if(this.typeFlag=="roofs")
					roofList.add(surfaceMember);
				surfaceMember = new SurfaceMember();
				
			}
			//System.out.println("Its not a PosList :(");
		}
	}
	private void visitMethod2(LinearRing linearRing){
		
	}

	public List<BuildingsClass> getBuildingsList() {
		return buildingsList;
	}

	public void setBuildingsList(List<BuildingsClass> buildingsList) {
		this.buildingsList = buildingsList;
	}
	

		
	
}
