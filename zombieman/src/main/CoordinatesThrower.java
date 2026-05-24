package main;

import java.awt.event.KeyEvent;
import net.*;
import entity.*;


/**
 * Thread, der die nächsten Koordinaten an Clients sendet, während W/A/S/D nicht freigegeben ist.
 * @author BoOom
 * @see Konstante
 * @see MapUpdateThrower
 * @see PowerUpThrower
 * @see Spieler
 * @see SpielerDaten
 * @see Sprite
 * @see Client
 * @see ClientHandler
 * @see Receiver
 * @see Sender
 * @see Server
 * 
 */
public class CoordinatesThrower extends Thread {

	private boolean up, right, left, down;
	private int id;
	private int speed = 1;
	private static boolean running;

	/**
	 * Ein Konstruktor für CoordinatesThrower.
	 * @param id Die eindeutige Kennung für Spieler 
	 */
	public CoordinatesThrower(int id) {
		this.id = id;
		up = down = right = left = false;
		setRunning(true);

	}

	/**
	 * Hier wird die Geschwindigkeit für die Bewegung gesetzt.
	 */
	public void setSpeed() {
		this.speed = 2 * speed;
	}

	/*
	 * falls PowerUp Geschwindigkeit gesammelt wird, wird die powerUpSpeed auf true
	 * gesetzt und in den PowerUps Fällen für bestimmte Zeit reingegangen. (Timer
	 * fehlt noch)
	 */
	boolean powerUpSpeed = true; // zum Testen dauerhaft auf true gesetzt

	/**
	 * Hier ist die Hauptmethode für die Thread.
	 */
	public void run() {
		int newX = Server.getSpieler()[id].getX();
		int newY = Server.getSpieler()[id].getY();

		while (running) {
			if (up || down || right || left) {

				speed = (Server.getSpieler()[id].getPowerUps(0) == 0) ? 1 : 2;

				if (up) {
					newY = Server.getSpieler()[id].getY() - speed * Konstante.RESIZE;
				} else if (down) {
					newY = Server.getSpieler()[id].getY() + speed * Konstante.RESIZE;
				} else if (right) {
					newX = Server.getSpieler()[id].getX() + speed * Konstante.RESIZE;
				} else if (left)
					newX = Server.getSpieler()[id].getX() - speed * Konstante.RESIZE;

				if (coordinateIsValid(newX, newY)) {
					ClientHandler.sendToAllClients(id + " newCoordinate " + newX + " " + newY);

					Server.getSpieler()[id].setX(newX);
					Server.getSpieler()[id].setY(newY);
				} else {
					newX = Server.getSpieler()[id].getX();
					newY = Server.getSpieler()[id].getY();
				}

				try {
					sleep(Konstante.KOORDINATEN_RATE_UPDATE);
				} catch (InterruptedException e) {
				}
			}
			try {
				sleep(0);
			} catch (InterruptedException e) {
			}
		}

	}
	/**
	 * Gibt die Spalte zurück von der Map.
	 * @param x Die x-Koordinate
	 * @return
	 */
	private int getColumnOfMap(int x) {
		return x / Konstante.SIZE_SPRITE_MAP;
	}

	/**
	 * Gibt die Zeile zurück von der Map.
	 * @param y Die y-Koordinate
	 * @return
	 */
	private int getLineOfMap(int y) {
		return y / Konstante.SIZE_SPRITE_MAP;
	}

