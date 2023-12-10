package net;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import entity.*;
import main.*;

//Jeder Client kriegt eigenen Thread
public class ClientHandler extends Thread {
   static List<PrintStream> listOutClients = new ArrayList<PrintStream>();

   public static void sendToAllClients(String outputLine) {
      for (PrintStream outClient : listOutClients)
         outClient.println(outputLine);
   }

   private Socket clientSocket = null;
   private Scanner in = null;
   private PrintStream out = null;
   private int id;

   CoordinatesThrower ct;

   ClientHandler(Socket clientSocket, int id) {
      this.id = id;
      this.clientSocket = clientSocket;
      (ct = new CoordinatesThrower(this.id)).start();

      try {
         System.out.print("Verbindung mit Spieler " + this.id + " wird hergestellt...\n");
         this.in = new Scanner(clientSocket.getInputStream()); // vom Client erhalten
         this.out = new PrintStream(clientSocket.getOutputStream(), true); // zum Client senden
      } catch (IOException e) {
         System.out.println(" Fehler: " + e + "\n");
         System.exit(1);
      }

      listOutClients.add(out);
      Server.spieler[id].setLogged(true);
      Server.spieler[id].lebt = true;
      sendInitialSettings(); // sendet eine Zeichenfolge

      //Benachrichtigung bereits vorhandener Clients
      for (PrintStream outClient: listOutClients)
         if (outClient != this.out)
            outClient.println(id + " playerJoined");
   }

   public void run() {
      while (in.hasNextLine()) { // Verbindung mit dem Client this.id wird hergestellt
         String str[] = in.nextLine().split(" ");
         
         if (str[0].equals("keyCodePressed") && Server.spieler[id].lebt) {    
            ct.keyCodePressed(Integer.parseInt(str[1]));
         } 
         else if (str[0].equals("keyCodeReleased") && Server.spieler[id].lebt) {
            ct.keyCodeReleased(Integer.parseInt(str[1]));
         }
      }
      clientDisconnected();
   }

   void sendInitialSettings() {
      out.print(id);
      for (int i = 0; i < Konstante.LIN; i++)
         for (int j = 0; j < Konstante.COL; j++)
            out.print(" " + Server.getMap()[i][j].img);

      for (int i = 0; i < Konstante.MAX_SPIELER; i++)
         out.print(" " + Server.spieler[i].lebt);

      for (int i = 0; i < Konstante.MAX_SPIELER; i++)
         out.print(" " + Server.spieler[i].x + " " + Server.spieler[i].y);
      out.print("\n");
   }

   void clientDisconnected() {
      listOutClients.remove(out);
      Server.spieler[id].setLogged(false);
      try {
         System.out.print("Verbindung mit Spieler " + this.id + " wurde getrennt...\n");
         in.close();
         out.close();
         clientSocket.close();
      } catch (IOException e) {
         System.out.println(" Fehler: " + e + "\n");
         System.exit(1);
      }
   }
}