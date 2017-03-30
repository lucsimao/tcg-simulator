package dm.fields;

import cards.ExtraDeckCard;
import cards.NormalDeckCard;
import dm.cards.DuelCard;
import dm.cards.DuelMonsterCard;
import dm.cards.DuelMonsterFusionCard;
import dm.fields.elements.DuelExtraDeck;
import dm.fields.elements.DuelHand;
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
		this.hand = new DuelHand();
		this.graveyard = new Graveyard();
		this.monsterZone = new MonsterZone();
		this.spellTrapZone = new SpellTrapZone();
		this.removeFromPlay = new RemoveFromPlay();
		this.deck = new DuelNormalDeck();
		this.extraDeck = new DuelExtraDeck();
	}
	
	public Field(DuelNormalDeck deck, DuelExtraDeck extraDeck){
		this.hand = new DuelHand();
		this.graveyard = new Graveyard();
		this.monsterZone = new MonsterZone();
		this.spellTrapZone = new SpellTrapZone();
		this.removeFromPlay = new RemoveFromPlay();
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

	public void summonMonster(DuelMonsterCard monsterCard, int index) {
		monsterZone.summonMonster(monsterCard,index);		
	}
	
	public void summonMonster(DuelMonsterCard monsterCard) {
		monsterZone.summonMonster(monsterCard);
	}

	public void sendToGraveyard(DuelMonsterCard monsterCard) {
		DuelCard card = monsterZone.remove(monsterCard);		
		graveyard.putCard(card);
	}
	
	public void sendToGraveyard(int index) {
		DuelCard card = monsterZone.remove(index);		
		graveyard.putCard(card);
	}

	public void returnToHand(DuelMonsterCard monsterCard) {
		DuelCard card = monsterZone.remove(monsterCard);
		hand.putCard(card);
	}

	public void returnToHand(int index) {
		DuelCard card = monsterZone.remove(index);		
		hand.putCard(card);
	}

	public int countHandCards() {
		return hand.size();
	}

	public int countGraveCards() {
		return graveyard.size();
	}

	public void returnMonsterToDeck(DuelMonsterCard monsterCard) {
		NormalDeckCard card = (NormalDeckCard) monsterZone.remove((DuelMonsterCard) monsterCard);
		deck.putCard(card);
	}
	
	public void returnMonsterToDeck(DuelMonsterFusionCard monsterCard) {
		ExtraDeckCard card = (ExtraDeckCard ) monsterZone.remove((DuelMonsterCard) monsterCard);
		extraDeck.putCard(card);
	}
	
	public void returnSpellOrTrapToDeck(NormalDeckCard monsterCard) {
		NormalDeckCard card = (NormalDeckCard) spellTrapZone.remove((DuelMonsterCard) monsterCard);
		deck.putCard(card);
	}

	public int countDeckCards() {
		return deck.size();
	}

	public int countExtraDeckCards() {
		return extraDeck.size();
	}
	
}
