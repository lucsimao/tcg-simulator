package dm.cards.abstracts;

import java.lang.reflect.Array;
import java.util.ArrayList;

import dm.cards.Effect;
import dm.constants.CardType;
import dm.constants.ColorPicture;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;

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

	private MonsterType type;
	private MonsterAttribute atribute;
	private int level;
	private int originalAttack;
	private int originalDefense;
	private int currentAttack;
	private int currentDefense;
	private int attacks_count;
	private int max_attacks;
	private int tributes_needed;
	private ArrayList<MonsterCard> tributed_monsters;
	
	public MonsterCard(String name, String description, ColorPicture normal, String picture, MonsterType spellcaster, MonsterAttribute dark,int level,
			int originalAttack, int originalDeffense, Effect effect, int copies_number) {
		super(name, description, CardType.MONSTER, normal, picture, effect, copies_number);
		this.level = level;
		this.type = spellcaster;
		this.atribute = dark;
		this.originalAttack = originalAttack;
		this.originalDefense = originalDeffense;
		this.currentAttack = originalAttack;
		this.currentDefense = originalDeffense;
		this.attacks_count = 0;
		this.setMaxAttacks(1);
		if(level<5)
			tributes_needed =0;
		else if(level <7)
			tributes_needed = 1;
		else 
			tributes_needed = 2;
		tributed_monsters = new ArrayList<>();
	}	
	
	public void setMaxAttacks(int max_attacks) {
		this.max_attacks = max_attacks;
	}

//	public boolean canAttack() {
//		return max_attacks > attacks_count;
//	}
	
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

	/**
	 * Identifica se um monstro vai poder ser invocado, ou seja, tem os sacrifícios necessários
	 */
	public boolean canBeSummoned() {
		return tributed_monsters.size() == tributes_needed;
	}
	
	public void resetStatus() {
		resetAttacksCount();
		resetTributedMonsters();
	}
	
	public void resetTributedMonsters() {
		this.tributed_monsters = new ArrayList<>();		
	}

	// Getters and Setters
	public MonsterType getType() {
		return type;
	}

	public void setType(MonsterType type) {
		this.type = type;
	}

	public MonsterAttribute getAtribute() {
		return atribute;
	}

	public void setAtribute(MonsterAttribute atribute) {
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

	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void addTributedMonster(MonsterCard monsterCard) {
		this.tributed_monsters.add(monsterCard);
	}
	
	public ArrayList<MonsterCard> getTributedMonsters() {
		return new ArrayList<>(this.tributed_monsters);
	}
	
	public void setTributesNeeded(int tributes_needed) {
		this.tributes_needed = tributes_needed;
	}
	
	public int getTributesNeeded() {
		return this.tributes_needed;
	}

	public int getMaxAttacks() {
		return this.max_attacks;
	}

	
}
