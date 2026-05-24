package net;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import main.Game;

/**
 * Die Klasse Sender erweitert KeyAdapter und ermöglicht das Senden von
 * Tastatureingaben von dem Client an den Server.
 * 
 * Unterstützt wird die Eingabe der Leertaste (zum legen einer Bombe), sowie das
 * Signal von Drücken und Loslassen von weiteren Tasten (Bewegung des Players).
 * 
 * @author BoOom
 * @version 1.0
 * 
 * @see Client
 */
public class Sender extends KeyAdapter {

	/** Die zuletzt gedrückte Teste. */
	private int lastKeyCodePressed;
	private static boolean running;

	public Sender() {
		setRunning(true);

	}

	/**
	 * Wird aufgerufen, wenn eine Taste gedrückt wird. Sendet daraufhin die
	 * gedrückte Taste an den Server.
	 *
	 * @param e ist ein KeyEvent, welches die gedrückte Taste enthält.
	 */
	public void keyPressed(KeyEvent e) {
		if (running) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				Client.getOut().println("pressedSpace " + Game.getDu().getX() + " " + Game.getDu().getY());
			} else if (isNewKeyCode(e.getKeyCode())) {
				Client.getOut().println("keyCodePressed " + e.getKeyCode());
			}
		}

	}

	/**
	 * Wird aufgerufen, wenn eine Taste losgelassen wird. Sendet daraufhin die
	 * losgelassene Taste an den Server.
	 * 
	 * @param e ist ein KeyEvent, welches die losgelassene Taste enthält.
	 */
	public void keyReleased(KeyEvent e) {
		if (running) {
			Client.getOut().println("keyCodeReleased " + e.getKeyCode());
			lastKeyCodePressed = -1; // der nächste Schlüssel wird immer neu sein
		}
	}

	/**
	 * Überprüfung ob eine unterschiedliche Taste als zuvor gedrückt wird
	 *
	 * @param keyCode ist der Key-Code in Form eines Integers
	 * @return true, wenn eine andere andere Taste gedrückt wird, wenn nicht false.
	 */
	boolean isNewKeyCode(int keyCode) {
		boolean ok = (keyCode != lastKeyCodePressed) ? true : false;
		lastKeyCodePressed = keyCode;
		return ok;
	}

	/**
	 * Setzt den Zustand der Klasse 
	 * @param privateRunning
	 */
	public static void setRunning(boolean privateRunning) {
		running = privateRunning;
	}
}
