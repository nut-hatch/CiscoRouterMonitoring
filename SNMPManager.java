import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.plaf.SliderUI;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPManager {
	
	SNMPClient client;
	String[][] cabinets;
	
	public SNMPManager() {
		// Region-Name, Community, Switch1 Cisco2960 New, Switch2 Cisco3560 Old, Raritan, Router, Sensor
		String[] france = {"France", "perccom", "33.99.0.211", "33.99.0.212", "33.99.0.102", "33.99.0.254", "33.20.0.1"};
		String[] sweden = {"Sweden", "perccom", "22.99.0.221", "22.99.0.222", "22.99.0.102", "22.99.0.254", "33.20.0.1"};
		String[] russia = {"Russia", "perccom", "44.99.0.221", "44.99.0.222", "44.99.0.102", "44.99.0.254", "33.20.0.1"};
		String[] finland = {"Finland", "perccom", "11.99.0.211", "11.99.0.212", "11.99.0.102", "11.99.0.254", "33.20.0.1"};
		String[][] mycabinets = {france, sweden, russia, finland};
		this.cabinets = mycabinets;
	}
	
	/**
	 * Gets the temperature and humidity from the sensor and calculates the comfort.
	 * @return
	 * @throws Exception
	 */
	public String[][] getSensorDataOfCabinets() throws Exception {
		
		String[][] cabinetTableData = new String[4][7];
		
		PMVCalculator comfortcalculator = new PMVCalculator();
		DecimalFormat df = new DecimalFormat("#0.00");
		double pmv = comfortcalculator.calculatePmv(19, 40);
		double ppd = comfortcalculator.calculatePpd(pmv);
		
		String strComfort = "";
		if ((pmv > -0.5) && (pmv < 0.5)) {
			strComfort = "Good";
		} else if ((pmv > -1) && (pmv < 1)) {
			strComfort = "Neutral";
		} else {
			strComfort = "Bad";
		}
		
		String[][] tmp = {{"France", "19", "C", "40", df.format(pmv), df.format(ppd), strComfort},{"Sweden", "21", "F", "51", df.format(pmv), df.format(ppd), strComfort},{"Russia", "22", "C", "52", df.format(pmv), df.format(ppd), strComfort},{"Finland", "23", "C", "53", df.format(pmv), df.format(ppd), strComfort}};
		return tmp;
	
//		for (int i = 0; i < cabinets.length; i++) {
//			client = new SNMPClient("udp:"+cabinets[i][6]+"/161", cabinets[i][1]);
//			
//			int temperature = this.getTemperature();
//			int temperatureUnit = this.getTemperatureUnit();
//			int humidity = this.getHumidity();
//			
//			int celsiusTemp = temperature;
//			if (temperatureUnit == 0) {
//				celsiusTemp = 9*temperature/5 + 32;
//			}
//
//			double pmv = comfortcalculator.calculatePmv(celsiusTemp, humidity);
//			double ppd = comfortcalculator.calculatePpd(pmv);
//			
//			String strComfort = "";
//			if ((pmv > -0.5) && (pmv < 0.5)) {
//				strComfort = "Good";
//			} else if ((pmv > -1) && (pmv < 1)) {
//				strComfort = "Neutral";
//			} else {
//				strComfort = "Bad";
//			}
//			
//			String strTemperatureUnit = "";
//			if (temperatureUnit == 0) {
//				strTemperatureUnit = "F";
//			} else if (temperatureUnit == 1) {
//				strTemperatureUnit = "C";
//			}
//			
//			String strTemperature = Integer.toString(temperature);
//			String strHumidity = Integer.toString(humidity);
//			DecimalFormat df = new DecimalFormat("#0.00");
//			String strPmv = df.format(pmv);
//			String strPpd = df.format(ppd);
//			
//			String[] row = {cabinets[i][0], strTemperature, strTemperatureUnit, strHumidity, strPmv, strPpd, strComfort};
//			
//			cabinetTableData[i] = row;
//		}
//		return cabinetTableData;
		
	}
	
	/**
	 * Gets the total energy usage of a switch.
	 * -Yet fixed for France.
	 * @param switchname
	 * @return
	 */
	public String getSwitchTotalEnergyUsage(String switchname) {
		return "44000";
//		if (switchname == "Cisco2960") {
//			this.client = new SNMPClient("udp:"+cabinets[0][2]+"/161", cabinets[0][1]);
//		} else if (switchname == "Cisco3560") {
//			this.client = new SNMPClient("udp:"+cabinets[0][3]+"/161", cabinets[0][1]);
//		} else {
//			return "???";
//		}
//		
//		String totalSwitchEnergyUsage = null;
//		try {
//			totalSwitchEnergyUsage = this.calculateTotalSwitchUsage();
//		} catch (Exception e) {
//			totalSwitchEnergyUsage = "???";
//		}
//		return totalSwitchEnergyUsage;
	}
	
	/**
	 * Gets the energy usage of the ports of a switch.
	 * -Yet fixed for France.
	 * @param switchname
	 * @return
	 */
	public String[] getSwitchPortsEnergyUsage(String switchname) {
		String[] portsEnergyUsage = {"1","1","1","1","5","1","1","1","1","10","1","1","1","1","15","1","1","1","1","20","1","1","1","24",};
		return portsEnergyUsage;
		
//		String[] portsEnergyUsage = new String[24];
//		if (switchname == "Cisco2960") {
//			this.client = new SNMPClient("udp:"+cabinets[0][2]+"/161", cabinets[0][1]);
//			try {
//				portsEnergyUsage = this.calculatePortsEnergyUsage(10);
//			} catch (Exception e) {
//				portsEnergyUsage[0] = "???";
//			}
//		} else if (switchname == "Cisco3560") {
//			this.client = new SNMPClient("udp:"+cabinets[0][3]+"/161", cabinets[0][1]);
//			try {
//				portsEnergyUsage = this.calculatePortsEnergyUsage(11);
//			} catch (Exception e) {
//				portsEnergyUsage[0] = "???";
//			}
//		} else {
//			portsEnergyUsage[0] = "???";
//		}
//		return portsEnergyUsage;
	}
	
	/**
	 * Gets the current traffic at the ports of a switch.
	 * @param switchname
	 * @return
	 */
	public String[] getSwitchPortsTraffic(String switchname) {
		String[] portsEnergyUsage = {"1","1","1","1","5","1","1","1","1","10","1","1","1","1","15","1","1","1","1","20","1","1","1","24",};;
		return portsEnergyUsage;
		
//		String[] portsEnergyUsage = new String[24];
//		if (switchname == "Cisco2960") {
//			this.client = new SNMPClient("udp:"+cabinets[0][2]+"/161", cabinets[0][1]);
//		} else if (switchname == "Cisco3560") {
//			this.client = new SNMPClient("udp:"+cabinets[0][3]+"/161", cabinets[0][1]);
//		} else {
//			portsEnergyUsage[0] = "???";
//			return portsEnergyUsage;
//		}
//		
//		try {
//			portsEnergyUsage = this.calculateCurrentPortTraffic();
//		} catch (Exception e) {
//			portsEnergyUsage[0] = "???";
//		}
//		
//		return portsEnergyUsage;
	}
	
	/**
	 * Prints Sensor Data on console.
	 * @param temperature
	 * @param temperatureUnitRaw
	 * @param humidity
	 */
	private void printSensorData(int temperature, int temperatureUnitRaw, int humidity) {
		String temperatureUnit = "";
		if (temperatureUnitRaw == 0) {
			temperatureUnit = "Fahrenheit";
		} else if (temperatureUnitRaw == 1) {
			temperatureUnit = "Celsius";
		}
		System.out.println("Temperature: " + temperature + "� "+temperatureUnit);
		System.out.println("Humidity: " + humidity + "%");
	}
	
	/**
	 * Prints Switch Data on Console.
	 * @param switchName
	 * @param totalSwitchEnergyUsage
	 * @param switchPortEnergyUsage
	 * @param switchPortTraffic
	 */
	private void printSwitchData(String switchName, int totalSwitchEnergyUsage, int[] switchPortEnergyUsage, float[] switchPortTraffic) {
		System.out.println(switchName+"\tEnergy\tTraffic");
		System.out.println("Total:\t\t"+totalSwitchEnergyUsage+"W");
		for (int i=0; i<=23; i++) {
			System.out.println("Port "+i+1+":\t"+switchPortEnergyUsage[i]+"W\t"+switchPortTraffic[i]);
		}
	}

	/**
	 * Get emperature from SensorProbe
	 * @return
	 * @throws Exception
	 */
	private int getTemperature() throws Exception {
		int temperatureUnitRaw = client.getAsInt(new OID(".1.3.6.1.4.1.3854.1.2.2.1.16.1.3.0"));
		return temperatureUnitRaw;
	}

	/**
	 * Get temperature unit from SensorProbe
	 * @return
	 * @throws Exception
	 */
	private int getTemperatureUnit() throws Exception {
		int temperatureUnitRaw = client.getAsInt(new OID(".1.3.6.1.4.1.3854.1.2.2.1.16.1.12.0"));
		return temperatureUnitRaw;
	}
	
	/**
	 * Get Humidity from SensorProbe
	 * @return
	 * @throws Exception
	 */
	private int getHumidity() throws Exception {
		int humidity = client.getAsInt(new OID(".1.3.6.1.4.1.3854.1.2.2.1.17.1.3.0"));
		return humidity;
	}
	
	/**
	 * Calculates total switch energy usage
	 * @return
	 * @throws Exception
	 */
	private String calculateTotalSwitchUsage() throws Exception {
		return client.getAsString(new OID(".1.3.6.1.4.1.9.9.683.1.25.0"));
	}
	
	/**
	 * Calculates energy usage of switch ports
	 * @param startPort the OID may differ from Switch to Switch, so the number of the first port's OID has to be specified.
	 * @return
	 * @throws Exception
	 */
	protected String[] calculatePortsEnergyUsage(int startPort) throws Exception {
		String[] portsEnergyUsage = new String[24];
		for (int j=startPort; j<=startPort+23; j++) {
			portsEnergyUsage[j-startPort] = client.getAsString(new OID(".1.3.6.1.4.1.9.9.683.1.6.1.8.10"+j));
		}
		return portsEnergyUsage;
	}
	
	/**
	 * Gets the total traffic of ports.
	 * @return
	 * @throws Exception
	 */
	private int[] getTotalPortsTraffic() throws Exception {
		int[] portsTotalTraffic = new int[30];
			for (int i=101;i<=128;i++) {
				int portOutputOctets = client.getAsInt(new OID(".1.3.6.1.2.1.2.2.1.16.10"+i));
				int portInputOctets = client.getAsInt(new OID(".1.3.6.1.2.1.2.2.1.10.10"+i));
				int totalTraffic = (portInputOctets + portOutputOctets) * 8; 
				int p = i-100;

				portsTotalTraffic[p] = totalTraffic;
				//System.out.print("Total traffic on port " +p+  " is: ");
				//System.out.println(totalTraffic + "b/s");
			}
		return portsTotalTraffic;
			
	}
	
	/**
	 * Calculates current traffic of ports.
	 * @return
	 * @throws Exception
	 */
	private String[] calculateCurrentPortTraffic() throws Exception {
		int[] initPortsTotalTraffic = this.getTotalPortsTraffic();
		int sleepTime = (5000);
		try {
			Thread.sleep(sleepTime);
		} catch (Exception e) {
			e.toString();
		}
		
		int[] endPortsTotalTraffic = this.getTotalPortsTraffic();
		
		String[] currentPortTraffic = new String[24];
		for (int i=1;i<=24;i++) {
			currentPortTraffic[i-1] = Float.toString((endPortsTotalTraffic[i] - initPortsTotalTraffic[i]) / sleepTime) ;
		}
		return currentPortTraffic;
	}
}
