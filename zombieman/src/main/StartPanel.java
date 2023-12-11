package main;

import javax.swing.JPanel;

import entity.*;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class StartPanel extends JPanel {

	ContentPanel cp;
	JButton toMenuButton;
	BgLabel bgLabel;

	public StartPanel(ContentPanel cp) {
		setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
		setLayout(null);

		JButton toMenuButton = new JButton();
		toMenuButton.setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
		toMenuButton.setFocusPainted(false);
		toMenuButton.setOpaque(false);
		toMenuButton.setContentAreaFilled(false);
		toMenuButton.setBorderPainted(false);
		toMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout layout = (CardLayout) cp.getLayout();
				layout.show(cp, "menu");
			}
		});
		add(toMenuButton);

		bgLabel = new BgLabel();
		add(bgLabel);

	}
}
