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

/**
 * Classe campo para inserir as cartas Obs: ClasseNonMonsterCard está
 * relacionada às SpellTrapCards O campo possui removedfromplay, graveyard,
 * zones, deck e hand
 * 
 * @author Simão
 */

public class Field {
	private Hand hand;// Mão do jogador
	private Graveyard graveyard;// Cemitério do jogador
	private MonsterZone monsterZone;// Zona de monstros do jogador
	private SpellTrapZone spellTrapZone;// Zone de cartas mágicas ou armadilhas
	private RemoveFromPlay removeFromPlay;// Monstros removidos de jogo
	private NormalDeck deck;// Deck do jogador
	private ExtraDeck extraDeck;// Extra deck do jogador

	// Métodos contrutores
	public Field() {
		this.hand = new Hand(5);
		this.graveyard = new Graveyard(10);
		this.monsterZone = new MonsterZone(2);
		this.spellTrapZone = new SpellTrapZone(2);
		this.removeFromPlay = new RemoveFromPlay(5);
		this.deck = new NormalDeck(50);
		this.extraDeck = new ExtraDeck(10);
	}

	public Field(NormalDeck deck, ExtraDeck extraDeck) {
		this.hand = new Hand();
		this.graveyard = new Graveyard();
		this.monsterZone = new MonsterZone();
		this.spellTrapZone = new SpellTrapZone();
		this.removeFromPlay = new RemoveFromPlay();
		this.deck = deck;
		this.extraDeck = extraDeck;
	}

	/**
	 * Setar uma carta qualquer virada para baixo, nós teremos duas sobrecargas:
	 * uma com os monstros e outra com as armadilhas ou traps.
	 */
	public void setCard(MonsterCard monsterCard, int index) {
		monsterZone.setMonster(monsterCard, index);
	}

	/** Sobrecarga de setCard */
	public void setCard(MonsterCard monsterCard) {
		monsterZone.setMonster(monsterCard);
	}

	/** Sobrecarga de setCard */
	public void setCard(Card spellTrapCard, int index) {
		spellTrapZone.setCard(spellTrapCard, index);
	}

	/** Sobrecarga de setCard */
	public void setCard(Card spellTrapCard) {
		spellTrapZone.setCard(spellTrapCard);
	}

	/** Métodos para summonar um monstro em modo de ataque */
	public void summonMonster(MonsterCard monsterCard, int index) {
		monsterZone.summonMonster(monsterCard, index);
	}

	/** Sobracarda de summonMonster */
	public void summonMonster(MonsterCard monsterCard) {
		monsterZone.summonMonster(monsterCard);
	}

	/**
	 * Métodos para enviar uma carta ao cemitério
	 */
	public void sendToGraveyard(MonsterCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		graveyard.putCard(card);
	}

	public void sendToGraveyard(NonMonsterCard spellTrapCard) {
		Card card = spellTrapZone.remove(spellTrapCard);
		graveyard.putCard(card);
	}

	/**
	 * Método para retornar uma carta à mão
	 * 
	 * @param monsterCard
	 */
	public void returnToHand(MonsterCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		hand.putCard(card);
	}

	/**
	 * Método para retornar uma carta à mão
	 * 
	 * @param nonMonsterCard
	 */
	public void returnToHand(NonMonsterCard monsterCard) {
		Card card = spellTrapZone.remove(monsterCard);
		hand.putCard(card);
	}

	/**
	 * Método para retornar uma carta à mão
	 * 
	 * @param monsterFusionCard
	 */
	public void returnToHand(MonsterFusionCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		extraDeck.putCard((ExtraDeckCard) card);
	}

	/**
	 * Método para retornar uma carta ao deck
	 * 
	 * @param monsterCard
	 */
	public void returnToDeck(MonsterCard monsterCard) {
		NormalDeckCard card = (NormalDeckCard) monsterZone.remove(monsterCard);
		deck.putCard(card);
	}

