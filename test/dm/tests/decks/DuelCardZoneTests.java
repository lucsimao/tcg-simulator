package dm.tests.decks;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import dm.cards.DuelMonsterEffectCard;
import dm.cards.DuelMonsterFusionCard;
import dm.cards.DuelSpellCard;
import dm.cards.DuelTrapCard;
import dm.cards.Effect;
import dm.exceptions.ZoneOccupedException;
import dm.fields.elements.zones.CardZone;

public class DuelCardZoneTests {

	private CardZone zone;
	private DuelMonsterFusionCard fusionCard;
	private DuelMonsterEffectCard effectCard;
	private DuelSpellCard spellCard;
	private DuelTrapCard trapCard;
	
	@Before
	public void initGraveyard() {
		zone = new CardZone();
		fusionCard = new DuelMonsterFusionCard(null, null, null, 0, 0, 0, 0, 0, null, 3);
		effectCard = new DuelMonsterEffectCard(null, null, null, 0, 0, 0, 0, new Effect(), 3);
		spellCard = new DuelSpellCard(null, null, null, null, 0, 3);
		trapCard = new DuelTrapCard(null, null, null, null, 0, 3);
	}
		
	@Test
	public void putCards() {
		zone.putCard(fusionCard,0);
		assertEquals(1, zone.countCards());
		zone.putCard(effectCard,1);
		assertEquals(2, zone.countCards());
		zone.putCard(spellCard,2);
		assertEquals(3, zone.countCards());
		zone.putCard(trapCard,3);
		assertEquals(4, zone.countCards());
		zone.putCard(fusionCard,4);
		assertEquals(5, zone.countCards());
	}
	
	@Test(expected= ZoneOccupedException.class)
	public void putCardsOverOther() {
		zone.putCard(fusionCard,0);
		zone.putCard(effectCard,1);
		zone.putCard(spellCard,2);
		zone.putCard(trapCard,2);
	}
	
	@Test(expected= ArrayIndexOutOfBoundsException.class)
	public void putCardsOutOfRange() {
		zone.putCard(fusionCard,0);
		zone.putCard(effectCard,1);
		zone.putCard(spellCard,2);
		zone.putCard(trapCard,3);
		zone.putCard(fusionCard,4);
		zone.putCard(fusionCard,5);
	}
	
	public void removeCards(){
		putCards();
		zone.remove(0);
		assertEquals(4, zone.countCards());
		zone.remove(1);
		assertEquals(3, zone.countCards());
		zone.remove(2);
		assertEquals(2, zone.countCards());
		zone.remove(3);
		assertEquals(1, zone.countCards());
		zone.remove(4);
		assertEquals(0, zone.countCards());
	}
	
	public void removeAllCards(){
		putCards();
		zone.removeAll();
		assertEquals(0, zone.countCards());
	}
	
}
