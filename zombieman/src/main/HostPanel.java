package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import entity.Konstante;
import entity.Sprite;
import net.Client;
import net.Server;


@SuppressWarnings("serial")

/**
 * @author BoOom
 * @see Konstante
 * @see Sprite
 * @see Client
 * @see Server
 * Die Klasse HostPanel repräsentiert das Panel, in dem der Benutzer ein neues Spiel hosten kann.
 * Hier kann der Benutzer seinen Benutzernamen, den Port, die IP-Adresse und die Anzahlen der Siege einstellen.
 */
public class HostPanel extends JPanel {

	private JPanel mapAuswahl;
	private Buttons gameStartButton, toMenuButton;
	private JComboBox<String> winRoundComboBox,playerNumberComboBox;
	private JLabel winRoundLabel, hostNameLabel, hostPortLabel, hostIPLabel,playerNumberLabel;
	private JTextField hostIPField, hostPortField, hostNameField;
	private ImageIcon imageIcon, map1Icon, map2Icon, map3Icon, map4Icon;
	private boolean portFree, validName, validPort, validIP;
	private GridBagConstraints mapGbc, gbc;
	private InetAddress inetAddress;
	private Image scaledMap1, scaledMap2, scaledMap3, scaledMap4;
	private JToggleButton map1Button, map2Button, map3Button, map4Button;
	private ContentPanel contentPanel;
	private Server server;
	private String mapSkin;
	private String hostIP;
	private JLabel errorLabel;
	private Timer errorTimer;
	private Client client;
	private int portNumber;
	private static int winRoundNumber;
	private boolean serverOK;

