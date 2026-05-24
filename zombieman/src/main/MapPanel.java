package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import entity.Konstante;
import entity.Sprite;
import net.Client;

/**
 * @author BoOom
 * @version 1.0
 * 
 * @see Konstante
 * @see Sprite
 * @see Client
 * 
 *      Diese Klasse MapPanel repräsentiert ein JPanel, das als Container zum
 *      Rendern der Spielkarte und der Spielfiguren dient.
 * 
 *      Die Karte wird mit einem angegebenen Skalierungsfaktor dargestellt, und
 *      Spielfiguren wie Gegner (gegner1, gegner2, gegner3) und der Spieler (du)
 *      werden auf der Karte gezeichnet. Die Karte selbst wird aus der
 *      Client-Klasse abgerufen, und die Sprites werden verwendet aus der
 *      Sprite-Klasse, um verschiedene Elemente auf der Karte darzustellen.
 */
public class MapPanel extends JPanel {

	private static SuddenDeath suddenDeath;

	/** Enthält die Informationen über den Spielzustand enthält. */
	private Game game;
	private int mapSizeW = Konstante.COL * Konstante.SIZE_SPRITE_MAP;
	private int mapSizeH = Konstante.LIN * Konstante.SIZE_SPRITE_MAP;
	private static int cDelapsedTimeInSeconds;

	/**
	 * Konstruiert ein neues MapPanel mit der angegebenen Game-Instanz.
	 * 
	 * @param game
	 */
	public MapPanel(Game game) {
		this.game = game;
		setLayout(null);
		setBackground(Color.RED);
		setSuddenDeath(new SuddenDeath(this)); // Breite und Geschwindigkeit der Zone
		cDelapsedTimeInSeconds = -1;
	}

	/**
	 * Überschreibt die paintComponent-Methode, um eine benutzerdefinierte
	 * Darstellung der Spielkarte und der Spielfiguren bereitzustellen.
	 * 
	 * @param g
	 */

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform oldAt = g2d.getTransform();
		g2d.scale(game.getMultiplicator(), game.getMultiplicator());
		drawMap(g2d);
		Game.getGegner1().draw(g2d);
		Game.getGegner2().draw(g2d);
		Game.getGegner3().draw(g2d);
		Game.getDu().draw(g2d);
		getSuddenDeath().draw(g2d);
		g2d.setTransform(oldAt);
		if(cDelapsedTimeInSeconds >= 0) {
			g.setColor(Color.RED);
			g.setFont(new Font("Arial", Font.BOLD, 150));
			g.drawString("" + cDelapsedTimeInSeconds, getWidth() / 2 - 40, getHeight() / 2);
		}
	}

	/**
	 * Zeichnet die Spielkarte.
	 * 
	 * @param g
	 */
	void drawMap(Graphics g) {
		for (int i = 0; i < Konstante.LIN; i++)
			for (int j = 0; j < Konstante.COL; j++)
				g.drawImage(Sprite.ht.get(Client.getMap()[i][j].getImg()), Client.getMap()[i][j].getX(),
						Client.getMap()[i][j].getY(), Konstante.SIZE_SPRITE_MAP, Konstante.SIZE_SPRITE_MAP, null);
	}

	/**
	 * @return holt sich das aktuelle suddendeath
	 */
	public static SuddenDeath getSuddenDeath() {
		return suddenDeath;
	}

	/**
	 * @param privateSuddenDeath hier wird suddendeath gesetzt.
	 */
	public void setSuddenDeath(SuddenDeath privateSuddenDeath) {
		suddenDeath = privateSuddenDeath;
	}

	/**
	 * @return hier wird die höhe der Map gegeben.
	 */
	public int getMapSizeH() {
		return mapSizeH;
	}

	/**
	 * @return hier die breite der Map.
	 */
	public int getMapSizeW() {
		return mapSizeW;
	}
	/**
	 * @param privateElapsedTimeInSeconds hier wird die abglaufene Zeit in Sekunden gesetzt.
	 */
	public static void setElapsedTimeInSecondsCountdownTimer(int privateElapsedTimeInSeconds) {
		cDelapsedTimeInSeconds = privateElapsedTimeInSeconds;
	}
}
