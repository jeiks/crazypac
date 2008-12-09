package crazypac;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Ticker;

/**
 * Classe para a criação de uma tela carregando
 * de forma mais fácil
 * 
 * @author Jacson RC Silva
 * baseada em um exemplo do Projeto Marge
 */
public class TelaCarregando extends Form {
	
	/**
	 * Construtor padrão
	 * @param title: título da tela
	 * @param text: texto da tela
	 * @param image: imagem da tela
	 */
	public TelaCarregando(String title, String text, Image image) {
		super(title);

		if (image != null) {
		    this.append(new ImageItem(null, image, ImageItem.LAYOUT_CENTER, title));
		}
		
		this.setTicker(new Ticker(title));
		this.append("\n" + text);
	}

}
