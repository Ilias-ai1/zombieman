package net;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.*;

import entity.*;
import main.*;


public class Client {
  
   private Socket socket = null;
   static PrintStream out = null;
   static Scanner in = null;
   public static int id;
   final static int rateStatusUpdate = 115;
   public static Koordinaten map[][] = new Koordinaten[Konstante.LIN][Konstante.COL];
   public static Koordinaten spawn[] = new Koordinaten[Konstante.MAX_SPIELER];
   public static boolean alive[] = new boolean[Konstante.MAX_SPIELER];

   Client(String host, int port) {
      try {
         System.out.print("Verbindung zum Server wird hergestellt...\n");
         this.socket = new Socket(host, port);
         out = new PrintStream(socket.getOutputStream(), true);  //an den Server senden
         in = new Scanner(socket.getInputStream()); //vom Server empfangen
      } 
      catch (UnknownHostException e) {
         System.out.println(" Fehler: " + e + "\n");
         System.exit(1);
      } 
      catch (IOException e) {
         System.out.println(" Fehler: kein Server vorhanden\n");
         System.exit(1);
      }
      
      receiveInitialSettings();
      new Receiver().start();
   }

   void receiveInitialSettings() {
      id = in.nextInt();

      //die Map
      for (int i = 0; i < Konstante.LIN; i++)
         for (int j = 0; j < Konstante.COL; j++)
            map[i][j] = new Koordinaten(Konstante.SIZE_SPRITE_MAP * j, Konstante.SIZE_SPRITE_MAP * i, in.next());
      
      //Ausgangsstatus ALLER Spieler
      for (int i = 0; i < Konstante.MAX_SPIELER; i++)
         Client.alive[i] = in.nextBoolean();

      //Startkoordinaten aller Spieler
      for (int i = 0; i < Konstante.MAX_SPIELER; i++)
         Client.spawn[i] = new Koordinaten(in.nextInt(), in.nextInt());
   }
   
   public static void main(String[] args) {
      new Client("127.0.0.1", 1331);
      new Window();
   }
}

class Window extends JFrame {
   private static final long serialVersionUID = 1L;

   Window() {
      Sprite.loadImages();
      Sprite.setMaxLoopStatus();
      
      add(new Game(Konstante.COL*Konstante.SIZE_SPRITE_MAP, Konstante.LIN*Konstante.SIZE_SPRITE_MAP));
      setTitle("ZombieMan");
      pack();
      setVisible(true);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

      addKeyListener(new Sender());
   }
}