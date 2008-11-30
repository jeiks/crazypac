package crazypac;

import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class SpriteDir extends Sprite {

	private char dir;

	public SpriteDir(Image image) {
		super(image);
		setDir('R');
	}

	public SpriteDir(Image image, int width, int height) {
		super(image, width, height);
		setDir('R');
	}
	
	public SpriteDir(Sprite sprite) {
		super(sprite);
		setDir('R');
	}
	
	public SpriteDir(Image image, char dir) {
		super(image);
		setDir(dir);
	}

	public SpriteDir(Image image, int width, int height, char dir) {
		super(image, width, height);
		setDir(dir);
	}
	
	public SpriteDir(Sprite sprite, char dir) {
		super(sprite);
		setDir(dir);
	}

	/**
	 * Obtém a imagem do arquivo, já realizando o
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
		if (this.getFrameSequenceLength() == 4)
			switch (this.dir) {
			case 'R':
				this.setFrame(0);
				break;
			case 'L':
				this.setFrame(1);
				break;
			case 'U':
				this.setFrame(2);
				break;
			case 'D':
				this.setFrame(3);
				break;
			}
	}
	
	public char getDir() {
		return dir;
	}
	
	public void setX(int X) {
		setPosition(X, getY());
	}
	
	public void setY(int Y) {
		setPosition(getX(), Y);
	}
}
