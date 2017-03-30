package dm.tests.cards;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import constants.CardType;
import constants.MonsterAttribute;
import constants.MonsterType;
import dm.cards.DuelMonsterCard;
import dm.cards.Effect;

public abstract class CardMonsterTests extends CardTests<DuelMonsterCard> {

	protected static String name = "Dark Magician";
	protected static String description = "The ultimate wizard in terms of attack and defense";
	protected static int type = MonsterType.SPELLCASTER;
	protected static int atribute = MonsterAttribute.DARK;
	protected static int cardType = CardType.MONSTER;
//	protected static int colorPicture = ColorPicture.NORMAL;
	protected static int copies_number = 3;
	protected static int originalAttack = 2500;
	protected static int originalDefense = 2100;
	protected static Effect effect = null;
	
	
	public void increaseAttackAndDefense(int value){
		int lastAttack = getCard().getCurrentAttack();
		int lastDefense = getCard().getCurrentDefense();
		getCard().increaseAttack(value);
		if(getCard().getCurrentAttack()!=9999)
			assertEquals(lastAttack+value, getCard().getCurrentAttack());
		getCard().increaseDefense(value);
		if(getCard().getCurrentDefense()!=9999)
			assertEquals(lastDefense + value, getCard().getCurrentDefense());
	}
		
	public void descreaseAttackAndDefense(int value){
		int lastAttack = getCard().getCurrentAttack();
		int lastDefense = getCard().getCurrentDefense();
		getCard().decreaseAttack(value);
		if(getCard().getCurrentAttack()!=0)
			assertEquals(lastAttack-value, getCard().getCurrentAttack());
		getCard().decreaseDefense(value);
		if(getCard().getCurrentDefense()!=0)
			assertEquals(lastDefense-value, getCard().getCurrentDefense());
	}
	
	@Override
	@Before
	public abstract void initCard();
	
	@Test
	public void checkAttributesOnInitialCondition(){
		assertEquals(name,getCard().getName());
		assertEquals(description,getCard().getDescription());
		assertEquals(type,getCard().getType());
		assertEquals(atribute,getCard().getAtribute());
		assertEquals(cardType,getCard().getCardType());
//		assertEquals(colorPicture,getCard().getColorPicture());
		assertEquals(copies_number,getCard().getCopiesNumber());
		assertEquals(originalAttack,getCard().getOriginalAttack());
		assertEquals(originalDefense,getCard().getOriginalDefense());
	}
	
	@Test
	public void checkIfCurrentAttackAndDefAreEqualsOriginal(){
		assertEquals(getCard().getOriginalAttack(), getCard().getCurrentAttack());
		assertEquals(getCard().getOriginalDefense(), getCard().getCurrentDefense());
	}

	@Test
	public void testDecreaseAndIncrease(){
		for(int i = 0;i<originalAttack;i++)
			increaseAttackAndDefense(100);
		for(int i = 0;i<originalAttack;i++)
			descreaseAttackAndDefense(100);
	}
		
	@Test
	public void restoreOriginalAttackAndDefense(){
		increaseAttackAndDefense(3000);
		getCard().turnBackOriginalDefense();
		getCard().turnBackOriginalAttack();
		assertEquals(getCard().getOriginalAttack(),getCard().getCurrentAttack());
		assertEquals(getCard().getOriginalDefense(),getCard().getCurrentDefense());
	}
	
}
