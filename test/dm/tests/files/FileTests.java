package dm.tests.files;

import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dm.cards.Effect;
import dm.cards.MonsterEffectCard;
import dm.cards.MonsterNormalCard;
import dm.cards.SpellCard;
import dm.cards.TrapCard;
import dm.cards.abstracts.Card;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.constants.SpellType;
import dm.constants.TrapType;
import dm.exceptions.NoEffectException;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.files.CardDAO;
import dm.files.CardExistsException;
import dm.files.DeckDao;
import dm.game.Player;

public class FileTests {

	private CardDAO cardDAO;
	// private MonsterNormalCard monsterNormalCard;
	// private MonsterEffectCard monsterEffectCard;

	private Player player;
	private static final String name = "Seto Kaiba";
	private static final Image avatar = null;
	private NormalDeck deck = new NormalDeck(40);
	private final ExtraDeck extraDeck = new ExtraDeck();

	private ArrayList<Card> cardList;

	@Before
	public void init() {
		cardDAO = new CardDAO();
		// monsterNormalCard = new MonsterNormalCard("Dark Magician",
		// "The ultimate wizard in terms of attack and defense.", null,
		// MonsterType.SPELLCASTER,
		// MonsterAttribute.DARK, 2500, 2100, 0, 3);
		// monsterEffectCard = new MonsterEffectCard("Dark Magician Girl",
		// "The ultimate wizard in terms of attack and defense.", null, 0, 0, 0,
		// 0, new Effect(), 0);
		cardList = new ArrayList<>();
		cardList.add(new MonsterNormalCard("Island Turtle", "A hope turtle that is often mistaken with a island",
				"Island Turtle.jpg", MonsterType.AQUA, MonsterAttribute.WATER, 1100, 2000, 3));
		cardList.add(new MonsterNormalCard("Dark Magician", "The ultimate Wizard in therms of attack and deffense",
				"Dark Magician.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 2500, 2100, 3));
		cardList.add(new MonsterNormalCard("Summoned Skull", "A fiend with dark powers for confusing the enemy. Among the Fiend-Type monsters, this monster boasts considerable force.",
				"Summoned Skull.jpg", MonsterType.FIEND, MonsterAttribute.DARK, 2500, 1200, 3));
		cardList.add(new MonsterNormalCard("Hane-Hane", "FLIP: Return 1 monster on the field to its owner's hand.",
				"Hane-hane.jpg", MonsterType.BEAST, MonsterAttribute.EARTH, 450, 500, 3));
		cardList.add(new MonsterNormalCard("Gamma The Magnet Warrior", "Alpha, Beta, and Gamma meld as one to form a powerful monster.",
				"Gamma The Magnet Warrior.jpg", MonsterType.ROCK, MonsterAttribute.EARTH, 1500, 1800, 3));
		cardList.add(new MonsterNormalCard("Gearfried The Iron Knight", "When an Equip Card(s) is equipped to this card: Destroy that Equip Card(s).",
				"Gearfried The Iron Knight.jpg", MonsterType.WARRIOR, MonsterAttribute.EARTH, 1800, 1600, 3));
		cardList.add(new MonsterNormalCard("Suijin", "During damage calculation in your opponent's turn, if this card is being attacked: You can target the attacking monster; make that target's ATK 0 during damage calculation only (this is a Quick Effect). This effect can only be used once while this card is face-up on the field.",
				"Suijin.jpg", MonsterType.AQUA, MonsterAttribute.WATER, 2500, 2400, 3));
		cardList.add(new MonsterNormalCard("Shapesnatch", "A bow with handler power",
				"Shapesnatch.jpg", MonsterType.MACHINE, MonsterAttribute.DARK, 1200, 1700, 3));
		cardList.add(new MonsterNormalCard("Ryu-Ran", "A vicious little dragon.",
				"Ryu-Ran.jpg", MonsterType.DRAGON, MonsterAttribute.FIRE, 2200, 2600, 3));
		cardList.add(new MonsterNormalCard("Skull Mariner", "A pirate ship",
				"Skull Mariner.jpg", MonsterType.WARRIOR, MonsterAttribute.WATER, 1600, 900, 3));
		cardList.add(new MonsterNormalCard("The Drdek", "A card that I don't know what does",
				"The Drdek.jpg", MonsterType.FIEND, MonsterAttribute.DARK, 700, 800, 3));
		cardList.add(new MonsterNormalCard("Time Wizard", " O Mago do tempo",
				"Time Wizard.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 500,400, 3));
		cardList.add(new MonsterNormalCard("Luster Dragon", "O Dragão do Lustre",
				"Luster Dragon.jpg", MonsterType.BEAST, MonsterAttribute.EARTH, 1900, 1600, 3));
	
		cardList.add(new MonsterNormalCard("Exodia the Forbidden One", "The most powerfull card in the game",
				"Exodia the Forbidden One.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 1000, 1000, 1));
		
		cardList.add(new MonsterNormalCard("Left Leg of the Forbidden One", "Left Leg of The most powerfull card in the game",
				"Left Leg of the Forbidden One.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 200, 300, 1));
		cardList.add(new MonsterNormalCard("Right Leg of the Forbidden One", "Right Leg of The most powerfull card in the game",
				"Right Leg of the Forbidden One.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 200, 300, 1));
		cardList.add(new MonsterNormalCard("Right Arm of the Forbidden One", "Right Arm of The most powerfull card in the game",
				"Right Arm of the Forbidden One.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 200, 300, 1));
		cardList.add(new MonsterNormalCard("Right Leg of the Forbidden One", "Right Leg of The most powerfull card in the game",
				"Right Leg of the Forbidden One.jpg", MonsterType.SPELLCASTER, MonsterAttribute.DARK, 200, 300, 1));
		try {
			cardList.add(new SpellCard("Change of Heart",
					"Choose a monster of the adversary field", "Change of Heart.jpg",
					new Effect(), SpellType.NORMAL, 1));
			cardList.add(new SpellCard("A Legendary Ocean",
					"Um oceano lendário", "A Legendary Ocean.jpg",
					new Effect(), SpellType.FIELD, 3));
			cardList.add(new SpellCard("Allure of Darkness",
					"Draw a card", "Allure of Darkness.jpg",
					new Effect(), SpellType.NORMAL, 3));
			cardList.add(new SpellCard("Premature Burial",
					"Enterro prematuro", "Premature Burial.jpg",
					new Effect(), SpellType.NORMAL, 3));
			cardList.add(new SpellCard("Nobleman of Crossout",
					"Destroy a faceDown monster", "Nobleman of Crossout.jpg",
					new Effect(), SpellType.NORMAL, 3));
			cardList.add(new SpellCard("Mystical Space Typhoon",
					"Destroy a Spell of Trap card in the field", "Mystical Space Typhoon.jpg",
					new Effect(), SpellType.QUICK_PLAY, 3));
			cardList.add(new TrapCard("Skull Dice",
					"Toss a coin and multiply the damage by the result x 100", "Skull Dice.jpg",
					new Effect(), TrapType.NORMAL, 3));
		} catch (NoEffectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	// @Test
	// public void saveMonsterCard() throws FileNotFoundException, IOException,
	// ClassNotFoundException {
	// File f = new File("cards/cards.ygo");
	// if (f.exists())
	// cardDAO.saveToFile(monsterNormalCard);
	// else
	// cardDAO.saveToEndFile(monsterNormalCard);
	// cardDAO.saveToEndFile(monsterEffectCard);
	//
	// Card m = cardDAO.readFile("cards/cards.ygo");
	// List<Card> list = cardDAO.readAllFile("cards/cards.ygo");
	//
	// assertEquals("Dark Magician", m.getName());
	// assertEquals(list.get(1).getName(), "Dark Magician Girl");
	// }

	@Test
	public void saveDeck() throws FileNotFoundException, IOException, ClassNotFoundException {
		DeckDao deckDao = new DeckDao();
		player = new Player(name, avatar, deck, extraDeck);
		deckDao.saveDeck(player);
		NormalDeck deck = deckDao.readNormalDeck(player);
		ExtraDeck extraDeck = deckDao.readExtraDeck(player);
		assertEquals(deck.size(), player.getDeck().size());
		assertEquals(extraDeck.size(), player.getExtraDeck().size());
	}

	@Test
	public void saveAndDeleteMonsterCard() throws FileNotFoundException, IOException, ClassNotFoundException {
		// File f = new File("cards/cards.ygo");
		MonsterNormalCard m = new MonsterNormalCard();
		// if (f.exists())
		// cardDAO.saveToFile(m);
		// else
		try {
			cardDAO.saveToEndFile(m);

			// cardDAO.saveToEndFile(monsterEffectCard);
			// Card m = cardDAO.readFile("cards/cards.ygo");
			List<Card> list = cardDAO.readAllFile("cards/cards.ygo");
			int size = list.size();
			assertEquals(list.contains(m), true);
			cardDAO.deleteFile("cards/cards.ygo", m);
			list = cardDAO.readAllFile("cards/cards.ygo");
			//
			for (Card c : list)
				System.out.println(c);
			assertEquals(list.contains(m), false);
			assertEquals(size - 1, list.size());
		} catch (CardExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void saveMonsters() throws FileNotFoundException, IOException, ClassNotFoundException {
		for(Card card : cardList){
			try{
				new CardDAO().saveCard(card);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
