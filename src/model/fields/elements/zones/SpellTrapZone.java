package model.fields.elements.zones;

import config.constants.CardState;
import config.exceptions.CardNotFoundException;
import config.exceptions.NoEffectException;
import config.exceptions.ZoneOccupedException;
import model.cards.SpellCard;
import model.cards.abstracts.Card;
import model.cards.abstracts.NonMonsterCard;

/**
 * Zona de cartas mágicas, ela é uma zona que aceita cartas mágicas. Pode-se
 * setar ou ativar cartas nela.
 * 
 * @author Simão
 */

public class SpellTrapZone extends CardZone {

	public SpellTrapZone(int number) {
		for (int i = 0; i < number; i++) {
			try {
				NonMonsterCard c = new SpellCard();
				c.setState(CardState.FACE_UP_ATTACK);
				putCard(c, i);
			} catch (NoEffectException e) {
				e.printStackTrace();
			}
		}
	}

	public SpellTrapZone() {
		super();
	}

	public void setCard(Card spellTrapCard, int index) {
		putCard(spellTrapCard, index);
		spellTrapCard.setState(CardState.FACE_DOWN);
	}

	public void activateCard(Card spellTrapCard) {
		for (int i = 0; i < 5; i++)
			if (this.getCards()[i] == null) {
				this.getCards()[i] = spellTrapCard;
				spellTrapCard.setState(CardState.FACE_UP_ATTACK);
				return;
			}
		throw new ZoneOccupedException("We can't add more cards in the field");
	}
	
	public void setCard(Card spellTrapCard) {
		for (int i = 0; i < 5; i++)
			if (this.getCards()[i] == null) {
				this.getCards()[i] = spellTrapCard;
				spellTrapCard.setState(CardState.FACE_DOWN);
				return;
			}
		throw new ZoneOccupedException("We can't add more cards in the field");
	}

	public Card remove(NonMonsterCard spellTrapCard) {
		for (int i = 0; i < ZONE_SIZE; i++)
			if (spellTrapCard.equals(getCards()[i])) {
				return remove(i);
			}
		throw new CardNotFoundException("Card was not found");
	}
}
