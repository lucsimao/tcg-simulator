package dm.test.fields;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import constants.MonsterAttribute;
import constants.MonsterType;
import constants.SpellType;
import constants.TrapType;
import dm.cards.DuelMonsterCard;
import dm.cards.DuelMonsterFusionCard;
import dm.cards.DuelMonsterNormalCard;
import dm.cards.DuelSpellCard;
import dm.cards.DuelTrapCard;
import dm.cards.Effect;
import dm.fields.Field;

public class FieldTests{
	
	private Field field;
	
	private DuelMonsterCard monsterCard;
	private DuelMonsterCard monsterFusionCard;
	private DuelMonsterCard monsterEffectCard;
	private DuelSpellCard spellCard;
	private DuelTrapCard trapCard;


	@Before
	public void initCards(){
		monsterCard = new DuelMonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",null,MonsterType.SPELLCASTER,MonsterAttribute.DARK,2500,2100,0,3);
		monsterFusionCard = new DuelMonsterFusionCard("Gaia, The Dragon Champion", "The gaia dragon",null,MonsterType.WARRIOR,MonsterAttribute.EARTH,2600,2100,0,null, 3);
		monsterEffectCard =  new DuelMonsterFusionCard("Penguin Soldier", "[FLIP] return one card to your hand",null,MonsterType.AQUA,MonsterAttribute.WATER,500,300,0,new Effect(), 3);
		spellCard = new DuelSpellCard("Dark Hole","Destroy all monster on the field",null,new Effect(),SpellType.NORMAL, 3);
		trapCard = new DuelTrapCard("Mirror Force","Destroy all attacking monsters",null,new Effect(),TrapType.NORMAL, 3);
		field = new Field();
	}
	
	@Test
	public void setMonster(){
		field.setMonster(monsterCard);
		assertEquals(1,field.countMonsters());
		field.setMonster(monsterCard,2);
		assertEquals(2,field.countMonsters());
	}
	
	@Test
	public void summonMonster(){
		field.summonMonster(monsterCard);
		assertEquals(1,field.countMonsters());
		field.summonMonster(monsterCard,2);
		assertEquals(2,field.countMonsters());
	}
	
	@Test
	public void sendToGraveyardMonster(){
		int graveSize = field.countGraveCards();
		field.summonMonster(monsterCard);
		assertEquals(1,field.countMonsters());
		field.summonMonster(monsterCard,2);
		assertEquals(2,field.countMonsters());
		field.sendToGraveyard(monsterCard);
		assertEquals(1,field.countMonsters());
		field.sendToGraveyard(2);
		assertEquals(0,field.countMonsters());
		assertEquals(graveSize+2,field.countGraveCards());
	}
	
	public void setCardsOnMonsterField(){
		field.setMonster(monsterCard);
		field.setMonster(monsterFusionCard);
		field.setMonster(monsterEffectCard);
		field.setMonster(monsterCard);
		field.setMonster(monsterFusionCard);
		assertEquals(5,field.countMonsters());
	}
	
	@Test
	public void returnToHandMonster(){
		setCardsOnMonsterField();

		int zoneSize = field.countMonsters();
		int handSize = field.countHandCards();
		field.returnToHand(monsterCard);
		field.returnToHand(monsterEffectCard);

		assertEquals(zoneSize-2, field.countMonsters());
		assertEquals(handSize+2, field.countHandCards());
	}
	
//	@Test
//	public void returnToDeckMonster(){
//		field.returnToDeck(monsterCard);
//		field.returnToDeck(2);
//	}
//	@Test
//	public void removeFromPlayMonster(){
//		field.removeFromPlay(monsterCard);
//		field.removeFromPlay(2);
//	}
	
}