package dm.fields.elements.zones;

import dm.cards.abstracts.DuelCard;
import dm.cards.abstracts.DuelMonsterCard;
import dm.constants.CardState;
import dm.exceptions.CardNotFoundException;
import dm.exceptions.ZoneOccupedException;

public class SpellTrapZone extends CardZone{
	
	public void setCard(DuelCard spellTrapCard, int index) {
		putCard(spellTrapCard, index);	
		spellTrapCard.setState(CardState.FACE_DOWN);
	}
	
	public void setCard(DuelCard spellTrapCard) {
		for(int i=0;i<5;i++)
			if(this.getCards()[i]==null)
			{
				this.getCards()[i] = spellTrapCard;	
				spellTrapCard.setState(CardState.FACE_DOWN);
				return;
			}
		throw new ZoneOccupedException("We can't add more cards in the field");
	}
	
	public DuelCard remove(DuelMonsterCard spellTrapCard) {
		for(int i=0;i<ZONE_SIZE;i++)
			if(spellTrapCard.equals(getCards()[i]))
			{	
				return remove(i);
			}
		throw new CardNotFoundException("Card was not found");
	}
}