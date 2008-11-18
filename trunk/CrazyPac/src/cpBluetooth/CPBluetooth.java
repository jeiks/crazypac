package cpBluetooth;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import crazypac.CrazyPac;

public class CPBluetooth {

	private List deviceList;
	private DiscoveryAgent agent;
	private CrazyPac pai;
	
	public CPBluetooth(CrazyPac pai) {
		this.pai = pai;
	}
	
	/**
	 * @author Kjell Jorgen Hole
	 * @param f
	 * @throws MIDletStateChangeException 
	 */
	public void informacoesBlueTooth(Form f) throws MIDletStateChangeException {
		  LocalDevice local = null;
		  try {
		    local = LocalDevice.getLocalDevice();
		  } catch (BluetoothStateException e) {
		    f.append("Failed to retrieve the local device ("+e.getMessage()+")");
		    return;
		  }
		  f.append("Address: "+local.getBluetoothAddress()+"\n");
		  String name = local.getFriendlyName();
		  if (name == null)
		    f.append("Name: Failed to Retrieve");
		  else f.append("Name: " + name);
		  f.append("\n");
		  int mode = local.getDiscoverable();
		  StringBuffer text = new StringBuffer("Discoverable Mode: ");
		  switch (mode) {
		    case DiscoveryAgent.NOT_DISCOVERABLE:
		      text.append("Not Discoverable"); break;
		    case DiscoveryAgent.GIAC:
		      text.append("General"); break;
		    case DiscoveryAgent.LIAC:
		      text.append("Limited"); break;
		    default:
		      text.append("0x");
		      text.append(Integer.toString(mode,16)); break;
		  }
		  f.append(text.toString() + "\n");
		  f.append("API Version:"+
				    LocalDevice.getProperty("bluetooth.api.version")+"\n");
				  f.append("Master Switch Supported:" +
				    LocalDevice.getProperty("bluetooth.master.switch")+"\n");
				  f.append("Max Attributes:" + LocalDevice.getProperty(
				    "bluetooth.sd.attr.retrievable.max")+"\n");
				  f.append("Max Connected Devices:" +
				    LocalDevice.getProperty("bluetooth.connected.devices.max")+"\n");
				  f.append("Max Receive MTU:" +
				    LocalDevice.getProperty("bluetooth.l2cap.receiveMTU.max")+"\n");
		  //aki
		  addDevices();
		  try {
			  agent.startInquiry(DiscoveryAgent.GIAC, (DiscoveryListener) this.pai);
		  } catch (BluetoothStateException e) {
			  throw new MIDletStateChangeException("Unable to start the inquiry");
		  }
	  }

	  private void addDevices() {
		    RemoteDevice[] list = agent.retrieveDevices(DiscoveryAgent.PREKNOWN);
		    if (list != null) {
		      for (int i=0; i<list.length; i++) {
		        String address = list[i].getBluetoothAddress();
		        deviceList.insert(0,address+"-P",null); } // at start of list
		    }
		    list = agent.retrieveDevices(DiscoveryAgent.CACHED);
		    if (list != null) {
		      for (int i=0; i<list.length; i++) {
		        String address = list[i].getBluetoothAddress();
		        deviceList.insert(0,address+"-C",null); }
		    }
	  }

	
	public static DiscoveryAgent getLocalDiscoveryAgent() {
		try {
		    // getLocalDevice may throw an exception
		    LocalDevice local = LocalDevice.getLocalDevice();
		    DiscoveryAgent agent = local.getDiscoveryAgent();
		    return agent;
		} catch (BluetoothStateException e) { return null; }
	}
	
	public void deviceDiscovered(RemoteDevice device,DeviceClass cod) {
		  String address = device.getBluetoothAddress();
		  deviceList.insert(0, address + "-I", null);
	}
	
	public void servicesDiscovered(int transID,ServiceRecord[] record) {
	}
	
	public void serviceSearchCompleted(int transID, int type) {
	}
	
	public void inquiryCompleted(int type) {
		  Alert dialog = null;
		    // Determine if an error occurred. If one did occur display
		    // an Alert before allowing the application to exit
		  if (type != DiscoveryListener.INQUIRY_COMPLETED){
		    dialog = new Alert("Bluetooth Error",
		    "The inquiry failed to complete normally",
		    null, AlertType.ERROR);
		  } else {
		    dialog = new Alert("Inquiry Completed",
		    "The inquiry completed normally", null,AlertType.INFO);
		  }
		  dialog.setTimeout(Alert.FOREVER);
		  Display.getDisplay(this.pai).setCurrent(dialog);
	}


}
