package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JToggleButton;

import org.junit.jupiter.api.Test;

import main.ContentPanel;
import main.MenuPanel;

class MenuPanelTest {

	@Test
	public void testCreateLanguageToggleButton() {

		MenuPanel menuPanel = new MenuPanel(new ContentPanel(1707,960));

		JToggleButton toggleButton = menuPanel.createToggleButton(menuPanel.getGerOn());
		assertNotNull(toggleButton);		 		 
	}
	
	/**
	@Test
    public void testSoundSlider() {
        MenuPanel menuPanel = new MenuPanel(new ContentPanel(1707, 960));

        JSlider soundSlider = menuPanel.soundSlider;

        // Sound-Slider auf 3
        assertEquals(3, soundSlider.getValue());

        // Änder den Wert des Sound-Sliders und überprüfe  Aktualisierung
        soundSlider.setValue(2);
        assertEquals(2, soundSlider.getValue());

        // Ändert den Wert nochmal und überprüft erneut die Aktualisierung
        soundSlider.setValue(4);
        assertEquals(4, soundSlider.getValue());
    }
    **/
	
	@Test
	public void PaintComponentTest() {
		MenuPanel menuPanel = new MenuPanel(new ContentPanel(1707,960));


		//Test Graphics und Test Image
		BufferedImage jTestImage = new BufferedImage(1707, 960, BufferedImage.TYPE_INT_ARGB);
		Graphics jTestGraphics = jTestImage.getGraphics();

		// Simuliere paintComponent Methode aufrufen mit test graphics
		menuPanel.paintComponent(jTestGraphics);


		// Test graphics schließen
		jTestGraphics.dispose();
	}
	@Test
	public void ChangeLanguageTest() {
		
		ContentPanel contentPanel = new ContentPanel(1707, 960);

		
		MenuPanel menuPanel = new MenuPanel(contentPanel);

		// Default Sprache deutsch
		menuPanel.changeLanguage(true, false);

		// Button(Bilder) Deutsch ?
		assertEquals("res//menu//spielhosten.png", menuPanel.getHostButton().getImagePath());
		assertEquals("res//menu//spielbeitreten.png", menuPanel.getJoinButton().getImagePath());
		assertEquals("res//menu//hilfe.png", menuPanel.getHelpButton().getImagePath());
		assertEquals("res//menu//beenden.png", menuPanel.getExitButton().getImagePath());

		// Sprache von Deutsch -> Englisch
		menuPanel.changeLanguage(false, true);

		// Sind Button(Bilder) Englisch?
		assertEquals("res//menu//hostgame.png", menuPanel.getHostButton().getImagePath());
		assertEquals("res//menu//joingame.png", menuPanel.getJoinButton().getImagePath());
		assertEquals("res//menu//help.png", menuPanel.getHelpButton().getImagePath());
		assertEquals("res//menu//exit.png", menuPanel.getExitButton().getImagePath());
	}
}
