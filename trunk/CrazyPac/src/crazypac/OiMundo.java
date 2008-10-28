package crazypac;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class OiMundo extends MIDlet implements CommandListener{

	private Form mMainForm;
	private Command exit,ok;
	
	/**
	 * Método para o Oi do MIDlet
	 *
	 */
	public void HelloMIDlet() {
		mMainForm = new Form("MIDlet Oi");
		mMainForm.append(new StringItem(null,"Oi, ta programando!"));
		exit = new Command("Sair", Command.EXIT, 0);
		ok = new Command("OK", Command.OK, 1);
		mMainForm.addCommand(exit);
		mMainForm.addCommand(ok);
		mMainForm.setCommandListener(this);
	}
	
	public OiMundo() {
		HelloMIDlet();
	}
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub
		
	}

	protected void startApp() throws MIDletStateChangeException {
		Display.getDisplay(this).setCurrent(mMainForm);
	}

	public void commandAction(Command c, Displayable s) {
		if (c == exit) {
			notifyDestroyed();
		}else if (c == ok) {
			mMainForm.append(new StringItem(null,"sei mesmo"));
		}
	}

}
