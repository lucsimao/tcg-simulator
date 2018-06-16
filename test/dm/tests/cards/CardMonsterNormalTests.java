package dm.tests.cards;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import dm.tests.cards.abstracts.CardMonsterTests;
import model.cards.MonsterNormalCard;

public class CardMonsterNormalTests extends CardMonsterTests {

	@Override
	@Before
	public void initCard() {
		setCard(new MonsterNormalCard(name, description, null, type, atribute,level, originalAttack, originalDefense, 0));
	}

	@Test
	public void checkIfEffectIsNull() {
		/** Uma carta monstro normal não pode ter um efeito definido. */
		assertEquals(null, getCard().getEffect());
	}
		
	@Test
	public void cloneObject() {
		MonsterNormalCard c = null;
		try {
			c = (MonsterNormalCard) getCard().clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		assertEquals(c.getName(), getCard().getName());
		assertEquals(c.getCardType(), getCard().getCardType());
		assertEquals(c.getOriginalAttack(), getCard().getOriginalAttack());
	}
	
}
