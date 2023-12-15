
package main;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.OverlayLayout;
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
    ContentPanel contentPanel;

    public MenuPanel(ContentPanel contentPanel) {
        this.contentPanel = contentPanel;

        setLayout(new GridBagLayout());
      

        // JPanel für die Deutsch und Englisch ToggleButtons links oben
        JPanel languagePanel = new JPanel();
        languagePanel.setLayout(new BoxLayout(languagePanel, BoxLayout.X_AXIS));

        languageGermanToggleButton = new JToggleButton();
        languageGermanToggleButton.setIcon(new ImageIcon(MenuPanel.class.getResource("/menu/gerFlag.png")));
        languageGermanToggleButton.setFocusPainted(false);
        languageGermanToggleButton.setSelected(true);
        languagePanel.add(languageGermanToggleButton);

        languageEnglishToggleButton = new JToggleButton();
        languageEnglishToggleButton.setIcon(new ImageIcon(MenuPanel.class.getResource("/menu/engFlag.png")));
        languageEnglishToggleButton.setFocusPainted(false);
        languageEnglishToggleButton.setSelected(true);
        languagePanel.add(languageEnglishToggleButton);

        // Positioniere das languagePanel links oben im GridBagLayout
        GridBagConstraints gbcLanguage = new GridBagConstraints();
        gbcLanguage.gridx = 0;
        gbcLanguage.gridy = 0;
        gbcLanguage.anchor = GridBagConstraints.NORTHWEST;
        gbcLanguage.insets = new Insets(5, 5, 5, 5);
        add(languagePanel, gbcLanguage);

        // JPanel für die zentrierten Knöpfe
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());

        hostButton = new JButton("Spiel hosten");
        hostButton.setFocusPainted(false);
        hostButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) contentPanel.getLayout();
                layout.show(contentPanel, "host");
            }
        });
        GridBagConstraints gbcHostButton = new GridBagConstraints();
        gbcHostButton.gridx = 0;
        gbcHostButton.gridy = 0;
        gbcHostButton.fill = GridBagConstraints.HORIZONTAL;
        gbcHostButton.insets = new Insets(5, 5, 5, 5);
        centerPanel.add(hostButton, gbcHostButton);

        joinButton = new JButton("Spiel beitreten");
        joinButton.setFocusPainted(false);
        joinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) contentPanel.getLayout();
                layout.show(contentPanel, "join");
            }
        });
        GridBagConstraints gbcJoinButton = new GridBagConstraints();
        gbcJoinButton.gridx = 0;
        gbcJoinButton.gridy = 1;
        gbcJoinButton.fill = GridBagConstraints.HORIZONTAL;
        gbcJoinButton.insets = new Insets(5, 5, 5, 5);
        centerPanel.add(joinButton, gbcJoinButton);

        exitButton = new JButton("Beenden");
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        GridBagConstraints gbcExitButton = new GridBagConstraints();
        gbcExitButton.gridx = 0;
        gbcExitButton.gridy = 2;
        gbcExitButton.fill = GridBagConstraints.HORIZONTAL;
        gbcExitButton.insets = new Insets(5, 5, 5, 5);
        centerPanel.add(exitButton, gbcExitButton);

        // Positioniere das centerPanel in der Mitte des GridBagLayouts
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.gridx = 1;
        gbcCenter.gridy = 0;
        gbcCenter.weightx = 1.0;
        gbcCenter.weighty = 1.0;
        gbcCenter.fill = GridBagConstraints.CENTER;
        add(centerPanel, gbcCenter);

        // JPanel für den soundSlider und soundLabel rechts oben
        JPanel soundPanel = new JPanel();
        soundPanel.setLayout(new BoxLayout(soundPanel, BoxLayout.X_AXIS));

        soundLabel = new JLabel();
        soundLabel.setIcon(new ImageIcon(MenuPanel.class.getResource("/menu/speaker.png")));
        soundPanel.add(soundLabel);

        soundSlider = new JSlider();
        soundSlider.setOpaque(true);
        soundSlider.setMaximum(5);
        soundSlider.setMinorTickSpacing(1);
        soundSlider.setSnapToTicks(true);
        soundSlider.setPaintTicks(true);
        soundSlider.setPaintLabels(true);
        soundPanel.add(soundSlider);

        // Positioniere das soundPanel rechts oben im GridBagLayout
        GridBagConstraints gbcSound = new GridBagConstraints();
        gbcSound.gridx = 2;
        gbcSound.gridy = 0;
        gbcSound.anchor = GridBagConstraints.NORTHEAST;
        gbcSound.insets = new Insets(5, 5, 5, 5);
        add(soundPanel, gbcSound);


    }
}
