package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import entity.Konstante;
import entity.SpielerDaten;
import main.Koordinaten;

public class Server extends Thread {
	public static SpielerDaten spieler[] = new SpielerDaten[Konstante.MAX_SPIELER];
	private static Koordinaten map[][] = new Koordinaten[Konstante.LIN][Konstante.COL];
	ServerSocket ss;
	int portNumber;
	int winConditionNumber;

	public Server(int portNumber, int winConditionNumber) {
		this.winConditionNumber = winConditionNumber;
		this.portNumber = portNumber;

		setMap();
		setPlayerData();
	}

	boolean loggedIsFull() {
		for (int i = 0; i < Konstante.MAX_SPIELER; i++)
			if (spieler[i].isLogged() == false)
				return false;
		return true;
	}

	void setMap() {
		for (int i = 0; i < Konstante.LIN; i++)
			for (int j = 0; j < Konstante.COL; j++)
				map[i][j] = new Koordinaten(Konstante.SIZE_SPRITE_MAP * j, Konstante.SIZE_SPRITE_MAP * i, "block");

		// Randblöcke
		for (int j = 1; j < Konstante.COL - 1; j++) {
			map[0][j].img = "wall-up";
			map[Konstante.LIN - 1][j].img = "wall-down";
		}
		for (int i = 1; i < Konstante.LIN - 1; i++) {
			map[i][0].img = "wall-left";
			map[i][Konstante.COL - 1].img = "wall-right";
		}
		map[0][0].img = "wall-up-left";
		map[0][Konstante.COL - 1].img = "wall-up-right";
		map[Konstante.LIN - 1][0].img = "wall-down-left";
		map[Konstante.LIN - 1][Konstante.COL - 1].img = "wall-down-right";

		// Unzerstörbare Blöcke
		int counter = 0;
		for (int i = 2; i < Konstante.LIN - 2; i++)
			for (int j = 2; j < Konstante.COL - 2; j++)
				if (i % 2 == 0 && j % 2 == 0) {
					if (counter % 2 == 0)
						map[i][j].img = "wall-center";
					else
						map[i][j].img = "wall-center2";
					counter++;
				}

		// Spawn
		map[1][1].img = "floor-1";
		map[1][2].img = "floor-1";
		map[2][1].img = "floor-1";
		map[Konstante.LIN - 2][Konstante.COL - 2].img = "floor-1";
		map[Konstante.LIN - 3][Konstante.COL - 2].img = "floor-1";
		map[Konstante.LIN - 2][Konstante.COL - 3].img = "floor-1";
		map[Konstante.LIN - 2][1].img = "floor-1";
		map[Konstante.LIN - 3][1].img = "floor-1";
		map[Konstante.LIN - 2][2].img = "floor-1";
		map[1][Konstante.COL - 2].img = "floor-1";
		map[2][Konstante.COL - 2].img = "floor-1";
		map[1][Konstante.COL - 3].img = "floor-1";
	}

	void setPlayerData() {
		spieler[0] = new SpielerDaten(getMap()[1][1].x - Konstante.VAR_X_SPRITES,
				getMap()[1][1].y - Konstante.VAR_Y_SPRITES);

		spieler[1] = new SpielerDaten(getMap()[Konstante.LIN - 2][Konstante.COL - 2].x - Konstante.VAR_X_SPRITES,
				getMap()[Konstante.LIN - 2][Konstante.COL - 2].y - Konstante.VAR_Y_SPRITES);
		spieler[2] = new SpielerDaten(getMap()[Konstante.LIN - 2][1].x - Konstante.VAR_X_SPRITES,
				getMap()[Konstante.LIN - 2][1].y - Konstante.VAR_Y_SPRITES);
		spieler[3] = new SpielerDaten(getMap()[1][Konstante.COL - 2].x - Konstante.VAR_X_SPRITES,
				getMap()[1][Konstante.COL - 2].y - Konstante.VAR_Y_SPRITES);
	}

	@Override
	public void run() {
		Socket clientSocket = null;
		try {
			System.out.print("Lobby #" + portNumber + " geöffnet...\n");
			ss = new ServerSocket(portNumber); // Socket -> Port

			System.out.println();

			for (int id = 0; !loggedIsFull(); id = (++id) % Konstante.MAX_SPIELER)
				if (!spieler[id].isLogged()) {
					clientSocket = ss.accept();
					new ClientHandler(clientSocket, id).start();

				}
			// fährt den Server nicht herunter, während der Client-Thread weiter ausgeführt
			// wird
		} catch (IOException e) {
			System.out.println(" Fehler: " + e + "\n");
			System.out.println("Disconnection from " + clientSocket.getInetAddress());
			System.exit(1);
		}
	}

	public static Koordinaten[][] getMap() {
		return map;
	}

	public static void setMap(Koordinaten map[][]) {
		Server.map = map;
	}
}