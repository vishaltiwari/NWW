package meshGenerator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLPointerFunc;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.GLBuffers;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Intersection;
import gov.nasa.worldwind.geom.Line;
import gov.nasa.worldwind.geom.Matrix;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.pick.PickSupport;
import gov.nasa.worldwind.render.AbstractShape;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.terrain.Terrain;
//import gov.nasa.worldwind.util.OGLStackHandler;
import gov.nasa.worldwindx.examples.ApplicationTemplate;

public class MeshSurfaceVBO extends AbstractShape implements Renderable{

	//Center of the 2D heightMap, used for converting it to globe coordinates
	class vert {
		public float x;
		public float y;
		public float z;
		
		public vert(float x,float y,float z){
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
	
	private Position position;
	
	private float[] heightMap;
	private float[] Vertices;
	private int[] index;
	private int width;
	private int height;
	private int count;
	
	private int coordCount;
	private int indexCount;
	
	int pixelRes = 5;
	int heightRes=3;
	
	//Array of vertex
	private List<vert> verticesVBO;
	private int cnt=0;
	//VBO names
	private int m_nVBOVertices;
	
	//TODO: Set the position from the filename/ projection system / input Position
	public MeshSurfaceVBO(String filename){
		//sets the width and height of the height map as well.
		this.heightMap = loadHeightMap(filename);
		this.Vertices = HeightMapToVertices();
		this.index = CreateIndex();
		//position where to put the watersurface geometry
		this.position = new Position(Angle.fromDegrees(0),Angle.fromDegrees(0),100);
	}
	
	public MeshSurfaceVBO(float[] heightMap,int width,int height){
		this.width = width;
		this.height = height;
		this.count = width*height;
		
		this.heightMap = heightMap;
		this.Vertices = HeightMapToVertices();
		this.index = CreateIndex();
		
		this.position = new Position(Angle.fromDegrees(0),Angle.fromDegrees(0),100);
		
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public float[] getHeightMap() {
		return heightMap;
	}

	public void setHeightMap(float[] heightMap) {
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

	public int getCoordCount() {
		return coordCount;
	}

	public void setCoordCount(int coordCount) {
		this.coordCount = coordCount;
	}

	public int getIndexCount() {
		return indexCount;
	}

	public void setIndexCount(int indexCount) {
		this.indexCount = indexCount;
	}

	private float[] HeightMapToVertices(){
		this.coordCount = width*height*3;
		//System.out.println(this.coordCount);
		float[] vertices = new float[coordCount];
		//int size = width*height;
		
		int c=0;
		for(int i=0 ; i<height ; i++){
			float y = i*pixelRes;
			for(int j=0 ; j<width ; j++){
				float x = j*pixelRes;
				float z = this.getHeight(i, j);
				vertices[c++] = x;
				vertices[c++] = y;
				vertices[c++] = z;
			}
		}
		return vertices;
	}
	private int[] CreateIndex(){
		
		this.indexCount = (width-1)*(height-1)*2*3;
		//System.out.println("Prev indexCount"+this.indexCount);
		int c=0;
		List<Integer> mylist = new ArrayList<Integer>();
		
		for(int i=0 ; i<height-1 ; i++){
			for(int j=0 ; j<width-1 ; j++){
				int num = i*width+j;
				// Split it in two parts, add two triangle every iteration
				if(this.heightMap[num]!=0 || this.heightMap[num+width]!=0 || this.heightMap[num+1]!=0){
					mylist.add(num);
					mylist.add(num+width);
					mylist.add(num+1);
					//indx[c++] = num;
					//indx[c++] = num+width;
					//indx[c++] = num+1;
					
				}
				if(this.heightMap[num+1]!=0 || this.heightMap[num+width]!=0 || this.heightMap[num+width+1]!=0){
					mylist.add(num+1);
					mylist.add(num+width);
					mylist.add(num+width+1);
					//indx[c++] = num+1;
					//indx[c++] = num+width;
					//indx[c++] = num+width+1;
				}
			}
		}
		this.indexCount = mylist.size();
		int[] indx = new int[this.indexCount];
		for(Integer i : mylist){
			indx[c++] = i;
		}
		
		//System.out.println("New index count"+this.indexCount);
		return indx;
	}
	
	private float[] loadHeightMap(String filename){
		float[] HeightMap=null;
		this.count=0;
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			Raster image_raster = image.getData();
			
			this.width = image_raster.getWidth();
			this.height = image_raster.getHeight();
			
			HeightMap = new float[height*width];
			
			int[] pixel = new int[1];
			int[] buffer = new int[1];
			
			for(int i=0 ; i<height ; i++){
				for(int j=0 ; j<width; j++){
					
					pixel = image_raster.getPixel(i, j, buffer);
					HeightMap[count] = pixel[0];
					++count;
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
	
	public float getHeight(int i,int j){
		return heightMap[i*width+j];
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
	
	protected void drawMesh(GL2 gl){
		//Color not working
		//data
		//Vertex data
		FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(this.coordCount);
		vertexBuffer.put(Vertices);
		vertexBuffer.rewind();
		
		IntBuffer indexBuffer = GLBuffers.newDirectIntBuffer(this.indexCount);
		indexBuffer.put(index);
		indexBuffer.rewind();
		//System.out.println("CoordCount"+coordCount+" indexCount"+indexCount);
		/*float testArr[] = {0,0,100,  10,0,100,  20,0,100,  30,0,100,  40,0,100,   
				           0,10,200, 10,10,200,  20,10,200,  30,10,200,  40,10,200,
				           0,20,300,  10,20,300,  20,20,300,  30,20,300,  40,20,300,
				           0,30,400,  10,30,400,  20,30,400,  30,30,400,  40,30,400,
				           0,40,500,  10,40,500,  20,40,500,  30,40,500,  40,40,500};
		
		FloatBuffer testBuffer = GLBuffers.newDirectFloatBuffer(75);
		this.coordCount = 75;
		testBuffer.put(testArr);
		testBuffer.rewind();
		
		int testIndx[] = {0,5,1,1,5,6,1,6,2,2,6,7,2,7,3,3,7,8,3,8,4,4,8,9,5,10,6,6,10,11,6,11,7,7,11,12,7,12,8,8,12,13,8,13,9,9,13,14,10,15,11,11,15,16,11,16,12,12,16,17,12,17,13,13,17,18,13,18,14,14,18,19,15,20,16,16,20,21,16,21,17,17,21,22,17,22,18,18,22,23,18,23,19,19,23,24};
		this.indexCount=96;
		IntBuffer testIndxBuffer = GLBuffers.newDirectIntBuffer(this.indexCount);
		testIndxBuffer.put(testIndx);
		testIndxBuffer.rewind();*/
		
		/*float f[] = {1000,2000,-4000,-2000,-2000,-4000,2000,-2000,-4000,1000,-4000,-4000};
		FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(12);
		this.coordCount = 12;
		buffer.put(f);
		buffer.rewind();
		
		//Make sure all the triangle vertices are clockwise/antiClockwise.
		int indx[] = {0,1,2,1,3,2};
		IntBuffer indxBuffer = GLBuffers.newDirectIntBuffer(6); //Total number of vertices
		this.indexCount = 6;
		indxBuffer.put(indx);
		indxBuffer.rewind();*/
		
		float color[] = {1,0,1,0,0,0,0,0,0,1,0,0};
		FloatBuffer colorBuffer = GLBuffers.newDirectFloatBuffer(12);
		colorBuffer.put(color);
		colorBuffer.rewind();
	
		//setting up the VBO
		/*int nVBO = 2;
		int[] VBO = new int[nVBO];
		
		gl.glGenBuffers(nVBO, VBO,0);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO[0]);
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,VBO[1]);
		
		gl.glBufferData(GL.GL_ARRAY_BUFFER, this.coordCount*Float.SIZE, vertexBuffer, GL.GL_STATIC_DRAW);
		gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, this.indexCount*Integer.SIZE, indexBuffer, GL.GL_STATIC_DRAW);
		*/
		//createVBOVertices();
		
		//gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO[0]);
		//gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, VBO[1]);
		
		gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		//gl.glEnableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		//gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
		
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexBuffer);
		
		//gl.glColorPointer(3, GL.GL_FLOAT, 0, colorBuffer);
		
		//gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
		//gl.glDrawElements(GL.GL_TRIANGLES, this.indexCount, GL.GL_UNSIGNED_INT, 0);
		gl.glDrawElements(GL.GL_TRIANGLES, this.indexCount, GL.GL_UNSIGNED_INT, indexBuffer);
		
		//gl.glDisableClientState(GLPointerFunc.GL_COLOR_ARRAY);
		gl.glDisableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		//gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		//gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	protected void renderVBO(DrawContext dc){
		
		GL2 gl = dc.getGL().getGL2();
		
		drawMesh(gl);
		
	}
	protected void drawWaterSurface(DrawContext dc){
		GL2 gl = dc.getGL().getGL2();
		//OGLStackHandler osh = new OGLStackHandler();
		pixelRes = 5;
		heightRes=3;
		
		for(int i=0 ; i<height ; i++){
		
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			try{
				float y = i*pixelRes;

				for(int j=0 ; j<width; j++){
					float x = j*pixelRes;
					
					//gl.glColor3f(1.0f, 0.0f, 0.0f);
					gl.glVertex3f(x, y,(float)heightMap[i*width+j]*heightRes);
					if(i!=height-1){
						//gl.glColor3f(1.0f, 0.0f, 0.0f);
						gl.glVertex3f(x, (i+1)*pixelRes,(float)heightMap[(i+1)*width+j]*heightRes);
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
		
		GL2 gl = dc.getGL().getGL2();
		//Set the ModelView matrix
		setViewModelMatrix(dc);
		
		//Draw the primitive
		//Color pickColor = dc.getUniquePickColor();
		
		//PickSupport pickCandidates = new PickSupport();
		//pickCandidates.addPickableObject(pickColor.getRGB(), this, this.position);
		//System.out.println(pickColor.getRed() + " "+pickColor.getGreen() + " "+pickColor.getBlue());
        //gl.glColor3ub((byte) pickColor.getRed(), (byte) pickColor.getGreen(), (byte) pickColor.getBlue());
		renderVBO(dc);
		//drawWaterSurface(dc);
		
		endDrawing(dc);
	}

	@Override
	public Position getReferencePosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void moveTo(Position position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Sector getSector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AbstractShapeData createCacheEntry(DrawContext dc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doDrawInterior(DrawContext dc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doDrawOutline(DrawContext dc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doExportAsKML(XMLStreamWriter xmlWriter) throws IOException,
			XMLStreamException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean doMakeOrderedRenderable(DrawContext dc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void fillVBO(DrawContext dc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Intersection> intersect(Line line, Terrain terrain)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isOrderedRenderableValid(DrawContext dc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean mustApplyTexture(DrawContext dc) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
