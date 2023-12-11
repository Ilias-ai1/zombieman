package main;

public class Koordinaten {
	public int x, y;
	public String img;

	public Koordinaten(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Koordinaten(int x, int y, String img) {
		this.x = x;
		this.y = y;
		this.img = img;
	}
}