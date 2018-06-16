package dm.tests.cards.abstracts;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import config.constants.CardType;
import config.constants.MonsterAttribute;
import config.constants.MonsterType;
import model.cards.Effect;
import model.cards.abstracts.MonsterCard;

public abstract class CardMonsterTests extends CardTests<MonsterCard> {

	protected static String name = "Dark Magician";
	protected static String description = "The ultimate wizard in terms of attack and defense";
	protected static MonsterType type = MonsterType.SPELLCASTER;
	protected static MonsterAttribute atribute = MonsterAttribute.DARK;
	protected static CardType cardType = CardType.MONSTER;
	protected static int copies_number = 0;
	protected static int originalAttack = 2500;
	protected static int originalDefense = 2100;
	protected static int level = 4;
	protected static Effect effect = null;

	@Override
	@Before
	public abstract void initCard();

	
	/* Ver se a carta instanciada est� recebendo os atributos corretamente */
	@Test
	public void checkAttributesOnInitialCondition() {
		assertEquals(name, getCard().getName());
		assertEquals(description, getCard().getDescription());
		assertEquals(type, getCard().getType());
		assertEquals(atribute, getCard().getAtribute());
		assertEquals(cardType, getCard().getCardType());
		assertEquals(copies_number, getCard().getCopiesNumber());
		System.out.println("CARD " + getCard().getOriginalAttack());
		assertEquals(originalAttack, getCard().getOriginalAttack());
		assertEquals(originalDefense, getCard().getOriginalDefense());
	}
	
	/* Teste para aumentar o ataque e a defesa do monstro */
	public void increaseAttackAndDefense(int value) {
		int lastAttack = getCard().getCurrentAttack();
		int lastDefense = getCard().getCurrentDefense();
		getCard().increaseAttack(value);
		if (getCard().getCurrentAttack() != 9999)
			assertEquals(lastAttack + value, getCard().getCurrentAttack());
		getCard().increaseDefense(value);
		if (getCard().getCurrentDefense() != 9999)
			assertEquals(lastDefense + value, getCard().getCurrentDefense());
	}

	/* Teste para reduzir o ataque e a defesa do monstro */
	public void descreaseAttackAndDefense(int value) {
		int lastAttack = getCard().getCurrentAttack();
		int lastDefense = getCard().getCurrentDefense();
		getCard().decreaseAttack(value);
		if (getCard().getCurrentAttack() != 0)
			assertEquals(lastAttack - value, getCard().getCurrentAttack());
		getCard().decreaseDefense(value);
		if (getCard().getCurrentDefense() != 0)
			assertEquals(lastDefense - value, getCard().getCurrentDefense());
	}

	/* Aumentar e diminuir ataque */
	@Test
	public void testDecreaseAndIncrease() {
		for (int i = 0; i < originalAttack; i++)
			increaseAttackAndDefense(100);
		for (int i = 0; i < originalAttack; i++)
			descreaseAttackAndDefense(100);
	}

	/* Restaurar Ataque original */
	@Test
	public void restoreOriginalAttackAndDefense() {
		increaseAttackAndDefense(3000);
		getCard().turnBackOriginalDefense();
		getCard().turnBackOriginalAttack();
		assertEquals(getCard().getOriginalAttack(), getCard().getCurrentAttack());
		assertEquals(getCard().getOriginalDefense(), getCard().getCurrentDefense());
	}

	/* Ver se o current attack est� sendo inicializado corretamente */
	@Test
	public void checkIfCurrentAttackAndDefAreEqualsOriginal() {
		assertEquals(getCard().getOriginalAttack(), getCard().getCurrentAttack());
		assertEquals(getCard().getOriginalDefense(), getCard().getCurrentDefense());
	}	
}
