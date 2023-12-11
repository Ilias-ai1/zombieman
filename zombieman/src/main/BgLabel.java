package main;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import entity.Konstante;

public class BgLabel extends JLabel {
	public BgLabel() {
		// Platzhalter png
		setIcon(new ImageIcon(BgLabel.class.getResource("/menu/Bg.jpg")));
		setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
	}
}
