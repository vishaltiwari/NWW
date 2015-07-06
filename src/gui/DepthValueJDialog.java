package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import controller.StartUpGUI;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DepthValueJDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public static JLabel lblLatval;
	public static JLabel lblLongval;
	public static JLabel lblDepthval;
	public static JLabel lblEleval;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DepthValueJDialog dialog = new DepthValueJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DepthValueJDialog() {
		setBounds(100, 100, 233, 213);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new TitledBorder(null, "Depth Value", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblLatitude = new JLabel("Latitude:");
		lblLatval = new JLabel("");
		JLabel lblLongitude = new JLabel("Longitude:");
		lblLongval = new JLabel("");
		JLabel lblDepth = new JLabel("Depth:");
		lblDepthval = new JLabel("");
		JLabel lblElevation = new JLabel("Elevation:");
		lblEleval = new JLabel("");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLongitude)
						.addComponent(lblLatitude)
						.addComponent(lblDepth)
						.addComponent(lblElevation))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblEleval)
						.addComponent(lblDepthval)
						.addComponent(lblLatval)
						.addComponent(lblLongval))
					.addContainerGap(263, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLatitude)
						.addComponent(lblLatval))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLongitude)
						.addComponent(lblLongval))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDepth)
						.addComponent(lblDepthval))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblElevation)
						.addComponent(lblEleval))
					.addContainerGap(89, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Close");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						StartUpGUI.depthQueryFlag=0;
						dispose();
						StartUpGUI.Sticklayer.removeAllRenderables();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
