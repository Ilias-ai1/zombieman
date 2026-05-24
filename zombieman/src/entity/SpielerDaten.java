/**
 * Diese Klasse ist für die Daten der Spieler.
 */
package entity;

import net.Server;

public class SpielerDaten {
	private boolean logged;
	private boolean lebt;
	private int x; // aktuelle Koords
	private int y;
	private int numberOfBombe;
	private int powerUps[] = { 0, 0, 1, 0, 0 };
	private int wins;
	private boolean extraLife = false;
	private int fernFernsteuerungL;
	private int fernFernsteuerungC;
/**
 *  Konstruktor für die SpielerDaten-Klasse. Initialisiert die Position, den Status und die Anzahl der Bomben des Spielers.
 * 
 * @param x Die x-Koordinate des Spielers
 * @param y Die y-Koordinate des Spielers
 */
	public SpielerDaten(int x, int y) {
		this.setX(x);
		this.setY(y);
		this.setLogged(false);
		this.setLebt(false);
		this.setNumberOfBombs(1); // Bei 2 Bomben, wird je Bombe im eigenen Thread behandelt
	}
/**
 * Setzt die Power-Ups der Spieler zurück.
 */
	public void resetPowerUps() {
		for(int i = 0; i<5; i++)
			getPowerUps()[i]=0;
	}
/**
 * Gibt an welchen Power-Up der Spieler hat.
 */
	public void whichPowerUpIs() {

		for(int i = 0; i<5; i++)
			System.out.println(getPowerUps()[i]);
	}
/**
 * Erhöht Power-Ups um eins.
 * 
 * @param i Index des Power-Ups.
 */
	public void addPowerUps(int i) {
		System.out.println("whichPowerup"+ i);
		this.getPowerUps()[i]++;
	}
/**
 * Verringert Power-Ups um eins.
 * 
 * @param i Index des Power-Ups.
 */
	public void removePowerUps(int i) {

		System.out.println("whichPowerup"+ i);
		this.getPowerUps()[i]--;
	}
	/**
	 * Verringert Anzahl der Extraleben um eins.
	 */
	public void removeOneLife() {
		this.powerUps[1]--;

	}
/**
 * Gibt Anzahl eines Power-Ups an.
 * 
 * @param i Index des Power-Ups.
 * @return Die Anzahl des angegebenen Power-Ups.
 */
	public int getPowerUps(int i) {
		return this.powerUps[i];
	}
/**
 * Überprüft ob Spieler eingeloggt ist.
 * 
 * @return Wenn Spieler eingeloggt ist dann, True, ansonsten False.
 */
	public boolean isLogged() {
		return logged;
	}
/**
 * Aktualisiert den Einlogstatus des Spielers
 * 
 * @param logged Wenn er eingeloggt ist dann,True, ansonsten False.
 */
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
/**
 * Gibt an wieviele Bomben der Spieler legen kann.
 * 
 * @return Anzahl der Bomben.
 */
	public int getNumberOfBombs() {
		return numberOfBombe;
	}
/**
 * Setzt die Anzahl der Bomben die der Spiler setzen kann.
 * 
 * @param numberOfBombs Anzahl der Bomben.
 */
	public void setNumberOfBombs(int numberOfBombs) {
		this.numberOfBombe = numberOfBombs;
	}
/**
 * Überprüft ob Spieler lebt.
 * 
 * @return Wenn Spiler lebt, True, ansonsten False
 */
	public boolean isLebt() {
		return lebt;
	}
/**
 * Aktualisiert den Status ob Spieler lebt.
 * 
 * @param lebt Wenn der Spieler lebt dann,True ansonsten False.
 */
	public void setLebt(boolean lebt) {
		this.lebt = lebt;
	}
/**
 * Gibt die x-Koordinate an.
 * 
 * @return die x-Koordinate.
 */
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
/**
 * Gibt die y-Koordinate an.
 * 
 * @return die y-Koordinate.
 */
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	/**
	 * Überprüft ob Spieler Extraleben hat.
	 * 
	 * @return Wenn Extraleben dann, True, ansonsten False.
	 */
	public boolean getExtraLife() {
		return this.extraLife;
	}
	
	public int getFernFernsteuerungL() {
		return fernFernsteuerungL;
	}

	public int getFernFernsteuerungC() {
		return fernFernsteuerungC;
	}

	public void setFernFernsteuerungL(int privateFernFernsteuerungL) {
		fernFernsteuerungL = privateFernFernsteuerungL;
	}

	public void setFernFernsteuerungC(int privateFernFernsteuerungC) {
		fernFernsteuerungC = privateFernFernsteuerungC;
	}
/**
 * Gibt an welche Power-Ups der Spieler hat.
 * 
 * @return Array der Power-Ups
 */
	public int[] getPowerUps() {
		return powerUps;
	}

}
