import java.io.IOException;

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

/**
 * Class provides a client to send SNMP requests.
 */
public class SNMPClient {
	Snmp snmp = null;
	String address = null;
	String community = null;

	/**
	 * Constructor. The IP address of the SNMP device and the SNMP community have to be specified.
	 * @param add
	 * @param comm
	 */
	public SNMPClient(String add, String comm) {
		address = add;
		community = comm;
		try {
			this.start();
		} catch (Exception e) {
			System.out.print(e);
		}
	}
	
	/**
	 * Start the Snmp session. If you forget the listen() method you will not
	 * get any answers because the communication is asynchronous and the
	 * listen() method listens for answers.
	 * 
	 * @throws IOException
	 */
	private void start() throws IOException {
		TransportMapping transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		transport.listen();
	}

	/**
	 * Method which takes a single OID and returns the response from the agent
	 * as a String.
	 * 
	 * @param oid
	 * @return
	 * @throws IOException
	 */
	public String getAsString(OID oid) throws IOException {
		ResponseEvent event = get(oid);
		return event.getResponse().get(0).getVariable().toString();
	}
	
	/**
	 * Method which takes a single OID and returns the response from the agent
	 * as an Int.
	 * 
	 * @param oid
	 * @return
	 * @throws IOException
	 */
	public int getAsInt(OID oid) throws IOException {
		ResponseEvent event = get(oid);
		return event.getResponse().get(0).getVariable().toInt();
	}
	
	/**
	 * This method is capable of handling multiple OIDs
	 * 
	 * @param oids
	 * @return
	 * @throws IOException
	 */
	public ResponseEvent get(OID oids) throws IOException {
		PDU pdu = new PDU();

		pdu.add(new VariableBinding(oids));

		pdu.setType(PDU.GET);
		ResponseEvent event = snmp.send(pdu, getTarget(), null);
		if (event != null) {
			return event;
		}
		throw new RuntimeException("GET timed out");
	}

	/**
	 * This method returns a Target, which contains information about where the
	 * data should be fetched and how.
	 * 
	 * @return
	 */
	private Target getTarget() {
		Address targetAddress = GenericAddress.parse(address);
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(targetAddress);
		target.setRetries(2);
		target.setTimeout(3000);
		target.setVersion(SnmpConstants.version2c);
		return target;
	}
}
