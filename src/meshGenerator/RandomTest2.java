package meshGenerator;

import java.util.ArrayList;
import java.util.List;

public class RandomTest2 {
	private int width = 5;
	private int height = 5;
	private static int indexCount; 
	private int[] CreateIndex(){
		
		this.indexCount = (width-1)*(height-1)*2*3;
		int c=0;
		int[] indx = new int[this.indexCount];
		
		for(int i=0 ; i<height-1 ; i++){
			for(int j=0 ; j<width-1 ; j++){
				int num = i*width+j;
				//if(j<width-1){ //Even
					indx[c++] = num;
					indx[c++] = num+width;
					indx[c++] = num+1;
					indx[c++] = num+1;
					indx[c++] = num+width;
					indx[c++] = num+width+1;
				//}
				
			}
		}
		return indx;
	}
	public static void main(String argv[]){
		RandomTest2 obj = new RandomTest2();
		List<String> list  = new ArrayList<String>();
		
		
		int[] indx = obj.CreateIndex();
		System.out.println(indexCount);
		for(int i=0 ; i<indexCount ; i++){
			System.out.print(indx[i]+",");
		}
	}
}
