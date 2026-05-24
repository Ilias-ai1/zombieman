package entity;

import java.io.IOException;
import java.util.SplittableRandom;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.MapPanel;
import net.ClientHandler;
import net.Server;

//Thread, der schrittweise Änderungen an der Karte einleitet, die kurz nach dem Platzieren der Bombe erfolgen
public class MapUpdatesThrower extends Thread {

	private boolean bombPlanted;
	private int id, l, c;
	private int explosionReichweite;
	private boolean bombFernsteurungActive = false;
	private AudioInputStream bgmAudioStream;
	private Clip bgmClip;
	MapPanel mapPanel;

	// Mit den Variablen werden die ungültige Fälle einer Explosion auf dem Map
	// behandelt.
	private boolean right, up, down, left;

	private static boolean running;

	public MapUpdatesThrower(int id) {
		this.id = id;
		this.bombPlanted = false;
		setRunning(true);

	}
/**
 * Position der platzierten Bombe.
 * 
 * @param x x-Koordinate.
 * @param y y-Koordinate.
 */
	public void setBombPlanted(int x, int y) {
		if (Server.getSpieler()[id].isLebt()) {
			x += Konstante.BREITE_SPRITE_SPIELER / 2;
			y += 2 * Konstante.HOEHE_SPRITE_SPIELER / 3;

			this.c = x / Konstante.SIZE_SPRITE_MAP;
			this.l = y / Konstante.SIZE_SPRITE_MAP;

			this.bombPlanted = true;
		}
	}

	/** Ändert die Karte auf dem Server und benachrichtigt alle Clients.
	 * 
	 * @param keyWord
	 * @param l
	 * @param c
	 */
	public static void changeMap(String keyWord, int l, int c) {
		Server.getMap()[l][c].setImg(keyWord);
		ClientHandler.sendToAllClients("-1 mapUpdate " + keyWord + " " + l + " " + c);
	}

	private int getColumnOfMap(int x) {
		return x / Konstante.SIZE_SPRITE_MAP;
	}

	private int getLineOfMap(int y) {
		return y / Konstante.SIZE_SPRITE_MAP;
	}
	private void sendPowerUpChange() {
		String puString = "";
		for(int i = 0; i<=4;i++)
			puString += Integer.toString(Server.getSpieler()[id].getPowerUps(i)) + " ";
		String string = id + " PowerUpChange " + puString;
		ClientHandler.sendToAllClients(string);
	}
	// Überprüft, ob die Explosion einen Spieler getroffen hat
	private void checkIfExplosionKilledSomeone(int linSprite, int colSprite) {
		int linPlayer, colPlayer, x, y;

		TimerTask taskRemoveOneLive = new TimerTask() {
			public void run() {
				Server.getSpieler()[id].removeOneLife();
				sendPowerUpChange();
			}
		};

		for (int id = 0; id < Konstante.MAX_SPIELER; id++) {
			if (Server.getSpieler()[id].isLebt()) {
				x = Server.getSpieler()[id].getX() + Konstante.BREITE_SPRITE_SPIELER / 2;
				y = Server.getSpieler()[id].getY() + 2 * Konstante.HOEHE_SPRITE_SPIELER / 3;

				colPlayer = getColumnOfMap(x);
				linPlayer = getLineOfMap(y);

				if ((linSprite == linPlayer) && (colSprite == colPlayer)) { // Reichweite von Bombe muss angepasst
																			// werden, sobald PowerUp gesammelt.
					if (Server.getSpieler()[id].getPowerUps(3) > 0) {
					} else if (Server.getSpieler()[id].getPowerUps(1) > 0) {

						Timer timer = new Timer();
						timer.schedule(taskRemoveOneLive, 1500);
					}

					else {
						Server.getSpieler()[id].setLebt(false);
						ClientHandler.sendToAllClients(id + " newStatus dead");
						if (Server.checkWinnerDetermined()) {
							for (int i = 0; i < Konstante.MAX_SPIELER; i++) {
								if (Server.getSpieler()[i].isLebt()) {
									ClientHandler.sendToAllClients(i + " winner");
								}
							}
						}
					}
				}
			}
		}	
	}	
	private void checkIfZoneKilledSomeone() {
		int linZoneTop = getLineOfMap(Server.getZoneSize());
		int linZoneBottom = getLineOfMap(Konstante.LIN * Konstante.SIZE_SPRITE_MAP - Server.getZoneSize());
		int colZoneLeft = getColumnOfMap(Server.getZoneSize());
		int colZoneRight = getColumnOfMap(Konstante.COL * Konstante.SIZE_SPRITE_MAP - Server.getZoneSize());

		for (int playerId = 0; playerId < Konstante.MAX_SPIELER; playerId++) {
			if (Server.getSpieler()[playerId].isLebt()) {
				int x = Server.getSpieler()[playerId].getX() + Konstante.BREITE_SPRITE_SPIELER / 2;
				int y = Server.getSpieler()[playerId].getY() + 2 * Konstante.HOEHE_SPRITE_SPIELER / 3;

				int xPlayer = getColumnOfMap(x);
				int yPlayer = getLineOfMap(y);

				if (xPlayer <= colZoneLeft || xPlayer >= colZoneRight || yPlayer <= linZoneTop
						|| yPlayer >= linZoneBottom) {
					Server.getSpieler()[playerId].setLebt(false);
					ClientHandler.sendToAllClients(playerId + " newStatus dead");
					if (Server.checkWinnerDetermined()) {
						for (int i = 0; i <= Konstante.MAX_SPIELER; i++) {
							if (Server.getSpieler()[id].isLebt() == true)
								ClientHandler.sendToAllClients(id + "winner");
						}
					}
				}
			}
		}
	}

