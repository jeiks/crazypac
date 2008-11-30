package crazypac;

import java.util.Random;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;

/**
 * Classe padrão para as fases do jogo
 * @author Jacson Rodrigues
 */
public class Fase {

	/**
	 * Tamanho da fase
	 */
	private int width, height;
	/**
	 * distancia das retas de borda da fase
	 */
	private int canto = 2;
	/**
	 * paredes da fase
	 */
	private ArrayList paredes;
	/**
	 * fantasmas da fase
	 */
	private ArrayList fantasmas, fantasmasRemovidos;
	/**
	 * bolinhas de poder do jogo
	 */
	private ArrayList bolinhas, bolinhasRemovidas;
	/**
	 * variável ligada ao movimento dos personagens 
	 */
	private Random random = new Random();
	/**
	 * estado dos fantasmas
	 * true = ativos
	 * false = inativos
	 */
	private boolean estadoFantasmas;

	/**
	 * para diminuir o tempo de execucao, foi criado um vetor
	 * com os números dos fantasmas automaticos
	 */
	private int fantasmaCliente;
	
	/**
	 * define o movimento randomico
	 */
	private boolean movimentoRandomico;
	
	/**
	 * Construtor que define o tamanho da fase
	 * e cria suas bordas
	 * @param width
	 * @param height
	 */
	public Fase(int width, int height) {
		setWidth(width);
		setHeight(height);
		fantasmas = new ArrayList();
		fantasmasRemovidos = new ArrayList();
		paredes = new ArrayList();
		bolinhas = new ArrayList();
		bolinhasRemovidas = new ArrayList();
		estadoFantasmas = true;
		movimentoRandomico = true;
		//bordas da fase
		paredes.add(new Linha(canto, canto, width-canto, canto, width, height));
		paredes.add(new Linha(canto, canto, canto, height-canto, width, height));
		paredes.add(new Linha(canto, height-canto, width-canto, height-canto, width, height));
		paredes.add(new Linha(width-canto, canto, width-canto, height-canto, width, height));
	}

	/**
	 * define a largura da fase
	 * @param width
	 */
	private void setWidth(int width) {
		this.width = width;
	}

	/**
	 * obtem a largura da fase
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * define a altura da fase
	 * @param height
	 */
	private void setHeight(int height) {
		this.height = height;
	}

	/**
	 * obtem a altura da fase
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Obtem as paredes da fase
	 * @return
	 */
	public synchronized Linha[] getParedes() {
		Linha[] retorno =  new Linha[paredes.size()];
		for (int i=0; i<paredes.size(); i++)
			retorno[i] = (Linha) paredes.get(i);
		return retorno;
	}
	
	/**
	 * desenha a fase no Graphics g fornecido
	 * com a cor determinada
	 * @param g
	 * @param color
	 */
	public void desenha(Graphics g, int color) {
		g.setColor(color);
		for (int i=0; i<paredes.size(); i++)
			((Linha) paredes.get(i)).desenhaLinha(g);
		for (int i=0; i<fantasmas.size(); i++)
			((Sprite) fantasmas.get(i)).paint(g);
		for (int i=0; i<bolinhas.size(); i++)
			((Sprite) bolinhas.get(i)).paint(g);
	}
	
	public void adicionaParedes(int posX, int posY, int posX2, int posY2) {
		adicionaParedes(new Linha(posX, posY, posX2, posY2, getWidth(), getHeight()));
	}
	
	/**
	 * adiciona paredes na fase
	 * @param parede: uma parede somente
	 */
	public void adicionaParedes(Linha parede) {
		paredes.add(parede);
	}
	
	/**
	 * adiciona paredes na fase
	 * @param parede: várias paredes
	 */
	public void adicionaParedes(Linha[] parede) {
		for (Linha p: parede)
			paredes.add(p);
	}
	
	/**
	 * adiciona fantasmas na fase
	 * @param f: somente um fantasma
	 * @param x: posição X
	 * @param y: posição Y
	 */
	public void adicionaFantasmas(SpriteDir f, int x, int y) {
		f.setPosition(x, y);
		fantasmas.add(f);
	}

	public synchronized void apagaFantasmas(int index) {
		fantasmasRemovidos.add(fantasmas.remove(index));
	}

	public synchronized void recuperaFantasmas() {
		if (fantasmasRemovidos.size() != 0 ) {
			int aux = fantasmasRemovidos.size();
			for (int i = 0; i< aux; i++)
				fantasmas.add(fantasmasRemovidos.remove(0));
		}
	}
	
	/**
	 * adiciona fantasmas na fase
	 * deve-se, antes de chamar a função, especificar
	 *    a posição dos fantasmas com setPosition
	 * @param fs: vários fantasmas
	 */
	public synchronized void adicionaFantasmas(SpriteDir[] fs) {
		for (SpriteDir f: fs)
			fantasmas.add(f);
	}
	
	public synchronized void adicionaFantasmas(int posX, int posY) {
		adicionaFantasmas(
			new SpriteDir(SpriteDir.getImage("/images/fantasma_frames.png"), 16, 16),
			posX, posY);
	}
	
	/**
	 * retorna os fantasmas da fase
	 * @return
	 */
	public synchronized SpriteDir[] getFantasmas() {
		SpriteDir[] retorno =  new SpriteDir[fantasmas.size()];
		for (int i=0; i< fantasmas.size(); i++)
			retorno[i] = new SpriteDir(getFantasma(i));
		return retorno;
	}
	