	/**
	 * Findet heraus, auf welchen Karten-Sprites sich der Spieler befindet, und prüft, ob sie gültig sind
	 * @param newX
	 * @param newY
	 * @return
	 */
	private boolean coordinateIsValid(int newX, int newY) {
		if (!Server.getSpieler()[id].isLebt())
			return false;

		// Überprüft, ob der Spieler von der Explosion getroffen wurde
		// (Körperzentrumskoordinate)
		int xBody = newX + Konstante.BREITE_SPRITE_SPIELER / 2;
		int yBody = newY + 2 * Konstante.HOEHE_SPRITE_SPIELER / 3;

		if (Server.getMap()[getLineOfMap(yBody)][getColumnOfMap(xBody)].getImg().contains("explosion")) {
			if (Server.getSpieler()[id].getPowerUps(1) == 0 && Server.getSpieler()[id].getPowerUps(3) == 0) {
				Server.getSpieler()[id].setLebt(false);
				ClientHandler.sendToAllClients(id + " newStatus dead");
				return true;
			}
		}

		int x[] = new int[4], y[] = new int[4];
		int c[] = new int[4], l[] = new int[4];

		// die neuen Koords

		// 0: oben links
		x[0] = Konstante.VAR_X_SPRITES + newX + Konstante.RESIZE;
		y[0] = Konstante.VAR_Y_SPRITES + newY + Konstante.RESIZE;
		// 1: oben rechts
		x[1] = Konstante.VAR_X_SPRITES + newX + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
		y[1] = Konstante.VAR_Y_SPRITES + newY + Konstante.RESIZE;
		// 2: unten links
		x[2] = Konstante.VAR_X_SPRITES + newX + Konstante.RESIZE;
		y[2] = Konstante.VAR_Y_SPRITES + newY + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
		// 3: unten rechts
		x[3] = Konstante.VAR_X_SPRITES + newX + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
		y[3] = Konstante.VAR_Y_SPRITES + newY + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;

		for (int i = 0; i < 4; i++) {
			c[i] = getColumnOfMap(x[i]);
			l[i] = getLineOfMap(y[i]);
		}

		if ((Server.getMap()[l[0]][c[0]].getImg().contains("floor")
						|| Server.getMap()[l[0]][c[0]].getImg().contains("explosion"))
				&& (Server.getMap()[l[1]][c[1]].getImg().contains("floor")
						|| Server.getMap()[l[1]][c[1]].getImg().contains("explosion"))
				&& (Server.getMap()[l[2]][c[2]].getImg().contains("floor")
						|| Server.getMap()[l[2]][c[2]].getImg().contains("explosion"))
				&& (Server.getMap()[l[3]][c[3]].getImg().contains("floor")
						|| Server.getMap()[l[3]][c[3]].getImg().contains("explosion"))) {
			return true; // nächste Koord ist gültig
		}
		if ((Server.getMap()[l[0]][c[0]].getImg().contains("block")
						|| Server.getMap()[l[0]][c[0]].getImg().contains("wall")
						|| Server.getMap()[l[0]][c[0]].getImg().contains("suddendeathzone"))
				|| (Server.getMap()[l[1]][c[1]].getImg().contains("block")
						|| Server.getMap()[l[1]][c[1]].getImg().contains("wall")
						|| Server.getMap()[l[1]][c[1]].getImg().contains("suddendeathzone"))
				|| (Server.getMap()[l[2]][c[2]].getImg().contains("block")
						|| Server.getMap()[l[2]][c[2]].getImg().contains("wall")
						|| Server.getMap()[l[2]][c[2]].getImg().contains("suddendeathzone"))
				|| (Server.getMap()[l[3]][c[3]].getImg().contains("block")
						|| Server.getMap()[l[3]][c[3]].getImg().contains("wall")
						|| Server.getMap()[l[3]][c[3]].getImg().contains("suddendeathzone"))) {

			return false; // nächste Koord nicht gültig
		}
		if ((Server.getMap()[l[0]][c[0]].getImg().contains("power"))
				|| (Server.getMap()[l[1]][c[1]].getImg().contains("power"))
				|| (Server.getMap()[l[2]][c[2]].getImg().contains("power"))
				|| (Server.getMap()[l[3]][c[3]].getImg().contains("power"))) {
			return true; // nächste Koord ist gültig
		}
		// die vorherigen Koords

		// 0: oben links
		x[0] = Konstante.VAR_X_SPRITES + Server.getSpieler()[id].getX() + Konstante.RESIZE;
		y[0] = Konstante.VAR_Y_SPRITES + Server.getSpieler()[id].getY() + Konstante.RESIZE;
		// 1: oben rechts
		x[1] = Konstante.VAR_X_SPRITES + Server.getSpieler()[id].getX() + Konstante.SIZE_SPRITE_MAP
				- 2 * Konstante.RESIZE;
		y[1] = Konstante.VAR_Y_SPRITES + Server.getSpieler()[id].getY() + Konstante.RESIZE;
		// 2: unten links
		x[2] = Konstante.VAR_X_SPRITES + Server.getSpieler()[id].getX() + Konstante.RESIZE;
		y[2] = Konstante.VAR_Y_SPRITES + Server.getSpieler()[id].getY() + Konstante.SIZE_SPRITE_MAP
				- 2 * Konstante.RESIZE;
		// 3: unten rechts
		x[3] = Konstante.VAR_X_SPRITES + Server.getSpieler()[id].getX() + Konstante.SIZE_SPRITE_MAP
				- 2 * Konstante.RESIZE;
		y[3] = Konstante.VAR_Y_SPRITES + Server.getSpieler()[id].getY() + Konstante.SIZE_SPRITE_MAP
				- 2 * Konstante.RESIZE;

		for (int i = 0; i < 4; i++) {
			c[i] = getColumnOfMap(x[i]);
			l[i] = getLineOfMap(y[i]);
		}

		if (Server.getMap()[l[0]][c[0]].getImg().contains("bomb-planted")
				|| Server.getMap()[l[1]][c[1]].getImg().contains("bomb-planted")
				|| Server.getMap()[l[2]][c[2]].getImg().contains("bomb-planted")
				|| Server.getMap()[l[3]][c[3]].getImg().contains("bomb-planted")) 
			return true; // Bombe wird platziert
		
		return false;
	}

	public void keyCodePressed(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_W:
			up = true;
			down = right = left = false;
			ClientHandler.sendToAllClients(this.id + " newStatus up");
			break;
		case KeyEvent.VK_S:
			down = true;
			up = right = left = false;
			ClientHandler.sendToAllClients(this.id + " newStatus down");
			break;
		case KeyEvent.VK_D:
			right = true;
			up = down = left = false;
			ClientHandler.sendToAllClients(this.id + " newStatus right");
			break;
		case KeyEvent.VK_A:
			left = true;
			up = down = right = false;
			ClientHandler.sendToAllClients(this.id + " newStatus left");
			break;
		}
	}

	/**
	 * Wird aufgerufen, wenn eine Taste gedrückt wird, um die Richtung der Spielerbewegung zu aktualisieren.
	 * @param keyCode die gedrückte Taste 
	 */
	public void keyCodeReleased(int keyCode) {
		if (keyCode != KeyEvent.VK_W && keyCode != KeyEvent.VK_S && keyCode != KeyEvent.VK_D
				&& keyCode != KeyEvent.VK_A) {
			return;
		}
		ClientHandler.sendToAllClients(this.id + " stopStatusUpdate");
		switch (keyCode) {
		case KeyEvent.VK_W: up = false;
			break;
		case KeyEvent.VK_S:	down = false;
			break;
		case KeyEvent.VK_D: right = false;
			break;
		case KeyEvent.VK_A: left = false;
			break;
		}
	}

	/**
	 * Hier wird der Zustand des Threads  gesetzt ,ob ausgefüht wird oder nicht.
	 * @param privateRunning
	 */
	public static void setRunning(boolean privateRunning) {
		running = privateRunning;
	}
}
