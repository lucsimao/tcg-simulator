package dm.tests.decks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dm.cards.DuelMonsterEffectCard;
import dm.cards.DuelMonsterFusionCard;
import dm.cards.DuelMonsterNormalCard;
import dm.cards.DuelSpellCard;
import dm.cards.DuelTrapCard;
import dm.cards.Effect;
import dm.exceptions.CardsOutException;
import dm.exceptions.MaxDeckSizeException;
import dm.fields.elements.DuelHand;

public class DuelHandTests {

	private DuelHand hand;
	private DuelMonsterFusionCard fusionCard;
	private DuelMonsterNormalCard normalCard;
	private DuelMonsterEffectCard effectCard;
	private DuelSpellCard spellCard;
	private DuelTrapCard trapCard;
	
	@Before
	public void initGraveyard() {
		hand = new DuelHand();
		fusionCard = new DuelMonsterFusionCard(null, null, null, 0, 0, 0, 0, 0, null, 3);
		normalCard = new DuelMonsterNormalCard(null, null, null, 0, 0, 0, 0, 0, 0);
		effectCard = new DuelMonsterEffectCard(null, null, null, 0, 0, 0, 0, new Effect(), 3);
		spellCard = new DuelSpellCard(null, null, null, null, 0, 3);
		trapCard = new DuelTrapCard(null, null, null, null, 0, 3);
		pushCards();
	}
		
	@Test
	public void deckout() {
		try {
			discardCardsUntilDeckout();
		}catch(Exception e) {

		}
		assertTrue(hand.isDeckout());
		assertEquals(0, hand.size());
	}
	
	@Test(expected=CardsOutException.class)
	public void discardCardsUntilDeckout() {
		while(true)
			hand.drawCard();
	}
	
	@Test(expected=CardsOutException.class)
	public void removeCardsUntilDeckout() {
		while(true)
			hand.drawCard();
	}
	
	@Test
	public void pushCardsUntilMax(){
		int size = hand.size();
		assertEquals(true,hand.isHandPlayable());
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
		
		assertEquals(false,hand.isHandPlayable());
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
