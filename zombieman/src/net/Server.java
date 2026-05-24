package net;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.Timer;

import entity.Konstante;
import entity.MapUpdatesThrower;
import entity.PowerUpThrower;
import entity.SpielerDaten;
import main.CoordinatesThrower;
import main.HostPanel;
import main.Koordinaten;

/**
 * Die Klasse Server repräsentiert die Serverkomponente eines
 * Mehrspieler-Spiels. Sie verwaltet Spielerdaten, initialisiert die Map.
 *
 * Die Map ist ein Raster von Koordinaten, die durch die Klasse Koordinaten
 * dargestellt werden. Die Map enthält Wände, unzerstörbare Blöcke, zerstörbare
 * Blöcke, freie Blöcke und Spieler-Spawnpunkte. Spielerdaten wie Position und
 * Login-Status werden in der Klasse SpielerDaten gespeichert.
 *
 * Der Server akzeptiert während der Initialisierung eine bestimmte Portnummer,
 * eine Anzahl an Runden die zum gewinnen benötigt wird und den Namen der Map.
 * Der Map-name gibt das visuelle Thema der abgebildeten Map an.
 *
 * Die Serverklasse erweitert die Thread-Klasse, um den Server in einem
 * separaten Thread auszuführen und damit eine gleichzeitige Bearbeitung von
 * Client-Verbindungen zu ermöglichen.
 *
 * @author BoOom
 * @version 1.0
 *
 * @see ClientHandler
 * @see Koordinaten
 * @see SpielerDaten
 * @see ServerSocket
 */
public class Server extends Thread {

	/** Die Spielerdaten. */
	private static SpielerDaten spieler[] = new SpielerDaten[Konstante.MAX_SPIELER];

	/** Die Map. */
	private static Koordinaten map[][] = new Koordinaten[Konstante.LIN][Konstante.COL];

	/** Der Server-Socket. */
	private static ServerSocket ss;

	/** Die Portnummer. */
	private int portNumber;

	/** Die Siegbedingungsnummer. */
	private static int winConditionNumber;

	/** Der Name des Map-Skins. */
	public static String mapSkinName;
	public static int playerNumber;
	private static int zoneSpeed = 8;
	private static int zoneSize = 0;
	private static int x,y;
	private static boolean running;
	private HostPanel hostPanel;
	private Timer timer,timerCD;
	private int elapsedTimeInSeconds, initialTimeInSeconds;
	private int elapsedTimeInSecondsCD, initialTimeInSecondsCD;
	private int playerAnz = 0;

	/**
	 * Instanziiert einen neuen Server.
	 *
	 * @param portNumber         ist die Portnummer
	 * @param winConditionNumber ist die Anzahl an Runden die zum gewinnen benötigt
	 *                           wird
	 * @param mapSkinName        ist der Name des Map-skin
	 */
	public Server(HostPanel hostPanel, int portNumber, int winConditionNumber, String mapSkinName, int playerNumber) {
		this.hostPanel = hostPanel;
		Server.winConditionNumber = winConditionNumber;
		this.portNumber = portNumber;
		Server.mapSkinName = mapSkinName;
		Server.playerNumber = playerNumber;
		running = true;
		setMap();
		setPlayerData();
	}

	/**
	 * Überprüft, ob das Spieler-Array voll ist.
	 *
	 * @return true, wenn erfolgreich
	 */
	public boolean loggedIsFull() {
		for (int i = 0; i < Konstante.MAX_SPIELER; i++) {
			if (getSpieler()[i].isLogged() == false) {
				return false;
			}
		}
		return true;
		
	}

	/**
	 * Überprüft ob die Runde fertig ist
	 * 
	 * @return
	 */
	public static boolean checkWinnerDetermined() {
		int alive = 0;
		for (int i = 0; i < Konstante.MAX_SPIELER; i++) {
			if (getSpieler()[i].isLebt() == true) {
				alive++;
			}
		}
		if (alive == 1) {
			return true;
		}
		return false;
	}

	public static void shutdown() {
		MapUpdatesThrower.setRunning(false);
		CoordinatesThrower.setRunning(false);
		ClientHandler.setRunning(false);
		PowerUpThrower.setRunning(false);
		
		close();
	}

