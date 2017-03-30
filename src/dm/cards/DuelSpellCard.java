package dm.cards;

import java.awt.Image;

import dm.cards.abstracts.NonMonsterCard;
import dm.constants.CardType;
import dm.constants.ColorPicture;
import dm.interfaces.NormalDeckCard;

public class DuelSpellCard extends NonMonsterCard implements  NormalDeckCard  {
	
	/*Classe carta mágica*/
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
