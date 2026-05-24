package test;

import static org.junit.jupiter.api.Assertions.*;

import java.net.BindException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import entity.Konstante;
import main.ContentPanel;
import main.HostPanel;
import net.Server;

public class ServerTest {

	Server server1;
	HostPanel hostPanel;
	ContentPanel contentPanel;
    
	@BeforeEach                                         
    void setUp() {
		contentPanel = new ContentPanel(500,500);
		hostPanel = new HostPanel(contentPanel);
    }
	
	
    @Test
    @DisplayName("Check ob Spielerdaten richtig gesetzt werden")
    public void testPlayerDataInitialization() {
        server1 = new Server(hostPanel,1331, 3, "krankenhaus",2);

        assertNotNull(Server.getSpieler()[0]);
        assertNotNull(Server.getSpieler()[1]);
        assertNotNull(Server.getSpieler()[2]);
        assertNotNull(Server.getSpieler()[3]);

    }
    @Test
    @DisplayName("Check ob maximale Anzahl der Spieler erkannt wird")
    public void testLoggedIsFull() {
        Server server = new Server(hostPanel,12345, 3, "friedhof",2);
        assertFalse(server.loggedIsFull());

        for (int i = 0; i < Konstante.MAX_SPIELER; i++) {
            Server.getSpieler()[i].setLogged(true);
        }

        assertTrue(server.loggedIsFull());
    }
    @Test
    @DisplayName("Check ob die bestimmte Koordinaten richtig gesetzt werden")
    public void testSetMap() {
        Server server = new Server(hostPanel,12345, 3, "friedhof",2);
        server.setMap();

        assertEquals("wall-up", Server.getMap()[0][1].getImg());
        assertEquals("wall-left", Server.getMap()[1][0].getImg());
        assertEquals("wall-center", Server.getMap()[2][2].getImg());
        assertEquals("floor-1", Server.getMap()[1][1].getImg());
    }

}
