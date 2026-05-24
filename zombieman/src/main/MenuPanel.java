package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * Die Klasse MenuPanel repräsentiert das Hauptmenü-Panel der Anwendung . Es
 * enthält Komponenten für die Sprachauswahl, Navigationsbuttons und einen
 * Lauter-Leiserregler.
 * 
 * @author BoOom
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class MenuPanel extends JPanel {

	private ContentPanel contentPanel;
	private Buttons hostButton, joinButton, exitButton, helpButton;
	private JToggleButton languageGermanToggleButton, languageEnglishToggleButton, sfxToggleButton, bgmToggleButton;
	private JLabel soundLabel, soundLabel2;
	private ImageIcon imageIcon, gerOn, gerOff, engOn, engOff, sfxOn, sfxOff, bgmOn, bgmOff;
	private boolean sfx, bgm;
	private JPanel soundPanel, soundPanel2;

	/**
	 * Konstruktor der Klasse MenuPanel.
	 * 
	 * @param contentPanel Initialisiert das MenuPanel mit den erforderlichen
	 *                     Komponenten und Einstellungen.
	 */
	public MenuPanel(ContentPanel contentPanel) {
		this.setContentPanel(contentPanel);
		this.contentPanel = contentPanel;
		// imageIcon = new ImageIcon("res//menu//serverbackground.gif");
		imageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/menu/serverbackground.gif")).getImage()
				.getScaledInstance(contentPanel.getScreenX(), contentPanel.getScreenY(), Image.SCALE_DEFAULT));

		repaint();
		setLayout(new GridBagLayout());
		setupLanguagePanel();
		setupCenterPanel();
		setupSoundPanel();
		setuphelpPanel();
	}

	/**
	 * Diese Methode erstellt ein JPanel, das für die Anzeige von sprachbezogenen
	 * Komponenten verwendet wird. Mit Constrains für das LanguagePanel.
	 */
	private void setupLanguagePanel() {
		JPanel languagePanel = new JPanel();
		languagePanel.setOpaque(false);
		languagePanel.setLayout(new BoxLayout(languagePanel, BoxLayout.X_AXIS));
		setupLanguageToggleButtons(languagePanel);

		GridBagConstraints gbcLanguage = new GridBagConstraints();
		gbcLanguage.gridx = 0;
		gbcLanguage.gridy = 0;
		gbcLanguage.anchor = GridBagConstraints.NORTHWEST;
		gbcLanguage.insets = new Insets(5, 5, 5, 5);
		add(languagePanel, gbcLanguage);

		// setLanguageButtonsState(true, false);
	}

	/**
	 * Setzt das Sprach-Panel auf. Hier werden die Toggle-Buttons für Deutsch und
	 * Englisch erstellt und konfiguriert.
	 * 
	 * @param languagePanel
	 */
	private void setupLanguageToggleButtons(JPanel languagePanel) {
		gerOn = new ImageIcon(MenuPanel.class.getResource("/menu/gerFlag.png"));
		gerOff = new ImageIcon(MenuPanel.class.getResource("/menu/gerFlagOff.png"));
		engOn = new ImageIcon(MenuPanel.class.getResource("/menu/engFlag.png"));
		engOff = new ImageIcon(MenuPanel.class.getResource("/menu/engFlagOff.png"));

		languageGermanToggleButton = createToggleButton(gerOn);
		languagePanel.add(languageGermanToggleButton);

		languageEnglishToggleButton = createToggleButton(engOff);
		languagePanel.add(languageEnglishToggleButton);
		addActionListenersToLanguageToggleButtons();
	}

	/**
	 * Setzt die Toggle-Buttons für Soundeffekte (SFX) und Hintergrundmusik (BGM)
	 * auf. Fügt ActionListener hinzu, um auf Klicks zu reagieren und den Sound
	 * entsprechend zu steuern.
	 */
	private void setupSoundToggleButtons() {
		sfx = true;
		bgm = true;
		sfxOn = new ImageIcon(MenuPanel.class.getResource("/menu/sfxOn.png"));
		sfxOff = new ImageIcon(MenuPanel.class.getResource("/menu/sfxOff.png"));
		bgmOn = new ImageIcon(MenuPanel.class.getResource("/menu/bgmOn.png"));
		bgmOff = new ImageIcon(MenuPanel.class.getResource("/menu/bgmOff.png"));

		sfxToggleButton = createToggleButton(sfxOn);
		soundPanel.add(sfxToggleButton);

		bgmToggleButton = createToggleButton(bgmOn);
		soundPanel2.add(bgmToggleButton);
		addActionListenersToSoundToggleButtons();
	}

	/**
	 * Fügt ActionListener zu den Sound-Toggle-Buttons hinzu, um auf Klicks zu
	 * reagieren.
	 */
	private void addActionListenersToSoundToggleButtons() {
		sfxToggleButton.addActionListener(e -> {
			getContentPanel().playButtonSound();
			if (sfx) {
				sfx = false;
				sfxToggleButton.setIcon(sfxOff);
				// SOUND GRUPPE MUSS SFX AUS MACHEN
				getContentPanel().changeSFX(true);
			} else {
				sfx = true;
				sfxToggleButton.setIcon(sfxOn);
				// SOUND GRUPPE MUSS SFX AN MACHEN
				getContentPanel().changeSFX(false);
			}
		});
		bgmToggleButton.addActionListener(e -> {
			getContentPanel().playButtonSound();
			if (bgm) {
				bgm = false;
				bgmToggleButton.setIcon(bgmOff);
				// SOUND GRUPPE MUSS BGM AUS MACHEN
				getContentPanel().changeBGM(true);
			} else {
				bgm = true;
				bgmToggleButton.setIcon(bgmOn);
				// SOUND GRUPPE MUSS BGM AN MACHEN
				getContentPanel().changeBGM(false);
			}
		});
	}

	/**
	 * Erstellt einen Toggle-Button mit dem angegebenen Icon.
	 * 
	 * @param icon
	 * @return
	 */
	public JToggleButton createToggleButton(ImageIcon icon) {
		JToggleButton toggleButton = new JToggleButton();
		toggleButton.setIcon(icon);
		toggleButton.setFocusPainted(false);
		toggleButton.setSelected(true);
		toggleButton.setOpaque(false);
		toggleButton.setPreferredSize(new Dimension(50, 35));
		return toggleButton;
	}

	/**
	 * Fügt ActionListener zu den Sprach-Toggle-Buttons hinzu, um auf Klicks zu
	 * reagieren.
	 */
	private void addActionListenersToLanguageToggleButtons() {
		languageGermanToggleButton.addActionListener(e -> {
			getContentPanel().playButtonSound();
			setLanguageButtonsState(true, false);
			languageGermanToggleButton.setIcon(gerOn);
			languageEnglishToggleButton.setIcon(engOff);
		});
		languageEnglishToggleButton.addActionListener(e -> {
			getContentPanel().playButtonSound();
			setLanguageButtonsState(false, true);
			languageEnglishToggleButton.setIcon(engOn);
			languageGermanToggleButton.setIcon(gerOff);
		});
	}

	/**
	 * Setzt die Zustände der Sprach-Toggle-Buttons basierend auf den übergebenen
	 * Parametern. Ändert auch die Hintergrundfarbe und ruft die Methode zur
	 * Sprachänderung im ContentPanel auf.
	 * 
	 * @param germanSelected
	 * @param englishSelected
	 */
	private void setLanguageButtonsState(boolean germanSelected, boolean englishSelected) {
		languageGermanToggleButton.setSelected(germanSelected);
		languageGermanToggleButton.setBackground(germanSelected ? Color.GREEN : Color.RED);
		languageEnglishToggleButton.setSelected(englishSelected);
		languageEnglishToggleButton.setBackground(englishSelected ? Color.GREEN : Color.RED);

		getContentPanel().changeLanguage(germanSelected, englishSelected);
	}

	/**
	 * hier wird das centerPanel gesetzt für die Buttons mit Constrainsts.
	 */
	private void setupCenterPanel() {
		JPanel centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new GridBagLayout());

		setupButtons(centerPanel);

		GridBagConstraints gbcCenter = new GridBagConstraints();
		gbcCenter.gridx = 1;
		gbcCenter.gridy = 0;
		gbcCenter.weightx = 1.0;
		gbcCenter.weighty = 1.0;
		gbcCenter.anchor = GridBagConstraints.CENTER;
		add(centerPanel, gbcCenter);
	}

	/**
	 * Setzt die Buttons im übergebenen Panel auf.
	 * 
	 * @param centerPanel
	 */
	private void setupButtons(JPanel centerPanel) {
		hostButton = new Buttons(this, "spielhosten");
		hostButton.setFocusPainted(false);
		hostButton.setPreferredSize(new Dimension(140, 40));

		joinButton = new Buttons(this, "spielbeitreten");
		joinButton.setFocusPainted(false);
		joinButton.setPreferredSize(new Dimension(140, 40));

		helpButton = new Buttons(this, "hilfe");
		helpButton.setFocusPainted(false);
		helpButton.setPreferredSize(new Dimension(50, 35));

		exitButton = new Buttons(this, "beenden");
		exitButton.setFocusPainted(false);
		exitButton.setPreferredSize(new Dimension(140, 40));

		GridBagConstraints gbcHostButton = createButtonConstraints(0);
		centerPanel.add(hostButton, gbcHostButton);

		GridBagConstraints gbcJoinButton = createButtonConstraints(1);
		centerPanel.add(joinButton, gbcJoinButton);

		GridBagConstraints gbcExitButton = createButtonConstraints(2);
		centerPanel.add(exitButton, gbcExitButton);
	}

	/**
	 * Erstellt Constraints für die Platzierung von Buttons im GridBagLayout.
	 * 
	 * @param gridy
	 * @return
	 */
	private GridBagConstraints createButtonConstraints(int gridy) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = gridy;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		return gbc;

	}

	/**
	 * Setzt das Hilfe-Panel auf und platziert es im Haupt-Panel
	 */
	private void setuphelpPanel() {
		GridBagConstraints gbchelpButton = new GridBagConstraints();
		gbchelpButton.gridx = 2;
		gbchelpButton.gridy = GridBagConstraints.REMAINDER;
		gbchelpButton.anchor = GridBagConstraints.SOUTHEAST;
		gbchelpButton.insets = new Insets(5, 5, 5, 5);
		add(helpButton, gbchelpButton);
	}

	/**
	 * Setzt das Sound-Panel auf, das die Lautstärke-Icons und Toggle-Buttons für
	 * SFX und BGM enthält
	 */
	private void setupSoundPanel() {
		soundPanel = new JPanel();
		soundPanel.setOpaque(false);
		soundPanel.setLayout(new BoxLayout(soundPanel, BoxLayout.X_AXIS));

		soundPanel2 = new JPanel();
		soundPanel2.setOpaque(false);
		soundPanel2.setLayout(new BoxLayout(soundPanel2, BoxLayout.X_AXIS));

		setupSoundToggleButtons();

		GridBagConstraints gbcSound = new GridBagConstraints();
		gbcSound.gridx = 1;
		gbcSound.gridy = 0;
		gbcSound.anchor = GridBagConstraints.NORTHEAST;
		gbcSound.insets = new Insets(5, 5, 5, 5);
		add(soundPanel, gbcSound);

		GridBagConstraints gbcSound2 = new GridBagConstraints();
		gbcSound2.gridx = 2;
		gbcSound2.gridy = 0;
		gbcSound2.anchor = GridBagConstraints.NORTHEAST;
		gbcSound2.insets = new Insets(5, 5, 5, 5);
		add(soundPanel2, gbcSound2);
	}

	/**
	 * Überschreibt die paintComponent-Methode, um das Hintergrundbild anzuzeigen.
	 * 
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		imageIcon.paintIcon(this, g, 0, 0);
	}

	/**
	 * Wechselt zum HostPanel im ContentPanel.
	 */
	public void switchToHost() {
		CardLayout layout = (CardLayout) getContentPanel().getLayout();
		layout.show(getContentPanel(), "host");
	}

	/**
	 * * Wechselt zum JoinPanel im ContentPanel.
	 */
	public void switchToJoin() {
		CardLayout layout = (CardLayout) getContentPanel().getLayout();
		layout.show(getContentPanel(), "join");
	}

	/**
	 * * Wechselt zum HelpPanel im ContentPanel.
	 */
	public void switchToHelp() {
		CardLayout layout = (CardLayout) getContentPanel().getLayout();
		layout.show(getContentPanel(), "help");
	}

	/**
	 * Beendet Die Anwendung.
	 */
	public void exit() {
		System.exit(0);
	}

	/**
	 * Ändert die Sprache im ContentPanel basierend auf den übergebenen Parametern.
	 * 
	 * @param germanSelected
	 * @param englishSelected
	 */
	public void changeLanguage(boolean germanSelected, boolean englishSelected) {
		if (germanSelected) {
			hostButton.changePath("spielhosten");
			joinButton.changePath("spielbeitreten");
			helpButton.changePath("hilfe");
			exitButton.changePath("beenden");
		} else {
			hostButton.changePath("hostgame");
			joinButton.changePath("joingame");
			helpButton.changePath("help");
			exitButton.changePath("exit");
		}
	}

	/**
	 * Gibt das ContentPanel-Objekt zurück, dem dieses MenuPanel zugeordnet ist.
	 * 
	 * @return
	 */
	public ContentPanel getContentPanel() {
		return contentPanel;
	}

	/**
	 * Setzt das ContentPanel-Objekt
	 * 
	 * @param contentPanel
	 */
	public void setContentPanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
	}

	public ImageIcon getGerOn() {
		return this.gerOn;
	}

	public Buttons getHostButton() {
		
		return this.hostButton;
	}

	public Buttons getJoinButton() {
		return this.joinButton;
	}

	public Buttons getHelpButton() {
		return this.helpButton;
	}

	public Buttons getExitButton() {
		return this.exitButton;
	}
	
}
