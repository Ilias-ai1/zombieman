package entity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;


public class Sprite {
   public final static String personSkins[] = {
		   "richie", 
		   "jackson",
		   "richie", 
		   "jackson" 
		  
   };

   public final static Hashtable<String, Image> ht = new Hashtable<String, Image>();
   //Map Blöcke
   final static String mapKeyWords[] = { 
      "background", "block","wall-center","wall-center2", "wall-down-left", "wall-down-right", 
      "wall-up-left", "wall-up-right","wall-left","wall-up", "wall-right", "wall-down","floor-1","floor-2"
   };
   //Spieler Sprites
   static final String personKeyWords[] = {
      "dead-0", "dead-1", "dead-2", "dead-3", "dead-4", 
      "down-0", "down-1", "down-2", "down-3", 
      "left-0", "left-1", "left-2", "left-3", 
      "right-0", "right-1", "right-2", "right-3", 
      "up-0", "up-1", "up-2", "up-3", 
      "wait-0", "wait-1", "wait-2", "wait-3", 
   };
   
   public final static Hashtable<String, Integer> maxLoopStatus = new Hashtable<String, Integer>();
   public static void setMaxLoopStatus() {
      maxLoopStatus.put("dead", 5);
      maxLoopStatus.put("down", 4);
      maxLoopStatus.put("left", 4);
      maxLoopStatus.put("right", 4);
      maxLoopStatus.put("up", 4);
      maxLoopStatus.put("wait", 4);
   }

   public static void loadImages() {
      try {
         System.out.print("Die Spielfäche wird geladen...\n");
         for (String keyWord : mapKeyWords)
            ht.put(keyWord, ImageIO.read(new File("res/map/"+keyWord+".png")));

         for (String skin : personSkins)
            for (String keyWord : personKeyWords)
               ht.put(skin+"/"+keyWord, ImageIO.read(new File("res/person/"+skin+"/"+keyWord+".png")));
      } catch (IOException e) {
         System.out.print(" Fehler!\n");
         System.exit(1);
      }
   }
}