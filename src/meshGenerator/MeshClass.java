package meshGenerator;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLPointerFunc;

import com.jogamp.opengl.util.GLBuffers;

import gov.nasa.worldwind.geom.Extent;
import gov.nasa.worldwind.geom.Matrix;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sphere;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.pick.PickSupport;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.OrderedRenderable;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.util.OGLUtil;

public class MeshClass extends Polygon implements OrderedRenderable{

	private Position position;
	
	//Stores the heightmap
	private float[] heightMap;
	private int width;
	private int height;
	
	
	//For the VBO/VAO
	private float[] Vertices;
	private int[] index;
	private float[] colorArr;
	private int coordCount;
	private int indexCount;
	
	private int count;
	
	//Rendering parameters
	int pixelRes = 5;
	int heightRes=3;
	
	//Needed for OrderedRendering
	protected PickSupport pickSupport = new PickSupport();
	protected Vec4 placePoint;
	protected double eyeDistance;
	protected Extent extent;
	protected long frameTimestamp = -1L;
	protected double size;
	
	public MeshClass(String filename,Position position,float[] colorArr){
		//sets the width and height of the height map as well.
		this.heightMap = loadHeightMap(filename);
		this.Vertices = HeightMapToVertices();
		this.index = CreateIndex();
		this.colorArr = colorArr;
		//position where to put the watersurface geometry
		this.position = position;
	}
	
	public MeshClass(float[] heightMap,int width,int height,Position position,float[] colorArr){
		this.width = width;
		this.height = height;
		this.count = width*height;
		
		this.heightMap = heightMap;
		this.Vertices = HeightMapToVertices();
		this.index = CreateIndex();
		this.colorArr = colorArr;
		
		this.position = position;
		
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
					mylist.add(num+width);
					mylist.add(num);
					mylist.add(num+1);
					//indx[c++] = num;
					//indx[c++] = num+width;
					//indx[c++] = num+1;
					
				}
				if(this.heightMap[num+1]!=0 || this.heightMap[num+width]!=0 || this.heightMap[num+width+1]!=0){
					mylist.add(num+width);
					mylist.add(num+1);
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
	public float getHeight(int i,int j){
		return heightMap[i*width+j];
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
	
	@Override
	public void render(DrawContext dc) {
		if (this.extent != null)
        {
            if (!this.intersectsFrustum(dc))
                return;

            // If the shape is less that a pixel in size, don't render it.
            if (dc.isSmall(this.extent, 1))
                return;
        }
		if (dc.isOrderedRenderingMode())
            this.drawOrderedRenderable(dc, this.pickSupport);
        else
            this.makeOrderedRenderable(dc);
		
	}

	@Override
	public double getDistanceFromEye() {
		return this.eyeDistance;
	}

	@Override
	public void pick(DrawContext dc, Point pickPoint) {
		this.render(dc);
		
	}
	 protected boolean intersectsFrustum(DrawContext dc)
	    {
	        if (this.extent == null)
	            return true; // don't know the visibility, shape hasn't been computed yet

	        if (dc.isPickingMode())
	            return dc.getPickFrustums().intersectsAny(this.extent);

	        return dc.getView().getFrustumInModelCoordinates().intersects(this.extent);
	    }

	protected void beginDrawing(DrawContext dc) 
    {   
        GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.

        int attrMask = GL2.GL_CURRENT_BIT | GL2.GL_COLOR_BUFFER_BIT;

        gl.glPushAttrib(attrMask);

        if (!dc.isPickingMode())
        {
            dc.beginStandardLighting();
            gl.glEnable(GL.GL_BLEND);
            OGLUtil.applyBlending(gl, false);

            // Were applying a scale transform on the modelview matrix, so the normal vectors must be re-normalized
            // before lighting is computed.
            gl.glEnable(GL2.GL_NORMALIZE);
        }

        // Multiply the modelview matrix by a surface orientation matrix to set up a local coordinate system with the
        // origin at the cube's center position, the Y axis pointing North, the X axis pointing East, and the Z axis
        // normal to the globe.
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        Matrix matrix = dc.getGlobe().computeSurfaceOrientationAtPosition(this.position);
        matrix = dc.getView().getModelviewMatrix().multiply(matrix);

        double[] matrixArray = new double[16];
        matrix.toArray(matrixArray, 0, false);
        gl.glLoadMatrixd(matrixArray, 0); 
    }   
	protected void endDrawing(DrawContext dc)
    {
        GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.

        if (!dc.isPickingMode())
            dc.endStandardLighting();

        gl.glPopAttrib();
    }

	protected void drawMesh(DrawContext dc){
		
		GL2 gl = dc.getGL().getGL2();
		
		FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(this.coordCount);
		vertexBuffer.put(Vertices);
		vertexBuffer.rewind();
		
		IntBuffer indexBuffer = GLBuffers.newDirectIntBuffer(this.indexCount);
		indexBuffer.put(index);
		indexBuffer.rewind();
		
		float color[] = {1,0,1,0,0,0,0,0,0,1,0,0};
		FloatBuffer colorBuffer = GLBuffers.newDirectFloatBuffer(12);
		colorBuffer.put(color);
		colorBuffer.rewind();
		
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE);
		gl.glColor3f(this.colorArr[0],this.colorArr[1],this.colorArr[2]);
		
		gl.glEnableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		gl.glFrontFace(GL.GL_CCW);
		
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertexBuffer);
		
		
		gl.glDrawElements(GL.GL_TRIANGLES, this.indexCount, GL.GL_UNSIGNED_INT, indexBuffer);
		
		
		gl.glDisableClientState(GLPointerFunc.GL_VERTEX_ARRAY);
		
		
		gl.glEnd();
		
	}

