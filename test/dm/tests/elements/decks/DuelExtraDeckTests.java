package dm.tests.elements.decks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterFusionCard;
import dm.exceptions.CardsOutException;
import dm.exceptions.MaxCardCopiesException;
import dm.exceptions.MaxDeckSizeException;
import dm.fields.elements.decks.ExtraDeck;
import dm.interfaces.ExtraDeckCard;

public class DuelExtraDeckTests {

	private ExtraDeck deck;
	private ExtraDeckCard unlimitedCard;
	private ExtraDeckCard semiLimitedCard;
	private ExtraDeckCard limitedCard;
	private ExtraDeckCard forbbidenCard;

	@Before
	public void initDeck() {
		deck = new ExtraDeck();
		unlimitedCard = new MonsterFusionCard(null, null, null, 0, 0, 0, 0, 0, null, 3);
		semiLimitedCard = new MonsterFusionCard(null, null, null, 0, 0, 0, 0, 0, null, 2);
		limitedCard = new MonsterFusionCard(null, null, null, 0, 0, 0, 0, 0, null, 1);
		forbbidenCard = new MonsterFusionCard(null, null, null, 0, 0, 0, 0, 0, null, 0);
	}
	
	@Test
	public void deckout() {
		assertTrue(deck.isEmpty());
		assertEquals(0, deck.size());
	}

	@Test(expected= MaxDeckSizeException.class)
	public void isDeckPlayable(){
		for(int i=0;i<70;i++){
			deck.putCard(new MonsterFusionCard(null, null, null, 0, 0, 0, 0, 0, null, 3));
//			if(i<39)
//				assertFalse(deck.isPlayable());
//			else if(i<60)
//				assertTrue(deck.isPlayable());
		}
	}
	
	@Test
	public void drawACard() {
		deck.putCard(unlimitedCard);
		int size = deck.size();
		ExtraDeckCard topCard = deck.top();
		ExtraDeckCard drawedCard = deck.drawCard();
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
		for(int i = 0; i< deck.getMaxCards(); i++)
			deck.putCard(new MonsterFusionCard(null, null, null, 0, 0, 0, 0, 0, null, 3));
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
