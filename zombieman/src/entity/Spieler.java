package entity;

import java.awt.Graphics;
import javax.swing.JPanel;

import main.StatusUpdater;
import net.Client;

// Spielerklasse
/**
 * @author BoOom
 * @version 1.0
 * 
 * @see StatusUpdater
 * @see Client Die Klasse Spieler repräsentiert die Spieler in einem Spiel. Ein
 *      Spieler hat Eigenschaften wie Position,Status,Lebensstatus.
 *
 */
public class Spieler {
	private int x;
	private int y;
	private String status;
	private String skin;
	private JPanel panel;
	private boolean lebt;
	private String name;
	private StatusUpdater sc;
	private int powerUps[]= {0,0,1,0,0};
	private int siege = 0;

	/**
	 * Konstruktor für die Spielerklasse. Initialisiert einen Spieler anhand der
	 * übergebenen ID und dem zugehörigen Panel.
	 * 
	 * @param id    Das ist die eindeutuige ID von einem Spieler
	 * @param panel Ist das Panel auf dem der Spieler gemalt wird.
	 */
	public Spieler(int id, JPanel panel) {
		this.setX(Client.getSpawn()[id].getX());
		this.setY(Client.getSpawn()[id].getY());
		this.skin = Sprite.getPersonskins()[id];
		this.setPanel(panel);
		this.setLebt(Client.getAlive()[id]);

		(sc = new StatusUpdater(this, "wait")).start();
	}

	/**
	 * Hier wird der Spieler gemalt wenn er am Leben ist.
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		if (isLebt()) {
			g.drawImage(Sprite.ht.get(skin + "/" + getStatus()), getX(), getY(), Konstante.BREITE_SPRITE_SPIELER,
					Konstante.HOEHE_SPRITE_SPIELER, null);
		}
	}

	/**
	 * Gibt den Namen zurück
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Hier wird der name gesetzt
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gibt den Skin zurück
	 * 
	 * @return
	 */
	public String getSkin() {
		return skin;
	}

	/**
	 * Gibt den Status zurück
	 * 
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Hier wird der Status gesetzt
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gibt den X-Koordinate zurück
	 * 
	 * @return Die X-Koordinate der Position des Spielers.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Hier wird die x-Koordinate zurück
	 * 
	 * @param x Die X-Koordinate der Position des Spielers.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gibt den Y-Koordinate zurück
	 * 
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * Hier wird die y-Koordinate zurück
	 * 
	 * @param y Die Y-Koordinate der Position des Spielers.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gibt das Panel zurück
	 * 
	 * @return
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * Hier wird das Panel gesetzt
	 * 
	 * @param panel
	 */
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	/**
	 * Überprüft, ob der Spieler am Leben ist.
	 * 
	 * @return true, wenn der Spieler am Leben ist, sonst false.
	 */
	public boolean isLebt() {
		return lebt;
	}

	/**
	 * Setzt den Lebensstatus des Spielers.
	 * 
	 * @param lebt lebt Der neue Lebensstatus des Spielers.
	 */
	public void setLebt(boolean lebt) {
		this.lebt = lebt;
	}

	/**
	 * Hier wird der Status-Updater des Spielers zurück gegeben.
	 * @return 
	 */
	public StatusUpdater getSc() {
		return sc;
	}

	/**
	 * Gibt den Wert des Power-Ups.
	 * @param i Die Posititon im Array
	 * @return der Wert 
	 */
	public int getPowerUps(int i) {
		return this.powerUps[i];
	}
	
	/**
	 * Setzt den Wert des Power-ups.
	 * @param i
	 * @param wert
	 */
	public void setPowerUp(int i, int wert) {
		this.powerUps[i] = wert;
	}
	/**
	 * Addiert die gegebene Anzahl Siege zum Gesamtanzahl der Siege des Spielers.
	 * @param i
	 */
	public void setSiege(int i) {
		this.siege = i;
		
	}
	/**
	 * Gibt die Gesamtanzahl der Siege des Spielers zurück.
	 * @return
	 */
	public int getSiege() {
		return this.siege;
		
	}
}
