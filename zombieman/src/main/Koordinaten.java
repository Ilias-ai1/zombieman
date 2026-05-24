package main;

/**
 * @author BoOom
 * Diese Klasse Koordinaten repräsentiert eine Position im Spiel mit x und y Koordinaten und img ein Bildattribute
 *
 */
public class Koordinaten {
	private int x, y;
	private String img;

	/**
	 * Hier ist ein Konstruktor mit y und x Koordinaten
	 * @param x
	 * @param y
	 */
	public Koordinaten(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	/**
	 * Hier ist ein Konstruktor mit y und x Koordinate und ein Bildattribute
	 * @param x
	 * @param y
	 * @param img
	 */
	public Koordinaten(int x, int y, String img) {
		this.setX(x);
		this.setY(y);
		this.setImg(img);
	}

	/**
	 * Gibt das Bildattribute der Koordinaten zurück
	 * @return
	 */
	public String getImg() {
		return img;
	}

	/**
	 * Hier wird es gesetzt
	 * @param img
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * Hier wird die x Koordinate zurück gegeben.  
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * Hier wird x gesetzt
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Hier wird die x Koordinate zurück gegeben. 
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * Hier wird y gesetzt
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
}
