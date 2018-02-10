package dm.fields.elements;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.fields.elements.decks.FieldElement;

/**
 * Classe Mão. Ela pode ter um número máximo de cartas na mão. Possui funções de
 * descatar, adicionar e remover cartas.
 * 
 * @author Simão
 */

public class Hand extends FieldElement<Card> {

	private static final long serialVersionUID = 1L;

	private final int MAX_CARDS = 7;

	private int maxCards;
	private boolean isHandPlayable;

	public Hand() {
		this.maxCards = MAX_CARDS;
		isHandPlayable = true;
	}

	public Hand(int number) {
		super(number, new MonsterNormalCard());
		this.maxCards = MAX_CARDS;
		isHandPlayable = true;

	}

	@Override
	public void putCard(Card card) {
		getCards().push(card);
		if (getCards().size() <= maxCards)
			isHandPlayable = true;
		else
			isHandPlayable = false;
	}

	public Card discard(Card card) {
		if (getCards().size() <= maxCards)
			isHandPlayable = false;
		else
			isHandPlayable = true;
		return remove(card);
	}

	public boolean isHandPlayable() {
		return isHandPlayable;
	}

	public void setMaxCards(int maxCards) {
		this.maxCards = maxCards;
	}

	public int getMaxCards() {
		return this.maxCards;
	}

	public Card discardFirst() {
		if (getCards().size() <= maxCards)
			isHandPlayable = false;
		else
			isHandPlayable = true;
		return removeFromTop();

	}
}
