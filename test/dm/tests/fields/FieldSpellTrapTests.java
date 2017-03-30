package dm.tests.fields;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import dm.cards.DuelSpellCard;
import dm.cards.DuelTrapCard;
import dm.cards.Effect;
import dm.constants.SpellType;
import dm.constants.TrapType;
import dm.fields.Field;

public class FieldSpellTrapTests{
	
	private Field field;
	
	private DuelSpellCard spellCard;
	private DuelTrapCard trapCard;


	@Before
	public void initCards(){
		spellCard = new DuelSpellCard("Dark Hole","Destroy all monster on the field",null,new Effect(),SpellType.NORMAL, 3);
		trapCard = new DuelTrapCard("Mirror Force","Destroy all attacking monsters",null,new Effect(),TrapType.NORMAL, 3);
		field = new Field();
	}
	
	@Test
	public void setSpellsAndTraps(){
		field.setCard(spellCard);
		assertEquals(1,field.countNonMonsters());
		field.setCard(trapCard,2);
		assertEquals(2,field.countNonMonsters());
	}
		
	@Test
	public void sendToGraveyardSpellTrap(){
		int graveSize = field.countGraveCards();
		setSpellsAndTraps();
		int spellSize = field.countNonMonsters();
		field.sendToGraveyard(spellCard);
		field.sendToGraveyard(trapCard);
		assertEquals(spellSize-2,field.countNonMonsters());
		assertEquals(graveSize+2,field.countGraveCards());
	}
	
	public void setCardsOnSpellTrapField(){
		field.setCard(spellCard);
		field.setCard(spellCard);
		field.setCard(spellCard);
		field.setCard(trapCard);
		field.setCard(trapCard);
		assertEquals(5,field.countNonMonsters());
	}
		
	@Test
	public void returnToHandSpellTrap(){
		setCardsOnSpellTrapField();

		int zoneSize = field.countNonMonsters();
		int handSize = field.countHandCards();
		field.returnToHand(spellCard);
		field.returnToHand(trapCard);

		assertEquals(zoneSize-2, field.countNonMonsters());
		assertEquals(handSize+2, field.countHandCards());
	}
	
	@Test
	public void returnToDeckMonster(){
		setCardsOnSpellTrapField();
		int zoneSize = field.countNonMonsters();
		int deckSize = field.countDeckCards();
		
		field.returnToDeck(spellCard);
		field.returnToDeck(trapCard);
		field.returnToDeck(spellCard);
		assertEquals(zoneSize - 3, field.countNonMonsters());
		assertEquals(deckSize + 3, field.countDeckCards());
	}
	
	@Test
	public void removeFromPlaySpellTrap(){
		setCardsOnSpellTrapField();
		int zoneSize = field.countNonMonsters();
		int remFPlaySize = field.countRemovedFromPlayCards();
		
		field.removeFromPlay(spellCard);
		field.removeFromPlay(spellCard);
		field.removeFromPlay(trapCard);
		
		assertEquals(zoneSize - 3, field.countNonMonsters());
		assertEquals(remFPlaySize + 3, field.countRemovedFromPlayCards());
	}
	
}