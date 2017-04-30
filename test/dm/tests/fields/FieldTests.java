package dm.tests.fields;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterNormalCard;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.fields.Field;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;

public class FieldTests {

	private Field field;

	// private MonsterCard monsterCard;
	// private MonsterFusionCard monsterFusionCard;
	// private MonsterCard monsterEffectCard;
	private NormalDeck deck;

	@Before
	public void initCards() {
		// monsterCard = new MonsterNormalCard("Dark Magician", "The ultimate
		// wizard in terms of attack and
		// defense.",null,MonsterType.SPELLCASTER,MonsterAttribute.DARK,2500,2100,0,3);
		// monsterFusionCard = new MonsterFusionCard("Gaia, The Dragon
		// Champion", "The gaia
		// dragon",null,MonsterType.WARRIOR,MonsterAttribute.EARTH,2600,2100,0,null,
		// 3);
		// monsterEffectCard = new MonsterEffectCard("Penguin Soldier", "[FLIP]
		// return one card to your
		// hand",null,MonsterType.AQUA,MonsterAttribute.WATER,500,300,new
		// Effect(), 3);
		deck = new NormalDeck();
		for (int i = 0; i < 40; i++)
			deck.putCard(new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
					null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 2500, 2100, 0, 3));
		field = new Field(deck, new ExtraDeck());
	}

	@Test
	public void drawCards() {
		int deckSize = field.countDeckCards();
		int handSize = field.countHandCards();
		field.draw();
		assertEquals(deckSize - 1, field.countDeckCards());
		assertEquals(handSize + 1, field.countHandCards());
	}

}