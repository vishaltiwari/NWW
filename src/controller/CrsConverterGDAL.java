package controller;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

public class CrsConverterGDAL {

	public double[] convertCoordinate(String srcProj,String desProj,double[] coordinate){
		SpatialReference oUTM = new SpatialReference();
		SpatialReference oLatLog = new SpatialReference();
		
		//oUTM.SetProjCS("WGS84");
		//oUTM.SetWellKnownGeogCS(desProj);
		//oUTM.ImportFromProj4(srcProj);
		try{
			oUTM.ImportFromEPSG(4326);
		}
		catch(Exception e){
			//e.printStackTrace();
			JOptionPane.showMessageDialog(new JDialog(), "OOPS, error occured while projectiong to WGS84", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
	
			System.out.println("This is the error"+e.getMessage());
		}
		//oUTM.SetUTM(20);
		//oUTM.SetUTM(20);
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
		coord[0] = 129;
		coord[1] = 29.475;
		coord[2] = 10;
		
		double x=coord[0] , y=coord[1] , z=coord[2];
		coord = obj.convertCoordinate("3068", "WGS84", coord);
		
		System.out.println(x+" "+y+" "+z+"\n"+coord[0]+" "+coord[1]+" "+coord[2]);
	}
}
