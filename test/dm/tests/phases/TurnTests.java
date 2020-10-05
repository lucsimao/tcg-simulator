package dm.tests.phases;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterNormalCard;
import dm.exceptions.EndOfTurnException;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;
import dm.game.Turn;
import dm.game.phases.BattlePhase;
import dm.game.phases.DrawPhase;
import dm.game.phases.EndPhase;
import dm.game.phases.MainPhase;
import dm.game.phases.StandByPhase;

public class TurnTests{


	private Turn turn;
	protected static NormalDeck deck = new NormalDeck();
	private Player player;
	
	@Before
	public void initDeck(){
		deck = new NormalDeck();
		for (int i = 0; i < 40; i++) {
			deck.putCard(new MonsterNormalCard());
		}
		player = new Player("TestPlayer1", deck);
		turn = new Turn(player);
	}
	
	@Test
	public void createATurn() {
		assertEquals(DrawPhase.class,turn.getPhase().getClass());
	}	
	
	@Test
	public void passDrawToStandBy() {
		turn.changePhase(new StandByPhase(player));
		assertEquals(StandByPhase.class,turn.getPhase().getClass());
	}
	
	
	@Test
	public void passStandByToMainPhase() {
		turn.changePhase(new MainPhase(player));
		assertEquals(MainPhase.class,turn.getPhase().getClass());
	}
	
	@Test
	public void passMainPhaseToBattlePhase() {
		turn.changePhase(new BattlePhase(player));
		assertEquals(BattlePhase.class,turn.getPhase().getClass());
	}
	
	@Test
	public void BattlePhaseToEndPhase() {
		turn.changePhase(new EndPhase(player));
		assertEquals(EndPhase.class,turn.getPhase().getClass());
	}
	
	@Test
	public void AdvancePhaseTest() {
		turn = new Turn(player);
		assertEquals(DrawPhase.class,turn.getPhase().getClass());
		turn.advancePhase();
		assertEquals(StandByPhase.class,turn.getPhase().getClass());
		turn.advancePhase();
		assertEquals(MainPhase.class,turn.getPhase().getClass());
		turn.advancePhase();
		assertEquals(BattlePhase.class,turn.getPhase().getClass());
		turn.advancePhase();
		assertEquals(EndPhase.class,turn.getPhase().getClass());
	}
	
	@Test (expected = EndOfTurnException.class)
	public void AdvancePhaseUntilEndPhaseTest() {
		turn = new Turn(player);
		assertEquals(DrawPhase.class,turn.getPhase().getClass());
		turn.advancePhase();
		assertEquals(StandByPhase.class,turn.getPhase().getClass());
		turn.advancePhase();
		assertEquals(MainPhase.class,turn.getPhase().getClass());
		turn.advancePhase();
		assertEquals(BattlePhase.class,turn.getPhase().getClass());
		turn.advancePhase();
		assertEquals(EndPhase.class,turn.getPhase().getClass());
		turn.advancePhase();
	}
	
	
	
}
