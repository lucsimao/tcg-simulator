package model.fields.elements.zones;

import config.constants.CardState;
import config.constants.RulesConstants;
import config.exceptions.CardNotFoundException;
import config.exceptions.ZoneOccupedException;
import model.cards.abstracts.Card;

/**
 * Zona de Cartas, super classe que contém as cartas em campo. Existem Zonas de
 * cartas armadilhas e mágicas.@author Simão
 */

public class CardZone {
	private Card[] cards;
	protected static final int ZONE_SIZE = RulesConstants.ZONE_SIZE;

	public CardZone() {
		cards = new Card[ZONE_SIZE];
	}

	protected Card[] getCards() {
		return cards;
	}

	public Card getCard(int index) {
		if (cards[index] != null)
			return cards[index];
		else
			throw new CardNotFoundException("Card is not found in this deck");
	}

	public int getCardIndex(Card card) throws CardNotFoundException {
		for (int i = 0; i < ZONE_SIZE; i++) {
			if (cards[i] != null && cards[i].equals(card))
				return i;
		}
		throw new CardNotFoundException("This card does not exist on the field. " + card.getName() );
	}

	public void putCard(Card card, int index) {
		if (cards[index] == null)
			cards[index] = card;
		else
			throw new ZoneOccupedException("The zone that you want to put a card is already in use");
	}

	public Card remove(int index) {
		Card card;
		if (cards[index] != null) {
			card = cards[index];
			card.setState(CardState.NONE);
			cards[index] = null;
			return card;
		} else
			throw new CardNotFoundException("Index does not has a card attached on it.");
	}

	public void removeAll() {
		for (int i = 0; i < ZONE_SIZE; i++)
			cards[i] = null;
	}

	public int countCards() {
		int number = 0;
		for (int i = 0; i < ZONE_SIZE; i++) {
			if (cards[i] != null)
				number++;
		}
		return number;
	}

}
