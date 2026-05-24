package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

import entity.SpielerDaten;

public class SpielerDatenTest {
	
	private SpielerDaten testee;
	
	@Test
	public void spielerDatenConstructor() {
		testee = new SpielerDaten(1, 1);

		assertEquals(1, testee.getX());
		assertEquals(1, testee.getY());
		assertTrue(!testee.isLogged());
		assertFalse(testee.isLogged());
		assertEquals(1, testee.getNumberOfBombs());
	}
	
	@Test
	public void addPowerUpsTest() {
		testee = new SpielerDaten(1, 1);

		for (int i = 0; i < 5; i++)
			testee.addPowerUps(i);

		for (int i = 0; i < 5; i++) {
			if (i == 2)
				assertEquals(2, testee.getPowerUps(i));
			else
				assertEquals(1, testee.getPowerUps(i));

		}
	}
	
	@Test
	public void resetPowerUpsTest() {
		testee = new SpielerDaten(1, 1);

		for (int i = 0; i < 5; i++)
			testee.addPowerUps(i);

		testee.resetPowerUps();

		for (int i = 0; i < 5; i++) {
			if (i == 2)
				assertEquals(1, testee.getPowerUps(i));
			else
				assertEquals(0, testee.getPowerUps(i));

		}
	}
	
	@Test
	public void removeOneLifeTest() {
		testee = new SpielerDaten(1, 1);

		testee.addPowerUps(1);
		testee.addPowerUps(1);
		testee.addPowerUps(1);

		assertEquals(3, testee.getPowerUps(1));

		testee.removeOneLife();

		assertEquals(2, testee.getPowerUps(1));
	}
}
