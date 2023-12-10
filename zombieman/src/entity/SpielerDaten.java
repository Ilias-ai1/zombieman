package entity;

public class SpielerDaten {
	   private boolean logged;
	   public boolean lebt;
	   public int x; //aktuelle Koords
	   public int y;

	   public SpielerDaten(int x, int y) {
	      this.x = x;
	      this.y = y;
	      this.setLogged(false);
	      this.lebt = false;
	   }
	   
	   public boolean isLogged() {
			return logged;
		}

		public void setLogged(boolean logged) {
			this.logged = logged;
		}

}