package test;
 
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import main.ContentPanel;
import java.io.File;
 
public class ContentPanelTest {
 
 
    @Test
    public void testPlayButtonSound() {
        ContentPanel contentPanel = new ContentPanel(1707, 960);
 
        try {
            // temporäre Test-Sounddatei
            File testSoundFile = new File( "res//sound//buttonSound.wav");
            testSoundFile.createNewFile();
 
            
            contentPanel.playButtonSound();
            // Löschen die temporäre Test-Sounddatei
            testSoundFile.delete();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}