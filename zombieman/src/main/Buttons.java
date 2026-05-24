package main;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
* Die Klasse Buttons stellt einen benutzerdefinierten JButton mit erweiterten Funktionen für ein Spielsystem-Menü dar.
* Sie besteht aus Funktionen wie Hover- und Klickeffekte, individuelle Bilder und Mausereignisse.
* Die Klasse ist dafür da um mit unterschiedlichen Panels in einer Spieleroberfläche benutzt zu werden,
* dazu gehören MenuPanel, HostPanel, JoinPanel, HelpPanel under der Game Klasse.
* 
* @author BoOom
* 
*/

@SuppressWarnings("serial")
public class Buttons extends JButton {

	private Game game;
	private MenuPanel menuPanel;
	private HostPanel hostPanel;
	private JoinPanel joinPanel;
	private HelpPanel helpPanel;
	private String action, imagePath, imagePathHover, imagePathClick;
	private boolean clicked, exited;

	/**
	 * Konstruktor für Buttons.
	 * @param game
	 * @param action
	 */
	public Buttons(Game game, String action) {
		this.game = game;
		initializeButton(action);
	}

	/**
	 * Konstruktor für Buttons.
	 * @param panel
	 * @param action
	 */
	public Buttons(MenuPanel panel, String action) {
		this.menuPanel = panel;
		initializeButton(action);
	}

	/**
	 * Konstruktor für Buttons.
	 * @param panel
	 * @param action
	 */
	public Buttons(HostPanel panel, String action) {
		this.hostPanel = panel;
		initializeButton(action);
	}

	/**
	 * Konstruktor für Buttons.
	 * @param panel
	 * @param action
	 */
	public Buttons(JoinPanel panel, String action) {
		this.joinPanel = panel;
		initializeButton(action);
	}

	/**
	 * Konstruktor für Buttons.
	 * @param panel
	 * @param action
	 */
	public Buttons(HelpPanel panel, String action) {
		this.helpPanel = panel;
		initializeButton(action);
	}
	 /**
	   * Der Code bereitet den Button vor und sagt ihm was er tun soll wenn man ihn klickt. Dabei werden auch die gundlegenden Einstellungen des Buttons festgelegt.
	   * 
	   * @param action sagt dass die Aktion, die der Button ausführen soll festgelegt ist.
	   */
	private void initializeButton(String action) {
		clicked = false;
		exited = true;
		this.action = action;
		this.imagePath = "/menu/" + action + ".png";
		this.imagePathHover = "/menu/" + action + "hover.png";
		this.imagePathClick = "/menu/" + action + "click.png";
		setProperties();
		addMouseListeners();
	}
	 /**
	  * Der Code sagt dem Button wie er aussehen soll. Er macht den Button durchsichtig.
	  */
	private void setProperties() {

		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
		setIcon(new ImageIcon(getClass().getResource(imagePath)));
	}
	 /**
	  * Der Code achtet darauf wenn die Maus darauf kommt, wenn sie weg geht, wenn darauf geklickt oder wenn der Klick losgelassen wird.
	  */
	private void addMouseListeners() {
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				exited = false;
				if (!clicked) {
					setIcon(new ImageIcon(getClass().getResource(imagePathHover)));			
				}
			}

			public void mouseExited(MouseEvent evt) {
				exited = true;
				setIcon(new ImageIcon(getClass().getResource(imagePath)));
			}

			public void mousePressed(MouseEvent evt) {
				clicked = true;
				setIcon(new ImageIcon(getClass().getResource(imagePathClick)));
			}

			public void mouseReleased(MouseEvent evt) {
				clicked = false;
				if (!exited) {
					setIcon(new ImageIcon(getClass().getResource(imagePathHover)));
					playButtonSound();
					mouseAction();
				} else {
					setIcon(new ImageIcon(getClass().getResource(imagePath)));
				}
			}
		});
	}
	 /**
	  * Hier zeigt der Code dass er die Bilder die auf dem Button erscheinen, ändern kann. 
	  *
	  * @param filename Wenn man ihm neuen Dateinamen gibt zeigt der Button andere Bilder an.
	  */
	public void changePath(String filename) {
		imagePath = "/menu/" + filename + ".png";
		imagePathHover = "/menu/" + filename + "hover.png";
		imagePathClick = "/menu/" + filename + "click.png";
		setIcon(new ImageIcon(getClass().getResource(imagePath)));
	}
	 /**
	  * Der Code spielt einen Ton ab welcher mit dem jeweiligen Button verknüpft ist. Wenn man den Button klickt wird ein Ton abgespielt.
	  */
	private void playButtonSound() {
		if (menuPanel != null) {
			menuPanel.getContentPanel().playButtonSound();
		} else if (hostPanel != null) {
			hostPanel.getContentPanel().playButtonSound();
		} else if (joinPanel != null) {
			joinPanel.getContentPanel().playButtonSound();
		} else if (helpPanel != null) {
			helpPanel.getContentPanel().playButtonSound();
		} else if (game != null) {
			game.getContentPanel().playButtonSound();
		}
	}
	/**
	* Der Code führt eine bestimmte Handlung aus je nachdem welchen Knopf du drückst und welcher Bereich im Spiel aktiv ist.
	* In diesem Teil, kann der Code zwischen verschiedenen Bereichen des Spieles wechseln. Je nachdem welcher Bereich gerade aktiv ist, führt er die entsprechende Handlung aus.
	*/
	private void mouseAction() {
		if (menuPanel != null) {
			switch (action) {
			case "spielhosten", "hostgame" -> menuPanel.switchToHost();
			case "spielbeitreten", "joingame" -> menuPanel.switchToJoin();
			case "hilfe", "help" -> menuPanel.switchToHelp();
			case "beenden", "exit" -> menuPanel.exit();
			}
		} else if (hostPanel != null) {
			switch (action) {
			case "spielstarten", "start" -> hostPanel.switchToGame();
			case "verlassen", "back" -> hostPanel.switchToMenu();
			}
		} else if (joinPanel != null) {
			switch (action) {
			case "spielbeitreten", "joingame" -> joinPanel.switchToGame();
			case "verlassen", "back" -> joinPanel.switchToMenu();
			}
		} else if (helpPanel != null) {
			switch (action) {
			case "hilfe", "help" -> helpPanel.switchToMenu();
			}
		} else if (game != null) {
			game.switchToMenu();
		}
	}

	public String getImagePath() {

		return null;
	}
}