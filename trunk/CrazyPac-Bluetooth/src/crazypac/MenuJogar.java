package crazypac;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import net.java.dev.marge.communication.ConnectionListener;
import net.java.dev.marge.entity.ServerDevice;
import net.java.dev.marge.entity.config.ServerConfiguration;
import net.java.dev.marge.factory.RFCOMMCommunicationFactory;
import net.java.dev.marge.inquiry.DeviceDiscoverer;
import net.java.dev.marge.inquiry.InquiryListener;

/**
 * Classe que define o menu de jogar, com as opções do usuário iniciar um servidor, ou cliente, ou voltar a tela inicial
 * @author  Jacson RC Silva
 */
public class MenuJogar extends List 
implements CommandListener, InquiryListener, ConnectionListener {

    private Command comandoSair;
    /**
     * Lista de dispositivos encontrados
     * em uma busca bluetooth
     */
    private DevicesList deviceList;

    /**
     * Padrão de projeto Singleton
     * Instância do Jogo
     */
	private static MenuJogar instance;
	
	/**
	 * Contrutor responsável por adicionar as opções
	 * de comandos na tela do Listener
	 */
	private MenuJogar() {
		super("CrazyPac", List.IMPLICIT);
		comandoSair = new Command("Sair", Command.EXIT, 0);
		addCommand(comandoSair);
		addCommand(new Command("OK", Command.OK, 1));
		
		this.append("Cliente", null);
        this.append("Servidor", null);
        this.append("Tela Inicial", null);

        this.setCommandListener(this);
        this.deviceList = new DevicesList();
	}

	/**
	 * @return  a instância do MenuJogar
	 * @uml.property  name="instance"
	 */
	public synchronized static MenuJogar getInstance() {
		if ( instance == null )
			newInstance();
			//instance = new MenuJogar();
		return instance;
	}
	
	/**
	 * cria uma nova instancia para o MenuJogar
	 */
	public synchronized static void newInstance() {
		instance = new MenuJogar();
	}

	/**
	 * recebe os comandos do usuário
	 */
	public void commandAction(Command cmd, Displayable arg1) {
        if (cmd == comandoSair) {
            CrazyPac.getInstance().notifyDestroyed();
        } else {
        	//tela inicial
            if (getSelectedIndex() == 2) {
                CrazyPac.getInstance().setCurrent(TelaInicial.getInstance());
            } else
            	//cliente
            	if (getSelectedIndex() == 0) {
            		Jogo.getInstance().isServer(false);
	                try {
	                    DeviceDiscoverer.getInstance().startInquiryGIAC(this);
	                    CrazyPac.getInstance().setCurrent(this.deviceList);
	                } catch (BluetoothStateException e) {
	                    e.printStackTrace();
	                }
            } else
            	//servidor
            	{
            	Jogo.getInstance().isServer(true);
                RFCOMMCommunicationFactory factory = new RFCOMMCommunicationFactory();
                ServerConfiguration serverConfiguration = new ServerConfiguration(Jogo.getInstance());
                factory.waitClients(serverConfiguration, this);
                try {
                    CrazyPac.getInstance().setCurrent(
                    		new TelaCarregando("Servidor", 
                    		                   "Iniciando... " + 
                    		                   LocalDevice.getLocalDevice().getBluetoothAddress(), 
                    		                   null));
                } catch (BluetoothStateException ex) {
                	CrazyPac.getInstance().setCurrent(
                			new TelaCarregando("Servidor",
                							   "Starting...", 
                							   null));
                }
            }
        }
	}

	/**
	 * recebe os dispositivos encontrados
	 */
	public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass arg1) {
		this.deviceList.addRemoteDevice(remoteDevice);
	}

	/**
	 * recebe a mensagem de inquiry completed
	 */
	public void inquiryCompleted(RemoteDevice[] arg0) {
		System.out.println("inquiryCompleted");
	}

	/**
	 * recebe o erro do inquiry
	 */
	public void inquiryError() {
		System.out.println("inquiryError");
	}

	/**
	 * recebe o dispositivo com o qual a conexão foi
	 * estabelecida
	 */
	public void connectionEstablished(ServerDevice serverDevice, RemoteDevice arg1) {
		CrazyPac.getInstance().setDevice(serverDevice);
        Jogo.getInstance().inicia();
        serverDevice.startListening();
	}

	/**
	 * recebe a mensagem de erro da conexão
	 */
	public void errorOnConnection(IOException arg0) {
		System.out.println("ErrorOnConnection: "+arg0.getMessage());
	}

	
}
