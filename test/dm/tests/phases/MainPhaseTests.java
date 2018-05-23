package dm.tests.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.MonsterCard;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.exceptions.NormalSummonException;
import dm.exceptions.SpecialSummonException;
import dm.exceptions.TributeSummonException;
import dm.game.Player;
import dm.game.phases.MainPhase;
import dm.interfaces.NormalDeckCard;

public class MainPhaseTests extends PhaseTests {

	private MainPhase mainPhase;
	private MonsterCard monsterCard;
	private Player p1;
	@Before
	public void initMainPhase() {
		mainPhase = new MainPhase(player);
		monsterCard = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 3, 2500, 2100, 3);
		deck.putCard((NormalDeckCard) monsterCard);
		p1 = mainPhase.getPlayer();
	}
	
	@Test
	public void normalSummonSucessfully() {
		
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
	}
	
	@Test (expected = NormalSummonException.class)
	public void tryTwoNormalSummonFailed() {
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
		monsterCard = new MonsterNormalCard("Dark Magician2", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 3, 2500, 2100, 3);
		deck.putCard((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(1));
	}
	
	@Test
	public void twoNormalSummonIfPlayerCan() {
		mainPhase.setMax_normal_summon(2);
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
		monsterCard = new MonsterNormalCard("Dark Magician2", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 3, 2500, 2100, 3);
		deck.putCard((NormalDeckCard) monsterCard);
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(1));
	}
	
	@Test
	public void specialSummonSucessfully() {
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.specialSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
	}
	
	@Test (expected = SpecialSummonException.class)
	public void specialSummonFailed() {
		mainPhase.setCan_special_summon(false);
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.specialSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
	}
	
	@Test
	public void tributeSummonSucessfully() {
		mainPhase.setMax_normal_summon(2);
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		MonsterCard tribute1 = monsterCard;
		monsterCard = new MonsterNormalCard("Dark Magician2", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 6, 2500, 2100, 3);
		mainPhase.setCan_special_summon(false);
		deck.putCard((NormalDeckCard) monsterCard);
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.tributeSummon(monsterCard,tribute1);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
	}
	
	@Test(expected = TributeSummonException.class)
	public void tributeSummonFailed() {
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		MonsterCard tribute1 = monsterCard;
		monsterCard = new MonsterNormalCard("Dark Magician2", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 6, 2500, 2100, 3);
		mainPhase.setCan_special_summon(false);
		deck.putCard((NormalDeckCard) monsterCard);
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.tributeSummon(monsterCard,tribute1);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
	}
	
}
