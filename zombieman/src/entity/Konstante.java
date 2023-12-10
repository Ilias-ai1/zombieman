package entity;


//Informationen auf die Client/Server zugreifen
public interface Konstante {
	
// Wenn es nicht 4 ist, müssen viele Dinge nicht mehr instanziiert werden
final static int MAX_SPIELER = 4; 

final static int LIN = 13, COL = 13; // immer unique
final static int RESIZE = 4; // Pixelgröße

final static int SIZE_SPRITE_MAP = 16 * RESIZE;
final static int BREITE_SPRITE_SPIELER = 22 * RESIZE;
final static int HOEHE_SPRITE_SPIELER = 33 * RESIZE;

// Unterschied in Pixeln zwischen der Karte und dem Spieler-Sprite
final static int VAR_X_SPRITES = 3 * RESIZE;
final static int VAR_Y_SPRITES = 16 * RESIZE;

//final static int BOMBE_RATE_UPDATE = 90;
//final static int BLOCK_RATE_UPDATE = 100;
final static int SPIELER_STATUS_RATE_UPDATE = 150;
final static int KOORDINATEN_RATE_UPDATE = 27;

}

