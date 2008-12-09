package crazypac;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;


public class Tabuleiro extends GameCanvas implements Runnable,CommandListener {
	
	private Command commandBack;
	private CrazyPac pai;
	private int currentX, currentY;
	private long delay;
	private boolean isPlay;
	private Fase[] fases;
	private int faseAtual;
	private int pegouBolinha;
	private int contador;
	/**
	 * boneco PacMan
	 */
	private SpriteDir pacman;
	/**
	 * Tamanho do movimento do PacMan
	 * influencia diretamente em sua velocidade
	 */
	private int pacmanMove = 2;
	/**
	 * Última tecla pressionada
	 */
	private int oldKeyStates = 0;
	
	public Tabuleiro(CrazyPac pai) {
		super(true);
		this.pai = pai;
		delay = 20;
		defineFase();
		adicionaComandos();
		criaPacman();
	}
	
	private void criaPacman() {
		currentX = getWidth()/2;
		currentY = getHeight()/2;
		pacman = new SpriteDir(SpriteDir.getImage("/images/pacman_frames.png"), 16, 16);
	}
	
	private void adicionaComandos() {
		commandBack = new Command("Voltar", Command.BACK, 0);
		addCommand(commandBack);
		this.setCommandListener(this);		
	}

	/**
	 * Define as paredes do Jogo
	 */
	private void defineFase() {
		fases = new Fase[1];
		fases[0] = new Fase1(getWidth(), getHeight());
		faseAtual = 0;
	}

	/**
	 * desenha as linhas do jogo
	 * @param g
	 * @param color
	 * @param linhas
	 * mantido por dozinha de deletar... hehe ;)
	private void desenhaLinhas(Graphics g, int color, Linha[] linhas){
		g.setColor(color);
		for (Linha linha: linhas)
			linha.desenhaLinha(g);
	}*/

	/**
	 * verifica a colisao entre o Pacman e as linhas
	 * que compõem o vetor
	 * @param nextX
	 * @param nextY
	 * @param linhas
	 * @return
	 */
	private boolean pacmanColide(int nextX, int nextY, Linha[] linhas)
	{
		for (Linha linha: linhas){
			if (linha.colideComLinha(nextX, nextY,
					                 nextX+pacman.getWidth(),
					                 nextY+pacman.getHeight()
					                )
				)
				return true;
		}
		return false;	
	}
	
	/**
	 * verifica os comandos do usuário sobre o menu
	 */
	public void commandAction(Command arg0, Displayable arg1) {
		if (arg0 == commandBack) {
			stop();
			pai.voltouJogo();
			pai.setCurrent();
		}
	}

	public void run() {
		Graphics g = getGraphics();
		drawScreen(g);
		while (isPlay == true){
			input();
			drawScreen(g);
			try { Thread.sleep(delay); }
			catch (InterruptedException ie) {}
		}
		pai.setCurrent();
	}

	/**
	 * verifica as entradas do jogo
	 */
	private void input() {
		int keyStates = getKeyStates();
		if (keyStates == 0) keyStates = oldKeyStates;
	
		int i = 0;
		for (Sprite aux: fases[faseAtual].getFantasmas())
		{
			if (Colisao.colisao(pacman,aux)){
				if (fases[faseAtual].getEstadoFantasmas()) {
					pai.perdeuJogo();
					stop();
				}else if (pegouBolinha == 1){
					fases[faseAtual].apagaFantasmas(i);
				}
			}
			i++;
		}
		
		i = 0;
		for (Sprite aux: fases[faseAtual].getBolinhas())
		{
			if (Colisao.colisao(pacman,aux)){
				fases[faseAtual].apagaBolinhas(i);
				pegouBolinha = 1;
				contador = 0;
				fases[faseAtual].defineEstadoFantasmas(false);
			}
			i++;
		}
		
		if (pegouBolinha == 1 || pegouBolinha == 2) contador++;
		if (contador == 400) {
			fases[faseAtual].recuperaFantasmas();
			pegouBolinha = 2;
		}
		
		if (contador == 450) {
			fases[faseAtual].defineEstadoFantasmas(true);
			pegouBolinha = 0;
			contador = 0;
		}
		
	    // Left
	    if ((keyStates & LEFT_PRESSED) != 0)
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(currentX - pacmanMove, currentY, fases[faseAtual].getParedes()))
	    	{
	    		currentX = Math.max(0, currentX - pacmanMove);
	    		pacman.setFrame(1);
	    	}
	    }
	    // Right
	    if ((keyStates & RIGHT_PRESSED) !=0 ) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(currentX + pacmanMove, currentY, fases[faseAtual].getParedes()))
	    	{
	    		currentX = Math.min(getWidth(), currentX + pacmanMove);
	    		pacman.setFrame(0);
	    	}
	    }
	    // Up
	    if ((keyStates & UP_PRESSED) != 0) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(currentX, currentY - pacmanMove, fases[faseAtual].getParedes()))
	    	{
	    		currentY = Math.max(0, currentY - pacmanMove);
	    		pacman.setFrame(2);
	    	}
	    }
	    // Down
	    if ((keyStates & DOWN_PRESSED) !=0) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(currentX, currentY + pacmanMove, fases[faseAtual].getParedes()))
	    	{
	    	  currentY = Math.min(getHeight(), currentY + pacmanMove);
	        	pacman.setFrame(3);
	      	}
	    }
	}

	/**
	 * desenha as telas do jogo
	 * @param g
	 */
	private void drawScreen(Graphics g) {
	    g.setColor(0xffffff);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    
	    fases[faseAtual].movimentaFantasmas();
	    fases[faseAtual].desenha(g, 0x000000);
	    
	    pacman.setPosition(currentX, currentY);
	    pacman.paint(g);
	    
	    flushGraphics();
	}

	public void start() {
		isPlay = true;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void stop(){
		isPlay = false;
	}

}
