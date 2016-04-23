package randomTest;

import java.util.LinkedHashSet;

import generateData.DepthFilling.Pair;

public class TestLinkedHashSet {

	public static class Pair{
		public int x;
		public int y;
		public int width;
		public Pair(int x,int y,int width){
			this.x = x;
			this.y = y;
			this.width = width;
		}
		public int hashCode(){
			int hashCode=0;
			hashCode = y*width+x;
			System.out.println("HashCode:"+hashCode);
			return hashCode;
		}
		public boolean equals(Object obj){
			if(obj instanceof Pair){
				Pair p = (Pair)obj;
				if(p.x==x && p.y==y)
					return true;
				return false;
			}
			else{
				return false;
			}
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		
	}
	public static void main(String argv[]){
		LinkedHashSet<Pair> hashSet = new LinkedHashSet<Pair>();
		hashSet.add(new Pair(1,1,10));
		hashSet.add(new Pair(2,2,10));
		hashSet.add(new Pair(2,3,10));
		
		System.out.println("Does it contain that pair?:"+hashSet.contains(new Pair(3,2,10)));
		
	}
}
