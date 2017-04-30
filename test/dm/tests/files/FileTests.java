package dm.tests.files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dm.cards.Effect;
import dm.cards.MonsterEffectCard;
import dm.cards.MonsterNormalCard;
import dm.cards.abstracts.Card;
import dm.constants.MonsterAttribute;
import dm.constants.MonsterType;
import dm.files.CardDAO;

public class FileTests {

	private CardDAO cardDAO;
	private MonsterNormalCard monsterNormalCard;
	private MonsterEffectCard monsterEffectCard;
	
	@Before
	public void init(){
		cardDAO = new CardDAO();
		monsterNormalCard = new MonsterNormalCard("Dark Magician", "The ultimate wizard in terms of attack and defense.",null,MonsterType.SPELLCASTER,MonsterAttribute.DARK,2500,2100,0,3);
		monsterEffectCard = new MonsterEffectCard("Dark Magician Girl", "The ultimate wizard in terms of attack and defense.",null, 0, 0, 0, 0, new Effect(), 0);
	}
	
	@Test
	public void saveMonsterCard() throws FileNotFoundException, IOException, ClassNotFoundException{
		cardDAO.saveToEndFile("cards.txt",monsterNormalCard);
		cardDAO.saveToEndFile("cards.txt",monsterEffectCard);
		
		Card m = (Card) cardDAO.readFile("cards.txt");
		List<Object> list = cardDAO.readAllFile("cards.txt");
		
		
		System.out.println(m.getName());
		System.out.println(((Card) list.get(1)).getName());
	}
	
}
