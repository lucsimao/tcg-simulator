package dm.cards;

import java.awt.Image;

import dm.cards.abstracts.MonsterCard;
import dm.constants.ColorPicture;
import dm.interfaces.ExtraDeckCard;

public class MonsterFusionCard extends MonsterCard implements ExtraDeckCard {

	public MonsterFusionCard(String name, String description, Image picture,
			int type, int atribute, int originalAttack, int originalDeffense,
			int state, Effect effect,int copies_number) {
		super(name, description,ColorPicture.FUSION, picture, type, atribute, originalAttack, originalDeffense,
				 effect,copies_number);
	}
}
