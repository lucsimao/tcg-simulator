package dm.cards;

import java.awt.Image;

import cards.NormalDeckCard;
import constants.CardType;
import constants.ColorPicture;

public class DuelTrapCard extends DuelCard implements  NormalDeckCard  {

	public int type;

	public DuelTrapCard(String name, String description, Image picture, Effect effect,
			int type,int copies_number) {
		super(name, description,CardType.TRAP,ColorPicture.TRAP, picture, effect,copies_number);
		this.type = type;
	}
}
