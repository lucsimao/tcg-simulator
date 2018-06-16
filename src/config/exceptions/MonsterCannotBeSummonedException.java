package config.exceptions;

import model.cards.abstracts.MonsterCard;

public class MonsterCannotBeSummonedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MonsterCannotBeSummonedException(MonsterCard monsterCard) {
		super(monsterCard.getName() + " cannot be summoned. \nIt needs " + monsterCard.getTributesNeeded() +  " tributes to be summoned.");
	}

	public MonsterCannotBeSummonedException(String message) {
		super(message);
	}
}
