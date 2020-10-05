package dm.tests.phases;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.MonsterCard;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.exceptions.MonsterCannotAttackException;
import dm.game.phases.BattlePhase;
import dm.interfaces.NormalDeckCard;

public class BattlePhaseTests extends PhaseTests {

	private BattlePhase battlePhase;
	private MonsterCard monsterCard;
	private MonsterCard monsterCard2;
	
	@Before
	public void initBattlePhase() {
		battlePhase = new BattlePhase(player);
		monsterCard = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 3, 2500, 2100, 3);
		monsterCard2 = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 3, 2100, 2100, 3);
		player.getDeck().putCard((NormalDeckCard) monsterCard);
		player2.getDeck().putCard((NormalDeckCard) monsterCard2);
	}
	
	@Test
	public void attackAMonsterInAttack() {
		player.bringCardToHand((NormalDeckCard) monsterCard);
		player2.bringCardToHand((NormalDeckCard) monsterCard2);
		player.summon(monsterCard);
		player2.summon(monsterCard2);
		assertEquals(1 , player2.getField().countMonsters());
		battlePhase.attack(monsterCard, player2, monsterCard2);
		assertEquals(1 , player.getField().countMonsters());
		assertEquals(0 , player2.getField().countMonsters());
		assertEquals(8000 - (monsterCard.getCurrentAttack() - monsterCard2.getCurrentAttack()), player2.getLP());
	}
	
	@Test
	public void attackAMonsterInDefense() {
		player.bringCardToHand((NormalDeckCard) monsterCard);
		player2.bringCardToHand((NormalDeckCard) monsterCard2);
		player.summon(monsterCard);
		player2.set(monsterCard2);
		assertEquals(1 , player2.getField().countMonsters());
		battlePhase.attack(monsterCard, player2, monsterCard2);
		assertEquals(0 , player2.getField().countMonsters());
		assertEquals(8000, player2.getLP());
	}
	
	@Test
	public void attackAMonsterAttackMorePowerful() {
		player.bringCardToHand((NormalDeckCard) monsterCard);
		player2.bringCardToHand((NormalDeckCard) monsterCard2);
		player.summon(monsterCard);
		player2.summon(monsterCard2);
		monsterCard2.setCurrentAttack(2800);
		assertEquals(1 , player2.getField().countMonsters());
		battlePhase.attack(monsterCard, player2, monsterCard2);
		assertEquals(0 , player.getField().countMonsters());
		assertEquals(1 , player2.getField().countMonsters());
		assertEquals(8000 + (monsterCard.getCurrentAttack() - monsterCard2.getCurrentAttack()), player.getLP());
		assertEquals(8000, player2.getLP());
	}
	
	@Test
	public void attackAMonsterDefenseMorePowerful() {
		player.bringCardToHand((NormalDeckCard) monsterCard);
		player2.bringCardToHand((NormalDeckCard) monsterCard2);
		player.summon(monsterCard);
		player2.set(monsterCard2);
		monsterCard2.setCurrentDefense(2800);
		assertEquals(1 , player2.getField().countMonsters());
		battlePhase.attack(monsterCard, player2, monsterCard2);
		assertEquals(1 , player.getField().countMonsters());
		assertEquals(1 , player2.getField().countMonsters());
		assertEquals(8000 + (monsterCard.getCurrentAttack() - monsterCard2.getCurrentDefense()), player.getLP());
		assertEquals(8000, player2.getLP());
	}
	
	@Test 
	public void attackWithEachMonster() {
		//TODO
	}
	
	@Test (expected = MonsterCannotAttackException.class)
	public void tryToAttackTwiceIfMonsterCant() {
		player.bringCardToHand((NormalDeckCard) monsterCard);
		player2.bringCardToHand((NormalDeckCard) monsterCard2);
		player.summon(monsterCard);
		player2.set(monsterCard2);
		monsterCard2.setCurrentDefense(2800);
		assertEquals(1 , player2.getField().countMonsters());
		battlePhase.attack(monsterCard, player2, monsterCard2);
		assertEquals(1 , player.getField().countMonsters());
		assertEquals(1 , player2.getField().countMonsters());
		assertEquals(8000 + (monsterCard.getCurrentAttack() - monsterCard2.getCurrentDefense()), player.getLP());
		assertEquals(8000, player2.getLP());
		battlePhase.attack(monsterCard, player2, monsterCard2);
	}
	
	@Test 
	public void attackTwiceIfMonsterCan() {
		player.bringCardToHand((NormalDeckCard) monsterCard);
		player2.bringCardToHand((NormalDeckCard) monsterCard2);
		player.summon(monsterCard);
		player2.set(monsterCard2);
		monsterCard2.setCurrentDefense(2800);
		monsterCard.setMaxAttacks(2);
		assertEquals(1 , player2.getField().countMonsters());
		battlePhase.attack(monsterCard, player2, monsterCard2);
		assertEquals(1 , player.getField().countMonsters());
		assertEquals(1 , player2.getField().countMonsters());
		assertEquals(8000 + (monsterCard.getCurrentAttack() - monsterCard2.getCurrentDefense()), player.getLP());
		assertEquals(8000, player2.getLP());
		battlePhase.attack(monsterCard, player2, monsterCard2);
	}
	
	@Test (expected = MonsterCannotAttackException.class)
	public void attackIfMonsterCantAttack() {
		player.bringCardToHand((NormalDeckCard) monsterCard);
		player2.bringCardToHand((NormalDeckCard) monsterCard2);
		player.summon(monsterCard);
		player2.set(monsterCard2);
		monsterCard2.setCurrentDefense(2800);
		monsterCard.setMaxAttacks(0);
		assertEquals(1 , player2.getField().countMonsters());
		battlePhase.attack(monsterCard, player2, monsterCard2);
		assertEquals(1 , player.getField().countMonsters());
		assertEquals(1 , player2.getField().countMonsters());
		assertEquals(8000 + (monsterCard.getCurrentAttack() - monsterCard2.getCurrentDefense()), player.getLP());
		assertEquals(8000, player2.getLP());
		battlePhase.attack(monsterCard, player2, monsterCard2);
	}
	
}
