package main;

import javax.swing.JPanel;
import entity.Konstante;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/**
 * @author BoOom
 * @version 1.0 Die Klasse StartPanel repräsentiert ein JPanel und dient als
 *          Startbildschirm des Spiels. Das Panel zeigt ein animiertes Bild und
 *          hört auf die 'Enter'-Taste, um zum Menü-Panel zu wechseln.
 */

@SuppressWarnings("serial")
public class StartPanel extends JPanel {

	private ContentPanel contentPanel;
	private ImageIcon imageIcon; // Repräsentiert das animierte Bild für den Startbildschirm.

	/**
	 * Konstruiert ein StartPanel.
	 * 
	 * @param contentPanel
	 */
	public StartPanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;

		// Hier wird das animierte Bild geladen und skaliert.
		imageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/menu/startbildzombieman.gif")).getImage()
				.getScaledInstance(contentPanel.getScreenX(), contentPanel.getScreenY(), Image.SCALE_DEFAULT));

		setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
		setLayout(null);
		repaint();
		setFocusable(true);
		addKeyListener(new KeyAdapterImplementation());
	}

	/**
	 * Zeichnet die Komponente, indem das animierte Bild auf das Panel gemalt wird.
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		imageIcon.paintIcon(this, g, 0, 0);
	}

	/**
	 * Der KeyAdapter reagiert auf Tastenereignisse und wechselt zum Menü-Panel,
	 * wenn die 'Enter'-Taste gedrückt wird.
	 */
	private class KeyAdapterImplementation extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				((CardLayout) contentPanel.getLayout()).show(contentPanel, "menu");
			}
		}
	}
}
