
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Class that creates the GUI of the SNMP Monitoring application.
 *
 */
public class AppGui {

	/**
	 * The main frame.
	 */
    JFrame guiFrame;
    
    /**
     * The tabpane.
     */
	JTabbedPane tabbedPane = new JTabbedPane();
	
	/**
	 * column headings of the table for temperature and humidity
	 */
    Object columnNamesTempHum[] = {"Country", "Temperature", "Humidity", "PMV", "PPD", "Comfort", "Graph"};
    
    /**
     * Table for temperature and humidity
     */
    JTable tableTempHum;
    
    /**
     * Column headings for table of cisco2960 switch
     */
    Object columnNamesCisco2960[] = {"Port", "Energy", "Traffic"};
    
    /**
     * Table for cisco2960 switch
     */
    JTable tableCisco2960;
    
    /**
     * Column headings for table of cisco2560 switch
     */
    Object columnNamesCisco3560[] = {"Port", "Energy", "Traffic"};

    /**
     * Table for cisco2560 switch
     */
    JTable tableCisco3560;
    //Charts for France, Sweden, Russia, Finland
    StripChartSensor[] tmpHumCharts = new StripChartSensor[4];
    
    /**
     * Constructor, initializes Tables and Charts
     */
    public AppGui() {
    	tmpHumCharts[0] = new StripChartSensor("Temperature and Humidity FRANCE");
    	tmpHumCharts[1] = new StripChartSensor("Temperature and Humidity SWEDEN");
    	tmpHumCharts[2] = new StripChartSensor("Temperature and Humidity RUSSIA");
    	tmpHumCharts[3] = new StripChartSensor("Temperature and Humidity FINLAND");
		
        guiFrame = new JFrame();
        guiFrame.setLayout(new FlowLayout());
        
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("SNMP Monitoring");
        guiFrame.setSize(700,500);
      
        //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
        
        this.addTemperatureHumidityTable();
//        this.addCabinetTables();
        
        guiFrame.setVisible(true);
    }
    
    /**
     * Updates temperature, humidity, comfort
     * @param tableData
     */
    public void updateTempHumTable(String[][] data) {
		tmpHumCharts[0].chartAddValues(data[0]);
		tmpHumCharts[1].chartAddValues(data[1]);
		tmpHumCharts[2].chartAddValues(data[2]);
		tmpHumCharts[3].chartAddValues(data[3]);
		
    	String[][] tableData = new String[4][7];

    	for (int i = 0; i < data.length; i++) {
			tableData[i][0] = data[i][0];
			tableData[i][1] = data[i][1]+"°"+data[i][2];
			tableData[i][2] = data[i][3]+"%";
			tableData[i][3] = data[i][4];
			tableData[i][4] = data[i][5];
			tableData[i][5] = data[i][6];
			tableData[i][6] = "Graph";
		}
    	DefaultTableModel model = new DefaultTableModel(tableData, columnNamesTempHum);
    	tableTempHum.setModel(model);
        new ButtonColumn(tableTempHum, mkShowAction(), 6);
    }
    
    /**
     * Creates the action for buttons in the temphumtable.
     * A click shows to graph of the corresponding line.
     * @return
     */
	private Action mkShowAction() {
		Action action = new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				try {
					tmpHumCharts[tableTempHum.getEditingRow()].setVisible(true);
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		};
		return action;
	}

    /**
     * Updates new switch energy usage table
     * @param totalEnergy
     * @param portsEnergy
     */
    public void updateCisco2960Table(String totalEnergy, String[] portsEnergy, String[] portsTraffic) {
    	String[][] tableData = this.prepareEnergyTableData(totalEnergy, portsEnergy, portsTraffic);
    	
    	DefaultTableModel model = new DefaultTableModel(tableData, columnNamesCisco2960);
    	tableCisco2960.setModel(model);
    }

    /**
     * Updates old switch energy usage table
     * @param totalEnergy
     * @param portsEnergy
     */
    public void updateCisco3560Table(String totalEnergy, String[] portsEnergy, String[] portsTraffic) {
    	String[][] tableData = this.prepareEnergyTableData(totalEnergy, portsEnergy, portsTraffic);
    	
    	DefaultTableModel model = new DefaultTableModel(tableData, columnNamesCisco3560);
    	tableCisco3560.setModel(model);
    }
    
