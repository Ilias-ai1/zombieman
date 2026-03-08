
package main;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import entity.Konstante;
import entity.Sprite;
import net.Client;
import net.Server;

public class HostPanel extends JPanel {

    public JButton gameStartButton;
    public JButton toMenuButton;
    JComboBox winRoundComboBox;
    JLabel winRoundLabel;
    JLabel hostNameLabel;
    JTextField hostNameField;
    ImageIcon imageIcon;
    boolean portFree = true;
    boolean validName = true;

    ContentPanel contentPanel;
    Server server;

    public HostPanel(ContentPanel contentPanel) {
        this.contentPanel = contentPanel;
        imageIcon = new ImageIcon("res//menu//serverbackground.gif");
        repaint();
        setBounds(0, 0, Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
        setLayout(new GridBagLayout());
        hostNameLabel = new JLabel("Name:");
        hostNameField = new JTextField();
        hostNameField.setColumns(10);

        winRoundLabel = new JLabel("Anzahl der Siege:");
        winRoundComboBox = new JComboBox<>(new String[]{"1 Runde", "2 Runden", "3 Runden", "4 Runden", "5 Runden"});
        winRoundComboBox.setFocusable(false);

        gameStartButton = new JButton("Spiel starten");
        gameStartButton.setFocusPainted(false);

        toMenuButton = new JButton("Verlassen");
        toMenuButton.setFocusPainted(false);

      //  bgLabel = new BgLabel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 2; // Setze die Breite auf 2, um den Button über zwei Spalten zu strecken

        add(hostNameLabel, gbc);
        gbc.gridy++;
        add(hostNameField, gbc);
        gbc.gridy++;
        add(winRoundLabel, gbc);
        gbc.gridy++;
        add(winRoundComboBox, gbc);
        gbc.gridy++;
        add(gameStartButton, gbc);
        gbc.gridy++;
        add(toMenuButton, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1; // Setze die Breite zurück auf 1 für den Rest der Komponenten
       // add(bgLabel, gbc);

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
                    CardLayout layout = (CardLayout) contentPanel.getLayout();
                    contentPanel.addGame();
                    layout.show(contentPanel, "game");
                    contentPanel.game.requestFocusInWindow();
                }
            }
        });

        toMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) contentPanel.getLayout();
                layout.show(contentPanel, "menu");
            }
        });
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        imageIcon.paintIcon(this, g, 0, 0);
    }
}
