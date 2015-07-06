package controller;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.ClickAndGoSelectListener;
import gov.nasa.worldwindx.examples.LayerPanel;
import gov.nasa.worldwindx.examples.analytics.AnalyticSurface;
import gov.nasa.worldwindx.examples.util.HighlightController;
import gov.nasa.worldwindx.examples.util.ToolTipController;
import gui.ApplicationTemplateTest;
import gui.DataLayer;
import gui.GraphPlot;
import gui.ImportCityGMLJDialog;
import gui.AnimateOptionJDialog;
import gui.ExportElevationJDialog;
import gui.DepthValueJDialog;
import gui.HydroGraphJDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.data.xy.XYSeries;

import render.CylinderPole;
import render.RenderAnalyticSurface;
import render.RenderObjects;
import waterSurfaceModel.SurfaceModelClass;
import citygmlModel.BuildingsClass;
import net.miginfocom.swing.MigLayout;

public class StartUpGUI extends ApplicationTemplateTest{
	private static JFrame frame;
	protected static StatusBar statusBar;
    public static WorldWindow wwd = new WorldWindowGLCanvas();
    public static ArrayList<RenderableLayer> dataLayer = new ArrayList<RenderableLayer>();
    public static DataLayer datalayerPanel;
    public static LayerPanel layerPanel;
    
    public static JButton btnPlay;
    public static JButton btnSpeedUpx;
    public static JButton btnSppedDownx;
    public static JSlider timeSlider;
    
    public static int depthQueryFlag=0;
    public static int hydroGraph=0;
    
    public static double positionx;
    public static double positiony;
    
    public static int setPosition=0;
    public static HydroGraphJDialog hydroFrame;
    
    private static Position p;
    
    public static RenderableLayer Sticklayer;
    //private int stickCount=0;
    //private static int flag=1;
	
