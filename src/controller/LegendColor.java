package controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import gov.nasa.worldwind.render.ScreenImage;
import gov.nasa.worldwind.util.WWMath;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurfaceLegend;

public class LegendColor extends AnalyticSurfaceLegend{
	
	 public static AnalyticSurfaceLegend fromColorShades(int width, int height, double minValue, double maxValue,
		        double HueVal, double minSaturation,double maxSaturation, Color borderColor, Iterable<? extends LabelAttributes> labels,
		        LabelAttributes titleLabel)
		    {   
		 		LegendColor legend = new LegendColor();
		        legend.screenImage = new ScreenImage();
		        /*legend.screenImage.setImageSource(legend.createColorShadeLegendImage(width, height, HueVal, minSaturation,maxSaturation,
		            borderColor));*/
		        legend.screenImage.setImageSource(legend.createHueShadeLegendImage(width, height, minSaturation,maxSaturation,0.6,
			            borderColor));
		        legend.labels = legend.createColorGradientLegendLabels(width, height, minValue, maxValue, labels, titleLabel);

		        return legend;
		    }
	
	public static AnalyticSurfaceLegend fromColorShades(double minValue, double maxValue,double HueVal, double minSaturation,double maxSaturation, Iterable<? extends LabelAttributes> labels, LabelAttributes titleLabel){
		 
		return fromColorShades(DEFAULT_WIDTH, DEFAULT_HEIGHT, minValue, maxValue, HueVal, minSaturation,maxSaturation, DEFAULT_COLOR,
		            labels,
		            titleLabel);
	}
	
	protected BufferedImage createColorShadeLegendImage(int width, int height, double HueVal, double minSaturation,
			double maxSaturation,
	        Color borderColor)
	    {
	        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
	        Graphics2D g2d = image.createGraphics();
	        try
	        {
	            for (int y = 0; y < height; y++)
	            {
	                double saturation = WWMath.mix(1d - y / (double) (height - 1), minSaturation, maxSaturation);
	                g2d.setColor(Color.getHSBColor((float) HueVal, (float)saturation, 0.6f));
	                g2d.drawLine(0, y, width - 1, y);
	            }

	            if (borderColor != null)
	            {
	                g2d.setColor(borderColor);
	                g2d.drawRect(0, 0, width - 1, height - 1);
	            }
	        }
	        finally
	        {
	            g2d.dispose();
	        }

	        return image;
	    }
	protected BufferedImage createHueShadeLegendImage(int width, int height, double minHue,	double maxHue, double brightness,
	        Color borderColor)
	    {
	        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
	        Graphics2D g2d = image.createGraphics();
	        try
	        {
	            for (int y = 0; y < height; y++)
	            {
	                double hue = WWMath.mix(1d - y / (double) (height - 1), minHue, maxHue);
	                g2d.setColor(Color.getHSBColor((float) hue, (float)1, (float)brightness));
	                g2d.drawLine(0, y, width - 1, y);
	            }

	            if (borderColor != null)
	            {
	                g2d.setColor(borderColor);
	                g2d.drawRect(0, 0, width - 1, height - 1);
	            }
	        }
	        finally
	        {
	            g2d.dispose();
	        }

	        return image;
	    }
}
