package crazypac;

import java.util.Random;

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
	private Random random = new Random();
	private Linha[] paredes;
	 
	/**
	 * boneco PacMan
	 */
	private SpriteDir pacman;
	/**
	 * Fantasmas
	 */
	private SpriteDir[] fantasmas;
	/**
	 * Tamanho do movimento do PacMan
	 * influencia diretamente em sua velocidade
	 */
	private int pacmanMove = 2;
	/**
	 * Última tecla pressionada
	 */
	private int oldKeyStates = 0;
	
	Tabuleiro(CrazyPac pai) {
		super(true);
		this.pai = pai;
		delay = 20;
		defineParedes();
		adicionaComandos();
		criaFantasmas();
		criaPacman();
	}
	
	private void criaPacman() {
		currentX = getWidth()/2;
		currentY = getHeight()/2;
		pacman = new SpriteDir(SpriteDir.getImage("/images/pacman_frames.png"), 16, 16);
	}
	
	private void criaFantasmas() {
		fantasmas = new SpriteDir[3];
		fantasmas[0] = new SpriteDir(SpriteDir.getImage("/images/fantasma.png"), 16, 16);
		fantasmas[0].setPosition(10, 10);
		fantasmas[1] = new SpriteDir(SpriteDir.getImage("/images/fantasma.png"), 16, 16);
		fantasmas[1].setPosition(getWidth()-20, getWidth()-20);
		fantasmas[2] = new SpriteDir(SpriteDir.getImage("/images/fantasma.png"), 16, 16);
		fantasmas[2].setPosition(40, 40);
	}

	private void adicionaComandos() {
		commandBack = new Command("Voltar", Command.BACK, 0);
		addCommand(commandBack);
		this.setCommandListener(this);		
	}

	/**
	 * Define as paredes do Jogo
	 */
	private void defineParedes() {
		int width = getWidth(), height = getHeight();
		paredes = new Linha[7];
		paredes[0] = new Linha(      2,        2, width-2,        2, width, height);
		paredes[1] = new Linha(      2,        2,       2, height-2, width, height);
		paredes[2] = new Linha(      2, height-2, width-2, height-2, width, height);
		paredes[3] = new Linha(width-2,        2, width-2, height-2, width, height);
		paredes[4] = new Linha(30, 40, 100, 40, width, height);
		paredes[5] = new Linha(40, 60, 40, 140, width, height);
		paredes[6] = new Linha(100, 140, 160, 140, width, height);
	}

	/**
	 * desenha as linhas do jogo
	 * @param g
	 * @param color
	 * @param linhas
	 */
	private void desenhaLinhas(Graphics g, int color, Linha[] linhas){
		g.setColor(color);
		for (Linha linha: linhas)
			linha.desenhaLinha(g);
	}

	/**
	 * verifica a colisao de dois personagens
	 * eles devem ter a mesma largura e a mesma altura
	 * @param per1
	 * @param per2
	 */
	public boolean colisao(Sprite per1, Sprite per2) 
	{
		return colisao(
				per1.getX(),per1.getY(),
				per1.getX()+per1.getWidth(),per1.getY()+per1.getHeight(),
				per2.getX(),per2.getY(),
				per2.getX()+per2.getWidth(),per2.getY()+per2.getHeight()
				);
	}
	

	/**
	 * Verifica a colisão entre duas retas
	 * @param pX
	 * @param pY
	 * @param pX2
	 * @param pY2
	 * @param poX
	 * @param poY
	 * @param poX2
	 * @param poY2
	 * @return
	 */
	public boolean colisao(int pX,  int pY, 
			               int pX2, int pY2,
			               int poX,  int poY,
			               int poX2, int poY2) 
	{
		Linha aux = new Linha(pX,pY,pX2,pY2,getWidth(),getHeight());
		return aux.colideComLinha(poX, poY, poX2, poY2);
	}
	
	/**
	 * verifica a colisao entre o Pacman e as linhas
	 * que compõem o vetor
	 * @param nextX
	 * @param nextY
	 * @param linhas
	 * @return
	 */
	private boolean colisao(int nextX, int nextY, Linha[] linhas)
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
	 * verifica a colisao entre um desenho e as linhas que
	 * compõem o vetor
	 * @param nextX
	 * @param nextY
	 * @param width
	 * @param height
	 * @param linhas
	 * @return
	 */
	private boolean colisao(int nextX, int nextY, int width, int height, Linha[] linhas)
	{
		for (Linha linha: linhas){
			if (linha.colideComLinha(nextX, nextY,
					                 nextX+width,
					                 nextY+height
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
	
		for (Sprite aux: fantasmas)
			if (colisao(pacman,aux)){
				pai.perdeuJogo();
				stop();
			}
		
	    // Left
	    if ((keyStates & LEFT_PRESSED) != 0)
	    {
	    	oldKeyStates = keyStates;
	    	if (!colisao(currentX - pacmanMove, currentY, paredes))
	    	{
	    		currentX = Math.max(0, currentX - pacmanMove);
	    		pacman.setFrame(1);
	    	}
	    }
	    // Right
	    if ((keyStates & RIGHT_PRESSED) !=0 ) 
	    {
	    	oldKeyStates = keyStates;
	    	//if ( currentX + pacman.getWidth() < width)
	    	if (!colisao(currentX + pacmanMove, currentY, paredes))
	    	{
	    		currentX = Math.min(getWidth(), currentX + pacmanMove);
	    		pacman.setFrame(0);
	    	}
	    }
	    // Up
	    if ((keyStates & UP_PRESSED) != 0) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!colisao(currentX, currentY - pacmanMove, paredes)) 
	    	{
	    		currentY = Math.max(0, currentY - pacmanMove);
	    		pacman.setFrame(2);
	    	}
	    }
	    // Down
	    if ((keyStates & DOWN_PRESSED) !=0) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!colisao(currentX, currentY + pacmanMove, paredes)) 
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
	    
	    pacman.setPosition(currentX, currentY);
	    pacman.paint(g);
	    
	    movimentaFantasmas();
	    for (Sprite aux: fantasmas)
	    	aux.paint(g);
	
	    desenhaLinhas(g, 0x000000, paredes);
	    
	    flushGraphics();
	}

	/**
	 * escolhe uma direção diferente do parâmetro de entrada
	 * @param dir: direção atual
	 * @return: outra direção
	 */
	private char modificaDir(char dir) {
		char vet [] = {'R','L','U','D'};
		int aux=0;
		do {
			aux = Math.abs(random.nextInt()%4);
		}while ( vet[aux] == dir );
		return vet[aux];
	}
	
	private void movimentaFantasmas() {
		for (SpriteDir aux: fantasmas) {
			int nextX = aux.getX(), 
				nextY = aux.getY();
			switch (aux.getDir()) {
			case 'R': nextX+=1;	break;
			case 'L': nextX-=1; break;
			case 'U': nextY-=1; break;
			case 'D': nextY+=1; break;
			}
			if (random.nextInt()%193 == 0) //223
				aux.setDir(modificaDir(aux.getDir()));
			
			if (!colisao(nextX,nextY,aux.getWidth(),aux.getHeight(),paredes))
				aux.setPosition(nextX, nextY);
			else
				aux.setDir(modificaDir(aux.getDir()));
		}
		
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
