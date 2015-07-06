package render;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.LegendColor;
import controller.StartUpGUI;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Extent;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.util.WWMath;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurface;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurfaceAttributes;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurfaceLegend;
import waterSurfaceModel.CreateDiffMaps;
import waterSurfaceModel.SurfaceModelClass;
import waterSurfaceModel.CreateDiffMaps.Pair;

public class RenderAnalyticSurface {
	protected static final double HUE_BLUE = 240d / 360d;
    protected static final double HUE_RED = 0d / 360d;

    protected static final double minHue = 0.534;
    protected static final double maxHue = 0.745;
    
    protected static double hueVal = 0.667;
    protected static double minSat=0.2f;
    protected static double maxSat=1f;
    protected static double brightness = 0.6;
    
	SurfaceModelClass surfaceData;
	public static AnalyticSurface surface;
	public static ArrayList<AnalyticSurface.GridPointAttributes> attributesList;
	private static int Width;
	private static double err;
	
	private static int startTime;
	private static int endTime;
	
	private static int TimerInterval;
	public static int Simulationinterval;
	public static int samplingRate;
	
	public static Timer timer;
	
	public static int startT;
	public static int endT;
	
	/////////////// States in the Timer
	public static int i;
	public static int t;
	
	public static SurfaceModelClass targetSurface;
	
	public static SurfaceModelClass prevSurface;
	
	public static int flag;
	public static int prevt;
	
	public static int[][] flagFirst; 
	
	public static SurfaceModelClass[] surfaceArray;
	
	public static double[][] prevVal;
	
	public static double min_val;
	public static double max_val;


	public static Sector sec;
	
	public static int globalWidth;
	public static int globalHeight;
	
	public static CreateDiffMaps obj;
	//protected long startTime = -1;
	
