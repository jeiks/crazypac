package crazypac;

import javax.microedition.lcdui.Graphics;

/**
 * Classe Linha que permite definir uma linha
 *     com valores máximos na horizontal e vertical
 *     
 * @author Jacson Rodrigues
 */
public class Linha {
	
	private int x;
	private int y;
	private int x2;
	private int y2;
	private int width;
	private int height;
	
	/**
	 * Construtor que já inicia uma linha com seus parâmetros
	 * @param x: ponto horizontal inicial
	 * @param y: ponto vertical inicial
	 * @param x2: ponto horizontal final
	 * @param y2: ponto vetival final
	 * @param width: tamanho máximo na horizontal
	 * @param height: tamanho máximo na vertical
	 */
	public Linha(int x, int y, int x2, int y2, int width, int height){
		setHeight(height); setWidth(width);
		setX(x); setY(y); setX2(x2); setY2(y2);
	}

	/**
	 * Construtor que inicia a linha com seus parâmetros iguais a zero,
	 * exceto os tamanhos máximos na horizontal e vertical
	 * @param width
	 * @param height
	 */
	public Linha(int width, int height){
		setHeight(height); setWidth(width);
		setX(0); setY(0); setX2(0); setY2(0);
	}
	
	/**
	 * Construtor que inicia a linha com todos 
	 * seus parâmetros iguais a 0
	 */
	public Linha(){
		setHeight(0); setWidth(0);
		setX(0); setY(0); setX2(0); setY2(0);
	}
	
	/**
	 * Define a largura máxima da linha
	 * @param width: largura
	 */
	private void setWidth(int width) { if (width >= 0)this.width = width; }

	/**
	 * Define a altura máxima da linha
	 * @param height: altura
	 */
	private void setHeight(int height) { if (height>=0)this.height = height; }
	
	/**
	 * Obtém a altura máxima da linha
	 * @return altura máxima da linha
	 */
	public int getHeight() { return height; }
	
	/**
	 * Obtém a largura máxima da linha
	 * @return largura máxima da linha
	 */
	public int getWidth() { return width; }
	
	/**
	 * Verifica se o valor está em uma posição viável na horizontal
	 * e retorna uma posição válida
	 * @param aux
	 * @return
	 */
	private int pegaPontoHor(int aux){
		if (aux < 0) return 0;
		else if(aux > getWidth()) return width;
		else return aux;
	}
	
	/**
	 * Verifica se o valor está em uma posição viável na vertical
	 * e retorna uma posição válida
	 * @param aux
	 * @return
	 */
	private int pegaPontoVer(int aux){
		if (aux < 0) return 0;
		else if(aux > getHeight()) return height;
		else return aux;
	}
	/**
	 * define o ponto horizontal inicial da linha
	 * @param x
	 */
	public void setX(int x) {
		this.x = pegaPontoHor(x);
	}
	
	/**
	 * define o ponto vertical inicial da linha
	 * @param y
	 */
	public void setY(int y){
		this.y = pegaPontoVer(y);
	}
	
	/**
	 * define o ponto horizontal final da linha 
	 * @param x2
	 */
	public void setX2(int x2){
		this.x2 = pegaPontoHor(x2);
	}
	
	/**
	 * define o ponto vertical final da linha
	 * @param y2
	 */
	public void setY2(int y2){
		this.y2 = pegaPontoVer(y2);
	}
	
	/**
	 * obtém o ponto horizontal inicial da linha
	 * @return
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * obtém o ponto vertical inicial da linha
	 * @return
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * obtém o ponto horizontal final da linha
	 * @return
	 */
	public int getX2(){
		return x2;
	}
	
	/**
	 * obtém o ponto vertical final da linha
	 * @return
	 */
	public int getY2(){
		return y2;
	}

	/**
	 * Verifica se a linha da classe colide com a linha
	 * especificada nos parâmetros
	 * @param posX
	 * @param posY
	 * @param posX2
	 * @param posY2
	 * @return
	 */
	public boolean colideComLinha(int posX, int posY, int posX2, int posY2) {
		int menorX = Math.min(getX(), getX2()),
		    maiorX = Math.max(getX(), getX2()),
		    menorY = Math.min(getY(), getY2()),
		    maiorY = Math.max(getY(), getY2());
		if ( (
				 ( menorX <= posX    && posX    <= maiorX ) ||
				 ( menorX <= posX2   && posX2   <= maiorX ) ||
				 ( posX   <= menorX  && menorX  <= posX2  ) ||
				 ( posX   <= maiorX  && maiorX  <= posX2)
				 )
				 &&
				 (
				 ( menorY <= posY    && posY   <= maiorY ) ||
				 ( menorY <= posY2   && posY2  <= maiorY ) ||
				 ( posY   <= menorY  && menorY <= posY2  ) ||
				 ( posY   <= maiorY  && maiorY <= posY2)
				 )
			   ) return true;
		
		return false;
	}
	
	/**
	 * Desenha a linha da classe em Graphics passado
	 * como parâmetro
	 * @param g
	 */
	public void desenhaLinha(Graphics g) {
		g.drawLine(getX(), getY(), getX2(), getY2());	
	}
}
