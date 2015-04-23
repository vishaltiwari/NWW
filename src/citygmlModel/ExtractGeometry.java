package citygmlModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.CityGMLBuilder;
import org.citygml4j.model.citygml.CityGML;
import org.citygml4j.model.citygml.CityGMLClass;
import org.citygml4j.model.citygml.building.AbstractBoundarySurface;
import org.citygml4j.model.citygml.building.BoundarySurfaceProperty;
import org.citygml4j.model.citygml.building.Building;
import org.citygml4j.model.citygml.building.GroundSurface;
import org.citygml4j.model.citygml.core.AbstractCityObject;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.citygml.core.CityObjectMember;
import org.citygml4j.model.citygml.generics.AbstractGenericAttribute;
import org.citygml4j.model.gml.geometry.GeometryProperty;
import org.citygml4j.model.gml.geometry.aggregates.MultiSurface;
import org.citygml4j.model.gml.geometry.primitives.AbstractSurface;
import org.citygml4j.model.gml.geometry.primitives.DirectPositionList;
import org.citygml4j.model.gml.geometry.primitives.LinearRing;
import org.citygml4j.model.gml.geometry.primitives.SurfaceProperty;
import org.citygml4j.util.walker.FeatureWalker;
import org.citygml4j.util.walker.GMLWalker;
import org.citygml4j.util.walker.GeometryWalker;
import org.citygml4j.xml.io.CityGMLInputFactory;
import org.citygml4j.xml.io.reader.CityGMLReader;

public class ExtractGeometry {
	public static void main(String args[]) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat("[HH:mm:ss] "); 

		System.out.println(df.format(new Date()) + "setting up citygml4j context and JAXB builder");
		
		CityGMLContext ctx = new CityGMLContext();
		CityGMLBuilder builder = ctx.createCityGMLBuilder();
		
		CityGMLInputFactory in = builder.createCityGMLInputFactory();
		CityGMLReader reader = in.createCityGMLReader(new File("/home/vishal/NWW/sampleData/LOD2_Buildings_v100.gml"));
		
		while(reader.hasNext())
		{
			CityGML citygml = reader.nextFeature();
			System.out.println("Found class:" + citygml.getCityGMLClass() + "\nVersion"+citygml.getCityGMLModule().getVersion());
			
			//Counting the no of buildings
			CityModel citymodel = new CityModel();
			if(citygml.getCityGMLClass() == CityGMLClass.CITY_MODEL)
			{
				citymodel = (CityModel)citygml;
				// Counting the no of buildings
				int count=0;
				for(CityObjectMember cityObjectMember : citymodel.getCityObjectMember())
				{
					AbstractCityObject cityobject = cityObjectMember.getCityObject();
					if(cityobject.getCityGMLClass() == CityGMLClass.BUILDING)
					{
						++count;
						//cityobject
					}
					/*Building building = (Building)cityobject;
					System.out.println(building.isSetGenericApplicationPropertyOfBuilding());
					for(AbstractGenericAttribute attribute : cityobject.getGenericAttribute())
					{
						System.out.println("The attributes are:"+attribute.getName());
					}*/
				}
				System.out.println("Building count"+count);
			}
			
			FeatureWalker buildingwalker = new FeatureWalker(){
				public void visit(Building building){
					System.out.println("Building id"+building.getId());
					System.out.println(building.getMeasuredHeight().getValue());
					//List<BoundarySurfaceProperty> list = building.getBoundedBySurface();
					//System.out.println(list);
					FeatureWalker surfacewalker = new FeatureWalker(){
						public void visit(GroundSurface groundSurface){
							System.out.println("GroundSurface ID"+groundSurface.getId());
							//groundSurface.get
							GMLWalker geomwalker = new GMLWalker(){
								public void visit(LinearRing linearRing)
								{
									System.out.println("inside the linearRing visitor");
									if(linearRing.isSetPosList()){
										System.out.println(linearRing.getSrsDimension());
										DirectPositionList posList = linearRing.getPosList();
										System.out.println(posList.getSrsDimension());
										List<Double> points = posList.toList3d();
										
										for(int i=0 ; i<points.size() ;i+=3){
											double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
											System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
										}
									}
								}
							};
							groundSurface.accept(geomwalker);
						}
					};
					
					building.accept(surfacewalker);
					
				}
			};
			
			/*FeatureWalker surfacewalker = new FeatureWalker(){
				public void visit(GroundSurface groundSurface){
					System.out.println("GroundSurface ID"+groundSurface.getId());
					//groundSurface.get
				}
			};
			
			GMLWalker geomwalker = new GMLWalker(){
				public void visit(LinearRing linearRing)
				{
					System.out.println("inside the linearRing visitor");
					if(linearRing.isSetPosList()){
						System.out.println(linearRing.getSrsDimension());
						DirectPositionList posList = linearRing.getPosList();
						System.out.println(posList.getSrsDimension());
						List<Double> points = posList.toList3d();
						
						for(int i=0 ; i<points.size() ;i+=3){
							double[] vals = new double[]{points.get(i) , points.get(i+1),points.get(i+2)};
							System.out.println(vals[0]+" "+vals[1]+" "+vals[2]);
						}
					}
				}
			};*/
			
			//walker.visit
			citymodel.accept(buildingwalker);
			//citymodel.accept(surfacewalker);
			//citymodel.accept(geomwalker);
		}
		reader.close();
		
	}
}
