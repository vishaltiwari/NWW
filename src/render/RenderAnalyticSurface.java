package render;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Timer;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.util.WWMath;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurface;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurfaceAttributes;
import waterSurfaceModel.CreateDiffMaps;
import waterSurfaceModel.SurfaceModelClass;
import waterSurfaceModel.CreateDiffMaps.Pair;

public class RenderAnalyticSurface {
	protected static final double HUE_BLUE = 240d / 360d;
    protected static final double HUE_RED = 0d / 360d;
    
	private SurfaceModelClass surfaceData;
	private static AnalyticSurface surface;
	private static ArrayList<AnalyticSurface.GridPointAttributes> attributesList;
	private static int Width;
	private static double err;
	
	private static int TimerInterval;

	/**
	 * Constructors
	 * **/
	public RenderAnalyticSurface(String filename){
		this.surfaceData = new SurfaceModelClass(filename);
		System.out.println("width:"+surfaceData.getWidth()+" height"+surfaceData.getHeight());
		TimerInterval = 100;
	}

	/**
	 * Other inner classes
	 * **/
	static AnalyticSurface.GridPointAttributes createGridAttribute(final double value,final Color color){
		return new AnalyticSurface.GridPointAttributes(){

			@Override
			public Color getColor() {
				// TODO Auto-generated method stub
				return color;
			}

			@Override
			public double getValue() {
				return value;
			}

		};
	}
	/**
	 * Rendering function for the AnalyticSyrface
	 * **/
	
	protected static void createSurface(double minHue, double maxHue, 
												int width, int height,SurfaceModelClass surfaceData, 
												RenderableLayer layer){
		
		surface = new AnalyticSurface();
		surface.setSector(surfaceData.getSector());
		//Adding an error of 0.0001 (so as to remove the tesselation lines of the surface).
		err = 0.5;
		surface.setAltitude(-1*err);
		//surface.setAltitude(100);
		surface.setVerticalScale(1.0);
		surface.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
		surface.setDimensions(surfaceData.getWidth(), surfaceData.getHeight());
		surface.setClientLayer(layer);
		
		AnalyticSurfaceAttributes attr2 = new AnalyticSurfaceAttributes();
        attr2.setDrawShadow(false);
        attr2.setInteriorOpacity(0.6);
        attr2.setOutlineWidth(0);
        
        attr2.setOutlineOpacity(0);
        surface.setSurfaceAttributes(attr2);

		
		Width = width;
		layer.addRenderable(surface);
		
		//create the analytic surface
        attributesList = new ArrayList<AnalyticSurface.GridPointAttributes>();
        
        //TODO:: find the min and max value from the array itself.
        double minValue = 43;
        double maxValue = 103;
		
        int size = surfaceData.getHeight() * surfaceData.getWidth();
        float[] heightMap = surfaceData.getHeightMap();
        
        for(int i=0 ; i<size ; i++){
        	
        	float value = heightMap[i];
        	double hueFactor = WWMath.computeInterpolationFactor(value, minValue, maxValue);
            Color color = Color.getHSBColor((float) WWMath.mixSmooth(hueFactor, minHue, maxHue), 1f, 1f); 
            //double opacity = WWMath.computeInterpolationFactor(value, minValue, minValue + (maxValue - minValue) * 0.1);
            Color rgbaColor = new Color(255, 255, 255, 255);

        	AnalyticSurface.GridPointAttributes attr = createGridAttribute(0,rgbaColor);
        	
        	attributesList.add(attr);
        }
        //set the surface values
        surface.setValues(attributesList);
	}
	
	public RenderableLayer renderWaterSurface(String dir){
		RenderableLayer layer = new RenderableLayer();
		
		layer.setPickEnabled(false);
        layer.setName("Analytic Surfaces");
        
        createSurface(HUE_BLUE, HUE_RED, surfaceData.getWidth(), surfaceData.getHeight(), surfaceData, layer);
        addAnimationFromData(dir);
		return layer;
	}
	