	protected void drawOrderedRenderable(DrawContext dc, PickSupport pickCandidates){
		this.beginDrawing(dc);
		try
        {
            GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.
            if (dc.isPickingMode())
            {
                Color pickColor = dc.getUniquePickColor();
                pickCandidates.addPickableObject(pickColor.getRGB(), this, this.position);
                gl.glColor3ub((byte) pickColor.getRed(), (byte) pickColor.getGreen(), (byte) pickColor.getBlue());
            }
            this.drawMesh(dc);
        }
		finally
        {
            this.endDrawing(dc);
        }


	}
	protected void makeOrderedRenderable(DrawContext dc)
    {
        // This method is called twice each frame: once during picking and once during rendering. We only need to
        // compute the placePoint and eye distance once per frame, so check the frame timestamp to see if this is a
        // new frame.
        if (dc.getFrameTimeStamp() != this.frameTimestamp)
        {
        	this.size = 1000;
            // Convert the cube's geographic position to a position in Cartesian coordinates.
            this.placePoint = dc.getGlobe().computePointFromPosition(this.position);

            // Compute the distance from the eye to the cube's position.
            this.eyeDistance = dc.getView().getEyePoint().distanceTo3(this.placePoint);

            this.extent = new Sphere(this.placePoint, Math.sqrt(3.0) * this.size / 2.0);

            this.frameTimestamp = dc.getFrameTimeStamp();
        }
        
        dc.addOrderedRenderable(this);
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

	public float[] getVertices() {
		return Vertices;
	}

	public void setVertices(float[] vertices) {
		Vertices = vertices;
	}

	public int[] getIndex() {
		return index;
	}

	public void setIndex(int[] index) {
		this.index = index;
	}

	public float[] getColorArr() {
		return colorArr;
	}

	public void setColorArr(float[] colorArr) {
		this.colorArr = colorArr;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPixelRes() {
		return pixelRes;
	}

	public void setPixelRes(int pixelRes) {
		this.pixelRes = pixelRes;
	}

	public int getHeightRes() {
		return heightRes;
	}

	public void setHeightRes(int heightRes) {
		this.heightRes = heightRes;
	}

	public PickSupport getPickSupport() {
		return pickSupport;
	}

	public void setPickSupport(PickSupport pickSupport) {
		this.pickSupport = pickSupport;
	}

	public Vec4 getPlacePoint() {
		return placePoint;
	}

	public void setPlacePoint(Vec4 placePoint) {
		this.placePoint = placePoint;
	}

	public double getEyeDistance() {
		return eyeDistance;
	}

	public void setEyeDistance(double eyeDistance) {
		this.eyeDistance = eyeDistance;
	}

	public Extent getExtent() {
		return extent;
	}

	public void setExtent(Extent extent) {
		this.extent = extent;
	}

	public long getFrameTimestamp() {
		return frameTimestamp;
	}

	public void setFrameTimestamp(long frameTimestamp) {
		this.frameTimestamp = frameTimestamp;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

}
