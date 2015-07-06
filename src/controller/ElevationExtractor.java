package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.data.ByteBufferRaster;
import gov.nasa.worldwind.formats.tiff.GeotiffWriter;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.ElevationModel;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwindx.examples.util.SectorSelector;
import gui.ImportCityGMLJDialog;

import org.citygml4j.model.gml.geometry.primitives.Envelope;

import render.AppFrame.RenderFrame;

//use the ElevationAndImage class, to manually extract the elevation	
public class ElevationExtractor {
	//private Envelope env;
	//private Sector sector;

	private static final double MISSING_DATA_SIGNAL = (double) Short.MIN_VALUE;

	private int globWidth;
	private int globHeight;
    public static JButton btnSaveElevations = null;
    private JButton btnSaveImage = null;
    public static Sector selectedSector = null;
    private JFileChooser fileChooser = null;
    public static SectorSelector selector;
    private JFrame renderFrame;

    public static double minLat;
    public static double maxLat;
    public static double minLon;
    public static double maxLon;
    
    public ElevationExtractor(JFrame frame){
    	
    	this.renderFrame = frame;
    	//this.renderFrame.setBounds(100, 100, 194, 138);
    	this.selector = new SectorSelector(StartUpGUI.wwd);
        this.selector.setInteriorColor(new Color(1f, 1f, 1f, 0.1f));
        this.selector.setBorderColor(new Color(1f, 0f, 0f, 0.5f));
        this.selector.setBorderWidth(3);
        
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        {   
            JButton
                // Set up a button to enable and disable region selection.
                btn = new JButton(new EnableSelectorAction());
            btn.setToolTipText("Press Start then press and drag button 1 on globe");
            btnPanel.add(btn);

            btnSaveElevations = new JButton(new SaveElevationsAction());
            btnSaveElevations.setEnabled(false);
            btnSaveElevations.setToolTipText("Click the button to save elevations of the selected area");
            btnPanel.add(btnSaveElevations);

            btnSaveImage = new JButton(/*new SaveImageAction()*/);
            btnSaveImage.setEnabled(false);
            btnSaveImage.setToolTipText("Click the button to save image of the selected area");
            //btnPanel.add(btnSaveImage);
        }
        btnPanel.setBorder(new TitledBorder(null, "Export Elevation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        renderFrame.add(btnPanel,BorderLayout.CENTER);

        this.selector.addPropertyChangeListener(SectorSelector.SECTOR_PROPERTY, new PropertyChangeListener()
        {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				 Sector sector = (Sector) evt.getNewValue();
                 if (null != sector)
                 {
                     selectedSector = sector;
                     btnSaveElevations.setEnabled(true);
                     btnSaveImage.setEnabled(true);
                 }
			}
        });
        
        this.enableNAIPLayer(renderFrame);
        
        renderFrame.add(btnPanel,BorderLayout.CENTER);
        
        
    }
    public void enableNAIPLayer(JFrame renderFrame)
    {
        LayerList list = StartUpGUI.wwd.getModel().getLayers();
        ListIterator<Layer> iterator = list.listIterator();
        while (iterator.hasNext())
        {
            Layer layer = (Layer) iterator.next();
            if (layer.getName().contains("NAIP"))
            {
                layer.setEnabled(true);
                break;
            }
        }
    }

    public static class GeotiffFileFilter extends javax.swing.filechooser.FileFilter
    {
        public boolean accept(File file)
        {
            if (file == null)
            {
                String message = Logging.getMessage("nullValue.FileIsNull");
                Logging.logger().severe(message);
                throw new IllegalArgumentException(message);
            }

            return file.isDirectory() || file.getName().toLowerCase().endsWith(".tif");
        }

        public String getDescription()
        {
            return "Geo-TIFF (tif)";
        }
    }
    
    private File selectDestinationFile(String title, String filename)
    {
        File destFile = null;

        if (this.fileChooser == null)
        {
            this.fileChooser = new JFileChooser();
            this.fileChooser.setCurrentDirectory(new File(Configuration.getUserHomeDirectory()));
            this.fileChooser.addChoosableFileFilter(new GeotiffFileFilter());
        }

        this.fileChooser.setDialogTitle(title);
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.fileChooser.setMultiSelectionEnabled(false);
        this.fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        this.fileChooser.setName(filename);

        int status = this.fileChooser.showSaveDialog(null);
        if (status == JFileChooser.APPROVE_OPTION)
        {
            destFile = this.fileChooser.getSelectedFile();
            if (!destFile.getName().endsWith(".tif"))
                destFile = new File(destFile.getPath() + ".tif");
        }
        return destFile;
    }

    private int[] adjustSize(Sector sector, int desiredSize)
    {
        int[] size = new int[] {desiredSize, desiredSize};

        if (null != sector && desiredSize > 0)
        {
            LatLon centroid = sector.getCentroid();
            Angle dLat = LatLon.greatCircleDistance(new LatLon(sector.getMinLatitude(), sector.getMinLongitude()),
                new LatLon(sector.getMaxLatitude(), sector.getMinLongitude()));
            Angle dLon = LatLon.greatCircleDistance(new LatLon(centroid.getLatitude(), sector.getMinLongitude()),
                new LatLon(centroid.getLatitude(), sector.getMaxLongitude()));

            double max = Math.max(dLat.radians, dLon.radians);
            double min = Math.min(dLat.radians, dLon.radians);

            int minSize = (int) ((min == 0d) ? desiredSize : ((double) desiredSize * min / max));

            if (dLon.radians > dLat.radians)
            {
                size[0] = desiredSize;      // width
                size[1] = minSize;  // height
            }
            else
            {
                size[0] = minSize;  // width
                size[1] = desiredSize;      // height
            }
        }

        return size;
    }

    private double[] readElevations(Sector sector){
    	double[] elevations = null;
    	
    	try{
    		Globe globe = StartUpGUI.wwd.getModel().getGlobe();
            ElevationModel model = globe.getElevationModel();
            
            double it = model.getBestResolution(sector);
            
            int width =  (int) ((sector.getMaxLatitude().radians - sector.getMinLatitude().radians)/model.getBestResolution(sector));
            int height = (int) ((sector.getMaxLongitude().radians - sector.getMinLongitude().radians)/model.getBestResolution(sector));
            
            this.globWidth = width;
            this.globHeight = height;
            System.out.println("width:"+width+ " height:"+ height);
            ArrayList<LatLon> latlons = new ArrayList<LatLon>(width * height);
            
            double lat = sector.getMinLatitude().radians;
            double lon = sector.getMinLongitude().radians;
            
            double latMax = sector.getMaxLatitude().radians;
            double lonMax = sector.getMaxLongitude().radians;
            
            File file = new File("/home/vishal/Desktop/Grass_Output/elevation.txt");
            if(!file.exists()){
            	file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(fw);
            
            for(double y=lat ; y<=latMax ; y+=it){
            	for(double x=lon ; x<=lonMax ; x+=it){
            		latlons.add(LatLon.fromRadians(y, x));
            		bw.write("("+lat+","+lon+")  ");
                    //lon = (x == maxx) ? lonMax : (lon + dLon);
            	}
            	bw.write("\n");
            }
            
            elevations = new double[latlons.size()];
            Arrays.fill(elevations, MISSING_DATA_SIGNAL);
            
            //filling the data
            //double achivedElev = model.getElevations(sector, latlons, it, elevations);
            model.composeElevations(sector, latlons, width, elevations);
            
            //print the elevation data to a file:
            
            
            int i=0;
            for (int y = height - 1; y >= 0; y--)
            {
                for (int x = 0; x < width; x++)
                {
                    bw.write((int) elevations[i++] + " ");
                }
                bw.write("\n");
            }
            bw.close();
            //System.out.println("This is the achieved resolution"+achivedElev);
    	}
    	catch (Exception e)
        {
            e.printStackTrace();
            elevations = null;
        }
    	return elevations;
    }
    private double[] readElevations(Sector sector, int width, int height)
    {
    	double[] elevations;

    	//width = (sector.getMaxLatitude().radians - sector.getMinLatitude().radians);
        double latMin = sector.getMinLatitude().radians;
        double latMax = sector.getMaxLatitude().radians;
        double dLat = (latMax - latMin) / (double) (height - 1);

        double lonMin = sector.getMinLongitude().radians;
        double lonMax = sector.getMaxLongitude().radians;
        double dLon = (lonMax - lonMin) / (double) (width - 1);

        ArrayList<LatLon> latlons = new ArrayList<LatLon>(width * height);

        System.out.println("dlon:"+dLat + " dlog" + dLon);
        
        int maxx = width - 1, maxy = height - 1;

        Globe globe = StartUpGUI.wwd.getModel().getGlobe();
        ElevationModel model = globe.getElevationModel();

        System.out.println("Best resolution"+model.getBestResolution(selectedSector));
        elevations = new double[width*height];
        Arrays.fill(elevations, 100);

        int i=0;
        double lat = latMin;
        for (int y = 0; y < height; y++)
        {
            double lon = lonMin;

            for (int x = 0; x < width; x++)
            {
            	if(model.contains(Angle.fromRadians(lat), Angle.fromRadians(lon)))
            		elevations[i++] = model.getElevation(Angle.fromRadians(lat), Angle.fromRadians(lon));
            	else{
            		System.out.println("oops data missing :( :(");
            	}
                //latlons.add(LatLon.fromRadians(lat, lon));
                lon = (x == maxx) ? lonMax : (lon + dLon);
            }

            lat = (y == maxy) ? latMax : (lat + dLat);
        }

        try
        {
/*            Globe globe = this.renderFrame.getWwd().getModel().getGlobe();
            ElevationModel model = globe.getElevationModel();

            System.out.println("Best resolution"+model.getBestResolution(selectedSector));
            elevations = new double[latlons.size()];
            Arrays.fill(elevations, MISSING_DATA_SIGNAL);

            // retrieve elevations
            model.composeElevations(sector, latlons, width, elevations);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
            elevations = null;
        }

        return elevations;

    }
    private void writeElevationsToFile(Sector sector, int width, int height, double[] elevations, File gtFile)
            throws IOException
        {
    		//New implementation of this function:
    		/*File file  = new File("/home/vishal/NWW/sampleData/floodPolygon2.tif");
    		
    		try{
    			BufferedImage img  = ImageIO.read(file);
    			ColorModel cm = img.getColorModel();
    			boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    			
    			BufferedImage im = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
    			
    			WritableRaster raster = im.copyData(null);
    			
    			int[] newval = new int[1];
    			int i=0;
    			for (int y = height - 1; y >= 0; y--)
                {
                    for (int x = 0; x < width; x++)
                    {
                        //raster.setDoubleAtPosition(y, x, elevations[i++]);
                    	newval[0] = (int) elevations[i++];
                        raster.setPixel(x, y, newval);
                        //System.out.print(elevations[i-1]+" ");
                    }
                    //System.out.println("");
                }
    			
    			BufferedImage newImg = new BufferedImage(cm,raster,isAlphaPremultiplied,null);
    			//write to file
    			ImageIO.write(newImg,"tiff",gtFile);
    		}
    		catch(IOException e){
    			e.printStackTrace();
    		}*/
    		
            // These parameters are required for writeElevation
            AVList elev32 = new AVListImpl();

            elev32.setValue(AVKey.SECTOR, sector);
            elev32.setValue(AVKey.WIDTH, width);
            elev32.setValue(AVKey.HEIGHT, height);
            elev32.setValue(AVKey.COORDINATE_SYSTEM, AVKey.COORDINATE_SYSTEM_GEOGRAPHIC);
            elev32.setValue(AVKey.PIXEL_FORMAT, AVKey.ELEVATION);
            elev32.setValue(AVKey.DATA_TYPE, AVKey.FLOAT32);
            elev32.setValue(AVKey.ELEVATION_UNIT, AVKey.UNIT_METER);
            elev32.setValue(AVKey.BYTE_ORDER, AVKey.BIG_ENDIAN);
            elev32.setValue(AVKey.MISSING_DATA_SIGNAL, MISSING_DATA_SIGNAL);
            //elev32.setValue(AVKey.PIXEL_HEIGHT, 10.0);
            //elev32.setValue(AVKey.PIXEL_WIDTH, 10.0);
            

            ByteBufferRaster raster = (ByteBufferRaster) ByteBufferRaster.createGeoreferencedRaster(elev32);
            // copy elevation values to the elevation raster
            int i = 0;
            for (int y = height - 1; y >= 0; y--)
            {
                for (int x = 0; x < width; x++)
                {
                    raster.setDoubleAtPosition(y, x, elevations[i++]);
                    //System.out.print(elevations[i-1]+" ");
                }
                //System.out.println("");
            }

            GeotiffWriter writer = new GeotiffWriter(gtFile);
            try
            {
                writer.write(raster);
            }
            finally
            {
                writer.close();
            }
        }

    
    public int[] getDimensions(Sector sector,double resx,double resy){
    	
    	//CrsConverterGDAL obj = new CrsConverterGDAL();
    	String epsgCode = null;
    	try{
    		epsgCode = ImportCityGMLJDialog.EPSGCodeTextField.getText();
    		double[] origin = new double[3];
        	origin[0] = 0;
        	origin[1] = 0;
        	origin[2] = 0;
        	origin = CrsConverter.convertCoordinate(epsgCode, "4326", origin,0);//convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", origin);
    		
    		double[] coord = new double[3];
    		coord[0] = resy;
    		coord[1] = resx;
    		coord[2] = 10;
    		
    		double x=coord[0] , y=coord[1] , z=coord[2];
    		coord = CrsConverter.convertCoordinate(epsgCode, "4326", coord,0);//obj.convertCoordinate("+proj=utm +zone=45 +ellps=WGS72 +towgs84=0,0,4.5,0,0,0.554,0.2263 +units=m +no_defs", "WGS84", coord);
    		
    		System.out.println(x+" "+y+" "+z+"\n"+coord[0]+" "+coord[1]+" "+coord[2]);

        	double latMin = sector.getMinLatitude().radians;
            double latMax = sector.getMaxLatitude().radians;
            double dLat = (latMax - latMin);

            double lonMin = sector.getMinLongitude().radians;
            double lonMax = sector.getMaxLongitude().radians;
            double dLon = (lonMax - lonMin);
            
            System.out.println("dLat:"+dLat+" dLon:"+dLon);
            
            double dResx = Angle.fromDegrees(origin[1]).radians - Angle.fromDegrees(coord[1]).radians;
            double dResy = Angle.fromDegrees(origin[0]).radians - Angle.fromDegrees(coord[0]).radians;
            
            System.out.println("dResx:"+dResx+" dResy:"+dResy);
            
            int height = Math.abs((int) (dLat/dResy));
            int width = Math.abs((int) (dLon/dResx));
            
            System.out.println("Inside getDimension function"+width+","+height);
            
            int[] size = new int[2];
            size[0] = width;
            size[1] = height;
            
        	return size;
    	}
    	catch(Exception e){
    		JOptionPane.showMessageDialog(new JDialog(), "No EPSG code, required to calculate the rows,cols of elevation");
    		System.out.println("No EPSG code, required to calculate the rows,cols of elevation");
    	}
    	return null;
    }
    
    public void doSaveElevations()
    {
    	final File saveToFile = this.selectDestinationFile(
                "Select a destination GeoTiff file to save elevations", "elevation");

            if (saveToFile == null)
                return;

            final JOptionPane jop = new JOptionPane("Requesting elevations ...",
                JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);

            final JDialog jd = jop.createDialog(this.renderFrame.getRootPane().getTopLevelAncestor(), "Please wait...");
            jd.setModal(false);
            jd.setVisible(true);
            
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                    	System.out.println(selectedSector.getMinLatitude() + " "+selectedSector.getMinLongitude());
                    	System.out.println(selectedSector.getMaxLatitude()+ " "+selectedSector.getMaxLongitude());
                        //int[] size = adjustSize(selectedSector, 512);
                    	double resx=10, resy=10;
                    	int[] size = getDimensions(selectedSector,resx,resy);
                        int width = size[0];
                        int height = size[1];
                    	//int width = selectedSector.get
                        System.out.println("width:"+width+" height"+height);

                        double[] elevations = readElevations(selectedSector, width, height);
                        //double[] elevations = readElevations(selectedSector);
                        //width = globWidth;
                        //height = globHeight;
                        
                        System.out.println("Outside width:"+width+" height:"+height);
                        if (null != elevations)
                        {
                            jd.setTitle("Writing elevations to " + saveToFile.getName());
                            writeElevationsToFile(selectedSector, width, height, elevations, saveToFile);
                            
                            jd.setVisible(false);
                            JOptionPane.showMessageDialog(new JDialog(),
                                "Elevations saved into the " + saveToFile.getName());
                        }
                        else
                        {
                            jd.setVisible(false);
                            JOptionPane.showMessageDialog(new JDialog(),
                                "Attempt to save elevations to the " + saveToFile.getName() + " has failed.");
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        jd.setVisible(false);
                        JOptionPane.showMessageDialog(new JDialog(), e.getMessage());
                    }
                    finally
                    {
                        SwingUtilities.invokeLater(new Runnable()
                        {
                            public void run()
                            {
                                //setCursor(Cursor.getDefaultCursor());
                                StartUpGUI.wwd.redraw();
                                jd.setVisible(false);
                            }
                        });
                    }
                }
            });

            //this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            StartUpGUI.wwd.redraw();
            t.start();
        }


    class SaveElevationsAction extends AbstractAction
    {
        public SaveElevationsAction()
        {
            super("Save elevations ...");
        }

        public void actionPerformed(ActionEvent e)
        {
            doSaveElevations();
        }
    }

    
	class EnableSelectorAction extends AbstractAction
    {
        public EnableSelectorAction()
        {
            super("Start selection");
        }

        public void actionPerformed(ActionEvent e)
        {
            ((JButton) e.getSource()).setAction(new DisableSelectorAction());
            selector.enable();
        }
    }
    
    class DisableSelectorAction extends AbstractAction
    {
        public DisableSelectorAction()
        {
            super("Clear selection");
        }
        
        public void actionPerformed(ActionEvent e)
        {
            selector.disable();
            btnSaveElevations.setEnabled(false);
            btnSaveImage.setEnabled(false);
            selectedSector = null;
            ((JButton) e.getSource()).setAction(new EnableSelectorAction());
        }
    }
}