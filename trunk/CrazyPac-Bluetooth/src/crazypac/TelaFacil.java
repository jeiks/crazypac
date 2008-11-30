package crazypac;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;

public class TelaFacil extends Form implements CommandListener {
	
	private Command comandoOk;
	
	public TelaFacil(String title, String text, Image image) {
		super(title);

		if (image != null) {
		    append(new ImageItem(null, image, ImageItem.LAYOUT_CENTER, title));
		}
		
		append(text);
		comandoOk = new Command("OK", Command.OK, 0);
		addCommand(comandoOk);
		setCommandListener(this);
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if (arg0 == comandoOk) {
			CrazyPac.getInstance().setCurrent(TelaInicial.getInstance());
		}
	}
}
