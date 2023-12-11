package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import entity.Konstante;

public class GameFrame extends JFrame {

	public ContentPanel contentPanel;

	public static void main(String[] args) {

		// System.setProperty( "sun.java2d.uiScale", "1.0" );

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameFrame frame = new GameFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GameFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP + 14, Konstante.LIN * Konstante.SIZE_SPRITE_MAP + 36);
		setTitle("ZombieMan");
		setResizable(false);

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		contentPanel = new ContentPanel();
		setContentPane(contentPanel);
		// mit pack gibt es aktuell probleme, deshalb vorerst feste werte mit setBounds
		// festgelegt
		// pack();
	}
}
