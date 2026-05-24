package net;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entity.Konstante;
import entity.MapUpdatesThrower;
import main.CoordinatesThrower;


/**
 * @author BoOom
 * 
 * @see Konstante
 * @see MapUpdatesThrower
 * @see CoordinatesThrower
 * 
 * Diese Klasse repräsentiert ein Thread, der mit einem Client kommuniziert.Jeder Client kriegt eigenen ClientHandler-Thread.
 * Ermöglichen Austausch von Information zwischen Server und CLient.
 */
public class ClientHandler extends Thread {
	private static List<PrintStream> listOutClients = new ArrayList<PrintStream>();
	private static List<String> playerNames = new ArrayList<>();
	private static List<String> scoreValue = new ArrayList<>();

	private Socket clientSocket = null;
	private Scanner in = null;
	private PrintStream out = null;
	private int id;
	private String name,score;
	private CoordinatesThrower ct;
	private MapUpdatesThrower mt;
	private static boolean running;
	private Server server;

	/**
	 * Konstruktor für ClientHandler. Initialisiert den Thread für die Kommunikation mit einem Client.
	 * @param clientSocket
	 * @param id
	 * @param server
	 */
	public ClientHandler(Socket clientSocket, int id, Server server) {
		this.server = server;
		this.id = id;
		this.clientSocket = clientSocket;
		setRunning(true);
		(ct = new CoordinatesThrower(this.id)).start();
		(mt = new MapUpdatesThrower(this.id)).start();

		try {
			//System.out.print("Verbindung mit Spieler " + this.id + " wird hergestellt...\n");
			this.in = new Scanner(clientSocket.getInputStream()); // vom Client erhalten
			this.out = new PrintStream(clientSocket.getOutputStream(), true); // zum Client senden
		} catch (IOException e) {
			System.out.println(" Fehler: " + e + "\n");
			System.exit(1);
		}

		listOutClients.add(out);
		Server.getSpieler()[id].setLogged(true);
		Server.getSpieler()[id].setLebt(true);
		sendInitialSettings(); // sendet eine Zeichenfolge
		// Benachrichtigung bereits vorhandener Clients
		for (PrintStream outClient : listOutClients)
			if (outClient != this.out)
				outClient.println(id + " playerJoined");
		try {
			sleep(1200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		receiveName();
		receiveScore();
	}

	/**
	 * Es wird ein String an alle Clients gesendet.
	 * @param outputLine
	 */
	public static void sendToAllClients(String outputLine) {
		for (PrintStream outClient : listOutClients)
			outClient.println(outputLine);
	}

    /**
     * Sendet ein timeupdate an alle Clients
     * @param timeInSeconds
     */
    public static void sendTimerUpdate(int timeInSeconds) {
        String timerUpdateMessage = -2 + " timerUpdate " + timeInSeconds;
        //System.out.println(timerUpdateMessage);
        	sendToAllClients(timerUpdateMessage);
    }
    /**
     * @param timeInSeconds die verbleibende zeit in sekunden.
     */
    public static void sendcountdownTimerUpdate(int timeInSeconds) {
        String timerUpdateMessage = -2 + " countdownTimerUpdate " + timeInSeconds;
        //System.out.println(timerUpdateMessage);
        	sendToAllClients(timerUpdateMessage);
    }
    
    /**
     * Hier wird Suddendeath an alle Clients gesendet.
     */
    public static void sendSuddendeath() {
    	String sendSuddendeath = -2 + " suddendeath";
    		sendToAllClients(sendSuddendeath);
    }
    
    /**
     * Die Größe der Zone wird an alle Clients übermittelt.
     * @param zoneSize
     */
    public static void sendSuddendeathUpdate(int zoneSize) {
    	String sendSuddendeath = -2 + " suddendeathUpdate " + zoneSize;
    		sendToAllClients(sendSuddendeath);
    }
    
	/**
	 *Thread wird ausgeführt für die Kommunikation mit dem Client.
	 */
	public void run() {
		while (in.hasNextLine() && running) { // Verbindung mit dem Client this.id wird hergestellt
			String str[] = in.nextLine().split(" ");
			if (str[0].equals("keyCodePressed") && Server.getSpieler()[id].isLebt()) {
				ct.keyCodePressed(Integer.parseInt(str[1]));
			} else if (str[0].equals("keyCodeReleased") && Server.getSpieler()[id].isLebt()) {
				ct.keyCodeReleased(Integer.parseInt(str[1]));
			} else if (str[0].equals("pressedSpace") && Server.getSpieler()[id].getNumberOfBombs() >= 1) {
				Server.getSpieler()[id].setNumberOfBombs(Server.getSpieler()[id].getNumberOfBombs() - 1);
				mt.setBombPlanted(Integer.parseInt(str[1]), Integer.parseInt(str[2]));
			} else if (str[0].equals("Win")) {
				if (Integer.parseInt(str[1]) >= Server.getWinConNumber())
					sendToAllClients(id + " Champion");
				else
					sendToAllClients("-2 newRound");
				server.getTimer().stop();
				Server.shutdown();
			}
		}
		clientDisconnected();
	}

	/**
	 * Es werden Initialisierungseinstellungen an den Client gesendet.
	 */
	public void sendInitialSettings() {
		out.println(Server.getMapSkinName());
		out.print(id);
		for (int i = 0; i < Konstante.LIN; i++)
			for (int j = 0; j < Konstante.COL; j++)
				out.print(" " + Server.getMap()[i][j].getImg());

		for (int i = 0; i < Konstante.MAX_SPIELER; i++)
			out.print(" " + Server.getSpieler()[i].isLebt());

		for (int i = 0; i < Konstante.MAX_SPIELER; i++)
			out.print(" " + Server.getSpieler()[i].getX() + " " + Server.getSpieler()[i].getY());
		out.print("\n");
	}

	/**
	 * Empfängt Namen des Clients.
	 */
	public void receiveName() {
		name = id + " " + in.nextLine();
		playerNames.add(name);
		for (PrintStream outClient : listOutClients)
			for (String playerName : playerNames)
				outClient.println(playerName);
	}
	public void receiveScore() {
		score = id + " " +  in.nextLine();
		scoreValue.add(score);
		System.out.println(score);
		for (PrintStream outClient : listOutClients)
			for (String scoreV : scoreValue)
				outClient.println(scoreV);
		
	}
	/**
	 * Hier geths um die Trennung eines Clients.
	 */
	public void clientDisconnected() {
		listOutClients.remove(out);
		Server.getSpieler()[id].setLogged(false);
		try {
			//System.out.print("Verbindung mit Spieler " + this.id + " wurde getrennt...\n");
			in.close();
			out.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println(" Fehler: " + e + "\n");
			// System.exit(1);
		}
	}

	public static void setRunning(boolean privateRunning) {
		running = privateRunning;
	}
}
