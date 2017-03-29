package dm.tests.cards;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dm.cards.DuelMonsterNormalCard;

public class CardMonsterNormalTests extends CardMonsterTests {
	
	@Override
	@Before
	public void initCard(){
		setCard(new DuelMonsterNormalCard(name, description, null, type, atribute, originalAttack, originalDefense,0, copies_number));
	}
	
	@Test
	public void checkIfEffectIsNull(){
		/**Uma carta monstro normal não pode ter um efeito definido.*/
		assertEquals(null,getCard().getEffect());
	}	
}
