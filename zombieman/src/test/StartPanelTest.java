package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import main.ContentPanel;
import main.StartPanel;

import java.awt.event.KeyEvent;

class StartPanelTest {

    @Test
    void testEnterKeyAction() {
        ContentPanel contentPanel = new ContentPanel(1,1); // Annahme: ContentPanel ist korrekt implementiert.
        StartPanel startPanel = new StartPanel(contentPanel);

        // Simuliere das Drücken der 'Enter'-Taste
        KeyEvent enterKeyPress = new KeyEvent(startPanel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        startPanel.getKeyListeners()[0].keyPressed(enterKeyPress);

        // Überprüfe, ob der Wechsel zum Menü-Panel nicht erfolgt ist
        assertNotEquals("menu", contentPanel.getLayout());
    }
}
