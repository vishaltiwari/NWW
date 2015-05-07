package meshGenerator;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Matrix;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
//import gov.nasa.worldwind.util.OGLStackHandler;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

public class MeshSurface extends ApplicationTemplate implements Renderable{

	//Center of the 2D heightMap, used for converting it to globe coordinates
	private Position position;
	
	private int[][] heightMap;
	private int width;
	private int height;
	
	//TODO: Set the position from the filename/ projection system / input Position
	public MeshSurface(String filename){
		//sets the width and height of the height map as well.
		this.heightMap = loadHeightMap(filename);
		//position where to put the watersurface geometry
		this.position = new Position(Angle.fromDegrees(0),Angle.fromDegrees(0),100);
	}
	
	public MeshSurface(int[][] heightMap,int width,int height){
		this.heightMap = heightMap;
		this.position = new Position(Angle.fromDegrees(0),Angle.fromDegrees(0),100);
		this.width = width;
		this.height = height;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int[][] getHeightMap() {
		return heightMap;
	}

	public void setHeightMap(int[][] heightMap) {
		this.heightMap = heightMap;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	private int[][] loadHeightMap(String filename){
		int[][] HeightMap=null;
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			Raster image_raster = image.getData();
			
			this.width = image_raster.getWidth();
			this.height = image_raster.getHeight();
			
			HeightMap = new int[height][width];
			
			int[] pixel = new int[1];
			int[] buffer = new int[1];
			
			for(int i=0 ; i<height ; i++){
				for(int j=0 ; j<width; j++){
					
					pixel = image_raster.getPixel(i, j, buffer);
					HeightMap[i][j] = pixel[0];
					//System.out.print(HeightMap[i][j] + " ");
				}
				//System.out.println("");
			}
			//System.out.println("width" + width + "\nHeight"+height);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return HeightMap;
	}
	
	protected void beginDrawing(DrawContext dc){
		GL2 gl = dc.getGL().getGL2();
		
		int attrMask = GL2.GL_CURRENT_BIT | GL.GL_COLOR_BUFFER_BIT;
	    gl.glPushAttrib(attrMask);
	    
	    if (!dc.isPickingMode())
	        dc.beginStandardLighting();
	    
	}
	protected void endDrawing(DrawContext dc){
		GL2 gl = dc.getGL().getGL2();
		
		if (!dc.isPickingMode())
	        dc.endStandardLighting();
		
		gl.glPopAttrib();
	}
	protected void setViewModelMatrix(DrawContext dc){
		GL2 gl = dc.getGL().getGL2();
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
		Matrix matrix = dc.getGlobe().computeSurfaceOrientationAtPosition(this.position);
		matrix = dc.getView().getModelviewMatrix().multiply(matrix);
		 
		double[] matrixArray = new double[16];
		matrix.toArray(matrixArray, 0, false);
		gl.glLoadMatrixd(matrixArray, 0);
		
	}
	protected void drawWaterSurface(DrawContext dc){
		GL2 gl = dc.getGL().getGL2();
		//OGLStackHandler osh = new OGLStackHandler();
		int pixelRes = 5;
		int heightRes=3;
		
		for(int i=0 ; i<height ; i++){
		
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			try{
				float y = i*pixelRes;

				for(int j=0 ; j<width; j++){
					float x = j*pixelRes;
					
					//gl.glColor3f(1.0f, 0.0f, 0.0f);
					gl.glVertex3f(x, y,(float)heightMap[i][j]*heightRes);
					if(i!=height-1){
						//gl.glColor3f(1.0f, 0.0f, 0.0f);
						gl.glVertex3f(x, (i+1)*pixelRes,(float)heightMap[i+1][j]*heightRes);
					}
				}
			}
			finally{
				gl.glEnd();
			}
		}
		//osh.pop(gl);
	}
	@Override
	public void render(DrawContext dc) {
		beginDrawing(dc);
		
		//Set the ModelView matrix
		setViewModelMatrix(dc);
		
		//Draw the primitive
		drawWaterSurface(dc);
		
		endDrawing(dc);
	}
	
	protected static class AppFrame extends ApplicationTemplate.AppFrame
    {   
        public AppFrame()
        {
            super(true, false, false);

            RenderableLayer layer = new RenderableLayer();
            //Cube cube = new Cube(Position.fromDegrees(35.0, -120.0, 3000), 1000);
            String filename = "/home/vishal/NWW/sampleData/DSM.png";
            MeshSurface waterSurface = new MeshSurface(filename);
            layer.addRenderable(waterSurface);

            getWwd().getModel().getLayers().add(layer);
        }
    }   

	public static void main(String[] args)
	{   
		Configuration.setValue(AVKey.INITIAL_LATITUDE, 0);
		Configuration.setValue(AVKey.INITIAL_LONGITUDE, 0);
		Configuration.setValue(AVKey.INITIAL_ALTITUDE, 15500);
		Configuration.setValue(AVKey.INITIAL_PITCH, 45);
		Configuration.setValue(AVKey.INITIAL_HEADING, 45);

		ApplicationTemplate.start("World Wind Custom Renderable Tutorial", AppFrame.class);
	}
	
}
