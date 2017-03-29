package dm.cards;

import java.awt.Image;

import cards.NormalDeckCard;
import constants.CardType;
import constants.ColorPicture;

public class DuelSpellCard extends DuelCard implements  NormalDeckCard  {

	public int type;

	public DuelSpellCard(String name, String description, Image picture, Effect effect,
			int type,int copies_number) {
		super(name, description,CardType.SPELL, ColorPicture.SPELL, picture, effect,copies_number);
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	
	
}