	public int numFantasmas() {
		return fantasmas.size();
	}
	
	public synchronized SpriteDir getFantasma(int qual) {
		return (SpriteDir) fantasmas.get(qual);
	}

	public boolean existeFantasmas() {
		if (fantasmas.size() == 0)
			return false;
		else return true;
	}
	
	public synchronized void modificaFantasma(int qual, SpriteDir fantasma) {
		if (estadoFantasmas) fantasma.setFrame(0);
		else fantasma.setFrame(1);
		fantasmas.set(qual, fantasma);
	}
	
	public synchronized void adicionaBolinhas(Sprite f, int x, int y) {
		f.setPosition(x, y);
		bolinhas.add(f);
	}
	
	public synchronized void adicionaBolinhas(int posX, int posY) {
		adicionaBolinhas(
				new SpriteDir(SpriteDir.getImage("/images/super_power.png"), 12, 12),
				posX, posY);
	}

	public synchronized void apagaBolinhas(int index) {
		bolinhasRemovidas.add(bolinhas.remove(index));
	}
	
	public synchronized Sprite[] getBolinhas() {
		Sprite[] retorno =  new Sprite[bolinhas.size()];
		for (int i=0; i< bolinhas.size(); i++)
			retorno[i] = new Sprite((Sprite) bolinhas.get(i));
		return retorno;
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
	
	/**
	 * movimenta os fantasmas pelo jogo
	 */
	public synchronized void movimentaFantasmas() {
		for (int i=0; i<fantasmas.size(); i++) {
			movimentaFantasmas(i);
		}
	}
	
	/**
	 * movimenta os fantasmas automaticos pelo jogo
	 */
	public synchronized void movimentaFantasmasAutomaticos() {
		for (int i=0; i<fantasmas.size(); i++) {
			if (i != fantasmaCliente)
				movimentaFantasmas(i);
		}
	}
	
	/**
	 * movimenta manualmente um fantasma
	 * @param qual
	 * @param nextX
	 * @param nextY
	 */
	public synchronized void movimentaFantasma(int qual, int nextX, int nextY) {
		if (qual > fantasmas.size()-1)
			return;
		
		SpriteDir aux = (SpriteDir) fantasmas.get(qual);
		if (!Colisao.colisao(nextX,nextY,aux.getWidth(),aux.getHeight(), getParedes()))
			aux.setPosition(nextX, nextY);
		
		fantasmas.set(qual, aux);
	}
	
	/**
	 * movimenta automaticamente um fantasma
	 * @param qual
	 */
	public synchronized void movimentaFantasmas(int qual) {
		SpriteDir aux = (SpriteDir) fantasmas.get(qual);
		int nextX = aux.getX(), 
			nextY = aux.getY();
		switch (aux.getDir()) {
		case 'R': nextX+=1;	break;
		case 'L': nextX-=1; break;
		case 'U': nextY-=1; break;
		case 'D': nextY+=1; break;
		}
		
		if (movimentoRandomico)	
			if (random.nextInt()%73 == 0)
				aux.setDir(modificaDir(aux.getDir()));
		
		if (!Colisao.colisao(nextX,nextY,aux.getWidth(),aux.getHeight(), getParedes()))
			aux.setPosition(nextX, nextY);
		else
			aux.setDir(modificaDir(aux.getDir()));
		fantasmas.set(qual, aux);
	}
	
	public void setEstadoFantasmas(boolean estado) {
		defineEstadoFantasmas(estado);
	}
	
	/**
	 * Troca as figuras dos fantasmas para tivos ou não
	 * @param estado
	 */
	public synchronized void defineEstadoFantasmas(boolean estado) {
		estadoFantasmas = estado;
		for (int i=0; i<fantasmas.size(); i++) {
			SpriteDir aux = (SpriteDir) fantasmas.get(i);
			if ( estado ) aux.setFrame(0);
			else aux.setFrame(1);
			fantasmas.set(i, aux);
		}
	}
	
	/**
	 * Obtém o estado dos fantasmas
	 * @return
	 */
	public boolean getEstadoFantasmas() {
		return estadoFantasmas;
	}
	
	public void setFantasmaCliente(int fantasmaCliente) {
		this.fantasmaCliente = fantasmaCliente;
	}
	
	public int getFantasmaCliente() {
		return fantasmaCliente;
	}
	
	public synchronized String getPosicaoFantasmas(){
		String retorno = "";
		for (int i=0; i < fantasmas.size(); i++)
		{
			SpriteDir fant = getFantasma(i);
			retorno+=fant.getX()+";"+fant.getY()+";";
		}
		return retorno;
	}
	
	public synchronized void setPosicaoFantasmas(String posicao) {
		int initialPos = 0;
		int posFantasmas=0;
		int posX, posY;

		while (initialPos>=0 && initialPos < posicao.length() && posFantasmas < fantasmas.size())
		{
			posX = Integer.parseInt(
					posicao.substring(initialPos, posicao.indexOf(';', initialPos))
					);
			initialPos = posicao.indexOf(';', initialPos)+1;
			posY = Integer.parseInt(
					posicao.substring(initialPos, posicao.indexOf(';', initialPos))
					);
			initialPos = posicao.indexOf(';', initialPos)+1;
			
			SpriteDir aux = (SpriteDir)fantasmas.get(posFantasmas);
			if (posFantasmas != fantasmaCliente)
				aux.setPosition(posX, posY);
			
			fantasmas.set(posFantasmas, aux);
		
			posFantasmas++;
		}
	}
}