	public void run() {
		while (running) {
			checkIfZoneKilledSomeone();
			if (bombPlanted) {
				bombPlanted = false;
				if (Server.getSpieler()[id].getPowerUps(4) == 0) {
					if (bombFernsteurungActive) {
						this.l = Server.getSpieler()[id].getFernFernsteuerungL();
						this.c = Server.getSpieler()[id].getFernFernsteuerungC();
						bombFernsteurungActive = false;
						
					}

					for (String index : Konstante.indexBombPlatziert) {
						changeMap("bomb-planted-" + index, l, c);
						try {
							sleep(Konstante.BOMBE_RATE_UPDATE);
						} catch (InterruptedException e) {
						}
					}

					explosionReichweite = Server.getSpieler()[id].getPowerUps(2);
					right = true;
					left = true;
					up = true;
					down = true;

					// Explosionseffekte
					new Thrower("center-explosion", Konstante.indexExplosion, Konstante.FIRE_RATE_UPDATE, l, c).start();
					checkIfExplosionKilledSomeone(l, c);
					for (int i = 1; i <= explosionReichweite; i++) {

						// unten
						if (Server.getMap()[l + i > 12 ? 12 : l + i][c].getImg().equals("floor-1") && down) {
							new Thrower("down-explosion", Konstante.indexExplosion, Konstante.FIRE_RATE_UPDATE, l + i,
									c).start();
							checkIfExplosionKilledSomeone(l + i, c);
						} else if (Server.getMap()[l + i > 12 ? 12 : l + i][c].getImg().contains("block") && down) {
							new Thrower("block-on-fire", Konstante.indexBlockOnFire, Konstante.BLOCK_RATE_UPDATE, l + i,
									c).start();
							addPowerUp(l + i, c);
							down = false; // Ungültiger Fall / Explosion nach unten wird blockiert
						} else if (Server.getMap()[l + i > 12 ? 12 : l + i][c].getImg().contains("center") && down) {
							down = false; // Ungültiger Fall / Explosion nach unten wird blockiert
						}

						// rechts
						if (Server.getMap()[l][c + i > 14 ? 14 : c + i].getImg().equals("floor-1") && right) {
							new Thrower("right-explosion", Konstante.indexExplosion, Konstante.FIRE_RATE_UPDATE, l,
									c + i).start();
							checkIfExplosionKilledSomeone(l, c + i > 14 ? 14 : c + i);
						} else if (Server.getMap()[l][c + i > 14 ? 14 : c + i].getImg().contains("block") && right) {
							new Thrower("block-on-fire", Konstante.indexBlockOnFire, Konstante.BLOCK_RATE_UPDATE, l,
									c + i).start();
							addPowerUp(l, c + i);
							right = false; // Ungültiger Fall / Explosion nach rechts wird blockiert
						} else if (Server.getMap()[l][c + i > 14 ? 14 : c + i].getImg().contains("center") && right) {
							right = false; // Ungültiger Fall / Explosion nach rechts wird blockiert
						}

						// oben
						if (Server.getMap()[l - i < 0 ? 0 : l - i][c].getImg().equals("floor-1") && up) {
							new Thrower("up-explosion", Konstante.indexExplosion, Konstante.FIRE_RATE_UPDATE, l - i, c)
									.start();
							checkIfExplosionKilledSomeone(l - i, c);
						} else if (Server.getMap()[l - i < 0 ? 0 : l - i][c].getImg().contains("block") && up) {
							new Thrower("block-on-fire", Konstante.indexBlockOnFire, Konstante.BLOCK_RATE_UPDATE, l - i,
									c).start();
							addPowerUp(l - i, c);
							up = false; // Ungültiger Fall / Explosion nach oben wird blockiert
						} else if (Server.getMap()[l - i < 0 ? 0 : l - i][c].getImg().contains("center") && up) {
							up = false; // Ungültiger Fall / Explosion nach oben wird blockiert
						}

						// links
						if (Server.getMap()[l][c - i < 0 ? 0 : c - i].getImg().equals("floor-1") && left) {
							new Thrower("left-explosion", Konstante.indexExplosion, Konstante.FIRE_RATE_UPDATE, l,
									c - i).start();
							checkIfExplosionKilledSomeone(l, c - i);
						} else if (Server.getMap()[l][c - i < 0 ? 0 : c - i].getImg().contains("block") && left) {
							new Thrower("block-on-fire", Konstante.indexBlockOnFire, Konstante.BLOCK_RATE_UPDATE, l,
									c - i).start();
							addPowerUp(l, c - i);
							left = false; // Ungültiger Fall / Explosion nach links wird blockiert
						} else if (Server.getMap()[l][c - i < 0 ? 0 : c - i].getImg().contains("center") && left) {
							left = false; // Ungültiger Fall / Explosion nach links wird blockiert
						}
					}
					playExplosionSound();
					Server.getSpieler()[id].setNumberOfBombs(Server.getSpieler()[id].getNumberOfBombs() + 1);
				} else if (Server.getSpieler()[id].getPowerUps(4) == 1 && !bombFernsteurungActive) {
					changeMap("bomb-planted-1", l, c);
					bombFernsteurungActive = true;
					Server.getSpieler()[id].setFernFernsteuerungL(l);
					Server.getSpieler()[id].setFernFernsteuerungC(c);
					Server.getSpieler()[id].removePowerUps(4);
					Server.getSpieler()[id].setNumberOfBombs(Server.getSpieler()[id].getNumberOfBombs() + 1);
					sendPowerUpChange();
				}
			}
			try {
				sleep(0);
			} catch (InterruptedException e) {
			}
		}
	}// run