    public class MouseEvents implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			p = wwd.getCurrentPosition();
			//System.out.println("Lat:"+p.latitude + " lon:"+p.longitude + " elevation:"+p.elevation);
			if(depthQueryFlag==1 || hydroGraph==1){
				
				double[] coordinate = new double[3];
				coordinate[0] = (new Double(p.longitude.degrees)).doubleValue();
				coordinate[1] = (new Double(p.latitude.degrees)).doubleValue();
				coordinate[2] = 0;
				
				System.out.println("depthQuerFlag"+depthQueryFlag);
				if(depthQueryFlag==1){
					DepthValueJDialog.lblLatval.setText((String.format("%.6g%n", new Double(p.latitude.degrees)).toString()));
					DepthValueJDialog.lblLongval.setText((String.format("%.6g%n", new Double(p.longitude.degrees)).toString()));
					DepthValueJDialog.lblEleval.setText((String.format("%.4g%n", new Double(p.elevation)).toString())+" m");
					
					Sticklayer.removeAllRenderables();
					
					CylinderPole pole = new CylinderPole(p.latitude.degrees,p.longitude.degrees,0,Sticklayer);
					
					pole.createPole();
				}
				
				double[] coord;
				try{
					String epsgCode = ImportCityGMLJDialog.EPSGCodeTextField.getText();
					if(!epsgCode.equals("")){
						coord = CrsConverter.convertCoordinate(epsgCode, "4326", coordinate, 1);
						System.out.println("x:"+coord[0]+ "y:"+coord[1]);
					
						Sector sector = RenderAnalyticSurface.sec;
						double minLat;
						double maxLat;
						double minLon;
						double maxLon;
						if(sector!=null){
							minLat = sector.getMinLatitude().degrees;
							maxLat = sector.getMaxLatitude().degrees;
							minLon = sector.getMinLongitude().degrees;
							maxLon = sector.getMaxLongitude().degrees;

							double[] lowerPoint = new double[3];
							lowerPoint[0] = minLon;
							lowerPoint[1] = minLat;
							lowerPoint[2] = 0;

							double[] upperPoint = new double[3];
							upperPoint[0] = maxLon;
							upperPoint[1] = maxLat;
							upperPoint[2] = 0;

							//convert the lat/lon to x/y
							lowerPoint = CrsConverter.convertCoordinate(epsgCode, "4326", lowerPoint, 1);
							upperPoint = CrsConverter.convertCoordinate(epsgCode, "4326", upperPoint, 1);

							System.out.println("Bound, LL,x:"+lowerPoint[0]+",y:"+lowerPoint[1]+"  UR,x:"+upperPoint[0]+",y:"+upperPoint[1]);
							//check if the clicked point is inside the selected sector:
							double posx;
							double posy;

							double samplex = (upperPoint[0] - lowerPoint[0])/RenderAnalyticSurface.globalWidth;
							double sampley = (upperPoint[1] - lowerPoint[1])/RenderAnalyticSurface.globalHeight;
							
							if(lowerPoint[0] < coord[0] && coord[0] < upperPoint[0]
									&& lowerPoint[1] < coord[1] && coord[1] < upperPoint[1]){
								System.out.println("Point lies insdie");
								posx = (coord[0] - lowerPoint[0])/samplex;
								posy = (upperPoint[1] - coord[1])/sampley;
								System.out.println("x:"+ posx+" y:"+posy);
								
								System.out.println("Value of HydroFlag:"+hydroGraph);
								try{
									if(hydroGraph==1){
										XYSeries series = HydroGraph.getXYData((int)posx,(int)posy,p.latitude.degrees,p.longitude.degrees);
										GraphPlot.dataset.addSeries(series);

										JPanel panel = GraphPlot.createChartPanel();
										hydroFrame.remove(0);
										hydroFrame.add(panel);
									}
								}
								catch(Exception e){

								}
								System.out.println("NOT REACHING HERE???");
								//System.out.println("width:"+RenderAnalyticSurface.globalWidth+ " height:"+RenderAnalyticSurface.globalHeight);
								//get the value by interpolating the values of the neighbours of x,y:
								System.out.println("value of depthQueryFlag:"+depthQueryFlag);
								if(depthQueryFlag==1){
									
									int lowx = (int)(posx);
									int lowy = (int)(posy);

									int upperx = lowx+1;
									int uppery = lowy+1;

									int flag=0;
									if(RenderAnalyticSurface.timer.isRunning()){
										flag=1;
										RenderAnalyticSurface.timer.stop();
									}
									ArrayList<AnalyticSurface.GridPointAttributes> list = RenderAnalyticSurface.attributesList;
									double sum=0;
									AnalyticSurface.GridPointAttributes attr = list.get(lowy*RenderAnalyticSurface.globalWidth+lowx);
									sum += attr.getValue();

									attr = list.get(uppery*RenderAnalyticSurface.globalWidth+lowx);
									sum += attr.getValue();

									attr = list.get(uppery*RenderAnalyticSurface.globalWidth+upperx);
									sum += attr.getValue();

									attr = list.get(lowy*RenderAnalyticSurface.globalWidth+upperx);
									sum += attr.getValue();

									//value to display:
									double avgDepth = sum/4;

									DepthValueJDialog.lblDepthval.setText((String.format("%.4g%n", new Double(avgDepth)).toString())+" m");

									if(flag==1)
										RenderAnalyticSurface.timer.start();
								}
								
							}
							else
								System.out.println("The points doesn't lie inside");
						}
						else{
							System.out.println("Dynamic data not added");
						}
					}
					
				}
				catch(Exception e){
					System.out.println("No data projected");
				}
				
				//double depth = CalculateDepth(p);
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
	public StartUpGUI(){

		frame = new JFrame();
		frame.setBounds(100, 100, 972, 688);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		((Component) wwd).setPreferredSize(new java.awt.Dimension(1200, 800));

		Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
		wwd.setModel(m);
		wwd.getInputHandler().addMouseListener(new MouseEvents());
		//wwd.setModel(new BasicModel());

		// Setup a select listener for the worldmap click-and-go feature
		wwd.addSelectListener(new ClickAndGoSelectListener(wwd, WorldMapLayer.class));
		
		//Sticklayer.setName("Pole");
		Sticklayer = new RenderableLayer();
		Sticklayer.setName("pole");
		StartUpGUI.insertBeforeCompass(wwd,Sticklayer);
		
        
		initialize();
	}

	public static void initialize(){

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmImport = new JMenuItem("Import");
		mntmImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ImportCityGMLJDialog dialog = new ImportCityGMLJDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		mnFile.add(mntmImport);

		JMenuItem mntmExportelevation = new JMenuItem("ExportElevation");
		mntmExportelevation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame exportFrame= new JFrame();
				exportFrame.setBounds(100, 100, 194, 100);
				exportFrame.setLayout(new BorderLayout());
				//exportFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				exportFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						ElevationExtractor.selector.disable();
						ElevationExtractor.selectedSector = null;
					}
				});
				exportFrame.setAlwaysOnTop(true);
				new ElevationExtractor(exportFrame);
				exportFrame.setVisible(true);
			}
		});
		mnFile.add(mntmExportelevation);

		JMenuItem mntmAnimate = new JMenuItem("Animate");
		mntmAnimate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AnimateOptionJDialog dialog = new AnimateOptionJDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		mnFile.add(mntmAnimate);

		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);

		JMenuItem mntmDepthAtA = new JMenuItem("Depth At a point");
		mntmDepthAtA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DepthValueJDialog dialog = new DepthValueJDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent evt){
						depthQueryFlag=0;
					}
				});
				dialog.setVisible(true);
				dialog.setAlwaysOnTop(true);
				depthQueryFlag=1;
			}
		});
		mnTools.add(mntmDepthAtA);

		JMenuItem mntmHydrograph = new JMenuItem("Hydrograph");
		mntmHydrograph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hydroGraph = 1;
				GraphPlot frame = new GraphPlot();
				frame.setVisible(true);
				frame.setAlwaysOnTop(true);
				
				frame.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent evt){
						hydroGraph=0;
					}
				});
			}
		});
		mnTools.add(mntmHydrograph);

		/*JMenuItem mntmClassify = new JMenuItem("Classify");
		mnTools.add(mntmClassify);*/
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "ToolBox", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 5, 2, 2));

		JButton btnNewButton = new JButton("Import CityGML");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ImportCityGMLJDialog dialog = new ImportCityGMLJDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		panel.add(btnNewButton);

		JButton btnAnimate = new JButton("Animate");
		btnAnimate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AnimateOptionJDialog dialog = new AnimateOptionJDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		panel.add(btnAnimate);

		JButton btnNewButton_1 = new JButton("Export Elevation");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				JFrame exportFrame= new JFrame();
				exportFrame.setBounds(100, 100, 194, 100);
				exportFrame.setLayout(new BorderLayout());
				//exportFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				exportFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						ElevationExtractor.selector.disable();
						ElevationExtractor.selectedSector = null;
					}
				});
				exportFrame.setAlwaysOnTop(true);
				new ElevationExtractor(exportFrame);
				exportFrame.setVisible(true);
			}
		});
		panel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Query depth");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DepthValueJDialog dialog = new DepthValueJDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent evt){
						depthQueryFlag=0;
						Sticklayer.removeAllRenderables();
					}
				});
				dialog.setVisible(true);
				dialog.setAlwaysOnTop(true);
				depthQueryFlag=1;
			}
		});
		panel.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("HydroGraph");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				hydroGraph = 1;
				GraphPlot frame = new GraphPlot();
				frame.setVisible(true);
				frame.setAlwaysOnTop(true);
				
				frame.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent evt){
						hydroGraph=0;
					}
				});
				
				//hydroFrame = new HydroGraphJDialog();
				//hydroFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				//hydroFrame.setVisible(true);
				//hydroFrame.setAlwaysOnTop(true);
			}
		});
		panel.add(btnNewButton_3);

