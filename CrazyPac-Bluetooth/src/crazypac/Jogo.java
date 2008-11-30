package crazypac;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

import net.java.dev.marge.communication.CommunicationListener;
import net.java.dev.marge.inquiry.DeviceDiscoverer;

public class Jogo extends GameCanvas 
implements CommunicationListener, Runnable, CommandListener {

	/**
	 * Instância do Jogo
	 */
	private static Jogo instance;
	/**
	 * Definição de Servidor
	 */
	private boolean isServer;
	/**
	 * Tempo de iteração da thread do Jogo
	 */
	private int threadSleep;

	/**
	 * Define o comando que sai do jogo e volta para
	 * a tela inicial
	 */
	private Command commandBack;
	/**
	 * Define se o jogo está em execução
	 * obs: controle interno da Thread, método run()
	 */
	private boolean isPlay;
	/**
	 * Fases do Jogo
	 */
	private Fase[] fases;
	/**
	 * Fase atual do Jogo
	 */
	private int faseAtual;
	/**
	 * Define se o PacMan pegou poderes
	 */
	private int pegouBolinha;
	/**
	 * tempo de poder do PacMan ao pegar uma bolinha
	 */
	private int contador;

	/**
	 * boneco PacMan
	 */
	private SpriteDir pacman;
	/**
	 * Tamanho do movimento do PacMan
	 * influencia diretamente em sua velocidade
	 */
	private int pacmanMove = 1;
	/**
	 * Última tecla pressionada
	 */
	private int oldKeyStates = 0;
	
	/**
	 * Construtor principal
	 * Cria o jogo, definindo a fase, os comandos e o pacman
	 * 
	 * privado pela utilização do padrão de projeto Singleton
	 */
	private Jogo() {
		super(false);
		threadSleep = 20;
		defineFase();
		adicionaComandos();
		criaPacman();
	}

	/**
	 * função que cria o PacMan
	 */
	private void criaPacman() {
		pacman = new SpriteDir(SpriteDir.getImage("/images/pacman_frames.png"), 16, 16);
		pacman.setPosition(getWidth()/2, getHeight()/2);
	}
	
	/**
	 * Função que adiciona os comandos ao CommandListener
	 */
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
	 * retorna a mesma instância do jogo para qualquer chamada
	 * padrão de projeto Singleton
	 * @return instância do Jogo
	 */
	public synchronized static Jogo getInstance() {
		if (instance == null)
			newInstance();
			//instance = new Jogo();
		return instance;
	}

	/**
	 * cria uma nova instancia para o jogo
	 */
	public synchronized static void newInstance() {
		instance = new Jogo();
	}
	/**
	 * define se é Servidor ou Cliente
	 * @param is: é ou não é servidor
	 */
	public void isServer(boolean is) {
		isServer = is;
	}
	
	/**
	 * Função que demonstra um erro
	 * ver documentação do Marge
	 */
	public synchronized void errorOnReceiving(IOException arg0) {
		//System.out.println("Error on Receiving: "+arg0.getMessage());
		if (!isPlay) return;
		mensagemDesconectou();
	}
	
	/**
	 * Função que demonstra um erro
	 * ver documentação do Marge
	 */
	public synchronized void errorOnSending(IOException arg0) {
		//System.out.println("Error on Sending: "+arg0.getMessage());
		if (!isPlay) return;
		mensagemDesconectou();
	}

	public void mensagemDesconectou() {
		stop();
		String msg;
		if (isServer) msg = "O Cliente desconectou!";
		else msg = "O Servidor desconectou!";
		CrazyPac.getInstance().setCurrent(
				new TelaFacil("Erro",msg,null)
				);
	}
	
	public void sendPosition() {
		String msg = "";
        if (this.isServer) {
        	int estado = (fases[faseAtual].getEstadoFantasmas())?1:0;
            msg = pacman.getX()+";"+pacman.getY()+";"+pacman.getDir()+";"+estado+";"+
            	  fases[faseAtual].getPosicaoFantasmas();
        } else if (fases[faseAtual].existeFantasmas()) {
            msg = fases[faseAtual].getFantasma(fases[faseAtual].getFantasmaCliente()).getX()+
                  ";"+fases[faseAtual].getFantasma(fases[faseAtual].getFantasmaCliente()).getY()+";";
        }
        msg += "@";
        sendMsg(msg);
	}
	
	/**
	 * Função que demonstra um erro
	 * ver documentação do Marge
	 */
	public synchronized void receiveMessage(byte[] arg0) {
		String mensagem = new String(arg0);
		
		int initialMessage = 0;
		while (mensagem.indexOf('@', initialMessage+1) > 0) {
			String msg = mensagem.substring(initialMessage, 
										    mensagem.indexOf('@', initialMessage)
										    );
			initialMessage = mensagem.indexOf('@', initialMessage)+1;
			
			
			if ( msg.length() == 0 ) continue;
			
			if (msg.charAt(0) == '#') {
				////////////////////////////////////////////////
				int num;
				switch (Integer.parseInt(msg.charAt(1)+"")) {
					case 0:
						CrazyPac.getInstance().setCurrent(
								new TelaFacil("Perdedor", "Que pena, você perdeu o jogo!\n"+
										"Chame-o para mais uma partida! ;)"
										,null)
								);
						break;
					case 1:
						CrazyPac.getInstance().setCurrent(
								new TelaFacil("Ganhador", "Parabéns, você ganhou o jogo!",null)
								);
						break;
					case 2:
						num = Integer.parseInt(msg.substring(3, msg.length()-1));
						fases[faseAtual].apagaFantasmas(num);
						System.out.println("[Client apg] "+msg);
						break;
					case 3:
						num = Integer.parseInt(msg.substring(3, msg.length()-1));
						fases[faseAtual].apagaBolinhas(num);
						break;
					case 4:
						fases[faseAtual].recuperaFantasmas();
						break;
				}
				////////////////////////////////////////////////
				continue;
			}
			
			int initialPos = 0;
			int posX, posY;
			posX = Integer.parseInt(msg.substring(initialPos, msg.indexOf(';', initialPos)));
			initialPos = msg.indexOf(';', initialPos)+1;
			posY = Integer.parseInt(msg.substring(initialPos, msg.indexOf(';', initialPos)));
			initialPos = msg.indexOf(';', initialPos)+1;
			
			if (isServer) {
				if (fases[faseAtual].existeFantasmas())
				{
					SpriteDir aux = fases[faseAtual].getFantasma(0);
					aux.setX(posX); aux.setY(posY);
					fases[faseAtual].modificaFantasma(0, aux);
				}
			}else{
				try {
				char dir;
				dir = msg.charAt(initialPos);
				pacman.setPosition(posX, posY);
				pacman.setDir(dir);
				initialPos = msg.indexOf(';', initialPos)+1;
				int estado;
				estado = Integer.parseInt(msg.substring(initialPos, msg.indexOf(';', initialPos)));
				if (estado == 1) fases[faseAtual].setEstadoFantasmas(true);
				else fases[faseAtual].setEstadoFantasmas(false);
				initialPos = msg.indexOf(';', initialPos)+1;
				fases[faseAtual].setPosicaoFantasmas(msg.substring(initialPos, msg.length()));
				} catch (Exception e) {};
			}
		}
		//System.out.println("Recebi a posicao: "+posX+","+posY);
    }

	public synchronized void sendMsg(String msg) {
		CrazyPac.getInstance().getDevice().send(msg.getBytes());
	}
	
	/**
	 * Execução do Jogo
	 */
	public void run() {
		Graphics g = getGraphics();
		drawScreen(g);
		CrazyPac.getInstance().setCurrent(this);
		while (isPlay) {
			if (isServer) {
				inputServer();
				if (fases[faseAtual].getBolinhas().length == 0) {
					sendMsg("@#0#@");
					stop();
					CrazyPac.getInstance().setCurrent(
							new TelaFacil("Vencedor", "Parabéns, você venceu!",null)
							);
				}
				fases[faseAtual].movimentaFantasmasAutomaticos();
			} else if (fases[faseAtual].existeFantasmas()) {
				inputClient();	
			}
			
			drawScreen(g);
			sendPosition();
			try { Thread.sleep(threadSleep); }
			catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * verifica as entradas do jogo
	 */
	private synchronized void inputServer() {
		int keyStates = getKeyStates();
		if (keyStates == 0) keyStates = oldKeyStates;
	
		int i = 0;
		
		String msg = "";
		int numFantasmas[] = new int[fases[faseAtual].numFantasmas()];

		for (Sprite aux: fases[faseAtual].getFantasmas())
		{
			if (Colisao.colisao(pacman,aux))
			{
				if (fases[faseAtual].getEstadoFantasmas()) {
					CrazyPac.getInstance().setCurrent(
							new TelaFacil("Perdeu o Jogo",
									"Que pena, perdeu o jogo, tente novamente!", null)
							);
					sendMsg("@#1#@");
					stop();
				}else if (pegouBolinha == 1){
					numFantasmas[i] = 1;
					//System.out.println("[Server apg i] "+i);
				}
			}
			i++;
		}

		for (int j=0;j < numFantasmas.length;j++) {
			//System.out.println("[Server apg j] "+numFantasmas[j]);
			if (numFantasmas[j] == 1){
				fases[faseAtual].apagaFantasmas(j);
				msg = "@#2#"+j+"#@" + msg;
			}
		}

		sendMsg(msg);
		
		i = 0;
		msg = "";
		for (Sprite aux: fases[faseAtual].getBolinhas())
		{
			if (Colisao.colisao(pacman,aux)){
				fases[faseAtual].apagaBolinhas(i);
				pegouBolinha = 1;
				contador = 0;
				fases[faseAtual].defineEstadoFantasmas(false);
				msg = "@#3#"+i+"#@" + msg; 
			}
			i++;
		}
		sendMsg(msg);
		
		if (pegouBolinha == 1 || pegouBolinha == 2) contador++;
		if (contador == 400) {
			fases[faseAtual].recuperaFantasmas();
			sendMsg("@#4#@");
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
	    	if (!pacmanColide(pacman.getX() - pacmanMove, pacman.getY(), fases[faseAtual].getParedes()))
	    	{
	    		pacman.setX(Math.max(0, pacman.getX() - pacmanMove));
	    		pacman.setDir('L');
	    	}
	    }
	    // Right
	    if ((keyStates & RIGHT_PRESSED) !=0 ) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(pacman.getX() + pacmanMove, pacman.getY(), fases[faseAtual].getParedes()))
	    	{
	    		pacman.setX(Math.min(getWidth(), pacman.getX() + pacmanMove));
	    		pacman.setDir('R');
	    	}
	    }
	    // Up
	    if ((keyStates & UP_PRESSED) != 0) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(pacman.getX(), pacman.getY() - pacmanMove, fases[faseAtual].getParedes()))
	    	{
	    		pacman.setY(Math.max(0, pacman.getY() - pacmanMove));
	    		pacman.setDir('U');
	    	}
	    }
	    // Down
	    if ((keyStates & DOWN_PRESSED) !=0) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(pacman.getX(), pacman.getY() + pacmanMove, fases[faseAtual].getParedes()))
	    	{
	    		pacman.setY(Math.min(getHeight(), pacman.getY() + pacmanMove));
	        	pacman.setDir('D');
	      	}
	    }
	    
	    sendPosition();
	}

	/**
	 * verifica as entradas do jogo
	 */
	private synchronized void inputClient() {
		int keyStates = getKeyStates();
		if (keyStates == 0) keyStates = oldKeyStates;
		
		if (!fases[faseAtual].existeFantasmas()) return;
		
		SpriteDir aux = fases[faseAtual].getFantasma(0);
	    // Left
	    if ((keyStates & LEFT_PRESSED) != 0)
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(aux.getX() - pacmanMove, aux.getY(), fases[faseAtual].getParedes()))
	    	{
	    		aux.setX(Math.max(0, aux.getX() - pacmanMove));
	    	}
	    }
	    // Right
	    if ((keyStates & RIGHT_PRESSED) !=0 ) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(aux.getX() + pacmanMove, aux.getY(), fases[faseAtual].getParedes()))
	    	{
	    		aux.setX(Math.min(getWidth(), aux.getX() + pacmanMove));
	    	}
	    }
	    // Up
	    if ((keyStates & UP_PRESSED) != 0) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(aux.getX(), aux.getY() - pacmanMove, fases[faseAtual].getParedes()))
	    	{
	    		aux.setY(Math.max(0, aux.getY() - pacmanMove));
	    	}
	    }
	    // Down
	    if ((keyStates & DOWN_PRESSED) !=0) 
	    {
	    	oldKeyStates = keyStates;
	    	if (!pacmanColide(aux.getX(), aux.getY() + pacmanMove, fases[faseAtual].getParedes()))
	    	{
	    		aux.setY(Math.min(getHeight(), aux.getY() + pacmanMove));
	      	}
	    }
	    
	    fases[faseAtual].modificaFantasma(0, aux);
	}
	
	/**
	 * desenha as telas do jogo
	 * @param g
	 */
	private void drawScreen(Graphics g) {
	    g.setColor(0xffffff);
	    g.fillRect(0, 0, getWidth(), getHeight());
	    
	    fases[faseAtual].desenha(g, 0x000000);
	    
	    pacman.paint(g);
	    
	    flushGraphics();
	}

	/**
	 * pára o Jogo
	 */
	public void stop(){
		try {
			DeviceDiscoverer.getInstance().cancelInquiry();
		} catch (BluetoothStateException e) { e.printStackTrace();}
		isPlay = false;
	}

	
	/**
	 * Inicia a execução do Jogo (iniciando a Thread)
	 */
	public void inicia() {
		isPlay = true;		
        Thread t = new Thread(this);
        t.start();
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if (arg0 == commandBack) {
			stop();
			CrazyPac.getInstance().setCurrent(TelaInicial.getInstance());
		}
	}
}
