package controller;

/*import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;*/


public class CrsConverterGeoTools_NOTWORKING {
	/*public static void main(String args[]) throws TransformException{
		CRSAuthorityFactory   factory = CRS.getAuthorityFactory(true);
        try {
			CoordinateReferenceSystem srcCRS = factory.createCoordinateReferenceSystem("EPSG:2326");
			CoordinateReferenceSystem dstCRS = factory.createCoordinateReferenceSystem("EPSG:4326");
			boolean lenient = true; // allow for some error due to different datums
	        MathTransform transform = CRS.findMathTransform(srcCRS, dstCRS, lenient);
	        double[] srcProjec = {818039, 836361};// easting, northing, 
	        double[] dstProjec = {0, 0};
	        transform.transform(srcProjec, 0, dstProjec, 0, 1);
	        System.out.println("longitude: " + dstProjec[0] + ", latitude: " + dstProjec[1]);


		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}*/
}