    /**
     * Prepare the array for switch energy usage table
     * @param totalEnergy
     * @param portsEnergy
     * @return
     */
    private String[][] prepareEnergyTableData(String totalEnergy, String[] portsEnergy, String[] portsTraffic) {
    	String[][] tableData = new String[25][4];
    	tableData[0][0] = "Total";
    	tableData[0][1] = totalEnergy;
    	tableData[0][2] = "";
    	
    	for (int i = 0; i < portsEnergy.length; i++) {
			tableData[i+1][0] = "Port "+(i+1);
			tableData[i+1][1] = portsEnergy[i];
			tableData[i+1][2] = portsTraffic[i];
		}
    	
    	return tableData;
    }
    
    /**
     * Temp Humid Table
     */
    private void addTemperatureHumidityTable() {
    	
    	final JPanel tempHumPanel = new JPanel();
    	tempHumPanel.setLayout(new BoxLayout(tempHumPanel, BoxLayout.PAGE_AXIS));

    	final JPanel upperBox = new JPanel();
    	JLabel lblHead = new JLabel("Temperature and Humidity");
    	upperBox.add(lblHead);
    	tempHumPanel.add(upperBox);

    	final JPanel lowerBox = new JPanel();
        DefaultTableModel model = new DefaultTableModel(columnNamesTempHum, 0);
        
        tableTempHum = new JTable(model);
        tableTempHum.setPreferredSize(new Dimension(550, 63));
        tableTempHum.setPreferredScrollableViewportSize(tableTempHum.getPreferredSize());
        tableTempHum.setFillsViewportHeight(true);
//        tableTempHum.getColumn("Graph").setCellRenderer(new ButtonRenderer());
//        tableTempHum.getColumn("Graph").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(tableTempHum);
        lowerBox.add(scrollPane, BorderLayout.LINE_START);
    	tempHumPanel.add(lowerBox);
    	
    	tabbedPane.addTab("Air Quality Analysis", null, tempHumPanel);
        guiFrame.add(tabbedPane);
    }
    
    /**
     * Tables for one cabinet
     */
    private void addCabinetTables() {
    	final JPanel franceCabinetPanel = new JPanel();
    	this.addCisco2960Table(franceCabinetPanel);
    	this.addCisco3560Table(franceCabinetPanel);
    	tabbedPane.addTab("France Cabinet", null, franceCabinetPanel);
    }
    
    /**
     * Elements for new switch energy usage
     */
    private void addCisco2960Table(JPanel franceCabinetPanel) {
    	
    	final JPanel cisco2960Panel = new JPanel();
    	cisco2960Panel.setLayout(new BoxLayout(cisco2960Panel, BoxLayout.PAGE_AXIS));
    	
    	final JPanel upperBox = new JPanel();
    	JLabel lblFrance = new JLabel("France - new switch (cisco 2960)");
    	upperBox.add(lblFrance);
    	cisco2960Panel.add(upperBox);

    	final JPanel lowerBox = new JPanel();
        DefaultTableModel model = new DefaultTableModel(columnNamesCisco2960, 0);
        tableCisco2960 = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableCisco2960);
        lowerBox.add(scrollPane);
    	cisco2960Panel.add(lowerBox);
    	franceCabinetPanel.add(cisco2960Panel);


    	//guiFrame.add(cisco2960Panel, BorderLayout.LINE_START);
    }
    
    /**
     * Elements for old switch energy usage
     */
    private void addCisco3560Table(JPanel franceCabinetPanel) {
    	
    	final JPanel cisco3560Panel = new JPanel();
    	cisco3560Panel.setLayout(new BoxLayout(cisco3560Panel, BoxLayout.PAGE_AXIS));

    	final JPanel upperBox = new JPanel();
    	JLabel lblFrance = new JLabel("France - old switch (cisco 3560)");
    	upperBox.add(lblFrance);
    	cisco3560Panel.add(upperBox);

    	final JPanel lowerBox = new JPanel();
        DefaultTableModel model = new DefaultTableModel(columnNamesCisco3560, 0);
        tableCisco3560 = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableCisco3560);
        lowerBox.add(scrollPane);
        cisco3560Panel.add(lowerBox);
    	franceCabinetPanel.add(cisco3560Panel);
    }
    
}
