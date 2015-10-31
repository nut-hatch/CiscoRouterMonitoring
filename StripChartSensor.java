import java.awt.BasicStroke;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * Creates a chart for a temperature/humidity sensor.
 */
public class StripChartSensor extends JFrame {

    final TimeSeries seriesTemp = new TimeSeries("temperature", Second.class);
    final TimeSeries seriesHum = new TimeSeries("humidity", Second.class);
    final ChartPanel chartPanel;
    

	/**
	 * Constructor, creates the chart.
	 * @param title
	 */
    public StripChartSensor(final String title) {

        super(title);

        this.setSize(new java.awt.Dimension(500, 270));
        
        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset, title);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

        seriesTemp.setMaximumItemCount(60);
        seriesHum.setMaximumItemCount(60);

    }
    
    /**
     * Adds values to the charts and updates it.
     * @param values
     */
    public void chartAddValues(String[] values) {
    	Second now = new Second();
    	seriesTemp.add(now, Float.parseFloat(values[1]));
    	seriesHum.add(now, Float.parseFloat(values[3]));
    	chartPanel.validate();
    }
    
    
    /**
     * initializes the data sets
     * @return
     */
    private XYDataset createDataset() {

        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(seriesTemp);
        dataset.addSeries(seriesHum);
                
        return dataset;
        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * @param title
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset, String title) {
        
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
            title,      // chart title
            "Time",                      // x axis label
            "Degree / %",                      // y axis label
            dataset,                  // data
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYItemRenderer renderer = plot.getRenderer();
        if (renderer instanceof XYLineAndShapeRenderer) {
            final XYLineAndShapeRenderer rr = (XYLineAndShapeRenderer) renderer;
            rr.setShapesFilled(true);
            rr.setSeriesStroke(0, new BasicStroke(2.0f));
            rr.setSeriesStroke(1, new BasicStroke(2.0f));
            java.awt.geom.Ellipse2D.Double shape = new java.awt.geom.Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0);
            rr.setSeriesShape(0, shape);
            rr.setSeriesShape(1, shape);
            rr.setSeriesShapesVisible(0, true);
            rr.setSeriesShapesVisible(1, true);
        }
        
        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss"));
        
        return chart;
        
    }

}