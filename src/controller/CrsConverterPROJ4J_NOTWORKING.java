package controller;

import org.osgeo.proj4j.BasicCoordinateTransform;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.ProjCoordinate;
import org.osgeo.proj4j.datum.Datum;
import org.osgeo.proj4j.datum.Ellipsoid;
import org.osgeo.proj4j.proj.Projection;

//use this class to convert the projection system of the cityGML file.
public class CrsConverterPROJ4J_NOTWORKING {
	public static void main(String args[]){
		//String firstProj = "+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs";
		//String secondProj = "+proj=longlat +datum=WGS84 +no_defs";
		
		Ellipsoid ell = new Ellipsoid();
		Projection proj1=null;
		proj1.initialize();
		Projection proj2 = null;
		//ProjCoordinate
		String params1[] = {"+proj=utm","+zone=45","+ellps=WGS72","+towgs84=0,0,4.5,0,0,0.554,0.2263","+units=m","+no_defs"};
		String params2[] = {"+proj=longlat","+datum=WGS84","+no_defs"};
		CoordinateReferenceSystem crs1 = new CoordinateReferenceSystem("srs1",params1,Datum.WGS84,proj1);
		CoordinateReferenceSystem crs2 = new CoordinateReferenceSystem("srs2",params2,Datum.WGS84,proj2);
		System.out.println(crs1.getName()+"\n"+crs1.getParameterString()+"\n"+crs1.getDatum()+"\n");
		BasicCoordinateTransform basicTransform  = new BasicCoordinateTransform(crs1,crs2);
		
		ProjCoordinate src = new ProjCoordinate();
		src.x = 10;
		src.y = 10;
		
		ProjCoordinate tgt = new ProjCoordinate();
		basicTransform.transform(src, tgt);
		
		System.out.println(tgt.x+" "+tgt.y);
		
	}
	
}
