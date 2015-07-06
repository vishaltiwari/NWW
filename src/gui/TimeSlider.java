package gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import render.AppFrame.RenderFrame;

public class TimeSlider {
	public TimeSlider(RenderFrame frame){
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Time Slider", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new MigLayout("", "[][center][center][grow,center]", "[]"));
		
		JButton btnNewButton_5 = new JButton("Play");
		panel_1.add(btnNewButton_5, "cell 1 0");
		
		JButton btnStop = new JButton("Stop");
		panel_1.add(btnStop, "cell 2 0");
		
		JSlider slider = new JSlider();
		slider.setValue(70);
		panel_1.add(slider, "cell 3 0");
	}
}
