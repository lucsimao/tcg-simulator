package dm.tests.elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterEffectCard;
import dm.cards.MonsterFusionCard;
import dm.cards.MonsterNormalCard;
import dm.cards.SpellCard;
import dm.cards.TrapCard;
import dm.cards.Effect;
import dm.exceptions.CardsOutException;
import dm.exceptions.NoEffectException;
import dm.fields.elements.Hand;

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
		fusionCard = new MonsterFusionCard(null, null, null, 0, 0, 0, 0, 0, null, 3);
		normalCard = new MonsterNormalCard(null, null, null, 0, 0, 0, 0, 0, 0);
		effectCard = new MonsterEffectCard(null, null, null, 0, 0, 0, 0, new Effect(), 3);
		spellCard = new SpellCard(null, null, null, new Effect(), 0, 3);
		trapCard = new TrapCard(null, null, null, new Effect(), 0, 3);
		pushCards();
	}
		
	@Test
	public void deckout() {
		try {
			discardCardsUntilDeckout();
		}catch(Exception e) {

		}
		assertTrue(hand.isEmpty());
		assertEquals(0, hand.size());
	}
	
	@Test(expected=CardsOutException.class)
	public void discardCardsUntilDeckout() {
		while(true)
			hand.discardFirst();
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
