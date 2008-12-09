package crazypac;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 * Extends da classe Sprite para adicionar
 * a posição e definir novos meios de facilitar a construção
 * dos Sprites.
 * 
 * @author  Jacson RC Silva
 */
public class SpriteDir extends Sprite {

	/**
	 * direção do personagem
	 * @uml.property  name="dir"
	 */
	private char dir;

	/**
	 * recebe uma imagem para criar um Sprite
	 * define a posição como Direita
	 * @param image: imagem do Sprite
	 */
	public SpriteDir(Image image) {
		super(image);
		setDir('R');
	}

	/**
	 * recebe a imagem, e o tamanho do Sprite
	 * para a criação do Sprite
	 * define a posição como Direita
	 * @param image: imagem do Sprite
	 * @param width: largura do Sprite
	 * @param height: altura do Sprite
	 */
	public SpriteDir(Image image, int width, int height) {
		super(image, width, height);
		setDir('R');
	}
	
	/**
	 * recebe um Sprite e transforma-o em
	 * SpriteDir
	 * @param sprite: Sprite
	 */
	public SpriteDir(Sprite sprite) {
		super(sprite);
		setDir('R');
	}
	
	/**
	 * recebe uma imagem e um sprite, criando um Sprite com
	 * imagem e direção
	 * @param image: imagem do Sprite
	 * @param dir: direção do Sprite
	 */
	public SpriteDir(Image image, char dir) {
		super(image);
		setDir(dir);
	}

	/**
	 * Cria um Sprite
	 * @param image: imagem do Sprite
	 * @param width: largura do Sprite
	 * @param height: altura do Sprite
	 * @param dir: direçao do Sprite
	 */
	public SpriteDir(Image image, int width, int height, char dir) {
		super(image, width, height);
		setDir(dir);
	}
	
	/**
	 * Cria um SpriteDir
	 * @param sprite: Sprite
	 * @param dir: direção do Sprite
	 */
	public SpriteDir(Sprite sprite, char dir) {
		super(sprite);
		setDir(dir);
	}

	/**
	 * Obtêm a imagem do arquivo, já realizando o
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
	
	/**
	 * verifica se a posição é correta
	 * e movimenta o Sprite para a direção desejada
	 * quando possivel
	 * @param dir: direção a ser definida
	 * @uml.property  name="dir"
	 */
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
	
	/**
	 * direção do SpriteDir
	 * @return  a direção do Sprite
	 * @uml.property  name="dir"
	 */
	public char getDir() {
		return dir;
	}
	
	/**
	 * define a posição horizontal
	 * @param X
	 */
	public void setX(int X) {
		setPosition(X, getY());
	}
	
	/**
	 * define a posição vertical
	 * @param Y
	 */
	public void setY(int Y) {
		setPosition(getX(), Y);
	}
}
