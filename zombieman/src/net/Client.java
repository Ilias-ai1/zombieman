package net;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import entity.*;
import main.*;

/**
 * Die Klasse Client repräsentiert den Client eines Mehrspieler-Spiels. Ein
 * Client verbindet sich mit einem Server, sendet und empfängt Daten und
 * verwaltet seinen eigenen Spielstatus.
 *
 * Der Client verwendet Sockets für die Kommunikation mit dem Server. Er kann
 * den Spielername, die Map-informationen, Die Spieler-ID, Spawn-koordinaten der
 * Spieler und den Spielstatus empfangen und senden. Zusätzlich startet die
 * Klasse einen Receiver-Thread, um Nachrichten vom Server zu empfangen.
 *
 * @author BoOom
 * @version 1.0
 * 
 * @see Receiver
 * @see Koordinaten
 * @see Konstante
 * @see Sprite
 * @see Socket
 */
public class Client {

	/** Der Socket. */
	private Socket socket = null;

	/** Der Name. */
	private String name;

	/** Die Ausgabe. */
	private static PrintStream out = null;

	/** Die Eingabe. */
	private static Scanner in = null;

	/** Die ID. */
	private static int id;

	/** Intervall der zu sendenen StatusUpdates. */
	private final static int rateStatusUpdate = 115;

	/** Die Map. */
	private static Koordinaten map[][] = new Koordinaten[Konstante.LIN][Konstante.COL];

	/** Der Spawn. */
	private static Koordinaten spawn[] = new Koordinaten[Konstante.MAX_SPIELER];

	/** Die Information, ob ein Spieler lebendig ist. */
	private static boolean alive[] = new boolean[Konstante.MAX_SPIELER];

	/** Der Name des Map-Skins. */
	private static String mapSkinName;
	private HostPanel hostPanel;
	private JoinPanel joinPanel;

	/**
	 * Instanziiert einen neuen Client.
	 *
	 * @param host ist die Adresse des Servers
	 * @param port ist der Port
	 * @param name ist der Name
	 */
	public Client(HostPanel hostPanel, String host, int port, String name) {
		this.name = name;
		this.hostPanel = hostPanel;
		this.name = name;
		start(host, port);
	}

	/**
	 * @param joinPanel
	 * @param host
	 * @param port
	 * @param name
	 */
	public Client(JoinPanel joinPanel, String host, int port, String name) {
		this.name = name;
		this.joinPanel = joinPanel;
		this.name = name;
		start(host, port);
	}

	/**
	 * @param host
	 * @param port
	 */
	private void start(String host, int port) {
		try {
			//System.out.print("Verbindung zum Server wird hergestellt...\n");
			this.socket = new Socket(host, port);
			setOut(new PrintStream(socket.getOutputStream(), true)); // an den Server senden
			in = new Scanner(socket.getInputStream()); // vom Server empfangen
		} catch (UnknownHostException e) {
			System.out.println(" Fehler: " + e + "\n");
			// System.exit(1);
		} catch (IOException e) {
			System.out.println(" Fehler: kein Server vorhanden\n");
			if (hostPanel != null) {
				hostPanel.keinServerError();
			} else {
				joinPanel.keinServerError();
			}
		}

		receiveInitialSettings();
		new Receiver(this).start();
		sendNameToServer();
		sendScoreToServer();

	}

	public void sendNameToServer() {
		out.println("sendName " + name);
	}
	public void sendScoreToServer() {
		if (hostPanel != null) {
			out.println("score " + hostPanel.getContentPanel().getSiege());
		} else {
			out.println("score " + joinPanel.getContentPanel().getSiege());
		}
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Empfängt die initialen Einstellungen.
	 * 
	 * @return
	 */

	void receiveInitialSettings() {
		mapSkinName = getIn().nextLine();

		Sprite.loadMapImages(mapSkinName);
		id = getIn().nextInt();

		// Die Map
		for (int i = 0; i < Konstante.LIN; i++)
			for (int j = 0; j < Konstante.COL; j++)
				getMap()[i][j] = new Koordinaten(Konstante.SIZE_SPRITE_MAP * j, Konstante.SIZE_SPRITE_MAP * i,
						getIn().next());

		// Ausgangsstatus aller Spieler
		for (int i = 0; i < Konstante.MAX_SPIELER; i++)
			Client.getAlive()[i] = getIn().nextBoolean();

		// Startkoordinaten aller Spieler
		for (int i = 0; i < Konstante.MAX_SPIELER; i++)
			Client.getSpawn()[i] = new Koordinaten(getIn().nextInt(), getIn().nextInt());
	}

	/**
	 *
	 * @return der Spawn
	 */
	public static Koordinaten[] getSpawn() {
		return spawn;
	}

	/**
	 * 
	 *
	 * @param spawn Setzt den Spawn.
	 */
	public static void setSpawn(Koordinaten spawn[]) {
		Client.spawn = spawn;
	}

	/**
	 *
	 * @return ob der Spieler am leben ist
	 */
	public static boolean[] getAlive() {
		return alive;
	}

	/**
	 *
	 * @param alive setzt ob man lebt
	 */
	public static void setAlive(boolean alive[]) {
		Client.alive = alive;
	}

	/**
	 *
	 * @return gibt die Map zurück
	 */
	public static Koordinaten[][] getMap() {
		return map;
	}

	/**
	 *
	 * @param map setzt die neue Map
	 */
	public static void setMap(Koordinaten map[][]) {
		Client.map = map;
	}

	/**
	 *
	 * @return gibt den Printstream zurück
	 */
	public static PrintStream getOut() {
		return out;
	}

	/**
	 *
	 * @param out ersetzt den Printstream
	 */
	public static void setOut(PrintStream out) {
		Client.out = out;
	}

	public void shutdown(boolean over, String winner) {
//		StatusUpdater
		StatusUpdater.setRunning(false);
//		Sender
		Sender.setRunning(false);
//		Game
//		Receiver
		Receiver.setRunning(false); // Scanner in
//		Client w/ , PrintStream out & socket
		in.close();
		out.close();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (over) {
			if (hostPanel != null) {
				hostPanel.startSiegerehrung(winner);
			} else {
				joinPanel.startSiegerehrung(winner);
			}
		} else {
//			Client verlassen um neuen zu starten
			if (hostPanel != null) {
				hostPanel.restartGame();
			}else {
				joinPanel.restartGame();
			}
		}
	}

	public static int getClientId() {
		return id;
	}

	public static Scanner getIn() {
		return in;
	}
	
	public static String getMapSkinName() {
		return mapSkinName;
	}


	public void addSiege() {
		if (hostPanel != null) {
			hostPanel.getContentPanel().addSiege();
		} else {
			joinPanel.getContentPanel().addSiege();
		}
	}

	public int getSiege() {
		if (hostPanel != null) {
			return hostPanel.getContentPanel().getSiege();
		} else {
			return joinPanel.getContentPanel().getSiege();
		}
	}
}
