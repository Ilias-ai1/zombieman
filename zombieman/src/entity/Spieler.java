package entity;

import java.awt.Graphics;
import javax.swing.JPanel;

import main.StatusUpdater;
import net.Client;


// Spielerklasse
public class Spieler {
   public int x;
   public int y;
   private String status;
   String skin;
   public JPanel panel;
   public boolean lebt;

   public StatusUpdater sc;

   public Spieler(int id, JPanel panel) {
	   	  this.x = Client.spawn[id].x;
	      this.y = Client.spawn[id].y;
	      this.skin = Sprite.personSkins[id];
	      this.panel = panel;
	      this.lebt = Client.alive[id];
	      
	      (sc = new StatusUpdater(this, "wait")).start();

   }

   public void draw(Graphics g) {
	   if(lebt) {
         g.drawImage(Sprite.ht.get(skin + "/" + getStatus()), x, y, Konstante.BREITE_SPRITE_SPIELER, Konstante.HOEHE_SPRITE_SPIELER, null);
	   }
   }

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}
}


