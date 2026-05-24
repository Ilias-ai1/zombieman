package entity;

/**
 * @author BoOom
 * 
 *         Das Interface Konstante definiert verschiedene Konstanten, die von
 *         Clients und dem Server gemeinsam genutzt werden, um auf gemeinsame
 *         Informationen zuzugreifen.
 * 
 *         Alle Konstanten in diesem Interface sind als statisch deklariert und
 *         können von Klassen implementiert werden, die dieses Interface
 *         verwenden.
 */
public interface Konstante {

	/**
	 * PowerUp Wahrscheinlichkeitstufe im Spiel.
	 */
	public final static int WAHRSCHEINLICHKEITS_NIVEAU = 100; 
	
	/**
	 * Der Timer für die Power-ups die auf Spieler angewendet werden.
	 */
	public final static int POWER_UP_TIMER_FOR_PLAYER = 20000;

	/**
	 * Der Timer für Power-Ups, die auf der Karte platziert werden.
	 */
	public final static int POWER_UP_ON_MAP = 8000;

	public final static int WAHRSCHEINLICHKEIT_MINE = 80;

	/**
	 * Wenn es nicht 4 ist, müssen viele Dinge nicht mehr instanziiert werden
	 */
	public final static int MAX_SPIELER = 4;

	/**
	 * Die Anzahl der Zeilen und Spalten auf dem Spielfeld
	 */
	public final static int LIN = 13, COL = 15;

	/**
	 * Pixelgröße
	 */
	public final static int RESIZE = 4;

	/**
	 * Die Größe der Spielfeld-Sprites.
	 */
	public final static int SIZE_SPRITE_MAP = 16 * RESIZE; // Die Größe der Spielfeld-Sprites.

	/**
	 * Die Breite der Spieler-Sprites.
	 */
	public final static int BREITE_SPRITE_SPIELER = 22 * RESIZE;// Die Breite der Spieler-Sprites.

	/**
	 * Die Höhe der Spiele-Sprites.
	 */
	public final static int HOEHE_SPRITE_SPIELER = 28 * RESIZE;// Die Höhe der Spiele-Sprites.

	/**
	 * Unterschied in Pixeln zwischen der Karte und dem Spieler-Sprite
	 */
	public final static int VAR_X_SPRITES = 3 * RESIZE;
	public final static int VAR_Y_SPRITES = 16 * RESIZE;

	/**
	 * Dieser Wert gibt an, wie oft pro Sekunde das Platzieren von Bomben
	 * aktualisiert wird.
	 */
	public final static int BOMBE_RATE_UPDATE = 120;

	/**
	 * Dieser Wert gibt an, wie oft pro Sekunde das Platzieren von Blöcken
	 * aktualisiert wird.
	 */
	public final static int BLOCK_RATE_UPDATE = 100;

	/**
	 * Dieser Wert gibt an, wie oft pro Sekunde die Ausbreitung von Explosionen
	 * aktualisiert wird.
	 */
	public final static int FIRE_RATE_UPDATE = 35;

	/**
	 * Dieser Wert gibt an, wie oft pro Sekunde der Status eines Spielers
	 * aktualisiert wird.
	 */
	public final static int SPIELER_STATUS_RATE_UPDATE = 150;

	/**
	 * Dieser Wert gibt an, wie oft pro Sekunde die Koordinaten eines Spielers
	 * aktualisiert werden.
	 */
	public final static int KOORDINATEN_RATE_UPDATE = 40;

	/**
	 * Die Liste der Sprites für das Platzieren von Bomben auf der Karte.
	 */
	public final static String indexBombPlatziert[] = { "1", "2", "3", "4", "5", "6", "1", "2", "3", "4", "5", "6",
			"red-1", "red-2", "red-3", "red-1", "red-2", "red-3", "red-1", "red-2", "red-3" };

	/**
	 * Die Liste der Sprites für die Explosion.
	 */
	public final static String indexExplosion[] = { "1", "2", "3", "4", "5", "4", "3", "4", "5", "4", "3", "4", "5",
			"4", "3", "4", "5", "4", "3", "2", "1" };

	/**
	 * Die Liste der Sprites für brennende Blöcke auf der Karte.
	 */
	public final static String indexBlockOnFire[] = { "1", "2", "1", "2", "1", "2", "3", "4", "5", "6" };

	final static String indexPowerUp[] = { "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
			"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
			"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
}
