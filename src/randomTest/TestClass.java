package randomTest;

import java.awt.Color;

public class TestClass {
	
	public static void main(String argv[]){
		Color color = new Color(0,0,20);
		float[] hsb = new float[3];
		Color.RGBtoHSB(0, 0, 20, hsb);
		System.out.println(hsb[0]);
	}
}
