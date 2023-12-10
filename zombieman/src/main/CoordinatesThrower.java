package main;

import java.awt.event.KeyEvent;
import net.*;
import entity.*;

//Thread, der die nächsten Koordinaten an Clients sendet, während W/A/S/D nicht freigegeben ist
public class CoordinatesThrower extends Thread {
 boolean up, right, left, down;
 int id;

 public CoordinatesThrower(int id) {
    this.id = id;
    up = down = right = left = false;
 }

 public void run() {
    int newX = Server.spieler[id].x;
    int newY = Server.spieler[id].y;
    
    while (true) {
       if (up || down || right || left) {
          if (up)           newY = Server.spieler[id].y - Konstante.RESIZE;
          else if (down)    newY = Server.spieler[id].y + Konstante.RESIZE;
          else if (right)   newX = Server.spieler[id].x + Konstante.RESIZE;
          else if (left)    newX = Server.spieler[id].x - Konstante.RESIZE;

          if (coordinateIsValid(newX, newY)) {
             ClientHandler.sendToAllClients(id + " newCoordinate " + newX + " " + newY);

             Server.spieler[id].x = newX;
             Server.spieler[id].y = newY;
          } else {
             newX = Server.spieler[id].x;
             newY = Server.spieler[id].y;
          }
          try {
             sleep(Konstante.KOORDINATEN_RATE_UPDATE);
          } catch (InterruptedException e) {}
       }
       try {sleep(0);} catch (InterruptedException e) {}
    }
 }

 int getColumnOfMap(int x) {
    return x/Konstante.SIZE_SPRITE_MAP;
 }
 int getLineOfMap(int y) {
    return y/Konstante.SIZE_SPRITE_MAP;
 }

 // Findet heraus, auf welchen Karten-Sprites sich der Spieler befindet, und prüft, ob sie gültig sind
 boolean coordinateIsValid(int newX, int newY) {
    if (!Server.spieler[id].lebt)
       return false;
    
    int x[] = new int[4], y[] = new int[4];
	int c[] = new int[4], l[] = new int[4];


    // die neuen Koords

    // 0: oben links
    x[0] = Konstante.VAR_X_SPRITES + newX + Konstante.RESIZE;
    y[0] = Konstante.VAR_Y_SPRITES + newY + Konstante.RESIZE;
    // 1: oben rechts
    x[1] = Konstante.VAR_X_SPRITES + newX + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    y[1] = Konstante.VAR_Y_SPRITES + newY + Konstante.RESIZE;
    // 2: unten links
    x[2] = Konstante.VAR_X_SPRITES + newX + Konstante.RESIZE;
    y[2] = Konstante.VAR_Y_SPRITES + newY + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    // 3: unten rechts
    x[3] = Konstante.VAR_X_SPRITES + newX + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    y[3] = Konstante.VAR_Y_SPRITES + newY + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    
    for (int i = 0; i < 4; i++) { 
       c[i] = getColumnOfMap(x[i]);
       l[i] = getLineOfMap(y[i]);
    }

    if (
       (Server.getMap()[l[0]][c[0]].img.equals("floor-1")) && 
       (Server.getMap()[l[1]][c[1]].img.equals("floor-1")) &&
       (Server.getMap()[l[2]][c[2]].img.equals("floor-1")) && 
       (Server.getMap()[l[3]][c[3]].img.equals("floor-1"))
    ) 
       return true; //nächste Koord ist gültig

    if (
       (Server.getMap()[l[0]][c[0]].img.contains("block") || Server.getMap()[l[0]][c[0]].img.contains("wall")) || 
       (Server.getMap()[l[1]][c[1]].img.contains("block") || Server.getMap()[l[1]][c[1]].img.contains("wall")) ||
       (Server.getMap()[l[2]][c[2]].img.contains("block") || Server.getMap()[l[2]][c[2]].img.contains("wall")) || 
       (Server.getMap()[l[3]][c[3]].img.contains("block") || Server.getMap()[l[3]][c[3]].img.contains("wall"))
    ) 
       return false; //nächste Koord nicht gültig



    // die vorherigen Koords

    // 0: oben links
    x[0] = Konstante.VAR_X_SPRITES + Server.spieler[id].x + Konstante.RESIZE;
    y[0] = Konstante.VAR_Y_SPRITES + Server.spieler[id].y + Konstante.RESIZE;
    // 1: oben rechts
    x[1] = Konstante.VAR_X_SPRITES + Server.spieler[id].x + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    y[1] = Konstante.VAR_Y_SPRITES + Server.spieler[id].y + Konstante.RESIZE;
    // 2: unten links
    x[2] = Konstante.VAR_X_SPRITES + Server.spieler[id].x + Konstante.RESIZE;
    y[2] = Konstante.VAR_Y_SPRITES + Server.spieler[id].y + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    // 3: unten rechts
    x[3] = Konstante.VAR_X_SPRITES + Server.spieler[id].x + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    y[3] = Konstante.VAR_Y_SPRITES + Server.spieler[id].y + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    
    for (int i = 0; i < 4; i++) { 
       c[i] = getColumnOfMap(x[i]);
       l[i] = getLineOfMap(y[i]);
    }
    
    return false;
 }

 public void keyCodePressed(int keyCode) {
    switch (keyCode) {
       case KeyEvent.VK_W: 
          up = true; down = right = left = false;
          ClientHandler.sendToAllClients(this.id + " newStatus up");
          break;
       case KeyEvent.VK_S: 
          down = true; up = right = left = false;
          ClientHandler.sendToAllClients(this.id + " newStatus down");
          break;
       case KeyEvent.VK_D: 
          right = true; up = down = left = false;
          ClientHandler.sendToAllClients(this.id + " newStatus right");
          break;
       case KeyEvent.VK_A: 
          left = true; up = down = right = false;
          ClientHandler.sendToAllClients(this.id + " newStatus left");
          break;
    }
 }

 public void keyCodeReleased(int keyCode) {
    if (keyCode != KeyEvent.VK_W && keyCode != KeyEvent.VK_S && keyCode != KeyEvent.VK_D && keyCode != KeyEvent.VK_A)
       return;

    ClientHandler.sendToAllClients(this.id + " stopStatusUpdate");
    switch (keyCode) {
       case KeyEvent.VK_W: up = false; break;
       case KeyEvent.VK_S: down = false; break;
       case KeyEvent.VK_D: right = false; break;
       case KeyEvent.VK_A: left = false; break;
    }
 }
}