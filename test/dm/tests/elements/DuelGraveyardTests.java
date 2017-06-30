package dm.tests.elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dm.cards.Effect;
import dm.cards.MonsterEffectCard;
import dm.cards.MonsterFusionCard;
import dm.cards.MonsterNormalCard;
import dm.cards.SpellCard;
import dm.cards.TrapCard;
import dm.exceptions.CardsOutException;
import dm.exceptions.NoEffectException;
import dm.fields.elements.Graveyard;

public class DuelGraveyardTests {

	private Graveyard graveyard;
	private MonsterFusionCard fusionCard;
	private MonsterNormalCard normalCard;
	private MonsterEffectCard effectCard;
	private SpellCard spellCard;
	private TrapCard trapCard;

	@Before
	public void initGraveyard() throws NoEffectException {
		graveyard = new Graveyard();
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
		assertTrue(graveyard.isEmpty());
		assertEquals(0, graveyard.size());
	}

	@Test(expected = CardsOutException.class)
	public void drawCardsUntilDeckout() {
		while (true)
			graveyard.drawCard();
	}

	@Test(expected = CardsOutException.class)
	public void removeCardsUntilDeckout() {
		while (true)
			graveyard.drawCard();
	}

	@Test
	public void pushCards() {
		int size = graveyard.size();
		graveyard.putCard(fusionCard);
		assertEquals(size + 1, graveyard.size());
		assertEquals(fusionCard, graveyard.top());

		size = graveyard.size();
		graveyard.putCard(normalCard);
		assertEquals(size + 1, graveyard.size());
		assertEquals(normalCard, graveyard.top());

		size = graveyard.size();
		graveyard.putCard(effectCard);
		assertEquals(size + 1, graveyard.size());
		assertEquals(effectCard, graveyard.top());

		size = graveyard.size();
		graveyard.putCard(spellCard);
		assertEquals(size + 1, graveyard.size());
		assertEquals(spellCard, graveyard.top());

		size = graveyard.size();
		graveyard.putCard(trapCard);
		assertEquals(size + 1, graveyard.size());
		assertEquals(trapCard, graveyard.top());
	}

	@Test
	public void moveToTopCard() {
		graveyard.moveCardToTop(fusionCard);
		assertEquals(fusionCard, graveyard.top());

		graveyard.moveCardToTop(normalCard);
		assertEquals(normalCard, graveyard.top());

		graveyard.moveCardToTop(effectCard);
		assertEquals(effectCard, graveyard.top());

		graveyard.moveCardToTop(spellCard);
		assertEquals(spellCard, graveyard.top());

		graveyard.moveCardToTop(trapCard);
		assertEquals(trapCard, graveyard.top());
	}

}
