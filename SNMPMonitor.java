import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

/**
 * SNMP Monitor.
 * 
 * Application to monitor values of network devices via SNMP.
 * 
 * @author Melanie, Rajeshwari, Shola, Niklas
 *
 */
public class SNMPMonitor {

	/**
	 * Main function. Starts Gui and SNMP requests.
	 * @param args
	 */
	public static void main(String[] args) {
        
		AppGui gui = new AppGui();
		SNMPManager snmpManager = new SNMPManager();

        try {
			while (true) {
				String[][] sensorData = snmpManager.getSensorDataOfCabinets();
				gui.updateTempHumTable(sensorData);
				
				//gui.updateCisco2960Table(snmpManager.getSwitchTotalEnergyUsage("Cisco2960"), snmpManager.getSwitchPortsEnergyUsage("Cisco2960"), snmpManager.getSwitchPortsTraffic("Cisco2960"));
	
				//gui.updateCisco3560Table(snmpManager.getSwitchTotalEnergyUsage("Cisco3560"), snmpManager.getSwitchPortsEnergyUsage("Cisco3560"), snmpManager.getSwitchPortsTraffic("Cisco3560"));
				
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.out.print(e);
		}

	}

}
