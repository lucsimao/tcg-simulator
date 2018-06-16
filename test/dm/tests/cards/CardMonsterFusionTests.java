package dm.tests.cards;

import org.junit.Before;

import dm.tests.cards.abstracts.CardMonsterTests;
import model.cards.Effect;
import model.cards.MonsterFusionCard;

public class CardMonsterFusionTests extends CardMonsterTests {
	@Override
	@Before
	public void initCard() {
		setCard(new MonsterFusionCard(name, description, null, type, atribute, level,originalAttack, originalDefense, 0,
				new Effect(), copies_number));
	}
}