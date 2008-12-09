package crazypac;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.midlet.MIDlet;

/**
 * MIDlet do CrazyPac
 * 
 * @author Jacson Rodrigues
 */
public class CrazyPac extends MIDlet implements CommandListener {

	/**
	 * Comandos utilizados na interface principal
	 */
	private Command comandoSair, comandoOk;
	/**
	 * Formul�rio padr�o da interface principal
	 */
	private Form mainForm;
	/**
	 * imagem do pacman
	 */
	private Image imgPacMan;
	/**
	 * objeto respons�vel pela tela
	 */
	private Display telaPrincipal; 
	

	/**
	 * Construtor
	 * cria o mainForm, carrega a imagem do PacMan,
	 * chama o m�todo para preencher o mainForm e
	 * obt�m o display
	 */
	public CrazyPac() {
		mainForm = new Form("CrazyPac");
		imgPacMan = null;
		try {
			imgPacMan = Image.createImage("/images/pacman-32x32.gif");
		} catch (IOException e) {
			System.out.println("Erro ao carregar imagem."+e);
		}
		preencheMainForm();
		telaPrincipal = Display.getDisplay(this);
		//CPBluetooth.informacoesBlueTooth(mainForm);
	}
	
	/**
	 * Adiciona os comandos da Interface Principal
	 *
	 */
	private void adicionaComandos() {
		comandoOk = new Command("Jogar", Command.OK, 0);
		comandoSair = new Command("Sair", Command.EXIT, 3);
		mainForm.addCommand(comandoOk);
		mainForm.addCommand(comandoSair);
		mainForm.setCommandListener(this);
	}
	
	/**
	 * Preenche a Interface Principal
	 */
	private void preencheMainForm() {
		adicionaComandos();
		ImageItem pacMan = new ImageItem("Bem Vindo ao CrazyPac", 
								   imgPacMan, ImageItem.LAYOUT_CENTER, null);
		mainForm.append(pacMan);	
	}
	
	public void voltouJogo() {
		mainForm.deleteAll();
		ImageItem pacMan = new ImageItem("Bem Vindo ao CrazyPac", 
								   imgPacMan, ImageItem.LAYOUT_CENTER, null);
		mainForm.append(pacMan);	
	}
	
	/**
	 * Preenche a Interface Principal
	 * com a mensagem de que o jogador perdeu
	 */
	public void perdeuJogo() {
		mainForm.deleteAll();
		ImageItem pacMan = new ImageItem("Que pena, voc� perdeu, tente novamente!", 
				   imgPacMan, ImageItem.LAYOUT_CENTER, null);
		mainForm.append(pacMan);	
	}
	
	/**
	 * Mostra o mainForm na tela
	 */
	public void setCurrent() {
		telaPrincipal.setCurrent(mainForm);
	}
	
	/**
	 * Modifica a tela para o Displayable que recebe 
	 * @param arg
	 */
	public void setCurrent(Displayable arg) {
		telaPrincipal.setCurrent(arg);
	}
	
	/**
	 * retorna o objeto responsavel pela tela
	 * @return
	 */
	public Display getDisplay() {
		return telaPrincipal;
	}
	
	/**
	 * sai do MIDlet
	 */
	protected void destroyApp(boolean arg0) {
		exit();
	}

	/**
	 * comandos para sair com sucesso do MIDlet
	 */
	private void exit() {
		System.gc();
		notifyDestroyed();
	}

	/**
	 * n�o utilizado
	 */
	protected void pauseApp() {	}

	/**
	 * Inicia a aplica��o exibindo-a na tela
	 */
	protected void startApp() {
		setCurrent();
	}

	/**
	 * captura os comandos do usu�rio
	 */
	public void commandAction(Command cmd, Displayable arg1) {
		if (cmd == comandoSair) {
			notifyDestroyed();
		}else if (cmd == comandoOk) {
			Tabuleiro tabuleiro = new Tabuleiro(this);
			tabuleiro.start();
			setCurrent(tabuleiro);
		}
	}

}
