package dm.cards.abstracts;

import java.io.Serializable;

import dm.cards.Effect;
import dm.constants.CardState;
import dm.constants.RulesConstants;

/**
 * Classe abstrata Card, ela contém contrutor e métodos que toda carta deverá
 * conter.
 * 
 * @author Simão
 */

public abstract class Card implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7720833260489417219L;

	private String name;
	private String description;
	private int cardType; // 0 for tokens, 1 for monster, 2 for spells, 3 for
							// traps
	private int colorPicture;
	private String picture;
	private Effect effect;
	private int copies_number;
	private int state;

	public Card(String name, String description, int cardType, int colorPicture, String picture, Effect effect,
			int copies_number) {
		super();
		this.name = name;
		this.description = description;
		this.cardType = cardType;
		this.colorPicture = colorPicture;
		this.picture = picture;
		this.effect = effect;
		// Esse pedaço de código protege de ter mais cópias do que o permitido
		// nas regras do jogo.
		if (copies_number >= RulesConstants.MAX_CARDS_REPEATED)
			this.copies_number = RulesConstants.MAX_CARDS_REPEATED;
		else
			this.copies_number = copies_number;
		this.state = CardState.NONE;// Diz que a carta não está no campo ainda.
	}

	// Getters and Setters
	public String getName() {
		return name;
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

	public String getDescription() {
		return description;
	}

	public int getColorPicture() {
		return colorPicture;
	}

	public String getPicture() {
		return picture;
	}

	public Effect getEffect() {
		return effect;
	}

	public int getCopiesNumber() {
		return copies_number;
	}

}
