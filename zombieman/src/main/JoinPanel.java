package main;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.Client;
import entity.*;

public class JoinPanel extends JPanel {

	JButton gameJoinButton;
	JButton toMenuButton;
	JLabel joinNameLabel;
	JTextField joinNameField;
	BgLabel bgLabel;
	boolean serverExists = true;
	boolean validName = true;

	ContentPanel cp;

	public JoinPanel(ContentPanel cp) {
		this.cp = cp;
		setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
		setLayout(null);

		gameJoinButton = new JButton("Spiel beitreten");
		gameJoinButton.setBounds(470, 371, 130, 35);
		gameJoinButton.setFocusPainted(false);
		gameJoinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (serverExists && validName) {
					Sprite.loadImages();
					Sprite.setMaxLoopStatus();
					new Client("127.0.0.1", 1331, joinNameField.getText());
					CardLayout layout = (CardLayout) cp.getLayout();
					cp.addGame();
					layout.show(cp, "game");
					cp.game.requestFocusInWindow();
				} else {
					JOptionPane.showMessageDialog(joinNameField, "Kein Server gefunden");
				}
			}
		});
		add(gameJoinButton);

		toMenuButton = new JButton("Verlassen");
		toMenuButton.setBounds(470, 417, 130, 35);
		toMenuButton.setFocusPainted(false);
		toMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout layout = (CardLayout) cp.getLayout();
				layout.show(cp, "menu");
			}
		});
		add(toMenuButton);

		joinNameLabel = new JLabel("Name");
		joinNameLabel.setBounds(470, 325, 41, 35);
		add(joinNameLabel);

		joinNameField = new JTextField();
		joinNameField.setColumns(10);
		joinNameField.setBounds(512, 325, 88, 35);
		add(joinNameField);

		bgLabel = new BgLabel();
		add(bgLabel);
	}

	public static boolean serverExists(String hostName, int port) {
		try {
			Socket socket = new Socket(hostName, port);
			socket.close();
			return true;
		} catch (SocketTimeoutException exception) {
			System.out.println("SocketTimeoutException " + hostName + ":" + port + ". " + exception.getMessage());
		} catch (IOException exception) {
			System.out.println("IOException - Unable to connect to " + hostName + ":" + port + ". " + exception.getMessage());
		}
		return false;
	}
}
