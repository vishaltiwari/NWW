package controller;

import javax.swing.JOptionPane;

import randomTest.ViewSwitch.AppFrame.ViewDisplay.ViewerClass;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.awt.ViewInputHandler;

public class ViewContoler {
	
	public WorldWindow wwd;
	public static class ViewerClass
    {
        protected String viewClassName;
        protected String inputHandlerClassName;
        protected View view;
        protected ViewInputHandler viewInputHandler;

        ViewerClass(String viewClassName, String inputHandlerClassName)
        {
            this.viewClassName = viewClassName;
            this.inputHandlerClassName = inputHandlerClassName;
            this.view = null;
            this.viewInputHandler = null;
        }
    }
	public ViewerClass orbitViewer = new ViewerClass(
            "gov.nasa.worldwind.view.orbit.BasicOrbitView",
            "gov.nasa.worldwind.view.orbit.OrbitViewInputHandler");
	
	public ViewContoler(){
		// Orbit view class information
	}
	public static void setViewer(WorldWindow wwd,ViewerClass vc, boolean copyValues)
    {
        if (vc.view == null)
        {
            vc.view = (View) WorldWind.createComponent(vc.viewClassName);
            vc.viewInputHandler =
                vc.view.getViewInputHandler();
        }
        if (copyValues)
        {
            View viewToCopy = wwd.getView();

            try
            {
                vc.view.copyViewState(viewToCopy);
                wwd.setView(vc.view);
            }
            catch (IllegalArgumentException iae)
            {
                System.out.println("Error");
            }
        }
        else
        {
            wwd.setView(vc.view);
        }
    }
        
}
