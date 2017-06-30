package dm.fields.elements.zones;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.cards.abstracts.MonsterCard;
import dm.constants.CardState;
import dm.exceptions.CardNotFoundException;
import dm.exceptions.ZoneOccupedException;

/**
 * Zona de Monstros. Ela é uma zona de cartas que somente aceita monstros. Logo,
 * ela pode special summon, normal summon, set ou mudar para o modo de
 * defesa.@author Simão
 */

public class MonsterZone extends CardZone {

	public MonsterZone(int number) {
		for (int i = 0; i < number; i++) {
			MonsterCard c = new MonsterNormalCard();
			c.setState(CardState.FACE_UP_ATTACK);
			putCard(c, i);
		}
	}

	public MonsterZone() {
		super();
	}

	public void setMonster(MonsterCard monsterCard, int index) {
		putCard(monsterCard, index);
		monsterCard.setState(CardState.FACE_DOWN);
	}

	public void setMonster(MonsterCard monsterCard) {
		for (int i = 0; i < 5; i++)
			if (this.getCards()[i] == null) {
				this.getCards()[i] = monsterCard;
				monsterCard.setState(CardState.FACE_DOWN);
				return;
			}
		throw new ZoneOccupedException("We can't add more cards in the field");
	}

	public void summonMonster(MonsterCard monsterCard, int index) {
		putCard(monsterCard, index);
		monsterCard.setState(CardState.FACE_UP_ATTACK);
	}

	public void summonMonster(MonsterCard monsterCard) {
		for (int i = 0; i < 5; i++)
			if (this.getCards()[i] == null) {
				this.getCards()[i] = monsterCard;
				monsterCard.setState(CardState.FACE_UP_ATTACK);
				return;
			}
		throw new ZoneOccupedException("We can't add more cards in the field");
	}

	public Card remove(MonsterCard monsterCard) {
		for (int i = 0; i < ZONE_SIZE; i++) {
			System.out.println(i + " MONSTER: " + getCards()[i] + " M " + monsterCard.getName());
			if (monsterCard.equals(getCards()[i])) {
				System.out.println("Igual " + i);
				return remove(i);
			}
		}

		throw new CardNotFoundException("Card was not found");
	}

	public void changeToDefense(MonsterCard monsterCard) {
		for (Card card : this.getCards()) {
			if (monsterCard.equals(card)) {
				monsterCard.setState(CardState.FACE_UP_DEFENSE_POS);
			}
		}
	}

	public void changeToAttack(MonsterCard monsterCard) {
		for (Card card : this.getCards()) {
			if (monsterCard.equals(card)) {
				monsterCard.setState(CardState.FACE_UP_ATTACK);
			}
		}
	}

}
