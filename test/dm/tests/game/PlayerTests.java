package dm.tests.game;

import static org.junit.Assert.assertEquals;

import java.awt.Image;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterNormalCard;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.exceptions.CardNotFoundException;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;

public class PlayerTests {

	private Player player;
	private Player player2;
	private static final String name = "Seto Kaiba";
	private static final Image avatar = null;
	private NormalDeck deck = new NormalDeck();
	private final ExtraDeck extraDeck = new ExtraDeck();

	@Before
	public void initPlayers() {
		initDeck();
		player = new Player(name, avatar, deck, extraDeck);
		player2 = new Player(name, avatar, deck, extraDeck);
		assertEquals(name, player.getName());
		assertEquals(avatar, player.getAvatar());
		assertEquals(deck, player.getDeck());
		assertEquals(extraDeck, player.getExtraDeck());
	}

	private void initDeck() {
		deck = new NormalDeck();
		for (int i = 0; i < 40; i++)
			deck.putCard(new MonsterNormalCard());
	}

	@Test
	public void startDuel() {
		int deckSize = player.countDeckCards();
		player.firstDraw();
		assertEquals(5, player.countHandCards());
		assertEquals(deckSize - 5, player.countDeckCards());
	}

	@Test
	public void setLp() {
		int lp = player.getLP();
		player.increaseLp(100);
		assertEquals(lp + 100, player.getLP());
		player.setLp(8000);
		assertEquals(8000, player.getLP());
		player.decreaseLp(100);
		assertEquals(lp - 100, player.getLP());
	}

	@Test
	public void DrawPhase() {
		int deckSize = player.countDeckCards();
		int handSize = player.countHandCards();
		player.draw();
		assertEquals(handSize + 1, player.countHandCards());
		assertEquals(deckSize - 1, player.countDeckCards());
	}

	@Test(expected = CardNotFoundException.class)
	public void attackAndWin() {
		MonsterNormalCard card = new MonsterNormalCard("Dark Magician",
				"The ultimate wizard in terms of attack and defense.", null, MonsterType.SPELLCASTER,
				MonsterAttribute.DARK, 2500, 2100, 0);
		MonsterNormalCard card2 = new MonsterNormalCard("Dark Magician",
				"The ultimate wizard in terms of attack and defense.", null, MonsterType.SPELLCASTER,
				MonsterAttribute.DARK, 2000, 2100, 0);
		int lp2 = player2.getLP();
		player.summon(card);
		player2.summon(card2);
		player.attack(card, player2, card2);
		assertEquals(lp2 - (card.getCurrentAttack() - card2.getCurrentAttack()), player2.getLP());
		player2.getMonsterCardIndex(card2);
	}

	@Test(expected = CardNotFoundException.class)
	public void attackAndLose() {
		MonsterNormalCard card = new MonsterNormalCard("Dark Magician",
				"The ultimate wizard in terms of attack and defense.", null, MonsterType.SPELLCASTER,
				MonsterAttribute.DARK, 2500, 2100, 0);
		MonsterNormalCard card2 = new MonsterNormalCard("Dark Magician",
				"The ultimate wizard in terms of attack and defense.", null, MonsterType.SPELLCASTER,
				MonsterAttribute.DARK, 2000, 2100, 0);
		int lp = player.getLP();
		player.summon(card2);
		player2.summon(card);
		player.attack(card2, player2, card);
		assertEquals(lp - (card.getCurrentAttack() - card2.getCurrentAttack()), player.getLP());
		player.getMonsterCardIndex(card2);
	}

	@Test(expected = CardNotFoundException.class)
	public void attackADefAndWin() {
		MonsterNormalCard card = new MonsterNormalCard("Dark Magician",
				"The ultimate wizard in terms of attack and defense.", null, MonsterType.SPELLCASTER,
				MonsterAttribute.DARK, 2500, 2100, 0);
		MonsterNormalCard card2 = new MonsterNormalCard("Dark Magician",
				"The ultimate wizard in terms of attack and defense.", null, MonsterType.SPELLCASTER,
				MonsterAttribute.DARK, 2000, 2100, 0);
		int lp2 = player2.getLP();
		player.summon(card);
		player2.summon(card2);
		player2.changeToDefense(card2);
		player.attack(card, player2, card2);
		assertEquals(lp2, player2.getLP());
		player2.getMonsterCardIndex(card2);
	}

	@Test
	public void attackADefAndLose() {
		player.draw();
		player2.draw();
		MonsterNormalCard card = (MonsterNormalCard) player2.getField().getHand().getCardsList().get(0);
		MonsterNormalCard card2 = (MonsterNormalCard) player.getField().getHand().getCardsList().get(0);
		int lp = player.getLP();
		player.summon(card2);
		player2.summon(card);
		player2.changeToDefense(card);
		player.attack(card2, player2, card);
		assertEquals(lp - (card.getCurrentDefense() - card2.getCurrentAttack()), player.getLP());
		player.getMonsterCardIndex(card2);
	}

	@Test
	public void attackASettedAndLose() {
		player.draw();
		player2.draw();
		MonsterNormalCard card = (MonsterNormalCard) player2.getField().getHand().getCardsList().get(0);
		MonsterNormalCard card2 = (MonsterNormalCard) player.getField().getHand().getCardsList().get(0);
		int lp = player.getLP();
		player.summon(card2);
		player2.set(card);
		player2.changeToDefense(card);
		player.attack(card2, player2, card);
		assertEquals(lp - (card.getCurrentDefense() - card2.getCurrentAttack()), player.getLP());
		player.getMonsterCardIndex(card2);
	}

}
