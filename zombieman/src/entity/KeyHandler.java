package entity;

import java.awt.event.KeyEvent;


import main.Game;


//Thread, der die nächsten Koordinaten an Clients sendet, während W/A/S/D nicht freigegeben ist
public class KeyHandler extends Thread {
 boolean up, right, left, down;
 int id;

 public KeyHandler(int id) {
    this.id = 0;
    up = down = right = left = false;
 }

 public void run() {
    int newX = Game.spieler[0].x;
    int newY = Game.spieler[0].y;
    
    while (true) {
       if (up || down || right || left) {
          if (up)           newY = Game.spieler[0].y - Konstante.RESIZE;
          else if (down)    newY = Game.spieler[0].y + Konstante.RESIZE;
          else if (right)   newX = Game.spieler[0].x + Konstante.RESIZE;
          else if (left)    newX = Game.spieler[0].x - Konstante.RESIZE;

          if (coordinateIsVal0(newX, newY)) {
             Game.spieler[0].x = newX;
             Game.spieler[0].y = newY;
          } else {
             newX = Game.spieler[0].x;
             newY = Game.spieler[0].y;
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
 boolean coordinateIsVal0(int newX, int newY) {
    if (!Game.spieler[0].lebt)
       return false;

    //prüft, ob der Spieler von der Explosion getroffen wurde (Körperzentrumskoordinate)
    int xBody = newX + Konstante.BREITE_SPRITE_SPIELER/2;
    int yBody = newY + 2*Konstante.HOEHE_SPRITE_SPIELER/3;

    if (Game.getMap()[getLineOfMap(yBody)][getColumnOfMap(xBody)].img.contains("explosion")) {
       Game.spieler[0].lebt = false;
       return true;
    }
    
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
       (Game.getMap()[l[0]][c[0]].img.equals("floor-1")) && 
       (Game.getMap()[l[1]][c[1]].img.equals("floor-1")) &&
       (Game.getMap()[l[2]][c[2]].img.equals("floor-1")) && 
       (Game.getMap()[l[3]][c[3]].img.equals("floor-1"))
    ) 
       return true; //nächste Koord ist gültig

    if (
       (Game.getMap()[l[0]][c[0]].img.contains("block") || Game.getMap()[l[0]][c[0]].img.contains("wall")) || 
       (Game.getMap()[l[1]][c[1]].img.contains("block") || Game.getMap()[l[1]][c[1]].img.contains("wall")) ||
       (Game.getMap()[l[2]][c[2]].img.contains("block") || Game.getMap()[l[2]][c[2]].img.contains("wall")) || 
       (Game.getMap()[l[3]][c[3]].img.contains("block") || Game.getMap()[l[3]][c[3]].img.contains("wall"))
    ) 
       return false; //nächste Koord nicht gültig



    // die vorherigen Koords

    // 0: oben links
    x[0] = Konstante.VAR_X_SPRITES + Game.spieler[0].x + Konstante.RESIZE;
    y[0] = Konstante.VAR_Y_SPRITES + Game.spieler[0].y + Konstante.RESIZE;
    // 1: oben rechts
    x[1] = Konstante.VAR_X_SPRITES + Game.spieler[0].x + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    y[1] = Konstante.VAR_Y_SPRITES + Game.spieler[0].y + Konstante.RESIZE;
    // 2: unten links
    x[2] = Konstante.VAR_X_SPRITES + Game.spieler[0].x + Konstante.RESIZE;
    y[2] = Konstante.VAR_Y_SPRITES + Game.spieler[0].y + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    // 3: unten rechts
    x[3] = Konstante.VAR_X_SPRITES + Game.spieler[0].x + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    y[3] = Konstante.VAR_Y_SPRITES + Game.spieler[0].y + Konstante.SIZE_SPRITE_MAP - 2 * Konstante.RESIZE;
    
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
          break;
       case KeyEvent.VK_S: 
          down = true; up = right = left = false;
          break;
       case KeyEvent.VK_D: 
          right = true; up = down = left = false;
          break;
       case KeyEvent.VK_A: 
          left = true; up = down = right = false;
          break;
    }
 }

 public void keyCodeReleased(int keyCode) {
    if (keyCode != KeyEvent.VK_W && keyCode != KeyEvent.VK_S && keyCode != KeyEvent.VK_D && keyCode != KeyEvent.VK_A)
       return;

    switch (keyCode) {
       case KeyEvent.VK_W: up = false; break;
       case KeyEvent.VK_S: down = false; break;
       case KeyEvent.VK_D: right = false; break;
       case KeyEvent.VK_A: left = false; break;
    }
 }

}