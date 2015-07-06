package gui;

import gov.nasa.worldwind.layers.RenderableLayer;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import controller.StartUpGUI;
import render.RenderAnalyticSurface;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Hashtable;

public class AnimateOptionJDialog extends JFrame {

	private final JPanel contentPanel = new JPanel();
	private JTextField DepthMapFolderTextField;
	private JTextField SimulationTimeIntervalTextField;
	private JTextField SamplingRateTextField;
	private JTextField BaseElevationTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AnimateOptionJDialog dialog = new AnimateOptionJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AnimateOptionJDialog() {
		setBounds(100, 100, 383, 311);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new TitledBorder(null, "Create Dynamic Layer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblDepthMaps = new JLabel("Depth Maps:");
		DepthMapFolderTextField = new JTextField();
		DepthMapFolderTextField.setColumns(10);
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			JFileChooser fileChooser = new JFileChooser();
			public void actionPerformed(ActionEvent e) {

				File destFolder = null;

				this.fileChooser.setDialogTitle("Time Series data");
				this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				this.fileChooser.setMultiSelectionEnabled(false);
				
				int ret = fileChooser.showOpenDialog(null);
		        if(ret == JFileChooser.APPROVE_OPTION){
		        	destFolder = fileChooser.getSelectedFile();
		        	DepthMapFolderTextField.setText(destFolder.getAbsolutePath());
		        }

			}
		});
		
		JLabel lblTimeInterval = new JLabel("Simulation Interval:");
		
		SimulationTimeIntervalTextField = new JTextField();
		SimulationTimeIntervalTextField.setText("in mins");
		SimulationTimeIntervalTextField.setColumns(10);
		
		JLabel lblSamplingRate = new JLabel("Sampling Rate:");
		
		SamplingRateTextField = new JTextField();
		SamplingRateTextField.setColumns(10);
		
		JLabel lblBaseElevation = new JLabel("Base elevation:");
		
		BaseElevationTextField = new JTextField();
		BaseElevationTextField.setColumns(10);
		
		JButton btnBrowse_1 = new JButton("Browse");
		btnBrowse_1.addActionListener(new ActionListener() {
			JFileChooser fileChooser = new JFileChooser();
			public void actionPerformed(ActionEvent e) {

				File destFolder = null;

				this.fileChooser.setDialogTitle("Base Elevation dAta");
				this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				this.fileChooser.setMultiSelectionEnabled(false);
				
				int ret = fileChooser.showOpenDialog(null);
		        if(ret == JFileChooser.APPROVE_OPTION){
		        	destFolder = fileChooser.getSelectedFile();
		        	BaseElevationTextField.setText(destFolder.getAbsolutePath());
		        }

			}
		});
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDepthMaps)
						.addComponent(lblBaseElevation)
						.addComponent(lblSamplingRate)
						.addComponent(lblTimeInterval))
					.addGap(12)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(SimulationTimeIntervalTextField)
						.addComponent(btnBrowse)
						.addComponent(BaseElevationTextField, 162, 162, Short.MAX_VALUE)
						.addComponent(btnBrowse_1)
						.addComponent(SamplingRateTextField, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
						.addComponent(DepthMapFolderTextField, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE))
					.addGap(32))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDepthMaps)
						.addComponent(DepthMapFolderTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnBrowse)
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBaseElevation)
						.addComponent(BaseElevationTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnBrowse_1)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(18)
							.addComponent(lblSamplingRate))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(SamplingRateTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(20)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTimeInterval)
						.addComponent(SimulationTimeIntervalTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(24, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						String depthMapDir = DepthMapFolderTextField.getText();
						String baseElevation = BaseElevationTextField.getText();
						String SimluationTimeInterval = SimulationTimeIntervalTextField.getText();
						String samplingRate = SamplingRateTextField.getText();
						int SimluationInterval = 0;
						int sampleRate = 0;
						if(depthMapDir.equals("") || baseElevation.equals("") || baseElevation.equals("") || SimluationTimeInterval.equals("") || samplingRate.equals("")){
							System.out.println("Important fileds are missing required for the simulation");
							JOptionPane.showMessageDialog(new JDialog(), "One of the DepthMap fields are empty!!");	
						}
						else{
							int flag=1;
							try{
								SimluationInterval = Integer.parseInt(SimluationTimeInterval);
								sampleRate = Integer.parseInt(samplingRate);
							}
							catch(Exception e1){
								JOptionPane.showMessageDialog(new JDialog(), "OOPS, Time Interval, or the samplingRate are not integers", "Dialog",
								        JOptionPane.ERROR_MESSAGE);
								e1.printStackTrace();
								flag=0;
							}
							if(flag==1){
								dispose();
								RenderAnalyticSurface waterSurface = new RenderAnalyticSurface(baseElevation,SimluationInterval,sampleRate);
								RenderableLayer waterlayer = waterSurface.renderWaterSurface(depthMapDir);
								waterlayer.setName("Water Layer");
								StartUpGUI.insertBeforePlacenames(StartUpGUI.wwd, waterlayer);

								StartUpGUI.dataLayer.add(waterlayer);
								StartUpGUI.datalayerPanel.update(StartUpGUI.wwd);
								StartUpGUI.btnPlay.setEnabled(true);
								StartUpGUI.btnSpeedUpx.setEnabled(true);
								StartUpGUI.btnSppedDownx.setEnabled(true);
								StartUpGUI.timeSlider.setEnabled(true);
								
								StartUpGUI.timeSlider.setValue(0);
								//StartUpGUI.timeSlider.setBorder(BorderFactory.createTitledBorder("Time (hrs)"));

								int maxVal = RenderAnalyticSurface.endT * RenderAnalyticSurface.samplingRate;
								//RenderAnalyticSurface.Simulationinterval = RenderAnalyticSurface.Simulationinterval/ RenderAnalyticSurface.samplingRate;
								int maxTime = ((RenderAnalyticSurface.endT * RenderAnalyticSurface.Simulationinterval)/(RenderAnalyticSurface.samplingRate))/60;
								StartUpGUI.timeSlider.setMajorTickSpacing(maxVal/5);
								int spacing = maxVal/5;
								int timeSpace = maxTime/5;
								//StartUpGUI.timeSlider.setMinorTickSpacing(1);
								StartUpGUI.timeSlider.setMaximum(maxVal);
								
								//Define the label on the slider:
								int timeVal=0;
								int pos=0;
								Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer,JLabel>();
								labelTable.put(new Integer(pos), new JLabel(Integer.toString(timeVal)));
								pos += spacing;
								timeVal += timeSpace;
								
								labelTable.put(new Integer(pos), new JLabel(Integer.toString(timeVal)));
								pos += spacing;
								timeVal += timeSpace;
								
								labelTable.put(new Integer(pos), new JLabel(Integer.toString(timeVal)));
								pos += spacing;
								timeVal += timeSpace;
								
								labelTable.put(new Integer(pos), new JLabel(Integer.toString(timeVal)));
								pos += spacing;
								timeVal += timeSpace;
								
								labelTable.put(new Integer(pos), new JLabel(Integer.toString(timeVal)));
								pos += spacing;
								timeVal += timeSpace;
								
								labelTable.put(new Integer(maxVal), new JLabel(Integer.toString(maxTime)));
								pos += spacing;
								timeVal += timeSpace;
								
								StartUpGUI.timeSlider.setLabelTable(labelTable);
								
								StartUpGUI.timeSlider.setPaintTicks(true);
								StartUpGUI.timeSlider.setPaintLabels(true);
								
								setCursor(null);
							}
							
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
