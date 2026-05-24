package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import entity.Konstante;
import entity.Sprite;
import net.Client;

@SuppressWarnings("serial")

/**
 * Die Klasse JoinPanel repräsentiert das Panel, in dem der Benutzer einem Spiel beitreten kann.
 * Hier kann der Benutzer seinen Benutzernamen, den Port und die IP-Adresse des Servers eingeben.
 */
public class JoinPanel extends JPanel {

	private Buttons gameJoinButton, toMenuButton;
	private JLabel joinNameLabel, joinPortLabel, joinIPLabel;
	private JTextField joinIPField, joinPortField, joinNameField;
	private ImageIcon imageIcon;
	private boolean serverExists, validName, validPort, validIP;
	private ContentPanel contentPanel;
	private GridBagConstraints gbc;
	private JLabel errorLabel;
	private Timer errorTimer;
	private int portNum;

    /**
     * Erstellt ein neues JoinPanel mit dem angegebenen ContentPanel.
     * contentPanel: Das ContentPanel, das mit diesem JoinPanel verknüpft ist.
     */
    public JoinPanel(ContentPanel contentPanel) {
        this.setContentPanel(contentPanel);
        
        setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
        setLayout(new GridBagLayout());
        serverExists = true;
        validName = true;
        validPort = true;
        validIP = true;

		initializeComponents();
	}
     //Initialisiert die GUI-Komponenten des JoinPanels.
    private void initializeComponents() {
		imageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/menu/serverbackground.gif")).getImage()
				.getScaledInstance(contentPanel.getScreenX(), contentPanel.getScreenY(), Image.SCALE_DEFAULT));

		joinNameLabel = new JLabel("Benutzername:");
		joinNameLabel.setForeground(Color.red);

		joinPortLabel = new JLabel("Port:");
		joinPortLabel.setForeground(Color.red);
		joinPortField = new JTextField("1331");
		joinPortField.setColumns(10);

		joinIPLabel = new JLabel("IP:");
		joinIPLabel.setForeground(Color.red);
		joinIPField = new JTextField("127.0.0.1");
		joinIPField.setColumns(10);

		gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridwidth = 1;
		add(joinNameLabel, gbc);

		joinNameField = new JTextField();
		joinNameField.setColumns(10);
		gbc.gridx = 0;
		gbc.gridy++;
		add(joinNameField, gbc);
		gbc.gridy++;
		add(joinPortLabel, gbc);
		gbc.gridy++;
		add(joinPortField, gbc);
		gbc.gridy++;
		add(joinIPLabel, gbc);
		gbc.gridy++;
		add(joinIPField, gbc);

		errorLabel = new JLabel();
		errorLabel.setForeground(Color.red);
		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.gridwidth = 1;

		errorTimer = new Timer(5000, e -> {
			errorLabel.setText("");
			errorTimer.stop();
		});
		gbc.gridy++;
		add(errorLabel, gbc);

		gameJoinButton = new Buttons(this, "spielbeitreten");
		gameJoinButton.setFocusPainted(false);
		gameJoinButton.setPreferredSize(new Dimension(140, 40));

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		add(gameJoinButton, gbc);

		toMenuButton = new Buttons(this, "verlassen");
		toMenuButton.setFocusPainted(false);
		toMenuButton.setPreferredSize(new Dimension(140, 40));

		toMenuButton.setPreferredSize(gameJoinButton.getPreferredSize());
		gbc.gridy++;
		add(toMenuButton, gbc);
	}
    
     //Wechselt zur Menüansicht.
    public void switchToMenu() {
        CardLayout layout = (CardLayout) getContentPanel().getLayout();
        layout.show(getContentPanel(), "menu");
    }

    
     //Wechselt zur Spielansicht, wenn alle Eingaben gültig sind.
    public void switchToGame() {
    	String name = joinNameField.getText().trim();
        if (serverExists && validName && validPort && validIP && name.length() > 0 && name.length() < 8) {
            Sprite.loadImages();
            //Sprite.loadNumberImages();
            Sprite.setMaxLoopStatus();
            errorLabel.setText(" ");
			errorTimer.stop();
			startGame();
        } else {
        	errorLabel.setText("Ungültiger Name! Bitte geben Sie einen Namen ein zwischen 1 und 8 Zeichen");
			errorTimer.start();}
    }
    
    
    
     //Startet das Spiel.
    private void startGame() {
    	new Client(this,joinIPField.getText(), Integer.valueOf(joinPortField.getText()), joinNameField.getText());
        CardLayout layout = (CardLayout) getContentPanel().getLayout();
        getContentPanel().addGame();
        layout.show(getContentPanel(), "game");
        getContentPanel().getGame().requestFocusInWindow();
	}
    
    
    
     //Startet das Spiel neu.
    public void restartGame() {
		CardLayout layout = (CardLayout) getContentPanel().getLayout();
		getContentPanel().addGame();
		layout.show(getContentPanel(), "lade");
		closeGame();
		// verarbeite rundeninfo
		// PROBLEM JOIN SCHNELLER ALS SERVER GEMACHT IST, MUSS KURZ WARTEN
		// LadeBildschirm ZEIGEN
		try {
			Thread.sleep(8800);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		startGame();
	}

	public void startSiegerehrung(String winner) {
		contentPanel.getSiegerehrung().setSieger(winner);
		CardLayout layout = (CardLayout) getContentPanel().getLayout();
		layout.show(getContentPanel(), "siegerehrung");
		getContentPanel().getSiegerehrung().requestFocusInWindow();
		closeGame();
	}

	public void keinServerError() {
		errorLabel.setText("Ungültige Server-Daten!");
		errorTimer.start();
	}

	private void closeGame() {
		getContentPanel().deleteGame();
		//System.out.println("deletegameJoin");
	}

	
	/**
     * Überprüft, ob ein Server mit der angegebenen IP-Adresse und dem Port existiert.
     * hostName: Die IP-Adresse des Servers.
     * port: Der Port des Servers.
     * return true: wenn der Server existiert, ansonsten false.
     */
    public static boolean serverExists(String hostName, int port) {
        try {
            Socket socket = new Socket(hostName, port);
            socket.close();
            return true;
        } catch (IOException e) {
            System.out.println("Unable to connect to " + hostName + ":" + port + ". " + e.getMessage());
        }
        return false;
    }

    @Override
    
     //Zeichnet das Hintergrundbild des JoinPanels.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        imageIcon.paintIcon(this, g, 0, 0);
    }

    
    /**
     * Passt die Sprache der GUI-Komponenten entsprechend den Benutzereinstellungen an.
     * germanSelected: true, wenn Deutsch ausgewählt ist, ansonsten false.
     * englishSelected: true, wenn Englisch ausgewählt ist, ansonsten false.
     */
    public void changeLanguage(boolean germanSelected, boolean englishSelected) {
        if (germanSelected) {
            gameJoinButton.changePath("spielbeitreten");
            toMenuButton.changePath("verlassen");
            joinNameLabel.setText("Benutzername:");
        } else {
            gameJoinButton.changePath("joingame");
            toMenuButton.changePath("back");
            joinNameLabel.setText("Username:");
        }
    }
    
    /**
     * Gibt das mit diesem JoinPanel verknüpfte ContentPanel zurück.
     * return: Das ContentPanel.
     */
	public ContentPanel getContentPanel() {
		return contentPanel;
	}

	
	/**
     * Setzt das ContentPanel für dieses JoinPanel.
     * contentPanel: Das ContentPanel.
     */
	public void setContentPanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
	}
}
