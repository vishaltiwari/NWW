package citygmlModel;

import java.util.List;

public class RandomTestClass {
	public static void main(String argv[]){
		String no = "34";
		System.out.println(Double.parseDouble(no));
		MultipleBuildingsFileClass obj = new MultipleBuildingsFileClass();
		String filepath = "/home/vishal/NWW/sampleData/Stadt-Ettenheim-LoD3_edited_v1.0.0.gml";
		try {
			obj.IterateGMLFile(filepath);
			List<BuildingsClass> buildingsList = obj.getBuildingsList();
			int sum=0;
			for(int i=0 ; i<buildingsList.size() ; i++){
				BuildingsClass building = buildingsList.get(i);
				List<SurfaceMember> surfaceList = building.getSurfacePolygon();
				System.out.println(surfaceList.size());
				sum = sum + surfaceList.size();
				for(int k=0 ; k<surfaceList.size() ; k++){
					SurfaceMember surface = surfaceList.get(k);
					
					PolygonClass poly = surface.getPolygon();
					List<CoordinateClass> coords = poly.getPolygon();
					for(int j=0 ; j<coords.size() ; j++){
						System.out.println(coords.get(j).getX()+" "+coords.get(j).getY()+" "+coords.get(j).getZ());
					}
					System.out.println("");
				}
			}
			System.out.println("done with the code, total surface:"+sum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
