package controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.LayerPanel;
import gui.AnimateOptionJDialog;
import gui.DataLayer;
import gui.DepthValueJDialog;
import gui.GraphPlot;
import gui.ImportCityGMLJDialog;
import net.miginfocom.swing.MigLayout;
import render.RenderAnalyticSurface;
import waterSurfaceModel.SurfaceModelClass;

public class RenderMain extends ApplicationTemplate {
	
	public static class MainAppFrame extends AppFrame{
		
		public static JButton btnPlay;
	    public static JButton btnSpeedUpx;
	    public static JButton btnSppedDownx;
	    public static JSlider timeSlider;
	    
	    public static int depthQueryFlag=0;
	    public static int hydroGraph=0;
		
		public MainAppFrame(){
			super(false,false,false);
			init();
		}
		
		public void init(){
			JMenuBar menuBar = new JMenuBar();
			this.setJMenuBar(menuBar);

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
			this.getContentPane().setLayout(new BorderLayout(0, 0));

			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "ToolBox", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			this.getContentPane().add(panel, BorderLayout.NORTH);
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
					/*dialog.addWindowListener(new WindowAdapter(){
						public void windowClosing(WindowEvent evt){
							depthQueryFlag=0;
							Sticklayer.removeAllRenderables();
						}
					});*/
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


			/*statusBar = new StatusBar();
			this.add(statusBar, BorderLayout.PAGE_END);
			statusBar.setEventSource(wwd);

			JPanel panel_2 = new JPanel();
			this.getContentPane().add(panel_2, BorderLayout.WEST);
			panel_2.setLayout(new MigLayout("", "[grow]", "[150px:n:350px,top][150px:n:250px,grow,bottom]"));

			layerPanel = new LayerPanel(wwd, null);
			panel_2.add(layerPanel, "cell 0 0,grow");

			datalayerPanel = new DataLayer(wwd,null);
			//JPanel panel_5 = new JPanel();
			//FlowLayout flowLayout_1 = (FlowLayout) panel_5.getLayout();
			//flowLayout_1.setVgap(10);
			//flowLayout_1.setHgap(50);
			//panel_5.setBorder(new TitledBorder(null, "Data Layers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_2.add(datalayerPanel, "cell 0 1,grow");*/

			JPanel panel_6 = new JPanel();
			panel_6.setBorder(new TitledBorder(null, "Time Slider", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			//panel_2.add(panel_6, "cell 0 2,grow");
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

			/*JPanel panel_3 = new JPanel();
			panel_3.setBorder(new TitledBorder(null, "WorldWindWindow", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			this.getContentPane().add((WorldWindowGLCanvas)wwd, BorderLayout.CENTER);

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
	        });*/
	        
	     // Search the layer list for layers that are also select listeners and register them with the World
	        // Window. This enables interactive layers to be included without specific knowledge of them here.
	        /*for (Layer layer : wwd.getModel().getLayers())
	        {
	            if (layer instanceof SelectListener)
	            {
	                wwd.addSelectListener((SelectListener) layer);
	            }
	        }*/

	        this.pack();


	     // Center the application on the screen.
	        WWUtil.alignComponent(null, this, AVKey.CENTER);
	        this.setResizable(true);
		}

		
	}
	public static void main(String[] args){
		RenderMain.start("4D flood simulation", MainAppFrame.class);
	}
}
