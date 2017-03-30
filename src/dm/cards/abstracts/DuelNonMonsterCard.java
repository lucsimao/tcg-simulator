package dm.cards.abstracts;

import java.awt.Image;

import dm.cards.Effect;

public abstract class DuelNonMonsterCard extends DuelCard {

	public DuelNonMonsterCard(String name, String description, int cardType, int colorPicture, Image picture,
			Effect effect, int copies_number) {
		super(name, description, cardType, colorPicture, picture, effect, copies_number);
	}

}
