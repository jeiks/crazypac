package crazypac;

import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import net.java.dev.marge.entity.ClientDevice;
import net.java.dev.marge.entity.config.ClientConfiguration;
import net.java.dev.marge.factory.RFCOMMCommunicationFactory;
import net.java.dev.marge.inquiry.DeviceDiscoverer;
import net.java.dev.marge.inquiry.ServiceDiscoverer;
import net.java.dev.marge.inquiry.ServiceSearchListener;

public class DevicesList extends List
implements CommandListener, ServiceSearchListener {

    private Command comandoSelecionar;
    private Command comandoVoltar;
    private Vector remoteDevices;
		
	public DevicesList() {
		super("Encontrar dispositivos", List.IMPLICIT);
	    comandoSelecionar = new Command("Selecionar", Command.SCREEN, 1);
	    comandoVoltar = new Command("Voltar", Command.BACK, 1);
	    remoteDevices = new Vector();
        addCommand(comandoSelecionar);
        addCommand(comandoVoltar);
        this.setCommandListener(this);
	}

    public void addRemoteDevice(RemoteDevice remoteDevice) {
        remoteDevices.addElement(remoteDevice);
        try {
            super.append(remoteDevice.getFriendlyName(false), null);
        } catch (IOException e) {
            super.append(remoteDevice.getBluetoothAddress(), null);
        }
    }
	
	public void commandAction(Command cmd, Displayable arg1) {
		if (cmd == comandoVoltar) {
			try {
				DeviceDiscoverer.getInstance().cancelInquiry();
			} catch (BluetoothStateException e) { e.printStackTrace(); }
			CrazyPac.getInstance().setCurrent(TelaInicial.getInstance());
		}else
		if (cmd == comandoSelecionar) {
			try {
				DeviceDiscoverer.getInstance().cancelInquiry();
			} catch (BluetoothStateException e) { e.printStackTrace(); }
			
            try {
            	int selectedIndex = this.getSelectedIndex();
				ServiceDiscoverer.getInstance().startSearch((RemoteDevice) remoteDevices.elementAt(selectedIndex), this);
			} catch (BluetoothStateException e) { e.printStackTrace(); }
		}
	}

	public void deviceNotReachable() {
		System.out.println("deviceNotReachable");
	}

	public void serviceSearchCompleted(RemoteDevice arg0, ServiceRecord[] arg1) {
		try {
            ClientConfiguration clientConfiguration = new ClientConfiguration(arg1[0], Jogo.getInstance());
            RFCOMMCommunicationFactory factory = new RFCOMMCommunicationFactory();
            ClientDevice clientDevice = factory.connectToServer(clientConfiguration);
            CrazyPac.getInstance().setDevice(clientDevice);
            Jogo.getInstance().inicia();
            clientDevice.startListening();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	public void serviceSearchError() {
		System.out.println("serviceSearchError");
	}
	
}
