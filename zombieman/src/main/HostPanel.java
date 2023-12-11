package main;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.*;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entity.Konstante;
import entity.Sprite;

public class HostPanel extends JPanel {

	public JButton gameStartButton;
	public JButton toMenuButton;
	JComboBox winRoundComboBox;
	JLabel winRoundLabel;
	JLabel hostNameLabel;
	JTextField hostNameField;
	BgLabel bgLabel;
	boolean portFree = true;
	boolean validName = true;

	ContentPanel cp;
	Server server;

	public HostPanel(ContentPanel cp) {
		this.cp = cp;
		setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
		setLayout(null);

		gameStartButton = new JButton("Spiel starten");
		gameStartButton.setBounds(466, 392, 130, 35);
		gameStartButton.setFocusPainted(false);
		add(gameStartButton);

		toMenuButton = new JButton("Verlassen");
		toMenuButton.setBounds(466, 438, 130, 35);
		toMenuButton.setFocusPainted(false);
		toMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout layout = (CardLayout) cp.getLayout();
				layout.show(cp, "menu");
			}
		});
		add(toMenuButton);

		winRoundComboBox = new JComboBox();
		winRoundComboBox.setModel(new DefaultComboBoxModel(new String[] { "1 Runde", "2 Runden", "3 Runden", "4 Runden", "5 Runden" }));
		winRoundComboBox.setBounds(532, 346, 80, 35);
		winRoundComboBox.setFocusable(false);
		add(winRoundComboBox);

		winRoundLabel = new JLabel("Siegbedingung");
		winRoundLabel.setBounds(450, 346, 80, 35);
		add(winRoundLabel);

		hostNameLabel = new JLabel("Name");
		hostNameLabel.setBounds(466, 300, 41, 35);
		add(hostNameLabel);

		hostNameField = new JTextField();
		hostNameField.setBounds(508, 300, 88, 35);
		add(hostNameField);
		hostNameField.setColumns(10);

		bgLabel = new BgLabel();
		add(bgLabel);

		gameStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (portFree && validName) {
					Sprite.loadImages();
					Sprite.setMaxLoopStatus();
					String string = (String) winRoundComboBox.getSelectedItem();
					char chara = string.charAt(0);
					int num = Character.getNumericValue(chara);
					server = new Server(1331, num);
					server.start();
					new Client("127.0.0.1", 1331, hostNameField.getText());
					CardLayout layout = (CardLayout) cp.getLayout();
					cp.addGame();
					layout.show(cp, "game");
					cp.game.requestFocusInWindow();
				}
			}
		});
	}
}
