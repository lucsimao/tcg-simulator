package dm.tests.phases;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import dm.exceptions.PlayerCannotDrawException;
import dm.game.phases.DrawPhase;
import dm.game.phases.MainPhase;

public class MainPhaseTests extends PhaseTests {

	private MainPhase mainPhase;
	
	@Before
	public void initMainPhase() {
		mainPhase = new MainPhase(player);
	}
	
	@Test
	public void normalSummonSucessfully() {
		//TODO
	}
	
	@Test 
	public void tryTwoNormalSummonFailed() {
		//TODO
	}
	
	@Test
	public void twoNormalSummonIfPlayerCan() {
		//TODO
	}
	
	@Test
	public void specialSummonSucessfully() {
		//TODO
	}
	
	@Test
	public void specialSummonFailed() {
		//TODO
	}
	
	@Test
	public void tributeSummonSucessfully() {
		//TODO
	}
	
	@Test
	public void tributeSummonFailed() {
		//TODO
	}
	
}
