package dm.testes.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.MonsterCard;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.exceptions.MonsterCannotBeSummonedException;
import dm.fields.Field;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;

public class TributeTests {

	private Field field;

	private MonsterCard monsterCard_lv6;
	private MonsterCard monsterCard_lv4;
	private MonsterCard monsterCard_lv8;
	private MonsterCard tribute;
	private MonsterCard tribute2;

	private int monstersSize;

	@Before
	public void initCards() {
		tribute = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.", null,
				MonsterType.SPELLCASTER, MonsterAttribute.DARK, 4, 2500, 2100, 3);
		tribute2 = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.", null,
				MonsterType.SPELLCASTER, MonsterAttribute.DARK, 4, 2500, 2100, 3);
		monsterCard_lv4 = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 4, 2500, 2100, 3);
		monsterCard_lv6 = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 6, 2500, 2100, 3);
		monsterCard_lv8 = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 8, 2500, 2100, 3);
		field = new Field(new NormalDeck(50), new ExtraDeck(10));
		monstersSize = field.countMonsters();
	}

	@Test
	public void summonMonsterLv4WithoutTribute() {
		for (int i = 1; i <= 4; i++) {
			monsterCard_lv4.setLevel(i);
			field.summonMonster(monsterCard_lv4);
			assertEquals(monstersSize + i, field.countMonsters());
		}
	}

	@Test(expected = MonsterCannotBeSummonedException.class)
	public void summonMonsterLv6WithoutTribute() {
		field.summonMonster(monsterCard_lv6);
		assertNotEquals(monstersSize + 1, field.countMonsters());
	}

	@Test(expected = MonsterCannotBeSummonedException.class)
	public void summonMonsterLv5WithoutTribute() {
		monsterCard_lv6.setLevel(5);
		field.summonMonster(monsterCard_lv6);
		assertNotEquals(monstersSize + 1, field.countMonsters());
	}

	@Test
	public void summonMonsterLv6WithTribute() {
		field.summonMonster(tribute);
		assertEquals(monstersSize + 1, field.countMonsters());

		field.tributeSummonMonster(monsterCard_lv6, tribute);
		assertEquals(monstersSize + 1, field.countMonsters());
		assertEquals(field.getMonsterCard(0), monsterCard_lv6);

	}

	@Test(expected = MonsterCannotBeSummonedException.class)
	public void summonMonsterLv7WithOneTribute() {
		field.summonMonster(tribute);
		assertEquals(monstersSize + 1, field.countMonsters());
		field.tributeSummonMonster(monsterCard_lv8, tribute);
		assertEquals(monstersSize + 1, field.countMonsters());
		assertEquals(field.getMonsterCard(0), monsterCard_lv8);
	}

	@Test
	public void summonMonsterLv7WithTwoTributes() {
		for (int i = 7; i <= 12; i++) {
			monsterCard_lv8.setLevel(i);
			field.summonMonster(tribute);
			field.summonMonster(tribute2);
			assertEquals(monstersSize + 2, field.countMonsters());
			field.tributeSummonMonster(monsterCard_lv8, tribute, tribute2);
			assertEquals(monstersSize + 1, field.countMonsters());
			assertEquals(field.getMonsterCard(0), monsterCard_lv8);
			field.sendToGraveyard(monsterCard_lv8);
		}
	}

	@Test(expected = MonsterCannotBeSummonedException.class)
	public void tributeAMonsterThatIsNotInTheField() {
		monsterCard_lv6.setLevel(6);
		field.tributeSummonMonster(monsterCard_lv6, tribute);
		assertEquals(monstersSize + 1, field.countMonsters());
		assertEquals(field.getMonsterCard(0), monsterCard_lv6);
	}

}