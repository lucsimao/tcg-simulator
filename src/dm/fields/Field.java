package dm.fields;

import dm.cards.MonsterFusionCard;
import dm.cards.abstracts.Card;
import dm.cards.abstracts.MonsterCard;
import dm.cards.abstracts.NonMonsterCard;
import dm.exceptions.CardNotFoundException;
import dm.fields.elements.Graveyard;
import dm.fields.elements.Hand;
import dm.fields.elements.RemoveFromPlay;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.fields.elements.zones.MonsterZone;
import dm.fields.elements.zones.SpellTrapZone;
import dm.interfaces.ExtraDeckCard;
import dm.interfaces.NormalDeckCard;

/*From @Simao
 * Classe campo, nele irão conter os elementos e os métodos de gerenciamento dos mesmos.*/

public class Field {

	/**
	 * Classe campo para inserir as cartas
	 * Obs: ClasseNonMonsterCard está relacionada às SpellTrapCards
	 * O campo possui removedfromplay, graveyard, zones, deck e hand
	 * */
	
	private Hand hand;//Mão do jogador
	private Graveyard graveyard;//Cemitério do jogador
	private MonsterZone monsterZone;//Zona de monstros do jogador
	private SpellTrapZone spellTrapZone;//Zone de cartas mágicas ou armadilhas
	private RemoveFromPlay removeFromPlay;//Monstros removidos de jogo
	private NormalDeck deck;//Deck do jogador
	private ExtraDeck extraDeck;//Extra deck do jogador
	
	//Métodos contrutores
	public Field(){
		this.hand = new Hand(5);
		this.graveyard = new Graveyard(10);
		this.monsterZone = new MonsterZone(2);
		this.spellTrapZone = new SpellTrapZone(2);
		this.removeFromPlay = new RemoveFromPlay(5);
		this.deck = new NormalDeck(50);
		this.extraDeck = new ExtraDeck(10);
	}
		
	public Field(NormalDeck deck, ExtraDeck extraDeck){
		this.hand = new Hand();
		this.graveyard = new Graveyard();
		this.monsterZone = new MonsterZone();
		this.spellTrapZone = new SpellTrapZone();
		this.removeFromPlay = new RemoveFromPlay();
		this.deck = deck;
		this.extraDeck = extraDeck;
	}
	
	//Setar uma carta qualquer virada para baixo, nós teremos duas sobrecargas
	//Uma com os monstros e outra com as armadilhas ou traps.
	public void setCard(MonsterCard monsterCard, int index) {
		monsterZone.setMonster(monsterCard,index);
	}

	public void setCard(MonsterCard monsterCard) {
		monsterZone.setMonster(monsterCard);
	}

	public void setCard(Card spellTrapCard, int index) {
		spellTrapZone.setCard(spellTrapCard,index);
	}
	
	public void setCard(Card spellTrapCard) {
		spellTrapZone.setCard(spellTrapCard);
	}
	
	//Métodos para summonar um monstro em modo de ataque
	public void summonMonster(MonsterCard monsterCard, int index) {
		monsterZone.summonMonster(monsterCard,index);		
	}
	
	public void summonMonster(MonsterCard monsterCard) {
		monsterZone.summonMonster(monsterCard);
	}

	//Métodos para enviar uma carta ao cemitério	
	public void sendToGraveyard(MonsterCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);		
		graveyard.putCard(card);
	}
	
	public void sendToGraveyard(NonMonsterCard spellTrapCard) {
		Card card = spellTrapZone.remove(spellTrapCard);		
		graveyard.putCard(card);
	}
	
	//Métodos para enviar uma carta à mão
	public void returnToHand(MonsterCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		hand.putCard(card);
	}

	public void returnToHand(NonMonsterCard monsterCard) {
		Card card = spellTrapZone.remove(monsterCard);
		hand.putCard(card);
	}
	
	public void returnToHand(MonsterFusionCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		extraDeck.putCard((ExtraDeckCard) card);
	}

	//Métodos para enviar uma carta ao deck
	public void returnToDeck(MonsterCard monsterCard) {
		NormalDeckCard card = (NormalDeckCard) monsterZone.remove((MonsterCard) monsterCard);
		deck.putCard(card);
	}
	
	public void returnToDeck(NonMonsterCard spellTrapCard) {
		NormalDeckCard card =  (NormalDeckCard) spellTrapZone.remove(spellTrapCard);
		deck.putCard(card);
	}
	
	public void returnToDeck(MonsterFusionCard monsterCard) {
		ExtraDeckCard card = (ExtraDeckCard ) monsterZone.remove((MonsterCard) monsterCard);
		extraDeck.putCard(card);
	}
	
	/*Métodos para remover de jogo*/
	public void removeFromPlay(MonsterCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		removeFromPlay.putCard(card);
	}
	
	public void removeFromPlay(NonMonsterCard spellCard) {
		Card card = spellTrapZone.remove(spellCard);
		removeFromPlay.putCard(card);
	}
	
	/*Métodos de Contagem:*/
	public int countMonsters() {return monsterZone.countCards();}
	public int countDeckCards() {return deck.size();}
	public int countExtraDeckCards() {return extraDeck.size();}
	public int countHandCards() {return hand.size();}
	public int countGraveCards() {return graveyard.size();}
	public int countRemovedFromPlayCards() {return removeFromPlay.size();}
	public int countNonMonsters() {return spellTrapZone.countCards();}
	/*Métodos de Contagem:*/

	public NormalDeck getDeck() {
		NormalDeck deck = this.deck;
		return deck;
	}
	
	public ExtraDeck getExtraDeck() {
		ExtraDeck extraDeck = this.extraDeck;
		return extraDeck;
	}

	public void draw() {
		NormalDeckCard card = deck.drawCard();
		hand.putCard((Card) card);		
	}

	public MonsterCard getMonsterCard(int index) {
		return (MonsterCard) monsterZone.getCard(index);
	}

	public int getMonsterCardIndex(Card card) throws CardNotFoundException {
		return monsterZone.getCardIndex(card);
	}

	public void changeToDefense(MonsterCard monsterCard) {
		monsterZone.changeToDefense(monsterCard);
		
	}
	
}
