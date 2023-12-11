package entity;

public class SpielerDaten {
	private boolean logged;
	public boolean lebt;
	public int x; // aktuelle Koords
	public int y;
	private int numberOfBombe;

	public SpielerDaten(int x, int y) {
		this.x = x;
		this.y = y;
		this.setLogged(false);
		this.lebt = false;
		this.setNumberOfBombs(1); // Bei 2 Bomben, wird je Bombe im eigenen Thread behandelt
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public int getNumberOfBombs() {
		return numberOfBombe;
	}

	public void setNumberOfBombs(int numberOfBombs) {
		this.numberOfBombe = numberOfBombs;
	}

}