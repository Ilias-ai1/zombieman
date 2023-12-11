package main;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;

import entity.*;

public class MenuPanel extends JPanel {

	JButton hostButton;
	JButton joinButton;
	JButton exitButton;
	JLabel languageLabel;
	JSlider soundSlider;
	JLabel soundLabel;
	JToggleButton languageGermanToggleButton;
	JToggleButton languageEnglishToggleButton;
	BgLabel bgLabel;
	ContentPanel cp;

	public MenuPanel(ContentPanel cp) {
		this.cp = cp;
		setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
		setLayout(null);

		hostButton = new JButton("Spiel hosten");
		hostButton.setBounds(469, 280, 130, 35);
		hostButton.setFocusPainted(false);
		hostButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout layout = (CardLayout) cp.getLayout();
				layout.show(cp, "host");
			}
		});
		add(hostButton);

		joinButton = new JButton("Spiel beitreten");
		joinButton.setBounds(469, 326, 130, 35);
		joinButton.setFocusPainted(false);
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout layout = (CardLayout) cp.getLayout();
				layout.show(cp, "join");
			}
		});
		add(joinButton);

		exitButton = new JButton("Beenden");
		exitButton.setBounds(469, 464, 130, 35);
		exitButton.setFocusPainted(false);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		add(exitButton);

		languageLabel = new JLabel("Sprache");
		languageLabel.setBounds(450, 372, 52, 35);
		add(languageLabel);

		soundSlider = new JSlider();
		soundSlider.setOpaque(true);
		soundSlider.setMaximum(10);
		soundSlider.setMinorTickSpacing(2);
		soundSlider.setSnapToTicks(true);
		soundSlider.setPaintTicks(true);
		soundSlider.setPaintLabels(true);
		soundSlider.setBounds(504, 418, 112, 35);
		add(soundSlider);

		soundLabel = new JLabel();
		soundLabel.setIcon(new ImageIcon(MenuPanel.class.getResource("/menu/speaker.png")));
		soundLabel.setBounds(450, 418, 52, 35);
		add(soundLabel);

		// ordentliche Icons müssen eingesetzt werden, die aktuellen sind nur
		// Platzhalter
		languageGermanToggleButton = new JToggleButton("DE");
		// languageGermanToggleButton.setIcon(new
		// ImageIcon(MenuPanel.class.getResource("/menu/engFlag.png")));
		languageGermanToggleButton.setFocusPainted(false);
		languageGermanToggleButton.setSelected(true);
		languageGermanToggleButton.setBounds(512, 372, 50, 35);
		add(languageGermanToggleButton);

		languageEnglishToggleButton = new JToggleButton("EN");
		// languageEnglishToggleButton.setIcon(new
		// ImageIcon(MenuPanel.class.getResource("/menu/gerFlag.png")));
		languageEnglishToggleButton.setFocusPainted(false);
		languageEnglishToggleButton.setBounds(566, 372, 50, 35);
		add(languageEnglishToggleButton);

		languageGermanToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				languageGermanToggleButton.setSelected(true);
				languageEnglishToggleButton.setSelected(false);
			}
		});
		languageEnglishToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				languageEnglishToggleButton.setSelected(true);
				languageGermanToggleButton.setSelected(false);
			}
		});

		bgLabel = new BgLabel();
		add(bgLabel);
	}
}