	///////////////
	/**
	 * Constructors
	 * **/
	public RenderAnalyticSurface(String filename,int SimulationInterval,int samplingRate){
		this.surfaceData = new SurfaceModelClass(filename);
		globalWidth = surfaceData.getWidth();
		globalHeight = surfaceData.getHeight();
		sec = this.surfaceData.getSector();
		System.out.println("width:"+surfaceData.getWidth()+" height"+surfaceData.getHeight());
		RenderAnalyticSurface.TimerInterval = 100;
		RenderAnalyticSurface.samplingRate = samplingRate;
		RenderAnalyticSurface.Simulationinterval = SimulationInterval;
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
	 * Color from value
	 * **/
	public static Color getColor(double newVal,double minVal,double maxVal){
		double hue = hueVal;
		double minSatVal = minSat;
		double maxSatVal = maxSat;
		double minColor = minHue;
		double maxColor = maxHue;
		
		/*double k = (maxColor - minColor)/(maxVal-minVal);
		double blue = newVal*k - minVal*k + minColor;
		//double blue = (255/(maxVal-minVal))*newVal - (255*minVal/maxVal-minVal);
		//System.out.println("minVal:"+minVal+" maxVal:"+maxVal+" newVal:"+newVal+" blue:"+blue);
		return new Color(0,0,(int) blue,255);*/
		double satFactor = WWMath.computeInterpolationFactor(newVal, minVal, maxVal);
//		Color color = Color.getHSBColor((float)hue, (float) WWMath.mixSmooth(satFactor, minSatVal, maxSatVal), 0.46f);
		Color color = Color.getHSBColor((float) WWMath.mixSmooth(satFactor, minColor, maxColor), 1f, (float)brightness);
		
		double opacity = WWMath.computeInterpolationFactor(newVal, minVal, minVal+ (maxVal - minVal) * 0.001);
        Color rgbaColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * opacity));
		return rgbaColor;
	}
	/**
	 * Rendering function for the static AnalyticSyrface
	 * **/
	
	protected static void createSurfaceSkeleton(int width, int height,SurfaceModelClass surfaceData, 
												RenderableLayer layer){
		
		surface = new AnalyticSurface();
		surface.setSector(surfaceData.getSector());
		//Adding an error of 0.0001 (so as to remove the tesselation lines of the surface).
		//err = 0.5;
		err = 0;
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
        //double minValue = 43;
        //double maxValue = 103;
		
        int size = surfaceData.getHeight() * surfaceData.getWidth();
        //float[] heightMap = surfaceData.getHeightMap();
        
        for(int i=0 ; i<size ; i++){
        	
        	//float value = heightMap[i];
        	//double hueFactor = WWMath.computeInterpolationFactor(value, minValue, maxValue);
            //Color color = Color.getHSBColor((float) WWMath.mixSmooth(hueFactor, minHue, maxHue), 1f, 1f); 
            //double opacity = WWMath.computeInterpolationFactor(value, minValue, minValue + (maxValue - minValue) * 0.1);
            Color rgbaColor = new Color(255, 255, 255, 0);

        	AnalyticSurface.GridPointAttributes attr = createGridAttribute(0,rgbaColor);
        	
        	attributesList.add(attr);
        }
        //set the surface values
        surface.setValues(attributesList);
	}
	
	/**
	 * Restore the previous attributes
	 * **/
	public static void reStoreSurface(int width, int height,SurfaceModelClass surfaceData,double min_val,double max_val){
		int size = surfaceData.getHeight() * surfaceData.getWidth();
        float[] heightMap = surfaceData.getHeightMap();
        ArrayList<AnalyticSurface.GridPointAttributes> attributesList = new ArrayList<AnalyticSurface.GridPointAttributes>();
        
        for(int i=0 ; i<size ; i++){
        	
        	float value = heightMap[i];
        	Color rgbaColor = getColor(value,min_val,max_val);
        	AnalyticSurface.GridPointAttributes attr;
            if(Double.compare(value, 0.0)==0)
            	attr = createGridAttribute(0,rgbaColor);
            else
            	attr = createGridAttribute(err+value,rgbaColor);
            
        	attributesList.add(attr);
        }
        //set the surface values
        RenderAnalyticSurface.attributesList = attributesList;
        surface.setValues(attributesList);
        if (surface.getClientLayer() != null){
			System.out.println("Inside the redraw method, Value is being updated");
            surface.getClientLayer().firePropertyChange(AVKey.LAYER, null, surface.getClientLayer());
		}
	}
	
	/**
	 * Start Of the dynamic layer computation
	 * **/
	public RenderableLayer renderWaterSurface(String dir){
		RenderableLayer layer = new RenderableLayer();
		
		layer.setPickEnabled(false);
        //layer.setName("Analytic Surfaces");
		obj = new CreateDiffMaps(dir);
		obj.saveSurfaceModel();
        int noFiles = obj.getFileList().length;
		startT = 0;
		endT = noFiles * samplingRate;
		System.out.println("StartT:" + startT+" endT:"+endT);
		
        createSurfaceSkeleton(surfaceData.getWidth(), surfaceData.getHeight(), surfaceData, layer);
        addAnimationFromData(HUE_BLUE,HUE_RED,surfaceData,layer,obj);
		return layer;
	}
	
	/**
	 * Method for dynamic visualization of water surface data.
	 * **/
	public static void createAnimation(CreateDiffMaps obj){
		
		final ArrayList<Map<Pair,Double> > timeStampedDiff = obj.getTimeStampedDiff();
		final double minVal = obj.getMinVal();
		final double maxVal = obj.getMaxVal();
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
					
					//get the color:
					Color blueShade = getColor(val,minVal,maxVal);
		        	AnalyticSurface.GridPointAttributes attrNew = createGridAttribute(newVal,blueShade);
		        	
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
	 * @throws IOException 
	 * **/
	//private SurfaceModelClass targetSurface;
	public static void createAnimationInterpolation(final CreateDiffMaps obj) throws IOException{
		
		RenderAnalyticSurface.surfaceArray = obj.getSurfaceModel();
		RenderAnalyticSurface.i=1;
		RenderAnalyticSurface.t=1;
		RenderAnalyticSurface.targetSurface = surfaceArray[0];
		
		RenderAnalyticSurface.prevSurface = new SurfaceModelClass(RenderAnalyticSurface.targetSurface.getWidth(),RenderAnalyticSurface.targetSurface.getHeight());
		
		RenderAnalyticSurface.flag=0;
		RenderAnalyticSurface.prevt = 0;
		
		RenderAnalyticSurface.prevVal = new double[RenderAnalyticSurface.targetSurface.getHeight()][RenderAnalyticSurface.targetSurface.getWidth()];
		RenderAnalyticSurface.min_val = obj.getMinVal();
		RenderAnalyticSurface.max_val = obj.getMaxVal();
		timer = new Timer(TimerInterval,new ActionListener(){
			protected long startTime = -1;
			
			int width = targetSurface.getWidth();
			int height = targetSurface.getHeight();
			
			//int noSnapShots = obj.getNumofSnapShots();
			
			public void actionPerformed(ActionEvent e) {
				if(this.startTime < 0){
					this.startTime = System.currentTimeMillis();
					System.out.println("Start Time"+ this.startTime);
					System.out.println("Inside timer width"+width+" height"+height);
				}
				//System.out.println("Hello "+ (e.getWhen()-this.startTime));
				if(StartUpGUI.timeSlider.getValue() != StartUpGUI.timeSlider.getMaximum()){
					StartUpGUI.timeSlider.setValue(StartUpGUI.timeSlider.getValue() + 1);
					System.out.println("t"+t);
				}
				else{
					((Timer)e.getSource()).stop();
				}
				
				//Change the height map over every timer call
				long start = System.currentTimeMillis();
				if(t%samplingRate==0 && flag==1){
					prevSurface = targetSurface;
					
					targetSurface = surfaceArray[i++];
					System.out.println(i+" t:"+t);
					//It's same only when changing the prev and target set:
					prevt = t;
					//set the surface values to the prevSurface data.
				}
				flag=1;
				System.out.println("Value at index 500 at Start: "+attributesList.get(500).getValue());
				/*float[] prevheight = prevSurface.getHeightMap();
				float[] targetheight = targetSurface.getHeightMap();
				double a = (targetheight[500] - prevheight[500])/samplingRate;
				double b = prevheight[500] - (a*prevt);
				double interpolVal = a*t + b;
				System.out.println("t:"+t+ "  targetHeight:"+targetheight[500]+ "  prevheight:"+prevheight[500]+ "depthValue:" + interpolVal);*/
				for(int y=0 ; y<height ; y++){
					for(int x=0 ; x<width; x++){
						//if(targetSurface.getHeight(x, y) - prevSurface.getHeight(x, y) < 0.001)
						//	continue;
						AnalyticSurface.GridPointAttributes attrOld = attributesList.get(y*width+x);
						//oldVal is the old depth Value at this pixel
						double oldVal = attrOld.getValue();

						double k1 = (targetSurface.getHeight(x, y) - prevSurface.getHeight(x, y)) / samplingRate;
						double k2 = prevSurface.getHeight(x, y) - (k1 * prevt);
						double deltaVal = k1*t + k2;
						
						//System.out.println();
						//System.out.println("deltaVal :" + deltaVal);
						
						double deltaAdd = deltaVal - oldVal;
						//System.out.println("deltaVal :" + deltaAdd);
						//prevVal[y][x] = deltaVal;
						
						if(samplingRate==1){
							deltaAdd = (targetSurface.getHeight(x, y) - prevSurface.getHeight(x, y));
						}

						//System.out.println("value here"+);
						Color rgbaColor = getColor(deltaVal,min_val,max_val);

						double newVal = attrOld.getValue() + deltaAdd;

						/*if(Double.compare(oldVal, 0.0)==0 && Double.compare(newVal, 0.0)!=0){
							newVal += err;
						}*/
						
						/*if(k1!=0){
							System.out.println("t:"+t+ " k1:"+k1 + "  targetHeight:"+targetSurface.getHeight(x, y)+ "  prevheight:"+prevSurface.getHeight(x, y)+" sample: "+samplingRate);
							System.out.println("deltaVal :" + deltaAdd);
							System.out.println("newVal: " + newVal);
						}*/
						AnalyticSurface.GridPointAttributes attrNew = createGridAttribute(newVal,rgbaColor);
						attributesList.set(y*width+x,attrNew);
					}
				}
				surface.setValues(attributesList);
				if (surface.getClientLayer() != null){
					System.out.println("Value is being updated");
                    surface.getClientLayer().firePropertyChange(AVKey.LAYER, null, surface.getClientLayer());
				}
				
				long endTime   = System.currentTimeMillis();
				long totalTime = endTime - start;
				System.out.println(" Total Time taken:"+totalTime);
				System.out.println("Done computation :) :), before timer goes off???");
				System.out.println("Value at index 500 at end: "+attributesList.get(500).getValue());
				t++;
				
			}			
		});
		//timer.start();
	}
	
	/**
	 * Method called to direct to the corresponding animation style
	 * **/
	public static void addAnimationFromData(double minHue1, double maxHue1, 
												SurfaceModelClass surfaceData, 
													RenderableLayer layer,CreateDiffMaps obj){
		//get the data from the time series depth map
		//See if there exists a cache file.
		
		//obj.createDiffMaps();
		
		// This is for visualization of time-step absolute height datminVala.
		/*obj.start();
		createAnimation(obj);*/
		
		//For interpolation between data:
		obj.saveSurfaceModel();
		
		   //Create the Legend here:
        final double altitude = surface.getAltitude();
        final double verticalScale = surface.getVerticalScale();
        Format legendLabelFormat = new DecimalFormat("# m")
        {   
            public StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition)
            {   
                double altitudeMeters = verticalScale * number;
                //double altitudeMillimeters = altitudeMeters*1000;
                return super.format(altitudeMeters, result, fieldPosition);
            }   
        };
        double minValue = obj.getMinVal();
        double maxValue = obj.getMaxVal();
        
        //Color minColor = getColor(obj.getMinVal(),obj.getMaxVal(),obj.getMaxVal());
        //Color maxColor = getColor(obj.getMaxVal(),obj.getMaxVal(),obj.getMaxVal());
        
        //calculate hue values from the max and min values:
//        minHue = 0.667;
//        maxHue = 0.73;
        //hueVal = 0.667;
        /*AnalyticSurfaceLegend legend = AnalyticSurfaceLegend.fromColorGradient(minValue, maxValue, minHue, maxHue,
                AnalyticSurfaceLegend.createDefaultColorGradientLabels(minValue, maxValue, legendLabelFormat),
                AnalyticSurfaceLegend.createDefaultTitle("Depth Values"));*/
        //minSat=0.5f;
        //maxSat=1f;
        /*AnalyticSurfaceLegend legend = LegendColor.fromColorShades(minValue, maxValue, hueVal, minSat, maxSat,
                AnalyticSurfaceLegend.createDefaultColorGradientLabels(minValue, maxValue, legendLabelFormat),
                AnalyticSurfaceLegend.createDefaultTitle("Depth Values"));*/
        AnalyticSurfaceLegend legend = LegendColor.fromColorShades(minValue, maxValue, hueVal, minHue, maxHue,
                AnalyticSurfaceLegend.createDefaultColorGradientLabels(minValue, maxValue, legendLabelFormat),
                AnalyticSurfaceLegend.createDefaultTitle("Depth Values"));
		
        legend.setOpacity(1);
    	legend.setScreenLocation(new Point(900, 400));
    	layer.addRenderable(createLegendRenderable(surface, 300, legend));
        
        
		try {
			createAnimationInterpolation(obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected static Renderable createLegendRenderable(final AnalyticSurface surface, final double surfaceMinScreenSize,
	        final AnalyticSurfaceLegend legend)
	    {
	        return new Renderable()
	        {
	            public void render(DrawContext dc)
	            {
	                Extent extent = surface.getExtent(dc);
	                if (!extent.intersects(dc.getView().getFrustumInModelCoordinates()))
	                    return;

	                if (WWMath.computeSizeInWindowCoordinates(dc, extent) < surfaceMinScreenSize)
	                    return;

	                legend.render(dc);
	            }
	        };
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
