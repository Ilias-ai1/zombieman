package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.Server;

/**
 * @author BoOom
 * @see Server
 * Die Klasse SiegerehrungPanel ist ein JPanel welches nach dem Spiel den Sieger anzeigt der die meisten Siege hat.
 *
 */
public class SiegerehrungPanel extends JPanel {

	private ContentPanel contentPanel;
	private ImageIcon imageIcon;
	private String sieger;

	/**
	 * Hier ist der SiegerehrungPanel Konstruktor mit dem contentPAnel als Parameter.
	 * @param contentPanel
	 */
	public SiegerehrungPanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
		setSieger("");
		 imageIcon = new ImageIcon(new
		 ImageIcon(getClass().getResource("/menu/siegerehrung.gif")).getImage().getScaledInstance(contentPanel.getScreenX(),
		 contentPanel.getScreenY(), Image.SCALE_DEFAULT));
		setLayout(null);
		repaint();
		setFocusable(true);
		addKeyListener(new KAdapter());
	}

	/**
	 * Wird genutzt um den Sieger und das Auffordern zum Verlassen zu schreiben.
	 *@param g 
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		 imageIcon.paintIcon(this, g, 0, 0);
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial", Font.BOLD, 100));
		g.drawString("Sieger: " + getSieger(), contentPanel.getScreenX() / 3 - 30, contentPanel.getScreenY() / 2);
	}

	/**
	 * Gibt den Namen des Siegers zurück
	 * @return
	 */
	public String getSieger() {
		return sieger;
	}

	/**
	 * Hier wird der Name des Siegers gesetzt.
	 * @param privateSieger
	 */
	public void setSieger(String privateSieger) {
		sieger = privateSieger;
	}

	/**
	 * wird benötigt damit wenn man die EnterTaste drückt auch zum Menu zurück kommt.
	 *
	 */
	private class KAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				((CardLayout) contentPanel.getLayout()).show(contentPanel, "menu");
				contentPanel.setSiege(0);
				Server.shutdown();
			}
		}
	}
}
