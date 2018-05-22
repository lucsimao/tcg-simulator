package dm.tests.phases;

import static org.junit.Assert.assertEquals;

import java.awt.Image;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterNormalCard;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.exceptions.CardNotFoundException;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;
import dm.game.phases.Phase;

public class PhaseTests {

	private static Phase phase;
	protected static Player player;
	protected static Player player2;
	protected static final String name = "Seto Kaiba";
	protected static final Image avatar = null;
	protected static NormalDeck deck = new NormalDeck();
	protected static ExtraDeck extraDeck = new ExtraDeck();
	

	@Before
	public void initPlayers() {
		initDeck();
		player = new Player(name, avatar, deck, extraDeck);
		player2 = new Player(name, avatar, deck, extraDeck);
		assertEquals(name, player.getName());
		assertEquals(avatar, player.getAvatar());
		assertEquals(deck, player.getDeck());
		assertEquals(extraDeck, player.getExtraDeck());
	}

	private void initDeck() {
		deck = new NormalDeck();
		for (int i = 0; i < 40; i++)
			deck.putCard(new MonsterNormalCard());
	}

	@Test
	public void playerIsCorrect() {
		phase = new Phase(player);
		assertEquals(player, phase.getPlayer());
	}

}
