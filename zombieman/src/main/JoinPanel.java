
package main;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entity.Const;
import entity.Sprite;
import net.Client;

public class JoinPanel extends JPanel {

    JButton gameJoinButton;
    JButton toMenuButton;
    JLabel joinNameLabel;
    JTextField joinNameField;
    BgLabel bgLabel;
    boolean serverExists = true;
    boolean validName = true;

    ContentPanel contentPanel;

    public JoinPanel(ContentPanel contentPanel) {
        this.contentPanel = contentPanel;
        setBounds(0, 0, Const.COL * Const.SIZE_SPRITE_MAP, Const.LIN * Const.SIZE_SPRITE_MAP);
        setLayout(new GridBagLayout());

        joinNameLabel = new JLabel("Name:");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(joinNameLabel, gbc);

        joinNameField = new JTextField();
        joinNameField.setColumns(10);
        gbc.gridx++;
        add(joinNameField, gbc);

        gameJoinButton = new JButton("Spiel beitreten");
        gameJoinButton.setFocusPainted(false);
        gameJoinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (serverExists && validName) {
                    Sprite.loadImages();
                    Sprite.setMaxLoopStatus();
                    new Client("127.0.0.1", 1331, joinNameField.getText());
                    CardLayout layout = (CardLayout) contentPanel.getLayout();
                    contentPanel.addGame();
                    layout.show(contentPanel, "game");
                    contentPanel.game.requestFocusInWindow();
                } else {
                    JOptionPane.showMessageDialog(joinNameField, "Kein Server gefunden");
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(gameJoinButton, gbc);

        toMenuButton = new JButton("Verlassen");
        toMenuButton.setFocusPainted(false);
        toMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) contentPanel.getLayout();
                layout.show(contentPanel, "menu");
            }
        });
        gbc.gridy++;
        add(toMenuButton, gbc);

//        bgLabel = new BgLabel();
//        gbc.gridx = 0;
//        gbc.gridy++;
//        gbc.gridwidth = 2;
       // add(bgLabel, gbc);
    }

    public static boolean serverExists(String hostName, int port) {
        try {
            Socket socket = new Socket(hostName, port);
            socket.close();
            return true;
        } catch (SocketTimeoutException exception) {
            System.out.println("SocketTimeoutException " + hostName + ":" + port + ". " + exception.getMessage());
        } catch (IOException exception) {
            System.out.println(
                    "IOException - Unable to connect to " + hostName + ":" + port + ". " + exception.getMessage());
        }
        return false;
    }
}
