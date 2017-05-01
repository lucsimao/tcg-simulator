package dm.tests.files;

import static org.junit.Assert.assertEquals;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dm.cards.Effect;
import dm.cards.MonsterEffectCard;
import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.files.CardDAO;
import dm.files.DeckDao;
import dm.game.Player;

public class FileTests {

	private CardDAO cardDAO;
	private MonsterNormalCard monsterNormalCard;
	private MonsterEffectCard monsterEffectCard;

	private Player player;
	private static final String name = "Seto Kaiba";
	private static final Image avatar = null;
	private NormalDeck deck = new NormalDeck();
	private final ExtraDeck extraDeck = new ExtraDeck();

	@Before
	public void init() {
		cardDAO = new CardDAO();
		monsterNormalCard = new MonsterNormalCard("Dark Magician",
				"The ultimate wizard in terms of attack and defense.", null, MonsterType.SPELLCASTER,
				MonsterAttribute.DARK, 2500, 2100, 0, 3);
		monsterEffectCard = new MonsterEffectCard("Dark Magician Girl",
				"The ultimate wizard in terms of attack and defense.", null, 0, 0, 0, 0, new Effect(), 0);
	}

	@Test
	public void saveMonsterCard() throws FileNotFoundException, IOException, ClassNotFoundException {
		File f = new File("cards/cards.ygo");
		if (f.exists())
			cardDAO.saveToFile(monsterNormalCard);
		else
			cardDAO.saveToEndFile(monsterNormalCard);
		cardDAO.saveToEndFile(monsterEffectCard);

		Card m = (Card) cardDAO.readFile("cards/cards.ygo");
		List<Card> list = cardDAO.readAllFile("cards/cards.ygo");

		assertEquals("Dark Magician", m.getName());
		assertEquals(list.get(1).getName(), "Dark Magician Girl");
	}

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

}
