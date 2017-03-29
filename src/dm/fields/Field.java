package dm.fields;

import dm.cards.DuelMonsterCard;
import dm.fields.elements.DuelExtraDeck;
import dm.fields.elements.DuelNormalDeck;
import dm.fields.elements.Graveyard;
import dm.fields.elements.MonsterZone;
import dm.fields.elements.RemoveFromPlay;
import dm.fields.elements.SpellTrapZone;

public class Field {
	
	private DuelHand hand;
	private Graveyard graveyard;
	private MonsterZone monsterZone;
	private SpellTrapZone spellTrapZone;
	private RemoveFromPlay removeFromPlay;
	private DuelNormalDeck deck;
	private DuelExtraDeck extraDeck;
	
	public Field(){
		this.graveyard = new Graveyard();
		this.monsterZone = new MonsterZone();
		this.spellTrapZone = new SpellTrapZone();
		this.removeFromPlay = new RemoveFromPlay();
		this.hand = new DuelHand();
		this.deck = new DuelNormalDeck();
		this.extraDeck = new DuelExtraDeck();
	}
	
	public Field(DuelNormalDeck deck, DuelExtraDeck extraDeck){
		this.graveyard = new Graveyard();
		this.monsterZone = new MonsterZone();
		this.spellTrapZone = new SpellTrapZone();
		this.removeFromPlay = new RemoveFromPlay();
		this.hand = new DuelHand();
		this.deck = deck;
		this.extraDeck = extraDeck;
	}
	
	public int countMonsters() {
		return monsterZone.countCards();
	}
	
	public void setMonster(DuelMonsterCard monsterCard, int index) {
		monsterZone.setMonster(monsterCard,index);
	}

	public void setMonster(DuelMonsterCard monsterCard) {
		monsterZone.setMonster(monsterCard);
	}

	public void summonMonster(DuelMonsterCard monsterCard) {
		monsterZone.summonMonster(monsterCard);		
	}

	public void summonMonster(DuelMonsterCard monsterCard, int index) {
		monsterZone.summonMonster(monsterCard,index);		
	}
	
//	public void summonMonster(DuelMonsterCard monsterCard) {
//		monsterZone.summonMonster(monsterCard);
//	}
//
//
//	public void summonMonster(DuelMonsterCard monsterCard, int index) {
//		monsterZone.summonMonster(monsterCard,index);
//	}
//
//

//
//
//	public void sendToGraveyard(DuelMonsterCard monsterCard) {
//		monsterZone.remove(monsterCard);		
//		graveyard.putCard(monsterCard);
//	}
}
