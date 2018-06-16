package model.cards;

import config.constants.CardType;
import config.constants.ColorPicture;
import config.constants.FilesConstants;
import config.constants.TrapType;
import config.exceptions.NoEffectException;
import model.cards.abstracts.NonMonsterCard;

/**
 * Classe carta armadilha Classe de Carta armadilha que serve para representar
 * as Traps do jogo Ela contém por padrão o ColorPicture.Trap, que infere que a
 * carta seja rosa. Ela não pode ter efeito nulo.
 * 
 * @author Simão
 */

public class TrapCard extends NonMonsterCard implements NormalDeckCard {

	private static final long serialVersionUID = -7918171498276202467L;

	public TrapType type;

	public TrapCard(String name, String description, String picture, Effect effect, TrapType type, int copies_number)
			throws NoEffectException {
		super(name, description, CardType.TRAP, ColorPicture.TRAP, picture, effect, copies_number);
		this.type = type;
	}

	public TrapCard(String name, String description, Effect effect, TrapType type, int copies_number)
			throws NoEffectException {
		this(name, description,name+".jpg", effect,type, copies_number);
		this.type = type;
	}

	
	public TrapCard() throws NoEffectException {
		super(nameGenerator(), "Teste de descrição", CardType.TRAP, ColorPicture.TRAP, FilesConstants.CARDS_IMG_DIR + FilesConstants.DEFAULT_MONTER_CARD_IMAGE, new Effect(), 3);
		this.type = TrapType.NORMAL;
	}

	public TrapType getType() {
		return type;
	}
}
