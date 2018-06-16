package dm.tests.elements.decks.abstracts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import config.exceptions.CardsOutException;
import model.fields.elements.decks.Deck;

public abstract class DeckTests<GenericCard> {

	/*
	 * Classe criada para ser a superclasse dos testes do Deck. Ainda
	 * implementando
	 */

	private Deck<GenericCard> deck;
	private GenericCard unlimitedCard;
	private GenericCard semiLimitedCard;
	private GenericCard limitedCard;
	private GenericCard forbbidenCard;

	public Deck<GenericCard> getDeck() {
		return deck;
	}

	public void setDeck(Deck<GenericCard> deck) {
		this.deck = deck;
	}

	public GenericCard getUnlimitedCard() {
		return unlimitedCard;
	}

	public void setUnlimitedCard(GenericCard unlimitedCard) {
		this.unlimitedCard = unlimitedCard;
	}

	public GenericCard getSemiLimitedCard() {
		return semiLimitedCard;
	}

	public void setSemiLimitedCard(GenericCard semiLimitedCard) {
		this.semiLimitedCard = semiLimitedCard;
	}

	public GenericCard getLimitedCard() {
		return limitedCard;
	}

	public void setLimitedCard(GenericCard limitedCard) {
		this.limitedCard = limitedCard;
	}

	public GenericCard getForbbidenCard() {
		return forbbidenCard;
	}

	public void setForbbidenCard(GenericCard forbbidenCard) {
		this.forbbidenCard = forbbidenCard;
	}

	@Test
	public void deckout() {
		assertTrue(deck.isEmpty());
		assertEquals(0, deck.size());
	}

	@Test
	public void drawACard() {
		deck.putCard(unlimitedCard);
		int size = deck.size();
		GenericCard topCard = deck.top();
		GenericCard drawedCard = deck.drawCard();
		assertEquals(size - 1, deck.size());
		assertEquals(drawedCard, topCard);
	}

	@Test(expected = CardsOutException.class)
	public void drawCardsUntilDeckout() {
		fillDeck();
		while (true)
			deck.drawCard();
	}

	protected abstract void fillDeck();

	@Test
	public void pushCard() {
		int size = deck.size();
		deck.putCard(unlimitedCard);
		assertEquals(size + 1, deck.size());
		assertEquals(unlimitedCard, deck.top());
	}

	@Test
	public void countCardsInDeck() {
		assertEquals(0, deck.countCards(unlimitedCard));
		deck.putCard(unlimitedCard);
		assertEquals(1, deck.countCards(unlimitedCard));
		deck.putCard(unlimitedCard);
		assertEquals(2, deck.countCards(unlimitedCard));
		deck.putCard(unlimitedCard);
		assertEquals(3, deck.countCards(unlimitedCard));
	}

}
