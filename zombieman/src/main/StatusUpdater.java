package main;

import entity.Konstante;
import entity.Spieler;
import entity.Sprite;

public class StatusUpdater extends Thread {
	   Spieler sp;
	   String status;
	   int index;
	   boolean playerInMotion;

	   public StatusUpdater(Spieler p, String initialStatus) {
	      this.sp = p;
	      this.status = initialStatus;
	      index = 0;
	      playerInMotion = true;
	   }
	   public void run() {
	      while (true) {
	         sp.setStatus(status + "-" + index);
	         if (playerInMotion) {
	            index = (++index) % Sprite.maxLoopStatus.get(status);
	            sp.panel.repaint();
	         }

	         try {
	            Thread.sleep(Konstante.SPIELER_STATUS_RATE_UPDATE);
	         } catch (InterruptedException e) {}

	      }
	   }
	   public void setLoopStatus(String status) {
		      this.status = status;
		      index = 1;
		      playerInMotion = true;
		   }
		   public void stopLoopStatus() {
		      playerInMotion = false;
		      index = 0;
		   }
}