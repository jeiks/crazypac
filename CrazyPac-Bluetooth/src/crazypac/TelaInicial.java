package crazypac;

import java.io.IOException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Classe que implementa a tela inicial do jogo com a op��o de jogar e sair.
 * @author  Jacson RC Silva
 */
public class TelaInicial extends Form
implements CommandListener {

	/**
	 * imagem padr�o do CrazyPac
	 */
	private Image imgPacMan;

	private Command comandoJogar;
	private Command comandoSair;

	/**
	 * padr�o de projeto Singleton
	 * Inst�ncia do jogo
	 */
	private static TelaInicial instance;
	
	/**
	 * Construtor padr�o
	 * respons�vel por criar a tela inicial
	 */
	private TelaInicial() {
		super("");
		comandoSair = new Command("Sair",Command.EXIT, 0);
		comandoJogar = new Command("Jogar", Command.OK, 1);
		//comandos
		addCommand(comandoSair);
		addCommand(comandoJogar);
		setCommandListener(this);
		//imagem pacman
		imgPacMan = null;
		try {
			imgPacMan = Image.createImage("/images/pacman-32x32.gif");
		} catch (IOException e) {
			System.out.println("Erro ao carregar imagem."+e);
		}
		ImageItem pacMan = new ImageItem("Bem Vindo ao CrazyPac", 
				   imgPacMan, ImageItem.LAYOUT_CENTER, null);
		append(pacMan);	
	}
	
	/**
	 * padr�o de projeto Singleton.
	 * Inst�ncia do jogo.
	 * @return a inst�ncia
	 * @uml.property  name="instance"
	 */
	public synchronized static TelaInicial getInstance() {
		if ( instance == null )
			instance = new TelaInicial();
		return instance;
	}
	
	/**
	 * recebe os comandos do usu�rio
	 */
	public void commandAction(Command cmd, Displayable arg1) {
		if (cmd == comandoSair) {
			try {
				CrazyPac.getInstance().destroyApp(true);
			} catch (MIDletStateChangeException e) {
				System.out.println("Erro ao Sair"+e.getMessage());
			}
		} else if (cmd == comandoJogar) {
			MenuJogar.newInstance();
			Jogo.newInstance();
			CrazyPac.getInstance().setCurrent(MenuJogar.getInstance());
		}

	}

}
