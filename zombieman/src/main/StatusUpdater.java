package main;

import entity.Konstante;
import entity.Spieler;
import entity.Sprite;

/**
 * @author BoOom
 * @version 1.0
 * 
 * @see Konstante
 * @see Spieler
 * @see Sprite Die Klasse StatusUpdater ist ein Thread, der für die
 *      Aktualisierung des Status eines Spieler-Sprites verantwortlich ist. Er
 *      aktualisiert kontinuierlich den Status des Spielers und zeichnet das
 *      Panel entsprechend neu.
 *
 */
public class StatusUpdater extends Thread {

	/** Der Spieler, dessen Status aktualisiert wird. */
	private Spieler sp;

	/** Der aktuelle Status des Spieler-Sprites. */
	private String status;

	/** Der Index wird verwendet für das Durchlaufen von Sprite-Animationen. */
	private int index;

	/**
	 * Das boolean gibt an ob sich ein spieler bewegt und der Sprite-Status
	 * aktualisiert werden soll.
	 */
	private boolean playerInMotion;
	private static boolean running;

	/**
	 * Konstruiert einen StatusUpdater für den angegebenen Spieler mit dem gegebenen
	 * Anfangsstatus.
	 * 
	 * @param p
	 * @param initialStatus
	 */
	public StatusUpdater(Spieler p, String initialStatus) {
		this.sp = p;
		this.status = initialStatus;
		index = 0;
		playerInMotion = true;
		setRunning(true);

	}

	/**
	 * Die run-Methode aktualisiert kontinuierlich den Status des Spieler-Sprites,
	 * zeichnet das Panel neu und überprüft dabei besondere Bedingungen wie den Tod
	 * des Spielers.
	 */
	public void run() {
		while (isRunning()) {
			sp.setStatus(status + "-" + index);
			if (playerInMotion) {
				index = (++index) % Sprite.maxLoopStatus.get(status);
				sp.getPanel().repaint();
			}

			try {
				Thread.sleep(Konstante.SPIELER_STATUS_RATE_UPDATE);
			} catch (InterruptedException e) {
				// hier Behandlung ,falls erforderlich der unterbrochenen Ausnahme
			}

			if (sp.getStatus().equals("dead-4")) {
				sp.setLebt(false);
			}
		}
	}

	/**
	 * Setzt einen Schleifenstatus für das Sprite des Spielers.
	 * 
	 * @param status
	 */
	public void setLoopStatus(String status) {
		this.status = status;
		index = 1;
		playerInMotion = true;
	}

	/**
	 * Hier wird die Schleife gestoppt.
	 */
	public void stopLoopStatus() {
		playerInMotion = false;
		index = 0;
	}

	/**
	 * Überprüft, ob der Thread ausgeführt wird.
	 * @return
	 */
	public static boolean isRunning() {
		return running;
	}

	/**
	 * Hier wird der Zustand des Threads gesetzt.
	 * @param privateRunning false um Thread zu stoppen und true für weiter
	 */
	public static void setRunning(boolean privateRunning) {
		running = privateRunning;
	}
}
