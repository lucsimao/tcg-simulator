package dm.fields;

import dm.cards.MonsterFusionCard;
import dm.cards.abstracts.Card;
import dm.cards.abstracts.MonsterCard;
import dm.cards.abstracts.NonMonsterCard;
import dm.constants.RulesConstants;
import dm.exceptions.CardNotFoundException;
import dm.exceptions.MonsterCannotBeSummonedException;
import dm.fields.elements.Graveyard;
import dm.fields.elements.Hand;
import dm.fields.elements.RemoveFromPlay;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.fields.elements.zones.CardZone;
import dm.fields.elements.zones.MonsterZone;
import dm.fields.elements.zones.SpellTrapZone;
import dm.interfaces.ExtraDeckCard;
import dm.interfaces.NormalDeckCard;

/**
 * Classe campo para inserir as cartas Obs: ClasseNonMonsterCard estï¿½
 * relacionada ï¿½s SpellTrapCards O campo possui removedfromplay, graveyard,
 * zones, deck e hand
 * 
 * @author Simï¿½o
 */

public class Field {
	private Hand hand;// Mï¿½o do jogador
	private Graveyard graveyard;// Cemitï¿½rio do jogador
	private MonsterZone monsterZone;// Zona de monstros do jogador
	private SpellTrapZone spellTrapZone;// Zone de cartas mï¿½gicas ou armadilhas
	private RemoveFromPlay removeFromPlay;// Monstros removidos de jogo
	private NormalDeck deck;// Deck do jogador
	private ExtraDeck extraDeck;// Extra deck do jogador

