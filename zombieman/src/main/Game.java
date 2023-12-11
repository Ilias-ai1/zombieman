package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;
import net.*;
import entity.*;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	public static Spieler du;
	public static Spieler gegner1;
	public static Spieler gegner2;
	public static Spieler gegner3;
	ContentPanel cp;

	public Game(ContentPanel cp, int width, int height) {
		setPreferredSize(new Dimension(width, height));
		this.cp = cp;
		System.out.print("Warten auf Spieler...\n");
		du = new Spieler(Client.id, this);
		gegner1 = new Spieler((Client.id + 1) % Konstante.MAX_SPIELER, this);
		gegner2 = new Spieler((Client.id + 2) % Konstante.MAX_SPIELER, this);
		gegner3 = new Spieler((Client.id + 3) % Konstante.MAX_SPIELER, this);
		addKeyListener(new Sender());
		System.out.println("Mein Spieler: " + Sprite.personSkins[Client.id] + "\n");
	}

	// paint() und repaint() werden aufgerufen
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawMap(g);
		gegner1.draw(g);
		gegner2.draw(g);
		gegner3.draw(g);
		du.draw(g);

		Toolkit.getDefaultToolkit().sync();
	}

	void drawMap(Graphics g) {
		for (int i = 0; i < Konstante.LIN; i++)
			for (int j = 0; j < Konstante.COL; j++)
				g.drawImage(Sprite.ht.get(Client.map[i][j].img), Client.map[i][j].x, Client.map[i][j].y,
						Konstante.SIZE_SPRITE_MAP, Konstante.SIZE_SPRITE_MAP, null);
	}

	public static void setSpriteMap(String keyWord, int l, int c) {
		Client.map[l][c].img = keyWord;
	}
}