	/**
	 * Setzt die Map.
	 */
	public void setMap() {
		// Zerstörbare Blöcke
		for (int i = 0; i < Konstante.LIN; i++)
			for (int j = 0; j < Konstante.COL; j++)
				map[i][j] = new Koordinaten(Konstante.SIZE_SPRITE_MAP * j, Konstante.SIZE_SPRITE_MAP * i, "block");

		// Randblöcke
		for (int j = 1; j < Konstante.COL - 1; j++) {
			map[0][j].setImg("wall-up");
			map[Konstante.LIN - 1][j].setImg("wall-down");
			map[0][7].setImg("wall-up-logo");
		}
		for (int i = 1; i < Konstante.LIN - 1; i++) {
			map[i][0].setImg("wall-left");
			map[i][Konstante.COL - 1].setImg("wall-right");
		}
		map[0][0].setImg("wall-up-left");
		map[0][Konstante.COL - 1].setImg("wall-up-right");
		map[Konstante.LIN - 1][0].setImg("wall-down-left");
		map[Konstante.LIN - 1][Konstante.COL - 1].setImg("wall-down-right");

		// Unzerstörbare Blöcke
		int counter = 0;
		for (int i = 2; i < Konstante.LIN - 2; i++) {
			for (int j = 2; j < Konstante.COL - 2; j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					if (counter % 2 == 0) {
						map[i][j].setImg("wall-center");
					} else {
						map[i][j].setImg("wall-center2");
					}
					counter++;
					if (j == Konstante.COL - 3) {
						counter--;
					}
				}
			}
		}

