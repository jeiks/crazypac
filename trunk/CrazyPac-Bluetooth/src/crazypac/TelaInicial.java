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
 * Classe que implementa a tela inicial do jogo com a opção de jogar e sair.
 * @author  Jacson RC Silva
 */
public class TelaInicial extends Form
implements CommandListener {

	/**
	 * imagem padrão do CrazyPac
	 */
	private Image imgPacMan;

	private Command comandoJogar;
	private Command comandoSair;

	/**
	 * padrão de projeto Singleton
	 * Instância do jogo
	 */
	private static TelaInicial instance;
	
	/**
	 * Construtor padrão
	 * responsável por criar a tela inicial
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
	 * padrão de projeto Singleton.
	 * Instância do jogo.
	 * @return a instância
	 * @uml.property  name="instance"
	 */
	public synchronized static TelaInicial getInstance() {
		if ( instance == null )
			instance = new TelaInicial();
		return instance;
	}
	
	/**
	 * recebe os comandos do usuário
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
