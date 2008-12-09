package crazypac;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import net.java.dev.marge.entity.Device;

/**
 * Classe principal do sistema responsável pelas chamadas do CrazyPac e pelo gerenciamento do Display que é mostrado na tela do celular Em sua inicialização, apresenta na tela a instância da classe TelaInicial
 * @author  Jacson RC Silva
 */
public class CrazyPac extends MIDlet {

	/**
	 * Padrão de Projeto Singleton
	 * Instância do objeto
	 */
	private static CrazyPac instance;
	/**
	 * @uml.property  name="display"
	 */
	private Display display;
	/**
	 * @uml.property  name="device"
	 */
	private Device device;
	
	/**
	 * Divergindo do Padrão, devido a necessidade
	 * de declarar a classe Contrutora como pública
	 */
	public CrazyPac() {
		instance = this;
		display = Display.getDisplay(this);
	}
	
	/**
	 * define o dispositivo
	 * @param device  o dispositivo a configurar
	 * @uml.property  name="device"
	 */
	public void setDevice(Device device) {
		this.device = device;
	}
	
	/**
	 * retorna o dispositivo
	 * @return  o dispositivo
	 * @uml.property  name="device"
	 */
	public Device getDevice() {
		return device;
	}
	
	/**
	 * retorna o display
	 * @return  the display
	 * @uml.property  name="display"
	 */
	public Display getDisplay() {
		return display;
	}	
	
	/**
	 * finaliza o programa
	 */
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		System.gc();
		notifyDestroyed();
	}

	/**
	 * definido para pausa
	 * OBS: não utilizado no trabalho
	 */
	protected void pauseApp() {	}

	/**
	 * Início da aplicação.
	 * Definição da instância da classe TelaInicial como
	 * tela do Jogo
	 */
	protected void startApp() throws MIDletStateChangeException {
		display.setCurrent(TelaInicial.getInstance());
	}

	/**
	 * retorna a instância do projeto
	 * @return  retorna a instância
	 * @uml.property  name="instance"
	 */
	public static CrazyPac getInstance() {
		return instance;
	}
	
	/**
	 * define o display atual da tela do celular
	 * @param d
	 */
	public void setCurrent(Displayable d) {
		display.setCurrent(d);
	}
}
