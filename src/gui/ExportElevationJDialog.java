package gui;

import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwindx.examples.util.SectorSelector;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ExportElevationJDialog extends JFrame {

	private final JPanel contentPanel = new JPanel();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ExportElevationJDialog dialog = new ExportElevationJDialog();
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ExportElevationJDialog() {
		setBounds(100, 100, 239, 149);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new TitledBorder(null, "Export Elevation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JButton btnStartSelection = new JButton("Start Selection");
		btnStartSelection.setBounds(34, 23, 162, 25);
		contentPanel.add(btnStartSelection);
		
		JButton btnSaveToFile = new JButton("Save To File");
		btnSaveToFile.setBounds(34, 60, 162, 25);
		contentPanel.add(btnSaveToFile);
	}
}
