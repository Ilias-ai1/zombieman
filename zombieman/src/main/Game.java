package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import entity.*;
//import net.Client;


public class Game extends JPanel {
	
   private static final long serialVersionUID = 1L;
   public static Spieler spieler1;
   public static SpielerDaten spieler[] = new SpielerDaten[Konstante.MAX_SPIELER];
   public static Koordinaten map[][] = new Koordinaten[Konstante.LIN][Konstante.COL];
   private static JFrame frame = new JFrame();

   public Game(int width, int height) {
      setPreferredSize(new Dimension(width, height));
      System.out.println("Mein Spieler: " + Sprite.personSkins[0]+"\n");
      spieler1 = new Spieler(1, this);

      

   }
   public void setMap() {
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
	            	if(counter % 2 == 0)
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
	   
	   void setSpielerDaten() {
		   spieler[0] = new SpielerDaten(
			         getMap()[1][1].x - Konstante.VAR_X_SPRITES, 
			         getMap()[1][1].y - Konstante.VAR_Y_SPRITES
			      );
	   }
	  
   //paint() und repaint() werden aufgerufen
   public void paintComponent(Graphics g) {
      
	  super.paintComponent(g);
      drawMap(g);
      spieler1.draw(g);
      
      //Toolkit.getDefaultToolkit().sync();
   }
  

   public static void setSpriteMap(String keyWord, int l, int c) {
      map[l][c].img = keyWord;
   }
   void drawMap(Graphics g) {
	      for (int i = 0; i < Konstante.LIN; i++)
	         for (int j = 0; j < Konstante.COL; j++)
	            g.drawImage(
	               Sprite.ht.get(map[i][j].img), 
	               map[i][j].x, map[i][j].y, 
	               Konstante.SIZE_SPRITE_MAP, Konstante.SIZE_SPRITE_MAP, null
	            );
	   }
   public static Koordinaten[][] getMap() {
		return map;
	}
   public static void setMap(Koordinaten newMap[][]) {
		map = newMap;
	}
   public static void main(String[] args) {
	   Sprite.loadImages();
	   Sprite.setMaxLoopStatus();
	   Game game = new Game(Konstante.COL * Konstante.SIZE_SPRITE_MAP, Konstante.LIN * Konstante.SIZE_SPRITE_MAP);
	   game.setMap();
	   game.setSpielerDaten();
	      
	      frame.add(game);
	      frame.setTitle("ZombieMan");
	      frame.pack();
	      frame.setVisible(true);
	      frame.setLocationRelativeTo(null);
	      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	      
   }
}