	/**
	 * Method for dynamic visualization of water surface data.
	 * **/
	public static void createAnimation(CreateDiffMaps obj){
		
		final ArrayList<Map<Pair,Double> > timeStampedDiff = obj.getTimeStampedDiff();
		//final int size = timeStampedDiff.size();
		Timer timer = new Timer(TimerInterval,new ActionListener(){
			protected long startTime = -1;
			int i=0;
			public void actionPerformed(ActionEvent e) {
				if(this.startTime < 0){
					this.startTime = System.currentTimeMillis();
					System.out.println("Start Time"+ this.startTime);
				}
				System.out.println("Hello "+ (e.getWhen()-this.startTime));
				
				//update the value, see how the value of i will be controlled
				Map<Pair,Double> map = timeStampedDiff.get(i++);
				
				Iterator<Entry<Pair, Double>> it = map.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<Pair, Double> mapEntry = (Map.Entry<Pair, Double>)it.next();
					//System.out.println("("+mapEntry.getKey().i + ","+mapEntry.getKey().j + ") -> " + mapEntry.getValue().doubleValue());
					Pair p = mapEntry.getKey();
					double val = mapEntry.getValue().doubleValue();
					
					//update the values in the SurfaceAnalytic.GridPointAttributes
					System.out.println((p.j)*Width+p.i);
					AnalyticSurface.GridPointAttributes attrOld = attributesList.get((p.j)*Width+p.i);
					Color rgbaColor = new Color(0, 0, 255, 255);

					//negative means addition towards sky. (it's oriented upside-down)
					double newVal = attrOld.getValue() + val + err;
		        	AnalyticSurface.GridPointAttributes attrNew = createGridAttribute(newVal,rgbaColor);
		        	
					attributesList.set((p.j)*Width+p.i,attrNew);
				}
				surface.setValues(attributesList);
				//update the scene
				if (surface.getClientLayer() != null)
                    surface.getClientLayer().firePropertyChange(AVKey.LAYER, null, surface.getClientLayer());
			}
		});
		timer.start();
		
	}
	/**
	 * Method for interpolation animation
	 * **/
	public static void createAnimationInterpolation(final CreateDiffMaps obj){
		
		final SurfaceModelClass[] surfaceArray = obj.getSurfaceModel();
		Timer timer = new Timer(TimerInterval,new ActionListener(){
			protected long startTime = -1;
			int i=1;
			int t=0;
			SurfaceModelClass targetSurface = surfaceArray[0];

			int width = targetSurface.getWidth();
			int height = targetSurface.getHeight();
			
			SurfaceModelClass prevSurface = new SurfaceModelClass(width,height);
			int noSnapShots = obj.getNumofSnapShots();
			int Timeinterval = obj.getTimeInterval();
			int TimeIntervalSimulation = obj.getTimeInterval();
			int flag=0;
			int prevt = 0;
			public void actionPerformed(ActionEvent e) {
				if(this.startTime < 0){
					this.startTime = System.currentTimeMillis();
					System.out.println("Start Time"+ this.startTime);
					System.out.println("Inside timer width"+width+" height"+height);
				}
				//System.out.println("Hello "+ (e.getWhen()-this.startTime));
				System.out.println("t"+t);
				
				//Change the height map over every timer call
				long start = System.currentTimeMillis();
				if(t%noSnapShots==0 && flag==1){
					prevSurface = targetSurface;
					targetSurface = surfaceArray[i++];
					prevt = t;
				}
				flag=1;
				for(int y=0 ; y<height ; y++){
					for(int x=0 ; x<width; x++){
						if(targetSurface.getHeight(x, y) - prevSurface.getHeight(x, y) < 0.001)
							continue;
						double k1 = (targetSurface.getHeight(x, y) - prevSurface.getHeight(x, y)) / this.noSnapShots;
						double k2 = prevSurface.getHeight(x, y) - k1 * prevt;
						double deltaVal = k1*t + k2;
						
						AnalyticSurface.GridPointAttributes attrOld = attributesList.get(y*width+x);
						Color rgbaColor = new Color(0, 0, 255, 255);
						
						double newVal = attrOld.getValue() + deltaVal;
						
						if(t==0)
							newVal += err;
						
						AnalyticSurface.GridPointAttributes attrNew = createGridAttribute(newVal,rgbaColor);
						attributesList.set(y*width+x,attrNew);
					}
				}
				long endTime   = System.currentTimeMillis();
				long totalTime = endTime - start;
				System.out.println(" Total Time taken:"+totalTime);
				System.out.println("Done computation :) :), before timer goes off???");
				t++;
			}
		});
		timer.start();
	}
	
	/**
	 * Method called to direct to the corresponding animation style
	 * **/
	public static void addAnimationFromData(String dir){
		//get the data from the time series depth map
		//See if there exists a cache file.
		CreateDiffMaps obj = new CreateDiffMaps(dir);
		//obj.createDiffMaps();
		
		// This is for visualization of time-step absolute height data.
		obj.start();
		createAnimation(obj);
		
		//For interpolation between data:
		/*obj.saveSurfaceModel();
		createAnimationInterpolation(obj);*/
		
	}
	
	/**
	 * Setter and getters 
	 **/
	
	public SurfaceModelClass getSurfaceData() {
		return surfaceData;
	}

	public void setSurfaceData(SurfaceModelClass surfaceData) {
		this.surfaceData = surfaceData;
	}
	
}
