package controller;

import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

public class CrsConverter {
	public static double[] convertCoordinate(String srcProj,String desProj,double[] coordinate,int flag){
		SpatialReference oUTM = new SpatialReference();
		SpatialReference oLatLog = new SpatialReference();
		
		//oLatLog.SetWellKnownGeogCS(desProj);
		oUTM.ImportFromEPSG(Integer.parseInt(srcProj));
		//System.out.println(oUTM.ExportToWkt());
		oLatLog = oUTM.CloneGeogCS();
		//System.out.println(oLatLog.ExportToWkt());
		
		double x = coordinate[0] , y = coordinate[1], z = coordinate[2];
		CoordinateTransformation transform;
		if(flag==0)
			transform = new CoordinateTransformation(oUTM,oLatLog);
		else
			transform = new CoordinateTransformation(oLatLog,oUTM);
		double[] coords = transform.TransformPoint(x, y, z);
		return coords;
		//System.out.println("Before:"+x+" "+y+"\nAfter:"+coordinate[0]+" "+coordinate[1]);
	}
	public static double[] convertCoordinate(String srcProj,String desProj,double[] coordinate){
		SpatialReference oUTM = new SpatialReference();
		SpatialReference oLatLog = new SpatialReference();
		
		//oLatLog.SetWellKnownGeogCS(desProj);
		oUTM.ImportFromEPSG(Integer.parseInt(srcProj));
		//System.out.println(oUTM.ExportToWkt());
		oLatLog = oUTM.CloneGeogCS();
		//System.out.println(oLatLog.ExportToWkt());
		//oUTM.ImportFromProj4(srcProj);
		//oUTM.SetProjCS("Some String dude");
		//oUTM.SetWellKnownGeogCS("WGS84");
		//oUTM.SetUTM(24);
		/*
		oUTM.SetProjCS("WGS84");
		oUTM.SetWellKnownGeogCS(desProj);
		//oUTM.ImportFromProj4(srcProj);
		try{
			oUTM.ImportFromEPSG(Integer.parseInt(srcProj));
		}
		catch(Exception e){
			//e.printStackTrace();
			JOptionPane.showMessageDialog(new JDialog(), "OOPS, error occured while projectiong to WGS84", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
	
			System.out.println("This is the error"+e.getMessage());
		}
		oLatLog = oUTM.CloneGeogCS();*/
		
		double x = coordinate[0] , y = coordinate[1], z = coordinate[2];
		CoordinateTransformation transform = new CoordinateTransformation(oUTM,oLatLog);
		double[] coords = transform.TransformPoint(x, y, z);
		return coords;
		//System.out.println("Before:"+x+" "+y+"\nAfter:"+coordinate[0]+" "+coordinate[1]);
	}
	/*public static void main(String argv[]){
		double[] coord = new double[3];
		coord[0] = 134.4984768431;
		coord[1] = 31.568872245685;
		coord[2] = 10;
		
		double x=coord[0] , y=coord[1] , z=coord[2];
		coord = convertCoordinate("3094","4326" , coord,1);
		
		System.out.println(x+" "+y+" "+z+"\n"+coord[0]+" "+coord[1]+" "+coord[2]);
	}*/
}
