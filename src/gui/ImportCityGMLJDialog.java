package gui;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.ViewInputHandler;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.poi.PointOfInterest;
import gov.nasa.worldwind.view.BasicView;
import gov.nasa.worldwind.view.firstperson.BasicFlyView;
import gov.nasa.worldwind.view.firstperson.FlyToFlyViewAnimator;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;

import render.RenderAnalyticSurface;
import render.RenderObjects;
import controller.CityGMLUtil;
import controller.CrsConverter;
import controller.StartUp;
import controller.StartUpGUI;
import controller.ViewContoler;
import controller.ViewContoler.ViewerClass;
import citygmlModel.BuildingsClass;
import citygmlModel.MultipleBuildingsFileClass;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImportCityGMLJDialog extends JFrame {

	private final JPanel contentPanel = new JPanel();
	private static JTextField filenameTextField;
	private static JTextField LayerNameTextField;
	public static JTextField EPSGCodeTextField;
	private static String filename=null;
	private static double[] center;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ImportCityGMLJDialog dialog = new ImportCityGMLJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ImportCityGMLJDialog() {
		setBounds(100, 100, 431, 281);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new TitledBorder(null, "Import CityGML", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.WEST);
		
		JLabel lblNewLabel = new JLabel("DataSet:");
		
		filenameTextField = new JTextField();
		filenameTextField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			private JFileChooser fileChooser = new JFileChooser();
			public void actionPerformed(ActionEvent e) {
				//FileChooser dialog = new FileChooser();
				//dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				//dialog.setVisible(true);
				//File file = dialog.getFilename();
				//System.out.println(file.getName());
				//String filename = 
				File destFile = null;

		        this.fileChooser.setDialogTitle("cityGML Upload");
		        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        this.fileChooser.setMultiSelectionEnabled(false);
		        
		        int ret = fileChooser.showOpenDialog(null);
		        if(ret == JFileChooser.APPROVE_OPTION){
		        	destFile = fileChooser.getSelectedFile();
		        	filename = destFile.getAbsolutePath();
		        	if (!destFile.getName().endsWith(".gml") && !destFile.getName().endsWith(".xml")){
		        		JOptionPane.showMessageDialog(new JDialog(), "Not a gml or cityGML, or xml file");
		        	}
		        	else{
		        		try {
		        			CityGMLUtil obj = new CityGMLUtil(filename);
		        			//obj.getLocation("/home/vishal/NWW/sampleData/waldbruecke_v1.0.0.gml");
		        			center = obj.getCenter();
		        			System.out.println(obj.getEPSG() + "," + center[0] + ","+center[1]+","+center[2]);
		        			EPSGCodeTextField.setText(obj.getEPSG());
		        			LayerNameTextField.setText(obj.getLayername());
		        		} catch (IOException e1) {
		        			JOptionPane.showMessageDialog(new JDialog(), "OOPS, error occured while loading EPSG Code, and area extent", "Dialog",
							        JOptionPane.ERROR_MESSAGE);
		        			e1.printStackTrace();
		        		}
			        	filenameTextField.setText(filename);
			        	System.out.println(filename);
		        	}
		        }
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("Layer Name:");
		
		LayerNameTextField = new JTextField();
		LayerNameTextField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("EPSG Code:");
		
		EPSGCodeTextField = new JTextField();
		EPSGCodeTextField.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(35)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1)
						.addComponent(lblNewLabel_2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(EPSGCodeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(LayerNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse)
						.addComponent(filenameTextField, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
					.addGap(72))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(30)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(filenameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnBrowse)
					.addGap(35)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel_1)
						.addComponent(LayerNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(EPSGCodeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(182, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Import");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//import the data
						if(filenameTextField.getText().isEmpty() || LayerNameTextField.getText().isEmpty() || EPSGCodeTextField.getText().isEmpty()){
							JOptionPane.showMessageDialog(new JDialog(), "One of the Text fields are empty!!");
						}
						else{
							//import the data:
							try {
								class ImportCityGMLThread extends SwingWorker<MultipleBuildingsFileClass,Object>{

									@Override
									protected MultipleBuildingsFileClass doInBackground() throws Exception {
										setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
										MultipleBuildingsFileClass cityGMLData=null;
										try{
											cityGMLData= new MultipleBuildingsFileClass();
											cityGMLData.setCrs(EPSGCodeTextField.getText());
											cityGMLData.setLayerName(LayerNameTextField.getText());
											cityGMLData.IterateGMLFile(filenameTextField.getText());
										}
										catch(Exception e){
											JOptionPane.showMessageDialog(new JDialog(), "OOPS, error occured while Importing data", "Dialog",
											        JOptionPane.ERROR_MESSAGE);
											e.printStackTrace();
										}
										
										List<BuildingsClass> buildingsList = cityGMLData.getBuildingsList();
										System.out.println("List size should be more than one:"+buildingsList.size());
										System.out.println("Solids/buildings numbers:"+buildingsList.get(0).getSolid().size());
										try{
											for(BuildingsClass building : buildingsList){
												RenderObjects renderFile = new RenderObjects(EPSGCodeTextField.getText());
												RenderableLayer buildingsLayer = renderFile.startRenderingBuildings(building);
												buildingsLayer.setName(LayerNameTextField.getText());
												StartUpGUI.insertBeforeCompass(StartUpGUI.wwd,buildingsLayer);
												StartUpGUI.dataLayer.add(buildingsLayer);
											}
											StartUpGUI.datalayerPanel.update(StartUpGUI.wwd);
											//StartUpGUI.layerPanel.update(StartUpGUI.wwd);
										}
										catch(Exception e){
											JOptionPane.showMessageDialog(new JDialog(), "OOPS, error occured while rendering cityGML data", "Dialog",
											        JOptionPane.ERROR_MESSAGE);
											e.printStackTrace();
										}
										return cityGMLData;
									}
									
									@Override
									protected void done(){
										try{
											MultipleBuildingsFileClass obj = get();
											setCursor(null);
											dispose();
											
											//Fly to the area:
											//BasicFlyView view = new BasicFlyView();
											//View view = StartUpGUI.wwd.getView();
											//StartUpGUI.wwd.setView(view);
											double[] coords;
											coords = CrsConverter.convertCoordinate(EPSGCodeTextField.getText(),"WGS84",center);
											double lon = coords[0];
											double lat = coords[1];
											double height = 1000;
											LatLon latlon = LatLon.fromDegrees(lat, lon);
											
											System.out.println(lat+","+lon);
											
											BasicOrbitView view = (BasicOrbitView) StartUpGUI.wwd.getView();
											System.out.println("EyePosition:"+view.getEyePosition().getLatitude().degrees+","+view.getEyePosition().getLongitude().degrees);
											view.addEyePositionAnimator(
													4000, view.getEyePosition(), new Position(latlon, height));

											//RenderAnalyticSurface waterSurface = new RenderAnalyticSurface("/home/vishal/Desktop/Grass_Output/01a_07_15.tif");
								            //RenderableLayer waterlayer = waterSurface.renderWaterSurface("/home/vishal/Desktop/Grass_Output/images10");
								            
								            //StartUpGUI.insertBeforePlacenames(StartUpGUI.wwd, waterlayer);
											
											//setViewer(StartUpGUI.wwd,orbitViewer,true);
											/*try{
											FlyToFlyViewAnimator animator =
							                        FlyToFlyViewAnimator.createFlyToFlyViewAnimator(view,
							                            view.getEyePosition(),
							                            new Position(latlon, height),
							                            view.getHeading(), view.getHeading(),
							                            view.getPitch(), view.getPitch(),
							                            view.getEyePosition().getElevation(), view.getEyePosition().getElevation(),
							                            10000, WorldWind.ABSOLUTE);
											
											view.addAnimator(animator);
						                    animator.start();
						                    
						                    view.firePropertyChange(AVKey.VIEW, null, view);
											}
											catch(Exception e){
												e.printStackTrace();
											}*/
										}
										catch(Exception e){
											JOptionPane.showMessageDialog(new JDialog(), "OOPS, error in Importing data", "Dialog",
											        JOptionPane.ERROR_MESSAGE);
											e.printStackTrace();
										}
										finally{
											System.out.println("Done with the import operation");
										}
									}
								}
								ImportCityGMLThread task = new ImportCityGMLThread();
								//(new ImportCityGMLThread()).execute();
								/*final JProgressBar progressBar = new JProgressBar();
								final ImportCityGMLThread task = new ImportCityGMLThread();
								task.addPropertyChangeListener(
										new PropertyChangeListener() {

											@Override
											public void propertyChange(PropertyChangeEvent arg0) {
												int progress = task.getProgress();
										        if (progress == 0) {
										            progressBar.setIndeterminate(true);
										            //taskOutput.append("No progress yet\n");
										            System.out.println("Creating the progress bar");
										        }
											}
								});*/
								task.execute();
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(new JDialog(), "OOPS, error in running the thread", "Dialog",
								        JOptionPane.ERROR_MESSAGE);
								e1.printStackTrace();
							}
							//List<BuildingsClass> buildingsList = obj.getBuildingsList();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
