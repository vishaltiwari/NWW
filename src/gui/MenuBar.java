package gui;

import java.awt.BorderLayout;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import render.AppFrame.RenderFrame;

public class MenuBar {
	public MenuBar(RenderFrame frame){
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmImport = new JMenuItem("Import");
		mnFile.add(mntmImport);
		
		JMenuItem mntmExportelevation = new JMenuItem("ExportElevation");
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
	}
}
