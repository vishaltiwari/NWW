package waterSurfaceModel;

import gov.nasa.worldwind.geom.Sector;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
//import org.gdal.osr.CoordinateTransformation;
//import org.gdal.osr.SpatialReference;

public class ExtractBoundry {
	
	static double[] GDALInfoReportCorner(Dataset hDataset, String corner_name,
			double x, double y)

	{
		double dfGeoX, dfGeoY;
		//String pszProjection;
		double[] adfGeoTransform = new double[6];
		//CoordinateTransformation hTransform = null;

		//System.out.print(corner_name + " ");

		/* -------------------------------------------------------------------- */
		/*      Transform the point into georeferenced coordinates.             */
		/* -------------------------------------------------------------------- */
		hDataset.GetGeoTransform(adfGeoTransform);
		{
			//pszProjection = hDataset.GetProjectionRef();

			dfGeoX = adfGeoTransform[0] + adfGeoTransform[1] * x
					+ adfGeoTransform[2] * y;
			dfGeoY = adfGeoTransform[3] + adfGeoTransform[4] * x
					+ adfGeoTransform[5] * y;
		}

		if (adfGeoTransform[0] == 0 && adfGeoTransform[1] == 0
				&& adfGeoTransform[2] == 0 && adfGeoTransform[3] == 0
				&& adfGeoTransform[4] == 0 && adfGeoTransform[5] == 0) {
			//System.out.println("(" + x + "," + y + ")");
			return null;
		}

		/* -------------------------------------------------------------------- */
		/*      Report the georeferenced coordinates.                           */
		/* -------------------------------------------------------------------- */
		double[] output = new double[2];
		//System.out.print("(" + dfGeoX + "," + dfGeoY + ") ");
		output[0] = dfGeoX;
		output[1] = dfGeoY;
		return output;

	}
	public static Sector extractBoundry(String filename){
		
		gdal.AllRegister();
		Dataset dataSet = gdal.Open(filename);
		
		//System.out.println(dataSet.GetProjectionRef());
		
		double[] transform = new double[6];
		dataSet.GetGeoTransform(transform);
		//System.out.println(transform[0] + " " + transform[3]);
		//System.out.println(transform[2] + " " + transform[5]);
		
		//GDALInfoReportCorner();
		double[] lowerLeft = GDALInfoReportCorner(dataSet,"Lower Left",0.0,dataSet.getRasterYSize());
		//System.out.println(lowerLeft[0]+ " "+lowerLeft[1]);
		
		double[] upperRight = GDALInfoReportCorner(dataSet,"Upper Right",dataSet.getRasterXSize(),0.0);
		//System.out.println(upperRight[0]+" "+upperRight[1]);
		
		Sector sector = Sector.fromDegrees(lowerLeft[1], upperRight[1], lowerLeft[0], upperRight[0]);
		
		return sector;
	}
	/*public static void main(String argv[]){
		String filename = "/home/vishal/Desktop/Grass_Output/images/20.tif";
		extractBoundry(filename);
	}*/
}
