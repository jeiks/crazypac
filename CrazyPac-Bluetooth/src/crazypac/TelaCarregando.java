package crazypac;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Ticker;

public class TelaCarregando extends Form {
	
	public TelaCarregando(String title, String text, Image image) {
		super(title);

		if (image != null) {
		    this.append(new ImageItem(null, image, ImageItem.LAYOUT_CENTER, title));
		}
		
		this.setTicker(new Ticker(title));
		this.append("\n" + text);
	}

}