	/**
	 * Hier ist der Konstrukto für das HostPanel 
	 * @param contentPanel
	 */
	public HostPanel(ContentPanel contentPanel) {
		this.setContentPanel(contentPanel);
		imageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/menu/serverbackground.gif")).getImage()
				.getScaledInstance(contentPanel.getScreenX(), contentPanel.getScreenY(), Image.SCALE_DEFAULT));
		repaint();
		setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
		setLayout(new GridBagLayout());
		portFree = true;
		validName = true;
		validPort = true;
		validIP = true;
		serverOK = true;
		initializeComponents();
		setupMapButtons();
		configureLayout();
	}

	/**
	 * Initialissiert die Komponenten 
	 * hier findet man die namenFelder IPFelder und die Start und verlassen Buttons usw.
	 */
	private void initializeComponents() {
		hostNameLabel = new JLabel("Benutzername:");
		hostNameLabel.setForeground(Color.red);
		hostNameField = new JTextField();
		hostNameField.setColumns(10);

		hostPortLabel = new JLabel("Port:");
		hostPortLabel.setForeground(Color.red);
		hostPortField = new JTextField("1331");
		hostPortField.setColumns(10);

		try {
			inetAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		hostIP = inetAddress.getHostAddress();

		hostIPLabel = new JLabel("IP:");
		hostIPLabel.setForeground(Color.red);
		hostIPField = new JTextField(hostIP);
		hostIPField.setColumns(10);

		winRoundLabel = new JLabel("Anzahl der Siege:");
		winRoundLabel.setForeground(Color.red);
		winRoundComboBox = new JComboBox<>(new String[] { "1 Runde", "2 Runden", "3 Runden", "4 Runden", "5 Runden" });
		winRoundComboBox.setFocusable(false);
		
		playerNumberLabel = new JLabel("Anzahl der Spieler:");
		playerNumberLabel.setForeground(Color.red);
		playerNumberComboBox = new JComboBox<>(new String[] {"2", "3", "4"});
		playerNumberComboBox.setFocusable(false);

		gameStartButton = new Buttons(this, "spielstarten");
		gameStartButton.setFocusPainted(false);
		gameStartButton.setPreferredSize(new Dimension(140, 40));

		toMenuButton = new Buttons(this, "verlassen");
		toMenuButton.setFocusPainted(false);
		toMenuButton.setPreferredSize(new Dimension(140, 40));

		mapAuswahl = new JPanel(new GridBagLayout());
		mapAuswahl.setOpaque(false);
	}
    
   
    /**
     * Konfiguriert die Layouteinstellungen für die Map-Auswahl.
     */
    private void setupMapButtons() {
        mapGbc = new GridBagConstraints();
        mapGbc.gridx = 0;
        mapGbc.gridy = 0;
        mapGbc.anchor = GridBagConstraints.NORTH;
        mapGbc.insets = new Insets(5, 5, 5, 5);

		map1Icon = new ImageIcon(getClass().getResource("/map/friedhof.png"));
		map2Icon = new ImageIcon(getClass().getResource("/map/krankenhaus.png"));
		map3Icon = new ImageIcon(getClass().getResource("/map/supermarkt.png"));
		map4Icon = new ImageIcon(getClass().getResource("/map/random_map.png"));

		scaledMap1 = map1Icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		scaledMap2 = map2Icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		scaledMap3 = map3Icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		scaledMap4 = map4Icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

		map1Icon = new ImageIcon(scaledMap1);
		map2Icon = new ImageIcon(scaledMap2);
		map3Icon = new ImageIcon(scaledMap3);
		map4Icon = new ImageIcon(scaledMap4);

		map1Button = createMapToggleButton(map1Icon);
		map2Button = createMapToggleButton(map2Icon);
		map3Button = createMapToggleButton(map3Icon);
		map4Button = createMapToggleButton(map4Icon);

		JToggleButton[] mapButtons = { map1Button, map2Button, map3Button, map4Button };
		
		// Wird benötigt um eine Map auszuwählen und der Layouteinstellungen
		ActionListener mapButtonListener = e -> {
			if (e.getSource() instanceof JToggleButton) {
				JToggleButton selectedMapButton = (JToggleButton) e.getSource();

				for (JToggleButton button : mapButtons) {
					button.setSelected(false);
					button.setBorderPainted(false);
				}

				mapSkin = getMapSkin(selectedMapButton);
				selectedMapButton.setSelected(true);
				selectedMapButton.setBorderPainted(true);
				selectedMapButton.setBorder(BorderFactory.createLineBorder(Color.RED, 5));

				for (JToggleButton button : mapButtons)
					if (button != selectedMapButton) {
						button.setBorderPainted(false);
					}
			}
		};

		map1Button.addActionListener(mapButtonListener);
		map2Button.addActionListener(mapButtonListener);
		map3Button.addActionListener(mapButtonListener);
		map4Button.addActionListener(mapButtonListener);
		map1Button.setFocusPainted(false);
		map2Button.setFocusPainted(false);
		map3Button.setFocusPainted(false);
		map4Button.setFocusPainted(false);

		mapAuswahl.add(map1Button, mapGbc);
		mapGbc.gridx++;
		mapAuswahl.add(map2Button, mapGbc);

		mapGbc.gridx = 0;
		mapGbc.gridy++;

		mapAuswahl.add(map3Button, mapGbc);
		mapGbc.gridx++;
		mapAuswahl.add(map4Button, mapGbc);
	}

	/**
	 * Hier wird ein ToggleButton erstellt für Map auswahl
	 * @param icon
	 * @return
	 */
	private JToggleButton createMapToggleButton(ImageIcon icon) {
		JToggleButton mapButton = new JToggleButton();
		mapButton.setIcon(icon);
		mapButton.setOpaque(false);
		mapButton.setPreferredSize(new Dimension(100, 100));
		return mapButton;
	}
	
	
	/**
	 * Ermittelt MapSkin durch denn ausgewählten mapButton
	 * @param mapButton Der ausgewählte Button für die Map auswahl.
	 * @return
	 */
	public String getMapSkin(JToggleButton mapButton) {
		if (mapButton == map1Button) {
			return "friedhof";
		} else if (mapButton == map2Button) {
			return "krankenhaus";
		} else if (mapButton == map3Button) {
			return "supermarkt";
		} else {
			return getRandomMap();
		}
	}


	/**
	 * hier wird der Timer für Fehlermeldungen initialisiert.
	 */
	private void iniError() {
		errorLabel = new JLabel();
		errorLabel.setForeground(Color.red);
		gbc.gridx = 1;
		gbc.gridy = 10;
		gbc.gridwidth = 1;

		errorTimer = new Timer(5000, e -> {
			errorLabel.setText("");
			errorTimer.stop();
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		imageIcon.paintIcon(this, g, 0, 0);
	}

	/**
	 * Hier wird zufällig eine Map gewählt und zurück gegeben. 
	 * @return
	 */
	private String getRandomMap() {
		Random random = new Random();
		int randomNummer = random.nextInt(3);
		switch (randomNummer) {
		case 0:
			return "friedhof";
		case 1:
			return "krankenhaus";
		case 2:
			return "supermarkt";
		default:
			return "friedhof"; // Sollte im Normalfall nicht erreicht werden
		}
	}

	/**
	 * Die Layouteinstellung für das HostPanel
	 */
	private void configureLayout() {
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridwidth = 2;

		iniError();

		add(mapAuswahl, gbc);
		gbc.gridy++;
		add(hostNameLabel, gbc);
		gbc.gridy++;
		add(hostNameField, gbc);
		gbc.gridy++;
		add(errorLabel, gbc);
		gbc.gridy++;
		add(hostPortLabel, gbc);
		gbc.gridy++;
		add(hostPortField, gbc);
		gbc.gridy++;
		add(hostIPLabel, gbc);
		gbc.gridy++;
		add(hostIPField, gbc);
		gbc.gridy++;
		add(winRoundLabel, gbc);
		gbc.gridy++;
		add(winRoundComboBox, gbc);
		gbc.gridy++;
		add(playerNumberLabel, gbc);
		gbc.gridy++;
		add(playerNumberComboBox, gbc);
		gbc.gridy++;
		add(gameStartButton, gbc);
		gbc.gridy++;
		add(toMenuButton, gbc);
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
	}

	/**
	 * Wenn alle eingegeben Daten gültig sind wird das Spiel gestartet
	 */
	public void switchToGame() {

		String name = hostNameField.getText().trim();
		if (portFree && validName && validPort && validIP && name.length() > 0 && name.length() <= 8) {
			Sprite.loadImages();
			// Sprite.loadNumberImages();
			Sprite.setMaxLoopStatus();
			errorLabel.setText(" ");
			errorTimer.stop();
			if (mapSkin == null)
				mapSkin = "friedhof";
			startGame();
		} else {
			errorLabel.setText("Ungültiger Name! Bitte geben Sie einen Namen ein zwischen 1 und 8 Zeichen");
			errorTimer.start();
		}
	}

	/**
	 * Startet das Spiel, indem der Server initialisiert und gestartet wirdund die Initialisierung des Clients.
	 */
	private void startGame() {
		serverOK = true;
		server = new Server(this, Integer.valueOf(hostPortField.getText()),
				Character.getNumericValue(((String) winRoundComboBox.getSelectedItem()).charAt(0)), mapSkin,
				Character.getNumericValue(((String) playerNumberComboBox.getSelectedItem()).charAt(0)));
		server.start();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (serverOK) {
			client = new Client(this, hostIPField.getText(), Integer.valueOf(hostPortField.getText()),
					hostNameField.getText());
			CardLayout layout = (CardLayout) getContentPanel().getLayout();
			getContentPanel().addGame();
			layout.show(getContentPanel(), "game");
			getContentPanel().getGame().requestFocusInWindow();
		}
	}

	/**
	 * Wechselt zur Menüansicht.
	 */
	public void switchToMenu() {
		CardLayout layout = (CardLayout) getContentPanel().getLayout();
		layout.show(getContentPanel(), "menu");
	}

	/**
	 * Ändert die Sprache der Oberfläsche. Nach ausgewählter Sprache
	 * @param germanSelected
	 * @param englishSelected
	 */
	public void changeLanguage(boolean germanSelected, boolean englishSelected) {
		if (germanSelected) {
			gameStartButton.changePath("spielstarten");
			toMenuButton.changePath("verlassen");
			hostNameLabel.setText("Benutzername:");
			winRoundLabel.setText("Anzahl der Siege:");
			winRoundComboBox.setModel(new DefaultComboBoxModel<>(
					new String[] { "1 Runde", "2 Runden", "3 Runden", "4 Runden", "5 Runden" }));
			playerNumberLabel.setText("Anzahl der Spieler:");
		} else {
			gameStartButton.changePath("start");
			toMenuButton.changePath("back");
			hostNameLabel.setText("Username:");
			winRoundLabel.setText("Number of wins:");
			winRoundComboBox.setModel(new DefaultComboBoxModel<>(
					new String[] { "1 Round", "2 Rounds", "3 Rounds", "4 Rounds", "5 Rounds" }));
			playerNumberLabel.setText("Number of Players:");
		}
	}

	/**
	 * @return
	 */
	public ContentPanel getContentPanel() {
		return contentPanel;
	}

	/**
	 * @param contentPanel
	 */
	public void setContentPanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
	}

	/**
	 * Wechselt zur Spielansicht nach einem Neustart.
	 */
	public void restartGame() {
		CardLayout layout = (CardLayout) getContentPanel().getLayout();
		// getContentPanel().addGame();
		layout.show(getContentPanel(), "lade");
		closeGame();
		// DO SOMETHING
		try {
			Thread.sleep(8500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		startGame();
	}
	
	/**
	 * Wird aufgerufen, wenn kein Serverfehler vorliegt.
	 */
	public void keinServerError() {}
			
	/**
	 * Bindet den Serverfehler und zeigt entsprechende Meldungen an.
	 */
	public void bindServerError() {
		errorLabel.setText("Ein Server ist auf diesen Port schon vorhanden!");
		errorTimer.start();
		serverOK = false;
	}

	/**
	 * Startet die Siegerehrung und wechselt zum entsprechenden Panel
	 * @param winner
	 */
	public void startSiegerehrung(String winner) {
		contentPanel.getSiegerehrung().setSieger(winner);
		CardLayout layout = (CardLayout) getContentPanel().getLayout();
		layout.show(getContentPanel(), "siegerehrung");
		getContentPanel().getSiegerehrung().requestFocusInWindow();
		closeGame();
	}

	/**
	 * Schließt das laufende Spiel.
	 */
	private void closeGame() {
		getContentPanel().deleteGame();
	}
}