	/**
	 * Método para retornar uma carta ao deck
	 * 
	 * @param spellCard
	 */
	public void returnToDeck(NonMonsterCard spellTrapCard) {
		NormalDeckCard card = (NormalDeckCard) spellTrapZone.remove(spellTrapCard);
		deck.putCard(card);
	}

	/**
	 * Método para retornar uma carta ao deck
	 * 
	 * @param monsterFusionCard
	 */
	public void returnToDeck(MonsterFusionCard monsterFusionCard) {
		ExtraDeckCard card = (ExtraDeckCard) monsterZone.remove(monsterFusionCard);
		extraDeck.putCard(card);
	}

	/**
	 * Métodos para remover de jogo
	 * 
	 * @param monsterCard
	 */
	public void removeFromPlay(MonsterCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		removeFromPlay.putCard(card);
	}

	/**
	 * Métodos para remover de jogo
	 * 
	 * @param spellCard
	 */
	public void removeFromPlay(NonMonsterCard spellCard) {
		Card card = spellTrapZone.remove(spellCard);
		removeFromPlay.putCard(card);
	}

	/**
	 * Métodos de Contagem:
	 * 
	 * @return o número de monstros
	 */
	public int countMonsters() {
		return monsterZone.countCards();
	}

	/**
	 * Métodos de Contagem:
	 * 
	 * @return o número de cartas no deck
	 */
	public int countDeckCards() {
		return deck.size();
	}

	/**
	 * Métodos de Contagem:
	 * 
	 * @return o número de cartas no extra deck
	 */
	public int countExtraDeckCards() {
		return extraDeck.size();
	}

	/**
	 * Métodos de Contagem:
	 * 
	 * @return o número cartas na mão
	 */
	public int countHandCards() {
		return hand.size();
	}

	/**
	 * Métodos de Contagem:
	 * 
	 * @return o número de cartas no cemitério
	 */
	public int countGraveCards() {
		return graveyard.size();
	}

	/**
	 * Métodos de Contagem:
	 * 
	 * @return o número de cartas removidas de jogo
	 */
	public int countRemovedFromPlayCards() {
		return removeFromPlay.size();
	}

	/**
	 * Métodos de Contagem:
	 * 
	 * @return o número de cartas mágicas e armadilha
	 */
	public int countNonMonsters() {
		return spellTrapZone.countCards();
	}

	/**
	 * Método de Contagem:
	 * 
	 * @return o deck que está no campo
	 */
	public NormalDeck getDeck() {
		NormalDeck deck = this.deck;
		return deck;
	}

	/** @return o extra deck do campo */
	public ExtraDeck getExtraDeck() {
		ExtraDeck extraDeck = this.extraDeck;
		return extraDeck;
	}

	/** Método de sacar uma carta */
	public void draw() {
		NormalDeckCard card = deck.drawCard();
		hand.putCard((Card) card);
	}

	public MonsterCard getMonsterCard(int index) {
		return (MonsterCard) monsterZone.getCard(index);
	}

	public NonMonsterCard getNonMonsterCard(int index) {
		return (NonMonsterCard) spellTrapZone.getCard(index);
	}

	/** Retorna o índice do monstro, se existir. */
	public int getMonsterCardIndex(Card card) throws CardNotFoundException {
		return monsterZone.getCardIndex(card);
	}

	/** Muda a carta para o modo de defesa */
	public void changeToDefense(MonsterCard monsterCard) {
		monsterZone.changeToDefense(monsterCard);

	}

	/** Retorna o índice do monstro, se existir. */
	public int getNonMonsterCardIndex(Card card) throws CardNotFoundException {
		return spellTrapZone.getCardIndex(card);
	}

	/** Muda a carta para o modo de ataque */
	public void changeToAttack(MonsterCard monsterCard) {
		monsterZone.changeToAttack(monsterCard);

	}

}
