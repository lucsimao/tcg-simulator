package dm.tests.phases;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import config.constants.MonsterAttribute;
import config.constants.MonsterType;
import config.exceptions.NormalSummonException;
import config.exceptions.SpecialSummonException;
import config.exceptions.TributeSummonException;
import model.cards.MonsterNormalCard;
import model.cards.NormalDeckCard;
import model.cards.abstracts.MonsterCard;
import model.game.Player;
import model.game.phases.MainPhase;

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
		monsterCard = (MonsterCard) p1.getDeck().search(monsterCard.getName());
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
	}
	
	@Test (expected = NormalSummonException.class)
	public void tryTwoNormalSummonFailed() {
		monsterCard = (MonsterCard) p1.getDeck().search(monsterCard.getName());
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
		monsterCard = (MonsterCard) p1.getDeck().search(monsterCard.getName());
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
		monsterCard = new MonsterNormalCard("Dark Magician2", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 3, 2500, 2100, 3);
		deck.putCard((NormalDeckCard) monsterCard);
		monsterCard = (MonsterCard) p1.getDeck().search(monsterCard.getName());
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(1));
	}
	
	@Test
	public void specialSummonSucessfully() {
		monsterCard = (MonsterCard) p1.getDeck().search(monsterCard.getName());
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.specialSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
	}
	
	@Test (expected = SpecialSummonException.class)
	public void specialSummonFailed() {
		mainPhase.setCan_special_summon(false);
		monsterCard = (MonsterCard) p1.getDeck().search(monsterCard.getName());
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.specialSummon(monsterCard);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
	}
	
	@Test
	public void tributeSummonSucessfully() {
		mainPhase.setMax_normal_summon(2);
		monsterCard = (MonsterCard) p1.getDeck().search(monsterCard.getName());
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		MonsterCard tribute1 = monsterCard;
		monsterCard = new MonsterNormalCard("Dark Magician2", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 6, 2500, 2100, 3);
		mainPhase.setCan_special_summon(false);
		deck.putCard((NormalDeckCard) monsterCard);
		monsterCard = (MonsterCard) p1.getDeck().search(monsterCard.getName());
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.tributeSummon(monsterCard,tribute1);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
	}
	
	@Test(expected = TributeSummonException.class)
	public void tributeSummonFailed() {
		monsterCard = (MonsterCard) p1.getDeck().search(monsterCard.getName());
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.normalSummon(monsterCard);
		MonsterCard tribute1 = monsterCard;
		monsterCard = new MonsterNormalCard("Dark Magician2", "The ultimate wizard in terms of attack and defense.",
				null, MonsterType.SPELLCASTER, MonsterAttribute.DARK, 6, 2500, 2100, 3);
		mainPhase.setCan_special_summon(false);
		deck.putCard((NormalDeckCard) monsterCard);
		monsterCard = (MonsterCard) p1.getDeck().search(monsterCard.getName());
		p1.bringCardToHand((NormalDeckCard) monsterCard);
		mainPhase.tributeSummon(monsterCard,tribute1);
		assertEquals(monsterCard, p1.getField().getMonsterCard(0));
	}
	
}
