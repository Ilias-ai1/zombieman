package entity;

import net.ClientHandler;
import net.Server;

//Thread, der schrittweise Änderungen an der Karte einleitet, die kurz nach dem Platzieren der Bombe erfolgen
public class MapUpdatesThrower extends Thread {
	boolean bombPlanted;
	int id, l, c;

	public MapUpdatesThrower(int id) {
		this.id = id;
		this.bombPlanted = false;
	}

	public void setBombPlanted(int x, int y) {
		x += Konstante.BREITE_SPRITE_SPIELER / 2;
		y += 2 * Konstante.HOEHE_SPRITE_SPIELER / 3;

		this.c = x / Konstante.SIZE_SPRITE_MAP;
		this.l = y / Konstante.SIZE_SPRITE_MAP;

		this.bombPlanted = true;
	}

	// Ändert die Karte auf dem Server und auf dem
	static void changeMap(String keyWord, int l, int c) {
		Server.getMap()[l][c].img = keyWord;
		ClientHandler.sendToAllClients("-1 mapUpdate " + keyWord + " " + l + " " + c);
	}

	int getColumnOfMap(int x) {
		return x / Konstante.SIZE_SPRITE_MAP;
	}

	int getLineOfMap(int y) {
		return y / Konstante.SIZE_SPRITE_MAP;
	}

	// prüft, ob die Explosion einen Spieler getroffen hat
	void checkIfExplosionKilledSomeone(int linSprite, int colSprite) {
		int linPlayer, colPlayer, x, y;

		for (int id = 0; id < Konstante.MAX_SPIELER; id++)
			if (Server.spieler[id].lebt) {
				x = Server.spieler[id].x + Konstante.BREITE_SPRITE_SPIELER / 2;
				y = Server.spieler[id].y + 2 * Konstante.HOEHE_SPRITE_SPIELER / 3;

				colPlayer = getColumnOfMap(x);
				linPlayer = getLineOfMap(y);

				if (linSprite == linPlayer && colSprite == colPlayer) {
					Server.spieler[id].lebt = false;
					ClientHandler.sendToAllClients(id + " newStatus dead");
				}
			}
	}

	public void run() {
		while (true) {
			if (bombPlanted) {
				bombPlanted = false;

				for (String index : Konstante.indexBombPlatziert) {
					changeMap("bomb-planted-" + index, l, c);
					try {
						sleep(Konstante.BOMBE_RATE_UPDATE);
					} catch (InterruptedException e) {
					}
				}

				// Explosionseffekte
				new Thrower("center-explosion", Konstante.indexExplosion, Konstante.FIRE_RATE_UPDATE, l, c).start();
				checkIfExplosionKilledSomeone(l, c);

				// unten
				if (Server.getMap()[l + 1][c].img.equals("floor-1")) {
					new Thrower("down-explosion", Konstante.indexExplosion, Konstante.FIRE_RATE_UPDATE, l + 1, c).start();
					checkIfExplosionKilledSomeone(l + 1, c);
				} else if (Server.getMap()[l + 1][c].img.contains("block"))
					new Thrower("block-on-fire", Konstante.indexBlockOnFire, Konstante.BLOCK_RATE_UPDATE, l + 1, c).start();

				// rechts
				if (Server.getMap()[l][c + 1].img.equals("floor-1")) {
					new Thrower("right-explosion", Konstante.indexExplosion, Konstante.FIRE_RATE_UPDATE, l, c + 1).start();
					checkIfExplosionKilledSomeone(l, c + 1);
				} else if (Server.getMap()[l][c + 1].img.contains("block"))
					new Thrower("block-on-fire", Konstante.indexBlockOnFire, Konstante.BLOCK_RATE_UPDATE, l, c + 1).start();

				// unten
				if (Server.getMap()[l - 1][c].img.equals("floor-1")) {
					new Thrower("up-explosion", Konstante.indexExplosion, Konstante.FIRE_RATE_UPDATE, l - 1, c).start();
					checkIfExplosionKilledSomeone(l - 1, c);
				} else if (Server.getMap()[l - 1][c].img.contains("block"))
					new Thrower("block-on-fire", Konstante.indexBlockOnFire, Konstante.BLOCK_RATE_UPDATE, l - 1, c).start();

				// links
				if (Server.getMap()[l][c - 1].img.equals("floor-1")) {
					new Thrower("left-explosion", Konstante.indexExplosion, Konstante.FIRE_RATE_UPDATE, l, c - 1).start();
					checkIfExplosionKilledSomeone(l, c - 1);
				} else if (Server.getMap()[l][c - 1].img.contains("block"))
					new Thrower("block-on-fire", Konstante.indexBlockOnFire, Konstante.BLOCK_RATE_UPDATE, l, c - 1).start();

				Server.spieler[id].setNumberOfBombs(Server.spieler[id].getNumberOfBombs() + 1); // libera bomba
			}
			try {
				sleep(0);
			} catch (InterruptedException e) {
			}
		}
	}
}

class Thrower extends Thread {
	String keyWord, index[];
	int l, c;
	int delay;

	Thrower(String keyWord, String index[], int delay, int l, int c) {
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
		// Nach der Explosion
		MapUpdatesThrower.changeMap("floor-1", l, c);
	}
}