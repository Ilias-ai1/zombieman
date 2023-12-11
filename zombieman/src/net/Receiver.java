package net;

import javax.swing.SwingUtilities;

import entity.*;
import main.*;

//Erhält alle Infos von Clients
public class Receiver extends Thread {
	Spieler s;
 
	Spieler fromWhichPlayerIs(int id) {
    if (id == Client.id)
       return Game.du;
    else if (id == (Client.id+1)%Konstante.MAX_SPIELER)
       return Game.gegner1;
    else if (id == (Client.id+2)%Konstante.MAX_SPIELER)
       return Game.gegner2;
    else if (id == (Client.id+3)%Konstante.MAX_SPIELER)
       return Game.gegner3;
    return null;
 }

 public synchronized void run() {
    String str;
    while (Client.in.hasNextLine()) {

       this.s = fromWhichPlayerIs(Client.in.nextInt()); //id des Clients
       str = Client.in.next();
 
       if (str.equals("mapUpdate")) { //p null
           Game.setSpriteMap(Client.in.next(), Client.in.nextInt(), Client.in.nextInt());
           SwingUtilities.invokeLater(() -> {
         	    Game.du.panel.repaint();
         	});
       }   
         else if (str.equals("newCoordinate")) {
          s.x = Client.in.nextInt();
          s.y = Client.in.nextInt();
          Game.du.panel.repaint();
       }
       else if (str.equals("newStatus")) {
          s.sc.setLoopStatus(Client.in.next());
       }
       else if (str.equals("stopStatusUpdate")) {
          s.sc.stopLoopStatus();
       }
       else if (str.equals("playerJoined")) {
          s.lebt = true;
       }
    }
    Client.in.close();
 }
}