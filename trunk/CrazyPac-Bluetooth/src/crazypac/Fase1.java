package crazypac;

public class Fase1 extends Fase {
	
	public Fase1(int width, int height) {
		super(width, height);
		//outras paredes
		adicionaParedes(30, 40, 100, 40);
		adicionaParedes(40, 60, 40, 140);
		adicionaParedes(100, 140, width, 140);
		adicionaParedes(120, 60, 120, 140);
		
		//fantasmas...
		adicionaFantasmas(10, 10);
		adicionaFantasmas(getWidth()-20, getHeight()-20);
		adicionaFantasmas(40, 40);		
		adicionaFantasmas(120, 120);
		
		//bolinhas
		adicionaBolinhas(30, 20);
		adicionaBolinhas(140, 140);
		adicionaBolinhas(30, 140);
		
		//fantasmaCliente
		setFantasmaCliente(0);
	}
}
