package dm.tests.decks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cards.NormalDeckCard;
import dm.cards.DuelMonsterNormalCard;
import dm.exceptions.CardsOutException;
import dm.exceptions.MaxCardCopiesException;
import dm.exceptions.MaxDeckSizeException;
import dm.fields.elements.decks.DuelNormalDeck;

public class DuelDeckTests {

	private DuelNormalDeck deck;
	private NormalDeckCard unlimitedCard;
	private NormalDeckCard semiLimitedCard;
	private NormalDeckCard limitedCard;
	private NormalDeckCard forbbidenCard;

	@Before
	public void initDeck() {
		deck = new DuelNormalDeck();
		unlimitedCard = new DuelMonsterNormalCard(null, null, null, 0, 0, 0, 0, 0, 3);
		semiLimitedCard = new DuelMonsterNormalCard(null, null, null, 0, 0, 0, 0, 0, 2);
		limitedCard = new DuelMonsterNormalCard(null, null, null, 0, 0, 0, 0, 0, 1);
		forbbidenCard = new DuelMonsterNormalCard(null, null, null, 0, 0, 0, 0, 0, 0);
	}
	
	@Test
	public void deckout() {
		assertTrue(deck.isDeckout());
		assertEquals(0, deck.size());
	}

	@Test(expected= MaxDeckSizeException.class)
	public void isDeckPlayable(){
		for(int i=0;i<70;i++){
			deck.putCard(new DuelMonsterNormalCard(null, null, null, 0, 0, 0, 0, 0, 3));
			if(i<39)
				assertFalse(deck.isPlayable());
			else if(i<60)
				assertTrue(deck.isPlayable());
		}
	}
	
	@Test
	public void drawACard() {
		deck.putCard(unlimitedCard);
		int size = deck.size();
		NormalDeckCard topCard = deck.top();
		NormalDeckCard drawedCard = deck.drawCard();
		assertEquals(size - 1, deck.size());
		assertEquals(drawedCard, topCard);
	}

	@Test(expected=CardsOutException.class)
	public void drawCardsUntilDeckout() {
		fillDeck();
		while(true)
			deck.drawCard();
	}
	
	private void fillDeck(){
		for(int i = 0; i< 40; i++)
			deck.putCard(new DuelMonsterNormalCard(null, null, null, 0, 0, 0, 0, 0, 3));
	}
	
	@Test
	public void pushCard() {
		int size = deck.size();
		deck.putCard(unlimitedCard);
		assertEquals(size + 1, deck.size());
		assertEquals(unlimitedCard, deck.top());
	}

	// Testes para o número máximo de cartas
	@Test(expected = MaxCardCopiesException.class)
	public void putForbbidenCard() {
		deck.putCard(forbbidenCard);
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

	@Test(expected = MaxCardCopiesException.class)
	public void putTwoLimitedCards() {
		deck.putCard(limitedCard);
		deck.putCard(limitedCard);
	}

	@Test(expected = MaxCardCopiesException.class)
	public void putThreeSemiLimitedCards() {
		deck.putCard(semiLimitedCard);
		deck.putCard(semiLimitedCard);
		deck.putCard(semiLimitedCard);
	}

	@Test(expected = MaxCardCopiesException.class)
	public void putFourEqualCards() {
		deck.putCard(unlimitedCard);
		deck.putCard(unlimitedCard);
		deck.putCard(unlimitedCard);
		deck.putCard(unlimitedCard);
	}
		
}
