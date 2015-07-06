package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import render.AppFrame.RenderFrame;

public class ToolBox {
	public ToolBox(RenderFrame frame){
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "ToolBox", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 6, 2, 2));
		
		JButton btnNewButton = new JButton("Import");
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		panel.add(btnNewButton);
		
		JButton btnAnimate = new JButton("Animate");
		panel.add(btnAnimate);
		
		JButton btnNewButton_1 = new JButton("Export");
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Query depth");
		panel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("HydroGraph");
		panel.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Classify");
		panel.add(btnNewButton_4);
	}
}
