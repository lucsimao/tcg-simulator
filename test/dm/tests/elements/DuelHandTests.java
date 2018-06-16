package dm.tests.elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import config.exceptions.CardsOutException;
import config.exceptions.NoEffectException;
import model.cards.MonsterEffectCard;
import model.cards.MonsterFusionCard;
import model.cards.MonsterNormalCard;
import model.cards.SpellCard;
import model.cards.TrapCard;
import model.fields.elements.Hand;

public class DuelHandTests {

	private Hand hand;
	private MonsterFusionCard fusionCard;
	private MonsterNormalCard normalCard;
	private MonsterEffectCard effectCard;
	private SpellCard spellCard;
	private TrapCard trapCard;

	@Before
	public void initGraveyard() throws NoEffectException {
		hand = new Hand();
		fusionCard = new MonsterFusionCard();
		normalCard = new MonsterNormalCard(0);
		effectCard = new MonsterEffectCard();
		spellCard = new SpellCard();
		trapCard = new TrapCard();
		pushCards();
	}

	@Test
	public void deckout() {
		try {
			discardCardsUntilDeckout();
		} catch (Exception e) {

		}
		assertTrue(hand.isEmpty());
		assertEquals(0, hand.size());
	}

	@Test(expected = CardsOutException.class)
	public void discardCardsUntilDeckout() {
		while (true)
			hand.discardFirst();
	}

	@Test
	public void pushCardsUntilMax() {
		int size = hand.size();
		assertEquals(true, hand.isHandPlayable());
		hand.putCard(fusionCard);
		assertEquals(size + 1, hand.size());
		assertEquals(fusionCard, hand.top());

		size = hand.size();
		hand.putCard(normalCard);
		assertEquals(size + 1, hand.size());
		assertEquals(normalCard, hand.top());

		size = hand.size();
		hand.putCard(effectCard);
		assertEquals(size + 1, hand.size());
		assertEquals(effectCard, hand.top());

		size = hand.size();
		hand.putCard(spellCard);
		assertEquals(size + 1, hand.size());
		assertEquals(spellCard, hand.top());

		size = hand.size();
		hand.putCard(trapCard);
		assertEquals(size + 1, hand.size());
		assertEquals(trapCard, hand.top());

		assertEquals(false, hand.isHandPlayable());
	}

	public void pushCards() {
		int size = hand.size();
		hand.putCard(fusionCard);
		assertEquals(size + 1, hand.size());
		assertEquals(fusionCard, hand.top());

		size = hand.size();
		hand.putCard(normalCard);
		assertEquals(size + 1, hand.size());
		assertEquals(normalCard, hand.top());

		size = hand.size();
		hand.putCard(effectCard);
		assertEquals(size + 1, hand.size());
		assertEquals(effectCard, hand.top());

		size = hand.size();
		hand.putCard(spellCard);
		assertEquals(size + 1, hand.size());
		assertEquals(spellCard, hand.top());

		size = hand.size();
		hand.putCard(trapCard);
		assertEquals(size + 1, hand.size());
		assertEquals(trapCard, hand.top());
	}
}
