package main;

import java.awt.CardLayout;

import javax.swing.JPanel;

import entity.Konstante;

public class ContentPanel extends JPanel {

	CardLayout cardLayout;
	StartPanel startPanel;
	MenuPanel menuPanel;
	HostPanel hostPanel;
	JoinPanel joinPanel;
	Game game;

	public ContentPanel() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);

		startPanel = new StartPanel(this);
		add(startPanel, "start");

		menuPanel = new MenuPanel(this);
		add(menuPanel, "menu");

		hostPanel = new HostPanel(this);
		add(hostPanel, "host");

		joinPanel = new JoinPanel(this);
		add(joinPanel, "join");
	}

	public void addGame() {
		game = new Game(this, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
		add(game, "game");
	}
}
