package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import java.awt.image.BufferedImage;

import entity.Konstante;
import entity.Spieler;
import entity.Sprite;
import net.Client;
import net.Sender;
import net.Server;

/**
 * @author BoOom
 * @see Konstante
 * @see Spieler
 * @see Sprite
 * @see Client
 * @see Sender
 * @see Server
 * 
 * Diese Klasse Game repäsentiert das Hauptspielfenster, hier findet das gesamte Gameplay statt.
 * Mit Spielerinformation, die Map und die Spielfiguren usw.
 *
 */
public class Game extends JPanel {

	private static Spieler du, gegner1, gegner2, gegner3;
	private ContentPanel contentPanel;
	private MapPanel mapPanel;
	private static int elapsedTimeInSeconds;
	private int guiHeight, guiWidth, offsetX;
	private double multiplicator;
	private JLabel backgroundLeftLabel, backgroundRightLabel;
	private ImageIcon backgroundLeftIcon, backgroundRightIcon;
	private Buttons toMenuButton;
	private int w, h;
	private Sender sender;
	private int mapSizeWNew,mapSizeW,mapSizeH,mapSizeHNew;
	private JToggleButton mapButton;
	private int siege [] = {0,0,0,0};
	private static List<Spieler> playerIsLogged = new ArrayList<>();
	
