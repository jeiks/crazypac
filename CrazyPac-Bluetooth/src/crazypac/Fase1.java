package crazypac;

/**
 * Criação de uma fase simples para o Jogo do CrazyPac
 * 
 * @author Jacson RC Silva
 */
public class Fase1 extends Fase {
	
	/**
	 * Contrutor que define a fase do jogo
	 * @param width: largura da fase
	 * @param height: altura da fase
	 */
	public Fase1(int width, int height) {
		super(width, height);
		/**
		 * define as paredes do jogo
		 */
		adicionaParedes(30, 40, 100, 40);
		adicionaParedes(40, 60, 40, 140);
		adicionaParedes(100, 140, width, 140);
		adicionaParedes(120, 60, 120, 140);
		
		/**
		 * define os fantasmas do jogo
		 */
		adicionaFantasmas(10, 10);
		adicionaFantasmas(getWidth()-20, getHeight()-20);
		adicionaFantasmas(40, 40);		
		adicionaFantasmas(120, 120);
		
		/**
		 * define os escudos do jogo
		 */
		adicionaBolinhas(30, 20);
		adicionaBolinhas(140, 140);
		adicionaBolinhas(30, 140);
	}
}
