package main;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import entity.Konstante;

/**
 * @author BoOom
 * @see Konstante
 * Ein Thread, der für die Verwaltung und Darstellung des SuddenDeaths-Modus im Spiel verantwortlich ist
 *
 */
public class SuddenDeath extends Thread {

	private int zoneSpeed;
	private int zoneSize;
	private MapPanel mapPanel;
	private BufferedImage backgroundImage;
	private boolean active;
	//private boolean zoneHitPlayer;

	/**
	 * Konstruktor für die SuddenDeath-Klasse. Initialisiert die Größe der Todeszone und lädt das zugehörige Bild.
	 * @param mapPanel
	 */
	public SuddenDeath(MapPanel mapPanel) {
		this.mapPanel = mapPanel;
		setZoneSize(0);
		zoneSpeed = 8;
		// Laden des transparenten Bildes für die Todeszone
		loadTransparentImage(getClass().getResource("/map/suddendeathzone.png"));
	}

	/**
	 * Startet den Thread und aktiviert die Zone.
	 */
	public void start() {
		active = true; 
	}

	/**
	 * Hier wird ein Bild geladen und als Hintergrundbild gespeichert 
	 * @param url
	 */
	private void loadTransparentImage(URL url) {
		try {
			Image originalImage = new ImageIcon(url).getImage();
			backgroundImage = new BufferedImage(originalImage.getWidth(null), originalImage.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2d = backgroundImage.createGraphics();
			float alpha = 0.5f; 
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));        
			g2d.drawImage(originalImage, 0, 0, null);
			g2d.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Aktualisiert die Größe der Zone 
	 */
	public void update() {
		if (active && getZoneSize() <= 300) // 100 ist ein Platzhalter
			zoneSize += zoneSpeed;
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	int getColumnOfMap(int x) {
		return x / Konstante.SIZE_SPRITE_MAP;
	}

	/**
	 * @param y
	 * @return
	 */
	int getLineOfMap(int y) {
		return y / Konstante.SIZE_SPRITE_MAP;
	}
	/**
	 * @return Gibt die aktualle Größe der Zone.
	 */
	public int getZoneSize() {
		return zoneSize;
	}

	/**
	 * @param privateZoneSize Setzt die Göße der Zone.
	 */
	public void setZoneSize(int privateZoneSize) {
		zoneSize = privateZoneSize;
	}
	/**
	 * Zeichnet die Zone.
	 * @param g
	 */
	public void draw(Graphics2D g) {
		if (mapPanel != null && active) {
			// Zeichne die Zone
			drawBackgroundImage(g, 0, zoneSize, zoneSize, mapPanel.getMapSizeH() - (2 * zoneSize)); // Zeichnung von																							// links
			drawBackgroundImage(g, mapPanel.getMapSizeW() - zoneSize, zoneSize, zoneSize,
			mapPanel.getMapSizeH() - (2 * zoneSize)); // Zeichnung von rechts
			drawBackgroundImage(g, 0, 0, mapPanel.getMapSizeW(), zoneSize); // Zeichnung von oben
			drawBackgroundImage(g, 0, mapPanel.getMapSizeH() - zoneSize, mapPanel.getMapSizeW(), zoneSize); // Zeichnung
																											// von unten
		}
	}

	/**
	 * Zeichnet das Hintergrundbild.
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void drawBackgroundImage(Graphics2D g, int x, int y, int width, int height) {
		g.drawImage(backgroundImage, x, y, width, height, null);
	}

}
	    