/*		JButton btnNewButton_4 = new JButton("Classify");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int flag=0;
				if(RenderAnalyticSurface.timer.isRunning()){
					flag=1;
					RenderAnalyticSurface.timer.stop();
				}
				
				//color the terrain from hue = 0.1 to 1;
				
				if(flag==1){
					RenderAnalyticSurface.timer.start();
				}
			}
		});
		panel.add(btnNewButton_4);*/


		statusBar = new StatusBar();
		frame.add(statusBar, BorderLayout.PAGE_END);
		statusBar.setEventSource(wwd);

		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new MigLayout("", "[grow]", "[150px:n:350px,top][150px:n:250px,grow,bottom]"));

		layerPanel = new LayerPanel(wwd, null);
		panel_2.add(layerPanel, "cell 0 0,grow");

		datalayerPanel = new DataLayer(wwd,null);
		//JPanel panel_5 = new JPanel();
		//FlowLayout flowLayout_1 = (FlowLayout) panel_5.getLayout();
		//flowLayout_1.setVgap(10);
		//flowLayout_1.setHgap(50);
		//panel_5.setBorder(new TitledBorder(null, "Data Layers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(datalayerPanel, "cell 0 1,grow");

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(null, "Time Slider", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(panel_6, "cell 0 2,grow");
		panel_6.setLayout(new GridLayout(5, 0, 0, 0));

		btnPlay = new JButton("Play");
		btnPlay.setEnabled(false);
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!RenderAnalyticSurface.timer.isRunning()){
					System.out.println("play button clicked");
					RenderAnalyticSurface.timer.start();
					btnPlay.setText("Pause");
				}
				else if(RenderAnalyticSurface.timer.isRunning()){
					System.out.println("pause button clicked");
					RenderAnalyticSurface.timer.stop();
					btnPlay.setText("Play");
				}
			}
		});
		panel_6.add(btnPlay);

		/*JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Reset the 
				RenderAnalyticSurface.timer.stop();
				RenderAnalyticSurface.attributesList.clear();
				RenderAnalyticSurface.timer.restart();
				
			}
		});
		panel_6.add(btnStop);*/

		btnSpeedUpx = new JButton("speed up(1.5x)");
		btnSpeedUpx.setEnabled(false);
		btnSpeedUpx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(RenderAnalyticSurface.timer.isRunning()){
					RenderAnalyticSurface.timer.stop();
					int prevInterval = RenderAnalyticSurface.timer.getDelay();
					int newInterval = (int) (prevInterval/1.5);
					if(newInterval >= 20){
						RenderAnalyticSurface.timer.setDelay(newInterval);
						System.out.println("Prev timer Interval:"+prevInterval+"New timer Interval:"+newInterval);
					}
					RenderAnalyticSurface.timer.start();
				}
				else if(!RenderAnalyticSurface.timer.isRunning()){
					//RenderAnalyticSurface.timer.stop();
					int prevInterval = RenderAnalyticSurface.timer.getDelay();
					int newInterval = (int) (prevInterval/1.5);
					if(newInterval >= 20){
						RenderAnalyticSurface.timer.setDelay(newInterval);
						System.out.println("Prev timer Interval:"+prevInterval+"New timer Interval:"+newInterval);
					}//RenderAnalyticSurface.timer.start();
				}
			}
		});
		panel_6.add(btnSpeedUpx);

		btnSppedDownx = new JButton("speed down(1.5x)");
		btnSppedDownx.setEnabled(false);
		btnSppedDownx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(RenderAnalyticSurface.timer.isRunning()){
					RenderAnalyticSurface.timer.stop();
					int prevInterval = RenderAnalyticSurface.timer.getDelay();
					int newInterval = (int) (prevInterval*1.5);
					if(newInterval >= 80){
						RenderAnalyticSurface.timer.setDelay(newInterval);
						System.out.println("Prev timer Interval:"+prevInterval+"New timer Interval:"+newInterval);
					}
					RenderAnalyticSurface.timer.start();
				}
				else if(!RenderAnalyticSurface.timer.isRunning()){
					int prevInterval = RenderAnalyticSurface.timer.getDelay();
					int newInterval = (int) (prevInterval*1.5);
					if(newInterval >= 80){
						RenderAnalyticSurface.timer.setDelay(newInterval);
						System.out.println("Prev timer Interval:"+prevInterval+"New timer Interval:"+newInterval);
					}
				}
			}
		});
		panel_6.add(btnSppedDownx);

		timeSlider = new JSlider();
		timeSlider.setEnabled(false);
		//timeSlider.addChangeListener(this);
		//change these properties in the CreateAnimationJFrame
		timeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider slider = ((JSlider)e.getSource());
				if(slider.getValueIsAdjusting()){
					int startFlag=1;
					if(RenderAnalyticSurface.timer.isRunning()){
						RenderAnalyticSurface.timer.stop();
					}
					else{
						startFlag=0;
					}
					System.out.println("The new value is:" + slider.getValue());
					int new_t = slider.getValue()/RenderAnalyticSurface.samplingRate;
					System.out.println("new_t"+new_t);
					RenderAnalyticSurface.t = new_t;
					SurfaceModelClass[] surfaceArray = RenderAnalyticSurface.surfaceArray;
					int width = surfaceArray[0].getWidth();
					int height = surfaceArray[0].getHeight();
					System.out.println();
					if(new_t==0){
						RenderAnalyticSurface.i = 1;
						RenderAnalyticSurface.flag = 0;
						RenderAnalyticSurface.prevSurface = new SurfaceModelClass(width,height);
						
						RenderAnalyticSurface.targetSurface = surfaceArray[0];
						
						RenderAnalyticSurface.reStoreSurface(width,height,RenderAnalyticSurface.prevSurface,RenderAnalyticSurface.min_val,RenderAnalyticSurface.max_val);
					}
					else{
						RenderAnalyticSurface.i = (new_t/RenderAnalyticSurface.samplingRate)+1;
						RenderAnalyticSurface.prevt = (new_t)/RenderAnalyticSurface.samplingRate;
						RenderAnalyticSurface.targetSurface = surfaceArray[RenderAnalyticSurface.i];
						/*double[][] test = RenderAnalyticSurface.prevVal;
						for(int i=0 ; i<height ; i++){
							Arrays.fill(test[i], 0.0);
						}*/
						RenderAnalyticSurface.reStoreSurface(width,height,RenderAnalyticSurface.targetSurface,RenderAnalyticSurface.min_val,RenderAnalyticSurface.max_val);
					}
					if(startFlag==1)
						RenderAnalyticSurface.timer.start();
				}
			}
		});
		panel_6.add(timeSlider);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "WorldWindWindow", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add((WorldWindowGLCanvas)wwd, BorderLayout.CENTER);

		// Create and install the view controls layer and register a controller for it with the World Window.
        ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
        insertBeforeCompass(wwd, viewControlsLayer);
        wwd.addSelectListener(new ViewControlsSelectListener(wwd, viewControlsLayer));
        
     // Register a rendering exception listener that's notified when exceptions occur during rendering.
        wwd.addRenderingExceptionListener(new RenderingExceptionListener()
        {
            public void exceptionThrown(Throwable t)
            {
                if (t instanceof WWAbsentRequirementException)
                {
                    String message = "Computer does not meet minimum graphics requirements.\n";
                    message += "Please install up-to-date graphics driver and try again.\n";
                    message += "Reason: " + t.getMessage() + "\n";
                    message += "This program will end when you press OK.";

                    //JOptionPane.showMessageDialog(StartUpGUI.class, message, "Unable to Start Program",
                    //    JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
        });
        
     // Search the layer list for layers that are also select listeners and register them with the World
        // Window. This enables interactive layers to be included without specific knowledge of them here.
        for (Layer layer : wwd.getModel().getLayers())
        {
            if (layer instanceof SelectListener)
            {
                wwd.addSelectListener((SelectListener) layer);
            }
        }

        frame.pack();


     // Center the application on the screen.
        WWUtil.alignComponent(null, frame, AVKey.CENTER);
        frame.setResizable(true);

	}

	public static void LoadCityGML(StartUp start){
		
		RenderableLayer layer = new RenderableLayer();
        
        //RenderingBuildingSurface
        try {
			//obj.IterateGMLFile(filePath);
			List<BuildingsClass> buildingsList = start.obj.getBuildingsList();
			//Each element in BuildingList is a new citygml file, so each will be a separate layer:
			for(BuildingsClass building : buildingsList){
				RenderObjects renderFile = new RenderObjects(start.obj.getCrs());
				RenderableLayer buildingsLayer = renderFile.startRenderingBuildings(building);
				insertBeforeCompass(wwd,buildingsLayer);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//ApplicationTemplateTest.start("Test GUI", GUIFrame.class);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//StartUp start = new StartUp();
					StartUpGUI window = new StartUpGUI();
					window.frame.setVisible(true);
					//LoadCityGML(start);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
