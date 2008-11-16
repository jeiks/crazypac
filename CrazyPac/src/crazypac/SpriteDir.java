package crazypac;

import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class SpriteDir extends Sprite {

	private char dir;

	public SpriteDir(Image image) {
		super(image);
		this.dir = 'R';
	}

	public SpriteDir(Image image, int width, int height) {
		super(image, width, height);
		this.dir = 'R';
	}
	
	public SpriteDir(Sprite sprite) {
		super(sprite);
		this.dir = 'R';
	}
	
	public SpriteDir(Image image, char dir) {
		super(image);
		this.dir = dir;
	}

	public SpriteDir(Image image, int width, int height, char dir) {
		super(image, width, height);
		this.dir = dir;
	}
	
	public SpriteDir(Sprite sprite, char dir) {
		super(sprite);
		this.dir = dir;
	}

	/**
	 * Obtém a imagem do arquivo já realizando o
	 * tratamento de erros
	 * @param fileName
	 * @return
	 */
	public static Image getImage(String fileName) {
		Image imagem = null;
		try {
			imagem = Image.createImage(fileName);
		} catch (IOException e) {e.printStackTrace();}
		return imagem;
	}
	
	public void setDir(char dir) {
		if (dir != 'R' && dir != 'L' && dir != 'U' && dir != 'D')
			this.dir = 'R';
		else this.dir = dir;
	}
	
	public char getDir() {
		return dir;
	}
}
