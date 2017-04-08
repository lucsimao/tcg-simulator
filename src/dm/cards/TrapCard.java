package dm.cards;

import java.awt.Image;

import dm.cards.abstracts.NonMonsterCard;
import dm.constants.CardType;
import dm.constants.ColorPicture;
import dm.constants.TrapType;
import dm.exceptions.NoEffectException;
import dm.interfaces.NormalDeckCard;

/*Classe carta armadilha
 * Classe de Carta armadilha que serve para representar as Traps do jogo
 * Ela contém por padrão o ColorPicture.Trap, que infere que a carta seja rosa.
 * Ela não pode ter efeito nulo.
 */

public class TrapCard extends NonMonsterCard implements  NormalDeckCard  {

	public int type;

	public TrapCard(String name, String description, Image picture, Effect effect,
			int type,int copies_number) throws NoEffectException {
		super(name, description,CardType.TRAP,ColorPicture.TRAP, picture, effect,copies_number);
		this.type = type;
	}
	public TrapCard(int copies_number) throws NoEffectException{
		super(null, null,CardType.TRAP,ColorPicture.TRAP, null,new Effect(),copies_number);
		this.type = TrapType.NORMAL;
	}
}
