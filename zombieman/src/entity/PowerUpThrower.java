
package entity;

import java.util.Timer;
import java.util.TimerTask;

import net.ClientHandler;
import net.Server;

/**
 * @author BoOom
 * @see ClientHandler
 * @see Server
 * Die Klasse PowerUpThrower repräsentiert ein Thread und die Klasse ist für das Platzieren und Entfernen von Power-Ups 
 * auf der Spielkarte für einen bestimmten Spieler verantwortlich.
 * Diese Klasse ermöglicht das zeitgesteuerte Platzieren und Entfernen von Power-Ups auf der Spielkarte für jeden Spieler im Spiel.
 * Die Klasse verwendet Timer, um Power-Up-Effekte auf der Karte zu steuern und den Spielern mitzuteilen.
 */
public class PowerUpThrower extends Thread {

	private boolean powerUpPlanted;
	private int id, l, c, whichPowerUp;
	private Timer timerForPlayer = new Timer("TimerForPlayer");
	private Timer timerForMap = new Timer("TimerForMap");
	private int linPlayer, colPlayer, x, y;
	private static boolean running;

	/**
	 * Der Code gibt die Anweisung ein Power-Up zu platzieren.
	 * Es sagt welcher Power-Up es sein soll und wo es im Spiel platziert werden soll.
	 * 
	 * @param id ordnet das Power-Up dem Spieler zu
	 * @param whichPowerUp gibt an um welche Art von Power-Up es sich handelt
	 * @param l gibt den vertikalen Standort des Power-Ups an.
	 * @param c gibt den horizontalen Standort des Power-Ups an.
	 */
	public PowerUpThrower(int id, int whichPowerUp, int l, int c) {
		this.id = id;
		this.powerUpPlanted = true;
		this.l = l;
		this.c = c;
		this.whichPowerUp = whichPowerUp;
		setRunning(true);
	}

	/**
	 * Dem Programm wird mitgeteilt dass ein bestimmtes Power-Up an einer bestimmten Stelle plaziert wurde.
	 * 
	 * @param x die Position an der x-Achse.
	 * @param y die Position an der y-Achse.
	 */
	public void setPowerUpPlanted(int x, int y) {
		x += Konstante.BREITE_SPRITE_SPIELER / 2;
		y += 2 * Konstante.HOEHE_SPRITE_SPIELER / 3;

		this.c = x / Konstante.SIZE_SPRITE_MAP;
		this.l = y / Konstante.SIZE_SPRITE_MAP;

		this.powerUpPlanted = true;
	}
	/**
	* Nach einer bestimmten Zeit wird das Power-Up vom Spieler entfernt.
	*/
	TimerTask taskRemovePowerUpFromPlayer = new TimerTask() {
		public void run() {
			Server.getSpieler()[id].removePowerUps(whichPowerUp);
			Server.getSpieler()[id].whichPowerUpIs();

			sendPowerUpChange();
		}
	};
	
	/**
	 * Sendet eine Nachricht an alle Clients, die über die Änderung der Power-Ups für den Spieler informiert.
	 */
	public void sendPowerUpChange() {
		String puString = "";
		for(int i = 0; i<=4;i++)
			puString += Integer.toString(Server.getSpieler()[id].getPowerUps(i)) + " ";
		String string = id + " PowerUpChange " + puString;
		ClientHandler.sendToAllClients(string);
	}
	
	/**
	 * Die Hauptlaufmethode, die für das Platzieren von Power-Ups und die Interaktion mit Spielern verantwortlich ist
	 */
	public void run() {

		while (running) {
			if (powerUpPlanted) {
				MapUpdatesThrower.changeMap("power-up-" + whichPowerUp, l, c);

				TimerTask taskRemovePowerUpFromMap = new TimerTask() {
					public void run() {
						MapUpdatesThrower.changeMap("floor-1", l, c);
						powerUpPlanted = false;
					}
				};

				timerForMap.schedule(taskRemovePowerUpFromMap, Konstante.POWER_UP_ON_MAP);

				x = Server.getSpieler()[id].getX() + Konstante.BREITE_SPRITE_SPIELER / 2;
				y = Server.getSpieler()[id].getY() + 2 * Konstante.HOEHE_SPRITE_SPIELER / 3;

				colPlayer = x / Konstante.SIZE_SPRITE_MAP;
				linPlayer = y / Konstante.SIZE_SPRITE_MAP;

				if (l == linPlayer && c == colPlayer) {

					MapUpdatesThrower.changeMap("floor-1", l, c);

					Server.getSpieler()[id].addPowerUps(whichPowerUp);
					Server.getSpieler()[id].whichPowerUpIs();

					if (whichPowerUp == 0 || whichPowerUp == 3) {
						timerForPlayer.schedule(taskRemovePowerUpFromPlayer, Konstante.POWER_UP_TIMER_FOR_PLAYER);
					}
					powerUpPlanted=false;
					sendPowerUpChange();
				}
			}
			try {
				sleep(200);
			} catch (InterruptedException e) {
			}
		}
		
	}
	/**
	* Sagt dem Programm ob der PowerUpThrower-Thread gerade aktiv sein soll.
	* 
	* @param privateRunning Teilt dem Programm mit ob der PowerUpThrower-Thread laufen soll.
	*/
	public static void setRunning(boolean privateRunning) {
		running = privateRunning;
	}
}
