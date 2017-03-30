package dm.fields;

import cards.ExtraDeckCard;
import cards.NormalDeckCard;
import dm.cards.DuelMonsterFusionCard;
import dm.cards.abstracts.DuelCard;
import dm.cards.abstracts.DuelMonsterCard;
import dm.fields.elements.DuelHand;
import dm.fields.elements.Graveyard;
import dm.fields.elements.RemoveFromPlay;
import dm.fields.elements.decks.DuelExtraDeck;
import dm.fields.elements.decks.DuelNormalDeck;
import dm.fields.elements.zones.MonsterZone;
import dm.fields.elements.zones.SpellTrapZone;

public class Field {

	/**
	 * Classe campo para inserir as cartas
	 * */
	
	private DuelHand hand;//Mão do jogador
	private Graveyard graveyard;//Cemitério do jogador
	private MonsterZone monsterZone;//Zona de monstros do jogador
	private SpellTrapZone spellTrapZone;//Zone de cartas mágicas ou armadilhas
	private RemoveFromPlay removeFromPlay;//Monstros removidos de jogo
	private DuelNormalDeck deck;//Deck do jogador
	private DuelExtraDeck extraDeck;//Extra deck do jogador
	
	//Métodos contrutores
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
	
	//Setar uma carta qualquer virada para baixo, nós teremos duas sobrecargas
	//Uma com os monstros e outra com as armadilhas ou traps.
	public void setCard(DuelMonsterCard monsterCard, int index) {
		monsterZone.setMonster(monsterCard,index);
	}

	public void setCard(DuelMonsterCard monsterCard) {
		monsterZone.setMonster(monsterCard);
	}

	public void setCard(DuelCard spellTrapCard, int index) {
		spellTrapZone.setCard(spellTrapCard,index);
	}
	
	public void setCard(DuelCard spellTrapCard) {
		spellTrapZone.setCard(spellTrapCard);
	}
	
	//Métodos para summonar um monstro em modo de ataque
	public void summonMonster(DuelMonsterCard monsterCard, int index) {
		monsterZone.summonMonster(monsterCard,index);		
	}
	
	public void summonMonster(DuelMonsterCard monsterCard) {
		monsterZone.summonMonster(monsterCard);
	}

	//Métodos para enviar uma carta ao cemitério	
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

	/*Métodos de Contagem:*/
	public int countMonsters() {return monsterZone.countCards();}
	public int countDeckCards() {return deck.size();}
	public int countExtraDeckCards() {return extraDeck.size();}
	public int countHandCards() {return hand.size();}
	public int countGraveCards() {return graveyard.size();}
	/*Métodos de Contagem:*/
}