	private void playExplosionSound() {

		try {
			bgmAudioStream = AudioSystem.getAudioInputStream(getClass().getResource("/sound/glasbombe.wav"));
			bgmClip = AudioSystem.getClip();
			bgmClip.open(bgmAudioStream);
			bgmClip.start();
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
			ex.printStackTrace();
		}
	}

	private void addPowerUp(int l, int c) {

		SplittableRandom random = new SplittableRandom();
		boolean isPowerUp = random.nextInt(1, 101) <= Konstante.WAHRSCHEINLICHKEITS_NIVEAU;

		int whichPowerUp; // for different powerUps

		/*
		 * PowerUp Type:
		 * 
		 * 0 - Geschwindigkeitserhöhung 1 - Extraleben 2 - Erhöhung der
		 * Explosionsreichweite 3 - Unsterblichkeit 4 - Fernsteuerung des
		 * Explosionsgegenstands per Knopfdruck
		 * 
		 */
		if (isPowerUp) {
			whichPowerUp = random.nextInt(0, 5);
			PowerUpThrower prozess = new PowerUpThrower(id, whichPowerUp, l, c);
			prozess.start();
		}
	}

	public static void setRunning(boolean privateRunning) {
		running = privateRunning;
	}

	private class Thrower extends Thread {
		private String keyWord, index[];
		private int l, c;
		private int delay;

		public Thrower(String keyWord, String index[], int delay, int l, int c) {
			this.keyWord = keyWord;
			this.index = index;
			this.delay = delay;
			this.l = l;
			this.c = c;
		}


		public void run() {
			for (String i : index) {
				MapUpdatesThrower.changeMap(keyWord + "-" + i, l, c);
				try {
					sleep(delay);
				} catch (InterruptedException e) {
				}
			}

			MapUpdatesThrower.changeMap("floor-1", l, c);

		}
	}
}
