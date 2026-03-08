package main;

import javax.swing.JPanel;

import entity.*;

import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class StartPanel extends JPanel {

	ContentPanel cp;
	JButton toMenuButton;
	ImageIcon imageIcon;
	URL url;

	public StartPanel(ContentPanel cp) {
		this.cp = cp;
		imageIcon = new ImageIcon("res//menu//startbildzombieman.gif");
        repaint();
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
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        imageIcon.paintIcon(this, g, 0, 0);
    }
}
