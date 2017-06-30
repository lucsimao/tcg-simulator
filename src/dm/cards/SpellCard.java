package dm.cards;

import dm.cards.abstracts.NonMonsterCard;
import dm.constants.CardType;
import dm.constants.ColorPicture;
import dm.constants.SpellType;
import dm.exceptions.NoEffectException;
import dm.interfaces.NormalDeckCard;

/**
 * Classe carta mágica Classe de Carta mágica que serve para representar as
 * Spells do jogo Ela contém por padrão o ColorPicture.Trap, que infere que a
 * carta seja verde. Ela não pode ter efeito nulo.
 * 
 * @author Simão
 */

public class SpellCard extends NonMonsterCard implements NormalDeckCard {

	private static final long serialVersionUID = 511682576858461550L;

	public int type;

	public SpellCard(String name, String description, String picture, Effect effect, int type, int copies_number)
			throws NoEffectException {
		super(name, description, CardType.SPELL, ColorPicture.SPELL, picture, effect, copies_number);
		this.type = type;
	}

	public SpellCard() throws NoEffectException {
		super(nameGenerator(), "Carta mágica padrão para testes", CardType.SPELL, ColorPicture.SPELL, null, new Effect(), 3);
		this.type = SpellType.NORMAL;
	}

	public int getType() {
		return type;
	}

}
