package dm.cards;

import java.awt.Image;

import cards.NormalDeckCard;
import dm.cards.abstracts.DuelMonsterCard;
import dm.constants.ColorPicture;

public class DuelMonsterNormalCard extends DuelMonsterCard implements NormalDeckCard  {

	public DuelMonsterNormalCard(String name, String description, Image picture,
			int type, int atribute, int originalAttack, int originalDeffense,
			int state, int copies_number) {
		super(name, description,ColorPicture.NORMAL, picture, type, atribute, originalAttack, originalDeffense,
			 null,copies_number);
	}
}
