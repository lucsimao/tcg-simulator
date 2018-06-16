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
import model.fields.elements.RemoveFromPlay;

public class DuelRemoveFromPlayTests {

	private RemoveFromPlay deck;
	private MonsterFusionCard fusionCard;
	private MonsterNormalCard normalCard;
	private MonsterEffectCard effectCard;
	private SpellCard spellCard;
	private TrapCard trapCard;

	@Before
	public void initGraveyard() throws NoEffectException {
		deck = new RemoveFromPlay();
		fusionCard = new MonsterFusionCard();
		normalCard = new MonsterNormalCard();
		effectCard = new MonsterEffectCard();
		spellCard = new SpellCard();
		trapCard = new TrapCard();
		pushCards();
	}

	@Test
	public void deckout() {
		try {
			drawCardsUntilDeckout();
		} catch (Exception e) {

		}
		assertTrue(deck.isEmpty());
		assertEquals(0, deck.size());
	}

	@Test(expected = CardsOutException.class)
	public void drawCardsUntilDeckout() {
		while (true)
			deck.drawCard();
	}

	@Test(expected = CardsOutException.class)
	public void removeCardsUntilDeckout() {
		while (true)
			deck.drawCard();
	}

	@Test
	public void pushCards() {
		int size = deck.size();
		deck.putCard(fusionCard);
		assertEquals(size + 1, deck.size());
		assertEquals(fusionCard, deck.top());

		size = deck.size();
		deck.putCard(normalCard);
		assertEquals(size + 1, deck.size());
		assertEquals(normalCard, deck.top());

		size = deck.size();
		deck.putCard(effectCard);
		assertEquals(size + 1, deck.size());
		assertEquals(effectCard, deck.top());

		size = deck.size();
		deck.putCard(spellCard);
		assertEquals(size + 1, deck.size());
		assertEquals(spellCard, deck.top());

		size = deck.size();
		deck.putCard(trapCard);
		assertEquals(size + 1, deck.size());
		assertEquals(trapCard, deck.top());
	}

	@Test
	public void moveToTopCard() {
		deck.moveCardToTop(fusionCard);
		assertEquals(fusionCard, deck.top());

		deck.moveCardToTop(normalCard);
		assertEquals(normalCard, deck.top());

		deck.moveCardToTop(effectCard);
		assertEquals(effectCard, deck.top());

		deck.moveCardToTop(spellCard);
		assertEquals(spellCard, deck.top());

		deck.moveCardToTop(trapCard);
		assertEquals(trapCard, deck.top());
	}

}
