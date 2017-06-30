package dm.tests.elements.zones;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterEffectCard;
import dm.cards.MonsterFusionCard;
import dm.cards.SpellCard;
import dm.cards.TrapCard;
import dm.cards.abstracts.Card;
import dm.exceptions.CardNotFoundException;
import dm.exceptions.NoEffectException;
import dm.exceptions.ZoneOccupedException;
import dm.fields.elements.zones.CardZone;

public class DuelCardZoneTests {

	private CardZone zone;
	private MonsterFusionCard fusionCard;
	private MonsterEffectCard effectCard;
	private SpellCard spellCard;
	private TrapCard trapCard;

	@Before
	public void initZone() throws NoEffectException {
		zone = new CardZone();
		fusionCard = new MonsterFusionCard();
		effectCard = new MonsterEffectCard();
		spellCard = new SpellCard();
		trapCard = new TrapCard();
	}

	@Test
	public void putCards() {
		zone.putCard(fusionCard, 0);
		assertEquals(1, zone.countCards());
		zone.putCard(effectCard, 1);
		assertEquals(2, zone.countCards());
		zone.putCard(spellCard, 2);
		assertEquals(3, zone.countCards());
		zone.putCard(trapCard, 3);
		assertEquals(4, zone.countCards());
		zone.putCard(fusionCard, 4);
		assertEquals(5, zone.countCards());
	}

	@Test(expected = ZoneOccupedException.class)
	public void putCardsOverOther() {
		zone.putCard(fusionCard, 0);
		zone.putCard(effectCard, 1);
		zone.putCard(spellCard, 2);
		zone.putCard(trapCard, 2);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void putCardsOutOfRange() {
		zone.putCard(fusionCard, 0);
		zone.putCard(effectCard, 1);
		zone.putCard(spellCard, 2);
		zone.putCard(trapCard, 3);
		zone.putCard(fusionCard, 4);
		zone.putCard(fusionCard, 5);
	}

	public void removeCards() {
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

	public void removeAllCards() {
		putCards();
		zone.removeAll();
		assertEquals(0, zone.countCards());
	}

	@Test(expected = CardNotFoundException.class)
	public void getMonstersIndex() {
		putCards();
		Card c = new MonsterEffectCard();
		assertEquals(0, zone.getCardIndex(fusionCard));
		zone.getCardIndex(c);
	}

}
