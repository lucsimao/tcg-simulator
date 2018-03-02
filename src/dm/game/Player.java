package dm.game;

import java.awt.Image;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.MonsterCard;
import dm.cards.abstracts.NonMonsterCard;
import dm.constants.CardState;
import dm.exceptions.CardNotFoundException;
import dm.exceptions.InvalidDeckException;
import dm.exceptions.InvalidTextAttributeException;
import dm.fields.Field;
import dm.fields.elements.Hand;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.fields.elements.zones.CardZone;

/**
 * Classe jogador. Ela possui os pontos de vida do jogados e gerencia o campo do
 * jogador.
 * 
 * @author Simão
 */
public class Player {

	private static final int INIT_LIFE_POINTS = 8000;
	private static final int NUMBER_INITIAL_HAND = 5;

	private Field field;
	private int lp;
	private Image avatar;
	private String name;

	public Player(String name, Image avatar, NormalDeck deck, ExtraDeck extraDeck) {
		lp = INIT_LIFE_POINTS;
		if(name == null|| name.equals(""))
			throw new InvalidTextAttributeException("Name cannot be empty");
		else
			this.name = name;
		this.avatar = avatar;
		if(deck==null||!deck.isPlayable()){
			throw new InvalidDeckException("Deck is not valid");
		}
		this.field = new Field(deck, extraDeck);
	}

	public Player(String name, NormalDeck deck) {
		this(name, null, deck, new ExtraDeck(10));
	}

	public void firstDraw() {
		for (int i = 0; i < NUMBER_INITIAL_HAND; i++) {
			field.draw();
		}
	}

	
	public void draw() {
		field.draw();
	}

	/* Métodos de contagem */
	public int countHandCards() {
		return field.countHandCards();
	}

	public int countDeckCards() {
		return field.countDeckCards();
	}

	/* Métodos de getters and setters */
	public Image getAvatar() {
		return avatar;
	}

	public void setAvatar(Image avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NormalDeck getDeck() {
		return field.getDeck();
	}

	public ExtraDeck getExtraDeck() {
		return field.getExtraDeck();
	}

	public int getLP() {
		return this.lp;
	}

	public void increaseLp(int increment) {
		this.lp += increment;
	}

	public void decreaseLp(int decrement) {
		if(lp-decrement<0)
		{
			lp=0;
		}
		else
			this.lp -= decrement;
	}

	public void setLp(int lp) {
		this.lp = lp;
	}

	public void attack(int index_attacking, Player player, int index_attacked) {

		MonsterCard attacking = getMonsterCard(index_attacking);
		MonsterCard attacked = player.getMonsterCard(index_attacked);
		attacking.incrementAttacksCount();
		if (attacked.getState() == CardState.FACE_UP_DEFENSE_POS || attacked.getState() == CardState.FACE_DOWN) {
			attacked.setState(CardState.FACE_UP_DEFENSE_POS);
			if (attacking.getCurrentAttack() > attacked.getCurrentDefense()) {
				player.destroy(attacked);
//				System.out.println("MESSAGE: " + player.getField().getGraveyard().top().getName());
			} else if (attacking.getCurrentAttack() < attacked.getCurrentDefense())
				decreaseLp(attacked.getCurrentDefense() - attacking.getCurrentAttack());
		} else if (attacked.getState() == CardState.FACE_UP_ATTACK) {
			if (attacking.getCurrentAttack() > attacked.getCurrentAttack()) {
				player.destroy(attacked);
				player.decreaseLp(attacking.getCurrentAttack() - attacked.getCurrentAttack());
			} else if (attacking.getCurrentAttack() < attacked.getCurrentAttack()) {
				decreaseLp(attacked.getCurrentAttack() - attacking.getCurrentAttack());
				destroy(attacking);
			} else {
				destroy(attacking);
				player.destroy(attacked);
			}
		}
	}

	public void destroy(MonsterCard monsterCard) {
		field.sendToGraveyard(monsterCard);
	}

	public void destroy(NonMonsterCard spellTrapCard) {
		field.sendToGraveyard(spellTrapCard);
	}

	public void attack(MonsterCard attacking, Player player_attacked, MonsterCard attacked) throws CardNotFoundException {
		attack(field.getMonsterCardIndex(attacking), player_attacked, player_attacked.getMonsterCardIndex(attacked));
	}

	public int getMonsterCardIndex(MonsterCard card) {
		return field.getMonsterCardIndex(card);
	}

	public MonsterCard getMonsterCard(int index) {
		return field.getMonsterCard(index);
	}

	public void summon(MonsterCard monsterCard) {
		field.summonMonster(monsterCard);
		field.getHand().remove(monsterCard);
	}
	public void activate(NonMonsterCard nonMonsterCard) {
		field.activate(nonMonsterCard);
		field.getHand().remove(nonMonsterCard);
	}

	public void set(MonsterCard monsterCard) {
		field.setCard(monsterCard);
		field.getHand().remove(monsterCard);
	}

	public void set(NonMonsterCard nonMonsterCard) {
		field.setCard(nonMonsterCard);
		field.getHand().remove(nonMonsterCard);
	}

	public void changeToDefense(MonsterCard monsterCard) {
		field.changeToDefense(monsterCard);

	}

	public void changeToAttack(MonsterCard monsterCard) {
		field.changeToAttack(monsterCard);

	}

	public Field getField() {
		return this.field;
	}

	public NonMonsterCard getNonMonsterCard(int index) {
		return field.getNonMonsterCard(index);
	}

	public void shuffleDeck() {
		this.getDeck().shuffle();
		
	}

	public CardZone getMonsterZone() {
		return field.getMonsterZone();
	}
	
	public CardZone getSpellTrapZone() {
		return field.getSpellTrapZone();
	}

	public Hand getHand() {
		return  this.getField().getHand();
	}
}
