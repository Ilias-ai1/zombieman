package entity;

import java.awt.Image;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * @author BoOom
 * Die Klasse Sprite hat Methoden zum Laden von Bildern und Tönen.
 * Die Klasse ermöglicht das Laden von Spielfeldern, Skins, Sprites.
 * Des Weiteren enthält sie Funktionen zum Abspielen von Sounds beim Sterben.
 *
 */
public class Sprite {
	
	/**
     * Eine Hashtable zum Speichern geladener Bilder.
     */
	public final static Hashtable<String, Integer> maxLoopStatus = new Hashtable<String, Integer>();
	
	/**
     * Eine Hashtable zum Speichern maximaler Wiederholungszustände.
     */
	public final static Hashtable<String, Image> ht = new Hashtable<String, Image>();
	
	/**
	 * Hier sind die Skins
	 */
	private final static String personSkins[] = { "richie", "charlotte", "ava", "jackson" };
	
	/**
	 * Hier die verfügbaren Maps.
	 */
	private final static String mapSkins[] = {"friedhof", "krankenhaus", "supermarkt"};
	
	/**
	 *  Map Designs
	 */
	private final static String mapSkinKeyWords[] = {"block","block-on-fire-1","block-on-fire-2","block-on-fire-3","block-on-fire-4","block-on-fire-5","block-on-fire-6",
			"bomb-planted-1","bomb-planted-2","bomb-planted-3","bomb-planted-4","bomb-planted-5","bomb-planted-6","bomb-planted-red-1","bomb-planted-red-2","bomb-planted-red-3",
			"center-explosion-1","center-explosion-2","center-explosion-3","center-explosion-4","center-explosion-5","down-explosion-1","down-explosion-2","down-explosion-3",
			"down-explosion-4","down-explosion-5","floor-1","floor-2","floor-3","floor-4","floor-5","floor-6","floor-7","floor-8","item-destruction-1","item-destruction-2",
			"item-destruction-3","item-destruction-4","item-destruction-5","item-destruction-6","item-destruction-7","left-explosion-1","left-explosion-2","left-explosion-3",
			"left-explosion-4","left-explosion-5","mid-hori-explosion-1","mid-hori-explosion-2","mid-hori-explosion-3","mid-hori-explosion-4","mid-hori-explosion-5",
			"mid-vert-explosion-1","mid-vert-explosion-2","mid-vert-explosion-3","mid-vert-explosion-4","mid-vert-explosion-5","right-explosion-1","right-explosion-2",
			"right-explosion-3","right-explosion-4","right-explosion-5","up-explosion-1","up-explosion-2","up-explosion-3","up-explosion-4","up-explosion-5","wall-center",
			"wall-center2","wall-down","wall-down-left","wall-down-right","wall-left","wall-right","wall-up","wall-up-left","wall-up-logo","wall-up-right"
			};
	
	/**
	 *  Map Blöcke
	 */
	private final static String mapKeyWords[] = { "background","power-up-0","power-up-1","power-up-2","power-up-3","power-up-4",
			"power-up-0-icon","power-up-1-icon","power-up-2-icon","power-up-3-icon","power-up-4-icon","suddendeathzone","mine" };
	
	/**
	 * Spieler Sprites
	 */
	private static final String personKeyWords[] = { "dead-0", "dead-1", "dead-2", "dead-3", "dead-4", "down-0",
			"down-1", "down-2", "down-3", "left-0", "left-1", "left-2", "left-3", "right-0", "right-1", "right-2",
			"right-3", "up-0", "up-1", "up-2", "up-3", "wait-0", "wait-1", "wait-2", "wait-3", "icon" };
	
	private final static String numberKeyWords[] = { "null", "eins", "zwei", "drei", "vier", "fuenf", "sechs", "sieben",
			"acht", "neun", "doppelpunkt" };

	/**
	 * Hier wird der Sound abgespielt wenn ein Spieler Stirbt.
	 */
	private static void playDeathSound() {
		try {
			File soundFile = new File("path/to/death.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Hier werden die maximalen Wiederholungszustände für verschiedene Animationen gesetzt.
     * Die Zustände "dead", "down", "left", "right", "up" und "wait" werden berücksichtigt.
	 */
	public static void setMaxLoopStatus() {
		maxLoopStatus.put("dead", 5);
		maxLoopStatus.put("down", 4);
		maxLoopStatus.put("left", 4);
		maxLoopStatus.put("right", 4);
		maxLoopStatus.put("up", 4);
		maxLoopStatus.put("wait", 4);

	}

	/**
	 * Hier werden die ganzen Bilder wie Map-Blöcke und Spieler-Sprites geladen.
	 * und bei einem Fehler wird das Programm beendent.
	 */
	public static void loadImages() {
		try {
			System.out.println("Die Spielfäche wird geladen...\n");
			for (String keyWord : mapKeyWords) {
				ht.put(keyWord, loadImage("/map/" + keyWord + ".png"));
			}
			for (String skin : personSkins) {
				for (String keyWord : personKeyWords) {
					String spriteKey = skin + "/" + keyWord;
					ht.put(spriteKey, loadImage("/person/" + spriteKey + ".png"));

					// Überprüfen, ob der geladene Sprite "dead-0" ist
					if (spriteKey.equals("dead-0")) {
						playDeathSound();
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Fehler beim Laden der Images!\n");
			System.exit(1);
		}
	}

	/**
	 * Lädt die Bilder für die Map mit dem angegeben Skins.
	 * Bei einem Fehler wird das Programm beendet.
	 * @param mapSkin 
	 */
	public static void loadMapImages(String mapSkin) {
		try {
			for (String keyWord : mapSkinKeyWords) {
				ht.put(keyWord, loadImage("/map/" + mapSkin + "/" + keyWord + ".png"));
			}
		} catch (IOException e) {
			System.out.println("Fehler beim Laden der Map Images!\n");
			System.exit(1);
		}
	}

	/**
	 * Hier werden die ganzen Nummern geladen.
	 * Bei einem Fehler wird das Programm beendet.
	 */
	public static void loadNumberImages() {
		try {
			for (String keyWord : numberKeyWords) {
				ht.put(keyWord, loadImage("/digits/" + keyWord + ".png"));
			}
		} catch (IOException e) {
			System.out.println("Fehler beim Laden der Nummern-Images!\n");
			System.exit(1);
		}
	}

	/**
	 * Hier wird ein Bild aus dem angegebenen Dateipfad geladen.
	 * @param path
	 * @return Das Bild 
	 * @throws IOException
	 */
	private static Image loadImage(String path) throws IOException {
		return ImageIO.read(Sprite.class.getResource(path));
	}

	/**
	 * Gibt die Skins die frei sind zurück.
	 * @return
	 */
	public static String[] getPersonskins() {
		return personSkins;
	}
}
