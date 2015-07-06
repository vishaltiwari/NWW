package gui;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLayeredPane;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Button;

import javax.swing.border.TitledBorder;

import java.awt.FlowLayout;

import javax.swing.JSplitPane;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JSlider;

import net.miginfocom.swing.MigLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class TestGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//make an instance of the TESTGUI class
					TestGUI window = new TestGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestGUI() {
		//WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
       // wwd.setPreferredSize(new java.awt.Dimension(1000, 800));
        //wwd.setModel(new BasicModel());
        
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 972, 688);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnFile.add(mntmExportelevation);
		
		JMenuItem mntmAnimate = new JMenuItem("Animate");
		mnFile.add(mntmAnimate);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmDepthAtA = new JMenuItem("Depth At a point");
		mnTools.add(mntmDepthAtA);
		
		JMenuItem mntmHydrograph = new JMenuItem("Hydrograph");
		mnTools.add(mntmHydrograph);
		
		JMenuItem mntmClassify = new JMenuItem("Classify");
		mnTools.add(mntmClassify);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "ToolBox", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 6, 2, 2));
		
		JButton btnNewButton = new JButton("Import");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ImportCityGMLJDialog dialog = new ImportCityGMLJDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		
		JButton btnNewButton_1 = new JButton("Export");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ExportElevationJDialog dialog = new ExportElevationJDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Query depth");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DepthValueJDialog dialog = new DepthValueJDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		panel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("HydroGraph");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HydroGraphJDialog dialog = new HydroGraphJDialog();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		panel.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Classify");
		panel.add(btnNewButton_4);
		
		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new MigLayout("", "[grow]", "[150px:n:150px,top][100px:n:100px,bottom][100px:n:100px,grow,bottom]"));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "WW Layers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(panel_4, "cell 0 0,grow");
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_5.getLayout();
		flowLayout_1.setVgap(10);
		flowLayout_1.setHgap(50);
		panel_5.setBorder(new TitledBorder(null, "Data Layers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(panel_5, "cell 0 1,grow");
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(null, "Time Slider", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.add(panel_6, "cell 0 2,grow");
		panel_6.setLayout(new GridLayout(4, 0, 0, 0));
		
		JButton btnPlay = new JButton("Play");
		panel_6.add(btnPlay);
		
		JButton btnSpeedUpx = new JButton("speed up(2x)");
		panel_6.add(btnSpeedUpx);
		
		JButton btnNewButton_7 = new JButton("speed down(2x)");
		panel_6.add(btnNewButton_7);
		
		JSlider slider_1 = new JSlider();
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
			}
		});
		panel_6.add(slider_1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "WorldWindWindow", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(panel_3, BorderLayout.CENTER);
		
		//frame.getContentPane().add(wwd, java.awt.BorderLayout.CENTER);
        
	}
}
