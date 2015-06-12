package render;

import java.awt.Color;
import java.util.ArrayList;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.util.WWMath;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurface;
import waterSurfaceModel.SurfaceModelClass;

public class RenderAnalyticSurface {
	protected static final double HUE_BLUE = 240d / 360d;
    protected static final double HUE_RED = 0d / 360d;
    
	private SurfaceModelClass surfaceData;

	/**
	 * Constructors
	 * **/
	public RenderAnalyticSurface(String filename){
		this.surfaceData = new SurfaceModelClass(filename);
		this.surfaceData.loadHeightMap();
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
	
	protected static void createTerrainSurface(double minHue, double maxHue, 
												int width, int height,SurfaceModelClass surfaceData, 
												RenderableLayer layer){
		
		AnalyticSurface surface = new AnalyticSurface();
		surface.setSector(surfaceData.getSector());
		surface.setAltitude(100);
		surface.setVerticalScale(1.0);
		surface.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
		surface.setDimensions(surfaceData.getWidth(), surfaceData.getHeight());
		surface.setClientLayer(layer);
		
		layer.addRenderable(surface);
		
		//create the analytic surface
        ArrayList<AnalyticSurface.GridPointAttributes> attributesList = new ArrayList<AnalyticSurface.GridPointAttributes>();
        
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
            Color rgbaColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 155);

        	AnalyticSurface.GridPointAttributes attr = createGridAttribute(0,rgbaColor);
        	attributesList.add(attr);
        }
        //set the surface values
        surface.setValues(attributesList);
    
	}
	
	public RenderableLayer renderWaterSurface(){
		RenderableLayer layer = new RenderableLayer();
		
		layer.setPickEnabled(false);
        layer.setName("Analytic Surfaces");
        
        createTerrainSurface(HUE_BLUE, HUE_RED, surfaceData.getWidth(), surfaceData.getHeight(), surfaceData, layer);
        
		return layer;
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
