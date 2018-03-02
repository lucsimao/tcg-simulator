package dm.cards.abstracts;

import dm.cards.Effect;
import dm.constants.CardType;

/**
 * Classe Monstro Classe abstrata para tratar os monstros do jogo. Eles possuem
 * níveis, atributos e tipo. Eles possuem ataque, defesa originais e correntes.
 * O ataque e defesa correntes podem ser alterados por cartas de equipamento.
 * Eles também podem voltar o ataque e defesa para o original.
 * 
 * @author Simão
 */

public abstract class MonsterCard extends Card {

	private static final long serialVersionUID = 1160252849178277811L;

	private int type;
	private int atribute;
	private int originalAttack;
	private int originalDefense;
	private int currentAttack;
	private int currentDefense;
	private int attacks_count;
	private int max_attacks;
	
	public MonsterCard(String name, String description, int colorPicture, String picture, int type, int atribute,
			int originalAttack, int originalDeffense, Effect effect, int copies_number) {
		super(name, description, CardType.MONSTER, colorPicture, picture, effect, copies_number);
		this.type = type;
		this.atribute = atribute;
		this.originalAttack = originalAttack;
		this.originalDefense = originalDeffense;
		this.currentAttack = originalAttack;
		this.currentDefense = originalDeffense;
		this.attacks_count = 0;
		this.max_attacks = 1;
	}	
	
	public void setMaxAttacks(int max_attacks) {
		this.max_attacks = max_attacks;
	}

	public boolean canAttack() {
		return max_attacks > attacks_count;
	}
	
	public void incrementAttacksCount() {
		this.attacks_count++;
	}
	
	public void resetAttacksCount(){
		this.attacks_count = 0;
	}
	
	public int getAttacksCount() {
		return this.attacks_count;
	}
	
	/**
	 * Aumenta o ataque em certa quantidade
	 * 
	 * @param increment
	 *            valor a ser incrementado
	 */
	public void increaseAttack(int increment) {
		this.currentAttack += increment;
		if (this.currentAttack > 9999)
			this.currentAttack = 9999;
	}

	/**
	 * Aumenta a defesa em certa quantidade
	 * 
	 * @param increment
	 *            valor a ser incrementado
	 */
	public void increaseDefense(int increment) {
		this.currentDefense += increment;
		if (this.currentDefense > 9999)
			this.currentDefense = 9999;
	}

	/**
	 * Diminui o ataque em certa quantidade
	 * 
	 * @param decrement
	 *            valor a ser decrementado
	 */
	public void decreaseAttack(int decrement) {
		this.currentAttack -= decrement;
		if (this.currentAttack < 0)
			this.currentAttack = 0;
	}

	/**
	 * Diminui a defesa em certa quantidade
	 * 
	 * @param decrement
	 *            valor a ser decrementado
	 */
	public void decreaseDefense(int increment) {
		this.currentDefense -= increment;
		if (this.currentDefense < 0)
			this.currentDefense = 0;
	}

	/**
	 * Quando o ataque de uma carta é alterada, ela é salva em currentAttack.
	 * Mas é possível retornar ao attack original com este método, que atribui o
	 * ataque original ao ataque atual.
	 */
	public void turnBackOriginalAttack() {
		this.currentAttack = this.originalAttack;
	}

	/**
	 * Quando a defesa de uma carta é alterada, ela é salva em currentDefense.
	 * Mas é possível retornar a defesa original com este método, que atribui a
	 * defesa original à defesa atual.
	 */
	public void turnBackOriginalDefense() {
		this.currentDefense = originalDefense;
	}

	// Getters and Setters
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAtribute() {
		return atribute;
	}

	public void setAtribute(int atribute) {
		this.atribute = atribute;
	}

	public int getCurrentAttack() {
		return currentAttack;
	}

	public void setCurrentAttack(int currentAttack) {
		this.currentAttack = currentAttack;
	}

	public int getCurrentDefense() {
		return currentDefense;
	}

	public void setCurrentDefense(int currentDeffense) {
		this.currentDefense = currentDeffense;
	}

	public int getOriginalAttack() {
		return originalAttack;
	}

	public int getOriginalDefense() {
		return originalDefense;
	}

}
