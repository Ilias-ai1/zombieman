package net;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import main.*;

public class Sender extends KeyAdapter {
 int lastKeyCodePressed;
 
public void keyPressed(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_SPACE)
		Client.out.println("pressedSpace " + Game.du.x + " " + Game.du.y);
	else if (isNewKeyCode(e.getKeyCode()))
		Client.out.println("keyCodePressed " + e.getKeyCode());
}
    
 public void keyReleased(KeyEvent e) {
    Client.out.println("keyCodeReleased " + e.getKeyCode());
    lastKeyCodePressed = -1; //der nächste Schlüssel wird immer neu sein
 }
 
 boolean isNewKeyCode(int keyCode) {
    boolean ok = (keyCode != lastKeyCodePressed) ? true : false;
    lastKeyCodePressed = keyCode;
    return ok;
 }
}