	/**
	 * Hier ist eine neue Game Konstruktor mit dem contentPanel und den Spielern Hintergrundmusik.
	 * 
	 * @param contentPanel Das Panel wird gebraucht für die Sounds.
	 */
	public Game(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
		w = contentPanel.getScreenX();
		h = contentPanel.getScreenY();

		guiHeight = h / 6;
		setPreferredSize(new Dimension(w, h));
		setLayout(null);
		System.out.print("Warten auf Spieler...\n");
		
		// Initialisiert die vier Spieler.
		du = new Spieler(Client.getClientId(), this);
		gegner1 = new Spieler((Client.getClientId() + 1) % Konstante.MAX_SPIELER, this);
		gegner2 = new Spieler((Client.getClientId() + 2) % Konstante.MAX_SPIELER, this);
		gegner3 = new Spieler((Client.getClientId() + 3) % Konstante.MAX_SPIELER, this);
		addKeyListener(new Sender());
		
		//Hier wird die Bilder-Sprites geladen und start von Hintergrundmusik
		Sprite.loadNumberImages();
		contentPanel.startBgm();
		
		//Hier ist alles zur Mapanzeige, die Skalierung.
		multiplicator = (double) (h - guiHeight) / (double) (Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
		mapSizeW = Konstante.COL * Konstante.SIZE_SPRITE_MAP;
		mapSizeH = Konstante.LIN * Konstante.SIZE_SPRITE_MAP;
		mapSizeWNew = (int) ((double) mapSizeW * multiplicator);
		guiWidth = mapSizeWNew;
		mapSizeHNew = h - guiHeight;
		offsetX = (w - mapSizeWNew) / 2;
		
		//hier wird das MapPanel und die Hintergrundbilder initialisiert 
		mapPanel = new MapPanel(this);
		mapPanel.setBounds(offsetX, guiHeight, mapSizeWNew, mapSizeHNew);
		
//		backgroundLeftIcon = new ImageIcon(new ImageIcon(getClass().getResource("/menu/left_mapPanel.png")).getImage()
//				.getScaledInstance(offsetX, h, Image.SCALE_DEFAULT));
		backgroundRightIcon = new ImageIcon(new ImageIcon(getClass().getResource("/menu/right_mapPanel.png")).getImage()
				.getScaledInstance(offsetX, h, Image.SCALE_DEFAULT));
		backgroundLeftLabel = new JLabel();
//		backgroundLeftLabel.setBounds(0, 0, offsetX, h);
//		backgroundLeftLabel.setIcon(backgroundLeftIcon);
		backgroundRightLabel = new JLabel();
		backgroundRightLabel.setBounds(w - offsetX, 0, offsetX, h);
		backgroundRightLabel.setIcon(backgroundRightIcon);

		toMenuButton = new Buttons(this, "verlassen");
		toMenuButton.setFocusPainted(false);
		toMenuButton.setPreferredSize(new Dimension(140, 40));
		toMenuButton.setBounds(w - 160, 20, 140, 40);
		
		//Hier werden sie zum Panel hinzugefügt
		add(toMenuButton);
		add(backgroundLeftLabel);
		add(backgroundRightLabel);
		add(mapPanel);

		//Wird genutz um zum Menu zurück zu kommen 
		toMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Stoppt alten sound
				contentPanel.stopBgm();
				contentPanel.getBgmClip().close();

				// startet neue sound
				contentPanel.playMenuBgm();

				switchToMenu();
			}
		});
	}
	
	/**
	 * Hier werden die ganzen Power-Ups und PlayerIcon gezeichnet so vie die Zeit und die vier Spieler
	 * @param g
	 */
	private void drawIngameGUI(Graphics g) {
		int playerInfoX = offsetX + 50;
		int playerInfoY = guiHeight / 2 + g.getFontMetrics().getAscent() / 2 + 20;
		int skinOffsetX = 30;
		int skinOffsetY = 5;
		int timerOffsetX = offsetX + 550;
		String formattedTime = formatTime(elapsedTimeInSeconds);
		int textX = offsetX + (guiWidth - getFormattedTimeWidth(formattedTime)) / 2;
		int textY = (guiHeight / 2 + g.getFontMetrics().getAscent() / 2) / 4;
		
		
		g.setColor(Color.BLACK);
		g.fillRect(offsetX, 0, guiWidth, guiHeight);

		g.setFont(new Font("Arial", Font.PLAIN, 14));
		
		if (du.isLebt()) {

			drawPlayerIcon(g, du, playerInfoX - skinOffsetX, guiHeight - (guiHeight/3));
			g.setColor(Color.GREEN);
			g.drawString(du.getName() +" | " + du.getSkin(), playerInfoX, guiHeight - (guiHeight/2) + 20);
			drawPowerUp(g, du, playerInfoX - skinOffsetX, guiHeight - (guiHeight/7) );
			playerInfoX += mapSizeWNew/4;
			if(playerInfoX >= textX && playerInfoX <= guiHeight/4) {
				playerInfoX = playerInfoX + guiWidth/7;
			}
		}
		if (gegner1.isLebt()) {

			drawPlayerIcon(g, gegner1, playerInfoX - skinOffsetX, guiHeight - (guiHeight/3));
			g.setColor(Color.RED);
			g.drawString(gegner1.getName() +" | " + gegner1.getSkin(), playerInfoX, guiHeight - (guiHeight/2)+ 20);
			drawPowerUp(g, gegner1, playerInfoX - skinOffsetX, guiHeight - (guiHeight/7));
			playerInfoX += mapSizeWNew/4;
			if(playerInfoX >= textX && playerInfoX <= guiHeight/4) {
				playerInfoX = playerInfoX + guiWidth/7;
			}
		}
		if (gegner2.isLebt()) {

			drawPlayerIcon(g, gegner2, playerInfoX - skinOffsetX, guiHeight - (guiHeight/3));
			g.setColor(Color.RED);
			g.drawString(gegner2.getName() +" | " + gegner2.getSkin(), playerInfoX, guiHeight - (guiHeight/2)+ 20);
			drawPowerUp(g, gegner2, playerInfoX - skinOffsetX, guiHeight - (guiHeight/7));
			playerInfoX += mapSizeWNew/4;
			if(playerInfoX >= textX && playerInfoX <= guiHeight/4) {
				playerInfoX = playerInfoX + guiWidth/7;
			}
		}
		if (gegner3.isLebt()) {

			drawPlayerIcon(g, gegner3, playerInfoX - skinOffsetX, guiHeight - (guiHeight/3));
			g.setColor(Color.RED);
			g.drawString(gegner3.getName() +" | " + gegner3.getSkin(), playerInfoX, guiHeight - (guiHeight/2)+ 20);
			drawPowerUp(g, gegner3, playerInfoX - skinOffsetX, guiHeight - (guiHeight/7));
			playerInfoX += mapSizeWNew/4;
			if(playerInfoX >= textX && playerInfoX <= guiHeight/4) {
				playerInfoX = playerInfoX + guiWidth/7;
			}
		}
		drawScoreboard(g);
		repaint();
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 20));

		for (char digit : formattedTime.toCharArray()) {
			if (Character.isDigit(digit) || digit == ':') {
				String digitImageKey = "null";
				switch (digit) {
				case '1':
					digitImageKey = "eins";
					break;
				case '2':
					digitImageKey = "zwei";
					break;
				case '3':
					digitImageKey = "drei";
					break;
				case '4':
					digitImageKey = "vier";
					break;
				case '5':
					digitImageKey = "fuenf";
					break;
				case '6':
					digitImageKey = "sechs";
					break;
				case '7':
					digitImageKey = "sieben";
					break;
				case '8':
					digitImageKey = "acht";
					break;
				case '9':
					digitImageKey = "neun";
					break;
				case ':':
					digitImageKey = "doppelpunkt";
					break;

				}
				Image digitImage = Sprite.ht.get(digitImageKey);
				if (digitImage != null) {
					g.drawImage(digitImage, textX, textY, null);
					textX += digitImage.getWidth(null);
				}
			}
		}
	}
	
	/**
	 * Hier werden die PowerUps die von einem Spieler gesammelt werden gezeichnet und die Anzahl die gesammelt wurden
	 * @param g
	 * @param player
	 * @param x Die Koordinate
	 * @param y Die Koordinate
	 */
	private void drawPowerUp(Graphics g, Spieler player, int x, int y) {
		
		int offsetX = 0;
		
		if(player.isLebt()) {
			ImageIcon icon1 = new ImageIcon(getClass().getResource("/map/power-up-0-icon.png"));
			Image iconImage1 = icon1.getImage();
			ImageIcon icon2 = new ImageIcon(getClass().getResource("/map/power-up-1-icon.png"));
			Image iconImage2 = icon2.getImage();
			ImageIcon icon3 = new ImageIcon(getClass().getResource("/map/power-up-2-icon.png"));
			Image iconImage3 = icon3.getImage();
			ImageIcon icon4 = new ImageIcon(getClass().getResource("/map/power-up-3-icon.png"));
			Image iconImage4 = icon4.getImage();
			ImageIcon icon5 = new ImageIcon(getClass().getResource("/map/power-up-4-icon.png"));
			Image iconImage5 = icon5.getImage();
			
			Image iconImages [] = {iconImage1,iconImage2,iconImage3,iconImage4,iconImage5};
			if (iconImages != null) {
				for(int i = 0; i < iconImages.length; i++) {
					g.drawImage(iconImages[i], x + offsetX, y - iconImages[i].getHeight(null), null);
					
					int powerUpAnz = player.getPowerUps(i);
					String anz = Integer.toString(powerUpAnz);
					g.setColor(Color.WHITE);
					 g.drawString(anz, x + offsetX + (iconImages[i].getWidth(null) - g.getFontMetrics().stringWidth(anz)) / 2, y + 10);
					 offsetX += iconImages[i].getWidth(null);
				}
			}
		}
	}
	void drawScoreboard(Graphics g) {
		int zaehler = 1;
		int height = contentPanel.getHeight();
	    List<Spieler> players = Arrays.asList(du, gegner1, gegner2, gegner3);
	    players.sort(Comparator.comparingInt(Spieler::getSiege).reversed());
	    

	    g.setColor(Color.BLACK);
	    g.fillRect(0, 0, offsetX, height);

	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Arial", Font.BOLD, 40));
	    g.drawString("Scoreboard", offsetX/4 - 30, height/4);
	    g.setColor(Color.RED);
	    g.drawRect(offsetX/4 - 40, height/5, 250, 250);

	    g.setFont(new Font("Arial", Font.PLAIN, 14));

	    for (int i = 0; i < players.size(); i++) {
	    	
	        Spieler player = players.get(i);
	        if(player.isLebt()) {
	        	playerIsLogged.add(player);
	        }

	        if (playerIsLogged != null) {
	        	if(player.getName() != null) {
	            g.setColor(Color.WHITE);
	            g.drawString(zaehler +". ", offsetX/4, height/3);
	            g.drawString(player.getName() + " | Siege: " + player.getSiege(), offsetX/3, height/3);
	            height += 75;
	            zaehler++;
	        	}
	        	else {
	        		
	        	}
	        }
	    }
	}
	
	/**
	 * Zeichnet das Spielericon für einen bestimmten Spieler an den angegebenen Koordinaten.
	 * @param g
	 * @param player
	 * @param x	Die Koordinate
	 * @param y Die Koordinate
	 */
	private void drawPlayerIcon(Graphics g, Spieler player, int x, int y) {
		if(player.isLebt()) {
			String skin = player.getSkin();
			String iconPath = "/person/" + skin + "/icon.png";

			ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
			Image iconImage = icon.getImage();

			if (iconImage != null) {
				g.drawImage(iconImage, x, y - iconImage.getHeight(null), null);
			}
		}
	}
	
	/**
	 * Hier wird die Breite zurück gegeben die gebraucht wird für die Zeit
	 * @param formattedTime
	 * @return
	 */
	private int getFormattedTimeWidth(String formattedTime) {
		int width = 0;
		for (char digit : formattedTime.toCharArray()) {
			String digitImageKey = getDigitImageKey(digit);
			Image digitImage = Sprite.ht.get(digitImageKey);
			if (digitImage != null) {
				width += digitImage.getWidth(null);
			}
		}
		return width;
	}
	
	/**
	 * Hier wird der Bildschlüssel ermittelt 
	 * @param digit
	 * @return
	 */
	private String getDigitImageKey(char digit) {
		switch (digit) {
		case '1':
			return "eins";
		case '2':
			return "zwei";
		case '3':
			return "drei";
		case '4':
			return "vier";
		case '5':
			return "fuenf";
		case '6':
			return "sechs";
		case '7':
			return "sieben";
		case '8':
			return "acht";
		case '9':
			return "neun";
		case ':':
			return "doppelpunkt";
		default:
			return "null";
		}
	}
	
	/**
	 * Damit kann man zu Menu switchen
	 */
	public void switchToMenu() {
		CardLayout layout = (CardLayout) getContentPanel().getLayout();
		layout.show(getContentPanel(), "menu");
	}
	
	/**
	 * Hier wird die Zeit in sekunden berechnet
	 * @param seconds
	 * @return
	 */
	private String formatTime(int seconds) {
		int minutes = seconds / 60;
		seconds %= 60;
		return String.format("%02d:%02d", minutes, seconds);
	}

	
	/**
	 *  Methode um Spiel zu aktualisieren auch wenn es zum SuddenDeath kommt
	 */
	public void updateGame() {
		MapPanel.getSuddenDeath().update();

	}
	
	/**
	 * hier wird das IngameGUI gezeichnet 
	 * @param g
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawIngameGUI(g);
		//updateGame();

		mapPanel.repaint();
		Toolkit.getDefaultToolkit().sync();
	}
	
	/**
	 * Setzt die Sprite-Map an der angegebenen Position.
	 * @param keyWord
	 * @param l
	 * @param c
	 */
	public static void setSpriteMap(String keyWord, int l, int c) {
		Client.getMap()[l][c].setImg(keyWord);
	}
	
	/**
	 * Hier wird die Sprache basierend auf  der Auswahl gewählt 
	 * @param germanSelected
	 * @param englishSelected
	 */
	public void changeLanguage(boolean germanSelected, boolean englishSelected) {
		toMenuButton.changePath(germanSelected ? "verlassen" : "back");
	}
	
	/**
	 * HIer wird der Multiplicator zurück gegeben für die Skalierung.
	 * @return
	 */
	public double getMultiplicator() {
		return multiplicator;
	}
	
	/**
	 * Hier wird der Multiplicator gesetzt. 
	 * @param multiplicator
	 */
	public void setMultiplicator(double multiplicator) {
		this.multiplicator = multiplicator;
	}
	
	/**
	 * Gibt das ContentPanel zurück, das mit dem Spiel verbunden ist.
     *
	 * @return
	 */
	public ContentPanel getContentPanel() {
		return contentPanel;
	}
	
	/**
	 * Hier wird das ContentPanel gesetzt.
	 * @param contentPanel
	 */
	public void setContentPanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
	}
	
	/**
	 * @return gibt gegner1 zurück
	 */
	public static Spieler getGegner1() {
		return gegner1;
	}
	
	/**
	 * @return gibt gegner2 zurück
	 */
	public static Spieler getGegner2() {
		return gegner2;
	}
	
	/**
	 * @return gibt gegner3 zurück
	 */
	public static Spieler getGegner3() {
		return gegner3;
	}
	
	/**
	 * @return gibt du zurück
	 */
	public static Spieler getDu() {
		return du;
	}
	
	/**
	 * Hier wird die zeit in sekunden gesetzt
	 * @param privateElapsedTimeInSeconds
	 */
	public static void setElapsedTimeInSecondsTimer(int privateElapsedTimeInSeconds) {
		elapsedTimeInSeconds = privateElapsedTimeInSeconds;
	}
}
