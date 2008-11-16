package cpBluetooth;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.lcdui.Form;

public class CPBluetooth {

	/**
	 * @author Kjell Jorgen Hole
	 * @param f
	 */
	public static void informacoesBlueTooth(Form f) {
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
	  }
}
