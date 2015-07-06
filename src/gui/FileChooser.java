package gui;

import gov.nasa.worldwind.Configuration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;

import controller.ElevationExtractor.GeotiffFileFilter;

public class FileChooser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	JFileChooser fileChooser = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FileChooser dialog = new FileChooser();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FileChooser() {
		setBounds(100, 100, 531, 372);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			this.fileChooser = new JFileChooser();
			contentPanel.add(fileChooser);
		}
	}
	public File getFilename(){
		return this.fileChooser.getSelectedFile();
	}
	public File selectDestinationFile(String title, String filename)
    {
        File destFile = null;

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

}
