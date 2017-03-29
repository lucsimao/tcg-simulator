package dm.cards;

import java.awt.Image;

import cards.Card;

public abstract class DuelCard implements Card {
	
	private String name;
	private String description;
	private int cardType; // 0 for tokens, 1 for monster, 2 for spells, 3 for traps
	private int colorPicture;
	private Image picture;
	private Effect effect;
	private int copies_number;
	private int state;
	
	public DuelCard(String name, String description, int cardType, int colorPicture, Image picture, Effect effect, int copies_number) {
		super();
		this.name = name;
		this.description = description;
		this.cardType = cardType;
		this.colorPicture = colorPicture;
		this.picture = picture;
		this.effect = effect;
		if(copies_number>=3)
			this.copies_number = 3;
		else
			this.copies_number = copies_number;
		this.state = 0;
	}

	
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getColorPicture() {
		return colorPicture;
	}

	public Image getPicture() {
		return picture;
	}

	public Effect getEffect() {
		return effect;
	}

	public int getCopiesNumber() {
		return copies_number;
	}
}
