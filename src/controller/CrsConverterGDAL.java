package controller;

import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

public class CrsConverterGDAL {
	public double[] convertCoordinate(String srcProj,String desProj,double[] coordinate){
		SpatialReference oUTM = new SpatialReference();
		SpatialReference oLatLog = new SpatialReference();
		
		oUTM.SetProjCS("UTM 44/ WGS84");
		oUTM.SetWellKnownGeogCS(desProj);
		//oUTM.ImportFromProj4(srcProj);
		//oUTM.ImportFromEPSG(32245);
		oUTM.SetUTM(44);
		
		oLatLog = oUTM.CloneGeogCS();
		
		double x = coordinate[0] , y = coordinate[1], z = coordinate[2];
		
		CoordinateTransformation transform = new CoordinateTransformation(oUTM,oLatLog);
		
		double[] coords = transform.TransformPoint(x, y, z);
		
		return coords;
		//System.out.println("Before:"+x+" "+y+"\nAfter:"+coordinate[0]+" "+coordinate[1]);
	}
	public static void main(String argv[]){
		CrsConverterGDAL obj = new CrsConverterGDAL();
		double[] coord = new double[3];
		coord[0] = 10;
		coord[1] = 20;
		coord[2] = 10;
		
		double x=coord[0] , y=coord[1] , z=coord[2];
		coord = obj.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", coord);
		
		System.out.println(x+" "+y+" "+z+"\n"+coord[0]+" "+coord[1]+" "+coord[2]);
	}
}
