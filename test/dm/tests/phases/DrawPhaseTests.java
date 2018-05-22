package dm.tests.phases;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dm.exceptions.PlayerCannotDrawException;
import dm.game.phases.DrawPhase;

public class DrawPhaseTests extends PhaseTests {

	private DrawPhase drawPhase;
	
	@Test
	public void playerDraw() {
		player.firstDraw();
		drawPhase = new DrawPhase(player,true);
		assertEquals(6,player.getHand().size());
	}
	
	@Test (expected = PlayerCannotDrawException.class)
	public void playerDrawWhenItCannotDraw() {
		player.firstDraw();
		drawPhase = new DrawPhase(player,false);
		assertEquals(5,player.getHand().size());
	}
	
}
