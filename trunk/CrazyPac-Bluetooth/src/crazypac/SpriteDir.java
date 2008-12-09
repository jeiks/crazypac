package crazypac;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/**
 * Extends da classe Sprite para adicionar
 * a posi��o e definir novos meios de facilitar a constru��o
 * dos Sprites.
 * 
 * @author  Jacson RC Silva
 */
public class SpriteDir extends Sprite {

	/**
	 * dire��o do personagem
	 * @uml.property  name="dir"
	 */
	private char dir;

	/**
	 * recebe uma imagem para criar um Sprite
	 * define a posi��o como Direita
	 * @param image: imagem do Sprite
	 */
	public SpriteDir(Image image) {
		super(image);
		setDir('R');
	}

	/**
	 * recebe a imagem, e o tamanho do Sprite
	 * para a cria��o do Sprite
	 * define a posi��o como Direita
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
	 * imagem e dire��o
	 * @param image: imagem do Sprite
	 * @param dir: dire��o do Sprite
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
	 * @param dir: dire�ao do Sprite
	 */
	public SpriteDir(Image image, int width, int height, char dir) {
		super(image, width, height);
		setDir(dir);
	}
	
	/**
	 * Cria um SpriteDir
	 * @param sprite: Sprite
	 * @param dir: dire��o do Sprite
	 */
	public SpriteDir(Sprite sprite, char dir) {
		super(sprite);
		setDir(dir);
	}

	/**
	 * Obt�m a imagem do arquivo, j� realizando o
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
	 * verifica se a posi��o � correta
	 * e movimenta o Sprite para a dire��o desejada
	 * quando possivel
	 * @param dir: dire��o a ser definida
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
	 * dire��o do SpriteDir
	 * @return  a dire��o do Sprite
	 * @uml.property  name="dir"
	 */
	public char getDir() {
		return dir;
	}
	
	/**
	 * define a posi��o horizontal
	 * @param X
	 */
	public void setX(int X) {
		setPosition(X, getY());
	}
	
	/**
	 * define a posi��o vertical
	 * @param Y
	 */
	public void setY(int Y) {
		setPosition(getX(), Y);
	}
}
