package main;

import java.awt.CardLayout;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

import net.Client;
import net.Server;

/**
 * @author BoOom
 * @see Client
 * @see Server
 * Das ContentPanel ist ein JPanel es enthält die verschiedenen Ansichten vom Spiel.
 * Hier werden StartPanel, MenuPanel, HelpPanel, HostPanel, JoinPanel, LadePanel, SiegerehrungPanel und das Game-Panel verwaltet.
 *
 */
@SuppressWarnings("serial")
public class ContentPanel extends JPanel {

	private CardLayout cardLayout;
	private StartPanel startPanel;
	private MenuPanel menuPanel;
	private HelpPanel helpPanel;
	private HostPanel hostPanel;
	private JoinPanel joinPanel;
	private LadePanel ladePanel;
	private SiegerehrungPanel siegerehrungPanel;
	private Game game;
	private int screenX, screenY;
	private boolean sfxMute, bgmMute;
	private File bgmFile, sfxFile;
	private AudioInputStream bgmAudioStream, sfxAudioStream;
	private Clip sfxClip, bgmClip;
	private int siege;

	/**
	 * Hier ist der Konstruktor zum ContentPanel.
	 * @param x ist die Breite des Screens
	 * @param y ist die Höhe des Screens
	 */
	public ContentPanel(int x, int y) {
		this.setScreenX(x);
		this.setScreenY(y);

		sfxMute = false;
		bgmMute = false;
		startBgm();

		cardLayout = new CardLayout();
		setLayout(cardLayout);

		startPanel = new StartPanel(this);
		add(startPanel, "start");

		menuPanel = new MenuPanel(this);
		add(menuPanel, "menu");

		helpPanel = new HelpPanel(this);
		add(helpPanel, "help");

		hostPanel = new HostPanel(this);
		add(hostPanel, "host");

		joinPanel = new JoinPanel(this);
		add(joinPanel, "join");

		ladePanel = new LadePanel(this);
		add(ladePanel, "lade");

		siegerehrungPanel = new SiegerehrungPanel(this);
		add(siegerehrungPanel, "siegerehrung");

		this.setSiege(0);
	}

	/**
	 * Fügt das GamePanel zum ContentPanel hinzu. 
	 */
	public void addGame() {
		setGame(new Game(this));
		add(getGame(), "game");
	}