		// Spawn
		map[1][1].setImg("floor-1");
		map[1][2].setImg("floor-1");
		map[2][1].setImg("floor-1");
		map[Konstante.LIN - 2][Konstante.COL - 2].setImg("floor-1");
		map[Konstante.LIN - 3][Konstante.COL - 2].setImg("floor-1");
		map[Konstante.LIN - 2][Konstante.COL - 3].setImg("floor-1");
		map[Konstante.LIN - 2][1].setImg("floor-1");
		map[Konstante.LIN - 3][1].setImg("floor-1");
		map[Konstante.LIN - 2][2].setImg("floor-1");
		map[1][Konstante.COL - 2].setImg("floor-1");
		map[2][Konstante.COL - 2].setImg("floor-1");
		map[1][Konstante.COL - 3].setImg("floor-1");
	}
	/**
	 * Setzt die Spielerdaten.
	 */
	private void setPlayerData() {
		getSpieler()[0] = new SpielerDaten(getMap()[1][1].getX() - Konstante.VAR_X_SPRITES,
				getMap()[1][1].getY() - Konstante.VAR_Y_SPRITES);
		getSpieler()[1] = new SpielerDaten(
				getMap()[Konstante.LIN - 2][Konstante.COL - 2].getX() - Konstante.VAR_X_SPRITES,
				getMap()[Konstante.LIN - 2][Konstante.COL - 2].getY() - Konstante.VAR_Y_SPRITES);
		getSpieler()[2] = new SpielerDaten(getMap()[Konstante.LIN - 2][1].getX() - Konstante.VAR_X_SPRITES,
				getMap()[Konstante.LIN - 2][1].getY() - Konstante.VAR_Y_SPRITES);
		getSpieler()[3] = new SpielerDaten(getMap()[1][Konstante.COL - 2].getX() - Konstante.VAR_X_SPRITES,
				getMap()[1][Konstante.COL - 2].getY() - Konstante.VAR_Y_SPRITES);
	}
	private void countPlayer( ) {
		int i = 0;
			if (getSpieler()[i].isLogged() == true) {
				i++;
				playerAnz++;
				System.out.println(playerAnz);
		}
	}
	/**
	 * Startet den Server und warrtet auf eingehende Clientverbindungen.
	 * Instanziiert ClientHandler zu jeder erfolgreichen Clientverbindung.
	 */
	@Override
	public void run() {
		Socket clientSocket = null;
		try {
			System.out.println("Lobby #" + portNumber + " geöffnet...\n");
			ss = new ServerSocket(portNumber); // Socket -> Port
			for (int id = 0; !loggedIsFull(); id = (++id) % Konstante.MAX_SPIELER)
				if (!getSpieler()[id].isLogged()) {
					clientSocket = ss.accept();
					new ClientHandler(clientSocket, id, this).start();
					setWaitingMap();
					countPlayer();
					if(playerNumber == playerAnz) {
						initializeCountdownTimer();
					}
				}
		} catch (BindException e) {
			System.out.println(" Fehler: " + e + "\n");
			ss = null;
			hostPanel.bindServerError();
			// System.out.println("Disconnection from " + clientSocket.getInetAddress());
			// System.exit(1);
		} catch (IOException e) {
			System.out.println(" Fehler: " + e + "\n");
			ss = null;
			// System.out.println("Disconnection from " + clientSocket.getInetAddress());
			// System.exit(1);
		}
		if (!running) {
			// TEST
		}
	}
	private void setWaitingMap() {
		for(int i = 0; i < Konstante.MAX_SPIELER; i++) {
			getSpieler()[i].setNumberOfBombs(0);
			MapUpdatesThrower.changeMap("wall-center",1,2);
			MapUpdatesThrower.changeMap("wall-center",2,1);
			
			MapUpdatesThrower.changeMap("wall-center2",Konstante.LIN - 3,Konstante.COL - 2);
			MapUpdatesThrower.changeMap("wall-center2",Konstante.LIN - 2,Konstante.COL - 3);
			
			MapUpdatesThrower.changeMap("wall-center",Konstante.LIN - 3,1);
			MapUpdatesThrower.changeMap("wall-center",Konstante.LIN - 2,2);
			
			MapUpdatesThrower.changeMap("wall-center2",2,Konstante.COL - 2);
			MapUpdatesThrower.changeMap("wall-center2",1,Konstante.COL - 3);
	}
	}
	private void initializeCountdownTimer() { 
		setWaitingMap();
		initialTimeInSecondsCD = 6;
		timerCD = new Timer(1000, e -> {
            elapsedTimeInSecondsCD--;
            if (elapsedTimeInSecondsCD < 0) {
            	initializeTimer();
            	timerCD.stop();
            }
            ClientHandler.sendcountdownTimerUpdate(elapsedTimeInSecondsCD);
        });
		elapsedTimeInSecondsCD = initialTimeInSecondsCD;
        timerCD.start();
        System.out.println(elapsedTimeInSecondsCD);
	}
	
	private void initializeTimer() {     
		for(int i = 0; i < Konstante.MAX_SPIELER;i ++) {
				getSpieler()[i].setNumberOfBombs(1);
				MapUpdatesThrower.changeMap("floor-1",1,1);
				MapUpdatesThrower.changeMap("floor-1",1,2);
				MapUpdatesThrower.changeMap("floor-1",2,1);
				
				MapUpdatesThrower.changeMap("floor-1",Konstante.LIN - 2,Konstante.COL - 2);
				MapUpdatesThrower.changeMap("floor-1",Konstante.LIN - 3,Konstante.COL - 2);
				MapUpdatesThrower.changeMap("floor-1",Konstante.LIN - 2,Konstante.COL - 3);
				
				MapUpdatesThrower.changeMap("floor-1",Konstante.LIN - 2,1);
				MapUpdatesThrower.changeMap("floor-1",Konstante.LIN - 3,1);
				MapUpdatesThrower.changeMap("floor-1",Konstante.LIN - 2,2);
				
				MapUpdatesThrower.changeMap("floor-1",1,Konstante.COL - 2);
				MapUpdatesThrower.changeMap("floor-1",2,Konstante.COL - 2);
				MapUpdatesThrower.changeMap("floor-1",1,Konstante.COL - 3);
		}
		initialTimeInSeconds = 120;
		timer = new Timer(1000, e -> {
            elapsedTimeInSeconds--;
            if (elapsedTimeInSeconds == 0) {
                ClientHandler.sendSuddendeath();
            }else if (elapsedTimeInSeconds < 0) {
            	setZoneSize(getZoneSize() + zoneSpeed);
                ClientHandler.sendSuddendeathUpdate(getZoneSize());
            }
            ClientHandler.sendTimerUpdate(elapsedTimeInSeconds);
        });
		elapsedTimeInSeconds = initialTimeInSeconds;
        timer.start();
        System.out.println(elapsedTimeInSeconds);
	}
	/**
	 *
	 * @return gibt die Map aus.
	 */
	public static Koordinaten[][] getMap() {
		return map;
	}

	/*
	 *
	 * @param Setzt die Map.
	 */
	public static void setMap(Koordinaten map[][]) {
		Server.map = map;
	}

	/**
	 * Schließt den Server-Socket, wenn das Spiel beendet wird.
	 */
	public static void close() {
		try {
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gibt die Spielerdaten zurück.
	 * @return 
	 */
	public static SpielerDaten[] getSpieler() {
		return spieler;
	}

	/**
	 * Gibt den Namen des Map-Skins zurück.
	 * @return
	 */
	public static String getMapSkinName() {
		return mapSkinName;
	}

	/**
	 * Gibt die Anzahl der Runden zurück die gebraucht werden um zu gewinnen.
	 * @return
	 */
	public static int getWinConNumber() {
		return winConditionNumber;
	}

	/**
	 * @return Der Timer für das Spiel.
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Setzt den Timer für das Spiel.
	 * @param privateTimer
	 */
	public void setTimer(Timer privateTimer) {
		timer = privateTimer;
	}
	/**
	 * Gibt die aktuelle Größe der Zone zurück
	 * @return
	 */
	public static int getZoneSize() {
		return zoneSize;
	}
	/**
	 * Setzt die Größe der Zone.
	 * @param privateZoneSize
	 */
	public static void setZoneSize(int privateZoneSize) {
		zoneSize = privateZoneSize;
	}
}
