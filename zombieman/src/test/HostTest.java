package test;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JToggleButton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.ContentPanel;
import main.HostPanel;

class HostTest {
	
	ContentPanel contentPanel;
	HostPanel hostPanel;

	@BeforeEach                                         
    void setUp() {
		contentPanel = new ContentPanel(500,500);
		hostPanel = new HostPanel(contentPanel);
    }

    @Test                                               
    @DisplayName("Check ob die Map Friedhof dem Server übergeben wird, wenn keine Auswahl durch den User stattgefunden hat")   
    void noMapSelected() {
        assertEquals("friedhof", hostPanel.getMapSkin(null));  
    }
}