	/**
	 * hier ist der Sound weenn man auf eine Taste drückt.
	 */
	public void playButtonSound() {
		if (!sfxMute) {
			try {
				sfxAudioStream = AudioSystem.getAudioInputStream(getClass().getResource("/sound/buttonSound.wav"));
				sfxClip = AudioSystem.getClip();
				sfxClip.open(sfxAudioStream);
				sfxClip.start();
			} catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Ändert die Sprache in den verschiedenen Panels und im Spiel.
	 * @param germanSelected
	 * @param englishSelected
	 */
	public void changeLanguage(boolean germanSelected, boolean englishSelected) {
		menuPanel.changeLanguage(germanSelected, englishSelected);
		hostPanel.changeLanguage(germanSelected, englishSelected);
		joinPanel.changeLanguage(germanSelected, englishSelected);
		helpPanel.changeLanguage(germanSelected, englishSelected);
		if (getGame() != null) {
			getGame().changeLanguage(germanSelected, englishSelected);
		}
	}

	/**
	 * Gibt die Breite des Bildschirms zurück.
	 * @return
	 */
	public int getScreenX() {
		return screenX;
	}

	/**
	 * Setzt die Breite des Bildschirms
	 * @param screenX
	 */
	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	/**
	 * Gibt die Höhe des Bildschirms zurück
	 * @return
	 */
	public int getScreenY() {
		return screenY;
	}

	/**
	 * Setzt die Höhe des Bildschirms.
	 * @param screenY
	 */
	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	/**
	 * Gibt das GamePanel zurück
	 * @return
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Setzt Das GamePanel.
	 * @param game
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * Gibt das SiegerehrungsPanel zurück.
	 * @return
	 */
	public SiegerehrungPanel getSiegerehrung() {
		return siegerehrungPanel;
	}

	/**
	 * Hier wird das aktuelle Game Panel gelöscht und ein neues hinzugefügt
	 */
	public void deleteGame() {
		setGame(new Game(this));
		add(getGame(), "game");
	}
	/**
	 * Ändert den Zustand der Soundeffekte. 
	 * @param sfxMute wenn true dann wird es gemutet
	 */
	public void changeSFX(boolean sfxMute) {
        this.sfxMute = sfxMute;
    }

    /**
     * Ändert den Zustand der Hintergrundmusik.
     * @param bgmMute wenn true dann wird es gemutet
     */
    public void changeBGM(boolean bgmMute) {
    	this.bgmMute = bgmMute;
        if (bgmMute) {
            stopBgm();
            stopMenuBgm();
        } else {
            playMenuBgm();
        }
    }

    /**
     * Hier wird die Hintergrundmusik im Menu abgespielt.
     */
    public void playMenuBgm() {
        if(!bgmMute) {
        	try {
        		bgmAudioStream = AudioSystem.getAudioInputStream(getClass().getResource("/sound/menuSound.wav"));
        		bgmClip = AudioSystem.getClip();
        		bgmClip.open(bgmAudioStream);
        		bgmClip.start();
        	} catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
        		ex.printStackTrace();
        	}
        } 
    }
	/**
	 * hier wird die Hintergrundmusik gestoppt 
	 */
	void stopMenuBgm() {
		getBgmClip().setFramePosition(0);
		getBgmClip().stop();
	}

    /**
     * Startet die Hintergrundmusik für das Spiel
     */
    public void startBgm() {
    	
    	if (bgmClip != null) {
			// Hintergrundsound läuft bereits
			bgmClip.stop();
			bgmClip.close();
		}
    	
    	if(!bgmMute) {

    	  	String SoundFileName = "/sound/menuSound.wav";
    		String bgmFileName = SoundFileName;
    		
    		if (bgmClip != null) {
    			// Hintergrundsound läuft bereits
    			bgmClip.stop();
    			bgmClip.close();
    		}

    		// Überprüfe den Map-Namen und setze den passenden Dateinamen
    		if ("friedhof".equals(Client.getMapSkinName())) {
    			bgmFileName = "/sound/friedhof.wav";

    		} else if ("krankenhaus".equals(Client.getMapSkinName())) {
    			bgmFileName = "/sound/krankenhaus.wav";
    		}else if ("supermarkt".equals(Client.getMapSkinName())) {
    			bgmFileName = "/sound/supermarkt.wav";
    		}

    		try {
    			bgmAudioStream = AudioSystem.getAudioInputStream(getClass().getResource(bgmFileName));
    			bgmClip = AudioSystem.getClip();
    			bgmClip.open(bgmAudioStream);
    			bgmClip.start();
    			bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
    		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
    			e.printStackTrace();
    		}
        }
    }

	/**
	 * Stoppt die Hintergrundmusik für das Spiel.
	 */
	public void stopBgm() {
		getBgmClip().setFramePosition(0);
		getBgmClip().stop();
		getBgmClip().close();
	}

	/**
	 * Erhöht die Siegesanzahl um 1.
	 */
	public void addSiege() {
		setSiege(siege + 1);
	}

	/**
	 * Gibt die aktuelle Siegesanzahl zurück
	 * @return
	 */
	public int getSiege() {
		return siege;
	}

	/**
	 * Setzt die Siegesanzahl.
	 * @param privateSiege
	 */
	public void setSiege(int privateSiege) {
		siege = privateSiege;
	}

	/**
	 * Gibt den Clip für die Hintergrundmusik zurück.
	 * @return
	 */
	public Clip getBgmClip() {
		return bgmClip;
	}

	/**
	 * Setzt den Clip für die Hintergrundmusik
	 * @param privateBgmClip
	 */
	public void setBgmClip(Clip privateBgmClip) {
		bgmClip = privateBgmClip;
	}
}
