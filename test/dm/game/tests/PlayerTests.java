package dm.game.tests;

import static org.junit.Assert.assertEquals;

import java.awt.Image;

import org.junit.Before;
import org.junit.Test;

import dm.cards.MonsterNormalCard;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;

public class PlayerTests {

	private Player player;
	
	private static final String name = "Seto Kaiba";
	private static final Image avatar = null;
	private NormalDeck deck = new NormalDeck();
	private final ExtraDeck extraDeck = new ExtraDeck();
	
	@Before
	public void initPlayers() {
		initDeck();
		player = new Player(name,avatar,deck,extraDeck);
		assertEquals(name,player.getName());
		assertEquals(avatar,player.getAvatar());
		assertEquals(deck,player.getDeck());
		assertEquals(extraDeck,player.getExtraDeck());
	}
	
	private void initDeck() {
		deck = new NormalDeck();
		for(int i=0;i<40;i++)
			deck.putCard(new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",null,MonsterType.SPELLCASTER,MonsterAttribute.DARK,2500,2100,0,3));
	}

	@Test
	public void startDuel(){
		int deckSize = player.countDeckCards();
		player.firstDraw();
		assertEquals(5,player.countHandCards());
		assertEquals(deckSize -5,player.countDeckCards());
	}
	
	@Test
	public void setLp(){
		int lp = player.getLP();
		player.increaseLp(100);
		assertEquals(lp+100,player.getLP());
		player.setLp(8000);
		assertEquals(8000,player.getLP());
		player.decreaseLp(100);
		assertEquals(lp-100,player.getLP());
	}
	
	@Test
	public void DrawPhase(){
		int deckSize = player.countDeckCards();
		int handSize = player.countHandCards();
		player.draw();
		assertEquals(handSize+1,player.countHandCards());
		assertEquals(deckSize -1,player.countDeckCards());
	}
		
//	@Test
//	public void attackAndWin(){
//		MonsterNormalCard card = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",null,MonsterType.SPELLCASTER,MonsterAttribute.DARK,2500,2100,0,3);
//		MonsterNormalCard card2 = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",null,MonsterType.SPELLCASTER,MonsterAttribute.DARK,2000,2100,0,3);
//		player.attack(index_attacking, new Player, index_attacked);
//	}
	
}
