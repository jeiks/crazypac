package crazypac;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import net.java.dev.marge.entity.Device;

public class CrazyPac extends MIDlet {

	
	private static CrazyPac instance;
	private Display display;
	private Device device;
	
	public CrazyPac() {
		instance = this;
		display = Display.getDisplay(this);
	}
	
	public void setDevice(Device device) {
		this.device = device;
	}
	
	public Device getDevice() {
		return device;
	}
	
	public Display getDisplay() {
		return display;
	}	
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		System.gc();
		notifyDestroyed();
	}

	protected void pauseApp() {	}

	protected void startApp() throws MIDletStateChangeException {
		display.setCurrent(TelaInicial.getInstance());
	}

	public static CrazyPac getInstance() {
		return instance;
	}
	
	public void setCurrent(Displayable d) {
		display.setCurrent(d);
	}
}
