package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Die Klasse HelpPanel ist ein Panel das Hilfsinformation bereitstellt. Es
 * enthält eine Schaltfläche zum Navigieren zwischen verschiedenen Hilfsabschnitten
 * und eine Schaltfläche zum Zurückkehren zum Hauptmenü.
 * 
 * @author BoOom
 *
 */
@SuppressWarnings("serial")
public class HelpPanel extends JPanel {

    private ContentPanel contentPanel;
    private JButton nextButton;
    private JLabel backgroundLabel;
    private ImageIcon[] backgroundIcon;
    private int w, h;
    private int infNummer;
    private int currentInfo;

    /**
     * Erstellt ein neues HelpPanel mit dem angegebenen ContentPanel.
     * 
     * @param contentPanel Das ContentPanel, das mit diesem HelpPanel verknüpft ist.
     */
    public HelpPanel(ContentPanel contentPanel) {
        this.setContentPanel(contentPanel);
        w = contentPanel.getScreenX();
        h = contentPanel.getScreenY();
        infNummer = 11;
        currentInfo = 0;

        setPreferredSize(new Dimension(w, h));
        setLayout(null);

        initializeIcons();
        initializeLabels();
        initializeButtons();
        add(backgroundLabel);
    }

    /**
     * Erstellt einen Button für die Navigation zum nächsten Hilfsabschnitt und einen
     * Button für die Rückkehr zum Hauptmenü. Initialisiert die Schaltflächen des
     * HelpPanels.
     */
    private void initializeButtons() {
        nextButton = new Buttons(this,"next1");
        nextButton.setBounds(w - (w/11), h/18, 140, 50);
        nextButton.setFocusPainted(false);
        nextButton.addActionListener(e -> switchToNextInfo());
        add(nextButton);

        // Der Button für die Rückkehr zum Hauptmenü bleibt unverändert
        Buttons toMenuButton = new Buttons(this, "hilfe");
        toMenuButton.setFocusPainted(false);
        toMenuButton.setBounds(w - 50 - 5, h - 35 - 5, 50, 35);
        add(toMenuButton);
    }

    /**
     * Durchläuft jeden infNummer und lädt das entsprechende Bild aus dem
     * "/menu"-Verzeichnis.
     */
    private void initializeIcons() {
        backgroundIcon = new ImageIcon[infNummer];
        for (int i = 0; i < infNummer; i++) {
            backgroundIcon[i] = new ImageIcon(
                    new ImageIcon(getClass().getResource("/menu/BackgroundInfo" + i + ".png")).getImage()
                            .getScaledInstance(w, h, Image.SCALE_DEFAULT));
        }
    }

    // Initialisiert die Labels des HelpPanels.
    private void initializeLabels() {
        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, w, h);
        backgroundLabel.setIcon(backgroundIcon[0]);
    }

    /**
     * Damit kann man zu Menu wechseln
     */
    public void switchToMenu() {
        CardLayout layout = (CardLayout) getContentPanel().getLayout();
        layout.show(getContentPanel(), "menu");
    }

    /**
     * Ändert die Sprache des Hilfe-Panels.
     * 
     * @param germanSelected  Wenn true, ist die deutsche Sprache ausgewählt.
     * @param englishSelected Wenn true, ist die englische Sprache ausgewählt.
     */
    public void changeLanguage(boolean germanSelected, boolean englishSelected) {
        if (germanSelected) {
            // Der Text des Buttons für die nächste Seite wird auch angepasst
            nextButton.setText("Nächste Seite");
        } else {
            // Der Text des Buttons für die nächste Seite wird auch angepasst
            nextButton.setText("Next Page");
        }
    }

    /**
     * Gibt das ContentPanel zurück, das mit diesem HelpPanel verbunden ist.
     * 
     * @return
     */
    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    /**
     * Setzt das ContentPanel für dieses HelpPanel.
     * 
     * @param contentPanel
     */
    public void setContentPanel(ContentPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    /**
     * Wechselt die angezeigten Hilfsinformationen
     * 
     * @param info
     */
    public void switchInfo(int info) {
        backgroundLabel.setIcon(backgroundIcon[info]);
        currentInfo = info;
    }

    /**
     * Wechselt zur nächsten Hilfsinformation
     */
    private void switchToNextInfo() {
        currentInfo = (currentInfo + 1) % infNummer;
        switchInfo(currentInfo);
    }
}
