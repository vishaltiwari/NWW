package controller;

import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

public class CrsConverterGDAL {
	public double[] convertCoordinate(String srcProj,String desProj,double[] coordinate){
		SpatialReference oUTM = new SpatialReference();
		SpatialReference oLatLog = new SpatialReference();
		
		oUTM.SetProjCS("UTM 44/ WGS84");
		oUTM.SetWellKnownGeogCS("WGS84");
		oUTM.SetUTM(44, 1);
		
		oLatLog = oUTM.CloneGeogCS();
		
		double x = coordinate[0] , y = coordinate[1], z = coordinate[2];
		
		CoordinateTransformation transform = new CoordinateTransformation(oUTM,oLatLog);
		
		double[] coords = transform.TransformPoint(x, y, z);
		
		return coords;
		//System.out.println("Before:"+x+" "+y+"\nAfter:"+coordinate[0]+" "+coordinate[1]);
	}
}
