package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



// Spielerklasse
public class Spieler {
   public int x;
   public int y;
   private String status;
   String skin;
   public JPanel panel;
   public boolean lebt;


   public Spieler(int id, JPanel panel) {
      this.x = 64;
      this.y = 64;
      this.skin = Sprite.personSkins[0];
      this.panel = panel;
      this.lebt = true;
      this.status = "wait-0";
      this.skin = "richie";
   }

   public void draw(Graphics g){
	   if(lebt) {
		 //g.setColor(Color.RED);
		 //g.drawRect(x, y, y, x);
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


