package net;

import javax.swing.SwingUtilities;

import entity.Konstante;
import entity.Spieler;
import main.Game;
import main.MapPanel;

//Erhält alle Infos von Clients
/**
 * @author BoOom
 * @see Konstante 
 * @see Spieler
 * @see Game 
 * @see MapPanel
 * Diese Klasse Empfängt Information von Clients und ist Verantworlich für das Aktualisieren des Spiels.
 * Die Klasse repräsentiert eine Thread-Klasse, für die gleichzeitige Kommunikation.
 */
public class Receiver extends Thread {
	
	/*s Stellt eine Spieler-Entität dar.*/
	private Spieler s;
	/* Stellt Client Verbindung her.*/
	private Client client;
	/*Das gibt an ob der Receiver Läuft oder nicht.*/
	private static boolean running;

	/**
	 * Konsturiet eine neue Receiver, die mit einem Client verbinden ist.
	 * @param client
	 */
	public Receiver(Client client) {
		this.client = client;
		setRunning(true);
	}

	/**
	 * Ordnet die Spieler anhand der ID zu.
	 * @param id
	 * @return der Spieler der mit id verknupft ist 
	 */
	private Spieler fromWhichPlayerIs(int id) {
		if (id == Client.getClientId())
			return Game.getDu();
		else if (id == (Client.getClientId() + 1) % Konstante.MAX_SPIELER)
			return Game.getGegner1();
		else if (id == (Client.getClientId() + 2) % Konstante.MAX_SPIELER)
			return Game.getGegner2();
		else if (id == (Client.getClientId() + 3) % Konstante.MAX_SPIELER)
			return Game.getGegner3();
		return null;
	}

	/**
	 * Hier wird der Thread ausgeführt, und synchronizierd eingehende Nachrichten vom Client.
	 */
	public synchronized void run() {
		String str;
		while (Client.getIn().hasNextLine() && running) {

			this.s = fromWhichPlayerIs(Client.getIn().nextInt()); // id des Clients
			str = Client.getIn().next();

			if (str.equals("mapUpdate")) { // p null
				 // Aktualisiere die Spielkarte und löse ein Repaint aus
				Game.setSpriteMap(Client.getIn().next(), Client.getIn().nextInt(), Client.getIn().nextInt());
				SwingUtilities.invokeLater(() -> {
					Game.getDu().getPanel().repaint();
				});
			} else if (str.equals("newCoordinate")) {
				// Aktualisiere Spielerkoordinaten und repainte das Panel
				s.setX(Client.getIn().nextInt());
				s.setY(Client.getIn().nextInt());
				Game.getDu().getPanel().repaint();
			} else if (str.equals("newStatus")) {
				// Aktualisiere den Spielerstatus-Loop
				s.getSc().setLoopStatus(Client.getIn().next());
			} else if (str.equals("stopStatusUpdate")) {
				// Stoppe den Spielerstatus-Loop
				s.getSc().stopLoopStatus();
			} else if (str.equals("playerJoined")) {
				// Markiere den Spieler als lebendig
				s.setLebt(true);
			} else if (str.equals("sendName")) {
				// Empfange und setze den Namen des Spielers
				String name = Client.getIn().next();
				s.setName(name);
//				s.name = Client.in.next();
			} else if (str.equals("timerUpdate")) {
				// Aktualisiere die Zeit im Timer
				int timeInSeconds = Integer.parseInt(Client.getIn().next());
				if(timeInSeconds < 0) {
					timeInSeconds = 0;
				}
				Game.setElapsedTimeInSecondsTimer((timeInSeconds));
			} else if (str.equals("countdownTimerUpdate")) {
				 // Aktualisiere die verbleibende Zeit im Countdown-Timer
				int timeInSeconds = Integer.parseInt(Client.getIn().next());
				MapPanel.setElapsedTimeInSecondsCountdownTimer((timeInSeconds));
				
			} else if (str.equals("suddendeath")) {
				 // Starte den Sudden Death-Modus
				MapPanel.getSuddenDeath().setZoneSize(0);
				MapPanel.getSuddenDeath().start();
			} else if (str.equals("suddendeathUpdate")) {
				 // Aktualisiere den Sudden Death-Modus
				MapPanel.getSuddenDeath().setZoneSize(Client.getIn().nextInt());
				MapPanel.getSuddenDeath().update();
			} else if (str.equals("winner")) {
				// Behandelt den Fall, wenn ein Spieler gewinnt
				if (s == Game.getDu()) {
					client.addSiege();
					Client.getOut().println("Win " + client.getSiege());
				}
				s.setSiege(1);
			} else if (str.equals("newRound")) {
				 // Beendet das Spiel für die aktuelle Runde
				client.shutdown(false, "null");
			} else if (str.equals("Champion")) {
				 // Beendet das Spiel und deklariere einen Gewinner
				client.shutdown(true, s.getName());
			} else if (str.equals("PowerUpChange")) {
				// Aktualisiert die Power-Ups des Spielers
				for(int i = 0; i<=4;i++)
					s.setPowerUp(i,Client.getIn().nextInt());
			} else if (str.equals("score")) {
				int score = Client.getIn().nextInt();
				s.setSiege(score);
			}
		}
		Client.getIn().close();
	}

	/**
	 * Hier wird der Zustand des Receivers gesetzt 
	 * @param privateRunning
	 */
	public static void setRunning(boolean privateRunning) {
		running = privateRunning;
	}
}
