package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphPlot extends JFrame {

	private JPanel contentPane;

	public static XYSeriesCollection dataset;
	public static JPanel chartPanel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphPlot frame = new GraphPlot();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GraphPlot() {
		dataset = new XYSeriesCollection();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		//contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	
		//setContentPane(contentPane);
		//contentPane.setLayout(new BorderLayout(0, 0));
		
		chartPanel = createChartPanel();
		add(chartPanel);	
	}
	
	/**
	 * Create the graphPanel
	 * **/
	public static JPanel createChartPanel(){
		String title = "Depth over time at (lat/lon)";
		String xAxisLabel = "Time (hours)";
		String yAxisLabel = "Depth (meters)";
		
		//createDataset();
		
		//if(dataset.a)
		JFreeChart chart = ChartFactory.createXYLineChart(title,xAxisLabel,yAxisLabel,dataset);
		return new ChartPanel(chart);
	}
	
	/**
	 * Create the dataSet:
	 * **/
	private static void createDataset(){
		
		XYSeries series1 = new XYSeries("Object 1");
	    XYSeries series2 = new XYSeries("Object 2");
	    XYSeries series3 = new XYSeries("Object 3");
	    series1.add(1.0, 2.0);
	    series1.add(2.0, 3.0);
	    series1.add(3.0, 2.5);
	    series1.add(3.5, 2.8);
	    series1.add(4.2, 6.0);
	 
	    series2.add(2.0, 1.0);
	    series2.add(2.5, 2.4);
	    series2.add(3.2, 1.2);
	    series2.add(3.9, 2.8);
	    series2.add(4.6, 3.0);
	 
	    series3.add(1.2, 4.0);
	    series3.add(2.5, 4.4);
	    series3.add(3.8, 4.2);
	    series3.add(4.3, 3.8);
	    series3.add(4.5, 4.0);
	 
	    dataset.addSeries(series1);
	    dataset.addSeries(series2);
	    dataset.addSeries(series3);
	}
}
