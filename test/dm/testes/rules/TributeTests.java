package dm.testes.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import dm.cards.Effect;
import dm.cards.MonsterEffectCard;
import dm.cards.MonsterFusionCard;
import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.MonsterCard;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.exceptions.EffectMonsterWithNoEffectException;
import dm.exceptions.MonsterCannotBeSummonedException;
import dm.fields.Field;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.tests.cards.abstracts.CardMonsterTests;

public class TributeTests {
	
	private Field field;
	
	private MonsterCard monsterCard_lv6;
	private MonsterCard monsterCard_lv4;
	private MonsterCard tribute;
	private MonsterFusionCard monsterFusionCard;
	private MonsterCard monsterEffectCard;
	
	private int monstersSize;
	
	@Before
	public void initCards() {
		tribute = 	new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK,4, 2500, 2100, 3);
		monsterCard_lv4 = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK,4, 2500, 2100, 3);
		monsterCard_lv6 = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK,6, 2500, 2100, 3);
		monsterFusionCard = new MonsterFusionCard("Gaia, The Dragon Champion", "The gaia dragon", null,
				MonsterType.WARRIOR, MonsterAttribute.EARTH,7, 2600, 2100, 0, null, 3);
		monsterEffectCard = new MonsterEffectCard("Penguin Soldier", "[FLIP] return one card to your hand", null,
				MonsterType.AQUA, MonsterAttribute.WATER,2, 500, 300, new Effect(), 3);
		field = new Field(new NormalDeck(50),new ExtraDeck(10));
		// deckSize = field.countDeckCards();
		// handSize = field.countHandCards();
		monstersSize = field.countMonsters();
	}

	@Test
	public void summonMonsterLv4WithoutTribute() {
		field.summonMonster(monsterCard_lv4);
		assertEquals(monstersSize + 1, field.countMonsters());
	}
	
	@Test (expected = MonsterCannotBeSummonedException.class)
	public void summonMonsterLv6WithoutTribute() {
		field.summonMonster(monsterCard_lv6);
		assertNotEquals(monstersSize + 1, field.countMonsters());
	}
	
	@Test 
	public void summonMonsterLv6WithTribute() {
		field.summonMonster(tribute);
		assertEquals(monstersSize+1, field.countMonsters());
		
		field.tributeSummonMonster(monsterCard_lv6,tribute);
		assertEquals(monstersSize+1, field.countMonsters());
		assertEquals(field.getMonsterCard(0), monsterCard_lv6);
		;
	}
	
}