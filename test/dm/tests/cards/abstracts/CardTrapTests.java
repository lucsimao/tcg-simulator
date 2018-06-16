package dm.tests.cards.abstracts;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import config.constants.CardType;
import config.constants.ColorPicture;
import config.constants.TrapType;
import config.exceptions.NoEffectException;
import model.cards.Effect;
import model.cards.TrapCard;

public abstract class CardTrapTests extends CardTests<TrapCard> {

	protected static String name = "Ultimate Offering";
	protected static String description = "The ultimate wizard in terms of attack and defense";
	protected static TrapType type = TrapType.NORMAL;
	protected static int copies_number = 3;
	protected static Effect effect = null;
	protected static String picture = null;

	protected static CardType cardType = CardType.TRAP;
	protected static ColorPicture colorPicture = ColorPicture.TRAP;

	@Override
	@Before
	public void initCard() {
		try {
			setCard(new TrapCard(name, description, picture, effect, type, copies_number));
		} catch (NoEffectException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void checkAttributesOnInitialCondition() {
		assertEquals(name, getCard().getName());
		assertEquals(description, getCard().getDescription());
		assertEquals(type, getCard().getType());
		assertEquals(cardType, getCard().getCardType());
		assertEquals(colorPicture, getCard().getColorPicture());
		assertEquals(copies_number, getCard().getCopiesNumber());
	}

}
