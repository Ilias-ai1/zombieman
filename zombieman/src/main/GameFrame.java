package main;

import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


@SuppressWarnings("serial")

/**
 * @author BoOom
 * Die Klasse GameFrame repräsentiert das Hauptfenster des Spiels.
 * Hier wird das Hauptpanel ContentPanel erstellt und als Inhalt des Fensters gesetzt.
 */
public class GameFrame extends JFrame {

	private ContentPanel contentPanel;

	/**
	 * Die main-Methode startet die Anwendung und erstellt ein neues GameFrame
	 * @param args
	 */
	public static void main(String[] args) {
		// System.setProperty( "sun.java2d.uiScale", "1.0" );
		EventQueue.invokeLater(() -> {
			try {
				GameFrame frame = new GameFrame();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Hier ist der Konstruktor und die Einstellungen für das GameFrame
	 */
	public GameFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = ge.getMaximumWindowBounds();
		setVisible(true); // TaskLeiste bei Windows bleibt erhalten
		setSize(bounds.width, bounds.height);
		Insets insets = getInsets();
		int frameWidth = bounds.width - insets.left - insets.right;
		int frameHeight = bounds.height - insets.top - insets.bottom;
		setSize(frameWidth, frameHeight);
		setLocation(bounds.x + insets.left, bounds.y + insets.top);
		setTitle("ZombieMan");
		setResizable(false);
		ImageIcon img = new ImageIcon(getClass().getResource("/menu/GameIcon.png"));
		setIconImage(img.getImage());
		contentPanel = new ContentPanel(frameWidth, frameHeight);
		setContentPane(contentPanel);
		pack();
		// setVisible(true);
	}
}
