package main;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * @author BoOom
 * Das LadePanel ist ein JPanel.Es zeigt ein Ladebild während des Spiels. 
 *
 */
public class LadePanel extends JPanel {

	private ContentPanel contentPanel; 
	private ImageIcon imageIcon; // ImageIcon zur Darstellung des Ladebilds

	/**
	 * Konstruktor für das LadePanel 
	 * @param contentPanel gehört zu diesem LadePanel.
	 */
	public LadePanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
		//Hier wird das Ladebild geladen und die größe des Bildes.
		imageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/menu/loadbild.gif")).getImage()
				.getScaledInstance(contentPanel.getScreenX(), contentPanel.getScreenY(), Image.SCALE_DEFAULT));

		setLayout(null);
		repaint();
		setFocusable(true);
	}

	/**
	 * Wird benutzt um das bild zu zeichnen
	 *@param g zum Zeichnen des Bildes 
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		imageIcon.paintIcon(this, g, 0, 0);
	}
}