	// Mï¿½todos contrutores
	public Field() {
		this.hand = new Hand(5);
		this.graveyard = new Graveyard(20);
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
	 * Setar uma carta qualquer virada para baixo, nï¿½s teremos duas sobrecargas:
	 * uma com os monstros e outra com as armadilhas ou traps.
	 */
	public void setCard(MonsterCard monsterCard, int index) {
		if(monsterCard.canBeSummoned()) {
			monsterZone.setMonster(monsterCard, index);
			monsterCard.resetAttacksCount();
		}else
			throw new MonsterCannotBeSummonedException(monsterCard);
	}

	/** Sobrecarga de setCard */
	public void setCard(MonsterCard monsterCard) {
		if(monsterCard.canBeSummoned()) {
			monsterZone.setMonster(monsterCard);
			monsterCard.resetAttacksCount();
		}else
			throw new MonsterCannotBeSummonedException(monsterCard);
	
	}

	/** Sobrecarga de setCard */
	public void setCard(Card spellTrapCard, int index) {
		spellTrapZone.setCard(spellTrapCard, index);
	}

	/** Sobrecarga de setCard */
	public void setCard(Card spellTrapCard) {
		spellTrapZone.setCard(spellTrapCard);
	}

	/** Mï¿½todos para summonar um monstro em modo de ataque 
	 * @throws MonsterCannotBeSummonedException */
	public void summonMonster(MonsterCard monsterCard, int index) throws MonsterCannotBeSummonedException {
		if(monsterCard.canBeSummoned()) {
			monsterZone.summonMonster(monsterCard, index);
			monsterCard.resetAttacksCount();
		}else
			throw new MonsterCannotBeSummonedException(monsterCard);
	}

	/** Sobracarda de summonMonster 
	 * @throws MonsterCannotBeSummonedException */
	public void summonMonster(MonsterCard monsterCard) throws MonsterCannotBeSummonedException {
		if(monsterCard.canBeSummoned()) {
			monsterZone.summonMonster(monsterCard);
			monsterCard.resetAttacksCount();
		}else
			throw new MonsterCannotBeSummonedException(monsterCard);
	}

	
	public void tributeSummonMonster(MonsterCard monsterCard, MonsterCard... tributes)  throws MonsterCannotBeSummonedException {
		for(MonsterCard tribute : tributes) {
			monsterCard.addTributedMonster(tribute);
		}
		
		if(monsterCard.canBeSummoned()) {
			try {
				for(MonsterCard tribute : tributes) {
					monsterZone.remove(tribute);
				}	
			} catch (Exception e) {
				throw new MonsterCannotBeSummonedException("Tribute is not in the field.");
			}
		
			monsterZone.summonMonster(monsterCard);
			monsterCard.resetStatus();
		}else
				throw new MonsterCannotBeSummonedException("Monsters cannot be Summoned");
	
	}
	
	public void tributeSetMonster(MonsterCard monsterCard, MonsterCard... tributes)  throws MonsterCannotBeSummonedException {
		for(MonsterCard tribute : tributes) {
			monsterCard.addTributedMonster(tribute);
		}
		
		if(monsterCard.canBeSummoned()) {
			try {
				for(MonsterCard tribute : tributes) {
					monsterZone.remove(tribute);
				}	
			} catch (Exception e) {
				throw new MonsterCannotBeSummonedException("Tribute is not in the field.");
			}
		
			monsterZone.setMonster(monsterCard);
			monsterCard.resetStatus();
		}else
				throw new MonsterCannotBeSummonedException("Monsters cannot be Summoned");
	
	}
	
	
	/**
	 * Mï¿½todos para enviar uma carta ao cemitï¿½rio
	 */
	public void sendToGraveyard(MonsterCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		graveyard.putCard(card);
	}

	public void sendToGraveyard(NonMonsterCard spellTrapCard) {
		Card card = spellTrapZone.remove(spellTrapCard);
		graveyard.putCard(card);
	}

	
	public void clearMonstersDestroying() {
		for(int i=0;i<RulesConstants.ZONE_SIZE;i++)
		{
			try {
				monsterZone.remove(i);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}
	
	/**
	 * Mï¿½todo para retornar uma carta ï¿½ mï¿½o
	 * 
	 * @param monsterCard
	 */
	public void returnToHand(MonsterCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		hand.putCard(card);
	}

	/**
	 * Mï¿½todo para retornar uma carta ï¿½ mï¿½o
	 * 
	 * @param nonMonsterCard
	 */
	public void returnToHand(NonMonsterCard monsterCard) {
		Card card = spellTrapZone.remove(monsterCard);
		hand.putCard(card);
	}

	/**
	 * Mï¿½todo para retornar uma carta ï¿½ mï¿½o
	 * 
	 * @param monsterFusionCard
	 */
	public void returnToHand(MonsterFusionCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		extraDeck.putCard((ExtraDeckCard) card);
	}

	/**
	 * Mï¿½todo para retornar uma carta ao deck
	 * 
	 * @param monsterCard
	 */
	public void returnFromFieldToDeck(MonsterCard monsterCard) {
		NormalDeckCard card = (NormalDeckCard) monsterZone.remove(monsterCard);
		deck.putCard(card);
	}

	/**
	 * Método para retornar uma carta ao deck
	 * 
	 * @param monsterCard
	 */
	public void returnFromHandToDeck(MonsterCard monsterCard) {
		NormalDeckCard card = (NormalDeckCard) hand.remove(monsterCard);
		deck.putCard(card);
	}
	
	/**
	 * Método para retornar uma carta ao deck
	 * 
	 * @param spellCard
	 */
	public void returnFromFieldToDeck(NonMonsterCard spellTrapCard) {
		NormalDeckCard card = (NormalDeckCard) spellTrapZone.remove(spellTrapCard);
		deck.putCard(card);
	}

	/**
	 * Mï¿½todo para retornar uma carta ao deck
	 * 
	 * @param spellCard
	 */
	public void returnFromHandToDeck(NonMonsterCard spellTrapCard) {
		NormalDeckCard card = (NormalDeckCard) hand.remove(spellTrapCard);
		deck.putCard(card);
	}
	
	/**
	 * Mï¿½todo para retornar uma carta ao deck
	 * 
	 * @param monsterFusionCard
	 */
	public void returnToDeck(MonsterFusionCard monsterFusionCard) {
		ExtraDeckCard card = (ExtraDeckCard) monsterZone.remove(monsterFusionCard);
		extraDeck.putCard(card);
	}

	/**
	 * Mï¿½todos para remover de jogo
	 * 
	 * @param monsterCard
	 */
	public void removeFromPlay(MonsterCard monsterCard) {
		Card card = monsterZone.remove(monsterCard);
		removeFromPlay.putCard(card);
	}

	/**
	 * Mï¿½todos para remover de jogo
	 * 
	 * @param spellCard
	 */
	public void removeFromPlay(NonMonsterCard spellCard) {
		Card card = spellTrapZone.remove(spellCard);
		removeFromPlay.putCard(card);
	}
	
	/**
	 * Mï¿½todos de Contagem:
	 * 
	 * @return o nï¿½mero de monstros
	 */
	public int countMonsters() {
		return monsterZone.countCards();
	}

	/**
	 * Mï¿½todos de Contagem:
	 * 
	 * @return o nï¿½mero de cartas no deck
	 */
	public int countDeckCards() {
		return deck.size();
	}

	/**
	 * Mï¿½todos de Contagem:
	 * 
	 * @return o nï¿½mero de cartas no extra deck
	 */
	public int countExtraDeckCards() {
		return extraDeck.size();
	}

	/**
	 * Mï¿½todos de Contagem:
	 * 
	 * @return o nï¿½mero cartas na mï¿½o
	 */
	public int countHandCards() {
		return hand.size();
	}

	/**
	 * Mï¿½todos de Contagem:
	 * 
	 * @return o nï¿½mero de cartas no cemitï¿½rio
	 */
	public int countGraveCards() {
		return graveyard.size();
	}

	/**
	 * Mï¿½todos de Contagem:
	 * 
	 * @return o nï¿½mero de cartas removidas de jogo
	 */
	public int countRemovedFromPlayCards() {
		return removeFromPlay.size();
	}

	/**
	 * Mï¿½todos de Contagem:
	 * 
	 * @return o nï¿½mero de cartas mï¿½gicas e armadilha
	 */
	public int countNonMonsters() {
		return spellTrapZone.countCards();
	}

	/**
	 * Mï¿½todo de Contagem:
	 * 
	 * @return o deck que estï¿½ no campo
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

	/** Mï¿½todo de sacar uma carta */
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

	/** Retorna o ï¿½ndice do monstro, se existir. */
	public int getMonsterCardIndex(Card card) throws CardNotFoundException {
		return monsterZone.getCardIndex(card);
	}

	/** Muda a carta para o modo de defesa */
	public void changeToDefense(MonsterCard monsterCard) {
		monsterZone.changeToDefense(monsterCard);

	}

	/** Retorna o ï¿½ndice do monstro, se existir. */
	public int getNonMonsterCardIndex(Card card) throws CardNotFoundException {
		return spellTrapZone.getCardIndex(card);
	}

	/** Muda a carta para o modo de ataque */
	public void changeToAttack(MonsterCard monsterCard) {
		monsterZone.changeToAttack(monsterCard);

	}

	public Graveyard getGraveyard() {
		return this.graveyard;
	}

	public Hand getHand() {
		return this.hand;
	}

	public void activate(NonMonsterCard nonMonsterCard) {
		spellTrapZone.activateCard(nonMonsterCard);
		
	}

	public CardZone getMonsterZone() {
		return this.monsterZone;
	}

	public CardZone getSpellTrapZone() {
		return this.spellTrapZone;
	}

	public void clearNonMonstersDestroying() {
		for(int i=0;i<RulesConstants.ZONE_SIZE;i++)
		{
			try {
				spellTrapZone.remove(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
}
