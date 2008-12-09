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

/**
 * Classe que demonstra a lista de dispositivos bluetooth
 * encontrados na pesquisa do cliente
 * 
 * adaptado por Jacson RC Silva da classe encontrada nos
 * exemplos do Projeto Marge
 */
public class DevicesList extends List
implements CommandListener, ServiceSearchListener {

    private Command comandoSelecionar;
    private Command comandoVoltar;
    private Vector remoteDevices;
	
    /**
     * Construtor padr�o que define os comandos
     * da interface da lista de dispositivos
     */
	public DevicesList() {
		super("Encontrar dispositivos", List.IMPLICIT);
	    comandoSelecionar = new Command("Selecionar", Command.SCREEN, 1);
	    comandoVoltar = new Command("Voltar", Command.BACK, 1);
	    remoteDevices = new Vector();
        addCommand(comandoSelecionar);
        addCommand(comandoVoltar);
        this.setCommandListener(this);
	}

	/**
	 * m�todo para adicionar um dispositivo � lista
	 * @param remoteDevice
	 */
    public void addRemoteDevice(RemoteDevice remoteDevice) {
        remoteDevices.addElement(remoteDevice);
        try {
            super.append(remoteDevice.getFriendlyName(false), null);
        } catch (IOException e) {
            super.append(remoteDevice.getBluetoothAddress(), null);
        }
    }
	
    /**
     * m�todo que recebe os comandos do usu�rio
     */
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

	/**
	 * m�todo de debug
	 * utilizado ao n�o encontrar um dispositivo
	 */
	public void deviceNotReachable() {
		System.out.println("deviceNotReachable");
	}

	/**
	 * m�todo chamado quando completa-se a pesquisa
	 * por dispositivos
	 */
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

	/**
	 * m�todo de debug para erro de pesquisa
	 * de dispositivos
	 */
	public void serviceSearchError() {
		System.out.println("serviceSearchError");
	}
	
}
