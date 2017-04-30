package dm.constants;

/**
 * Classe com as constantes do estado da carta. Pode ser nenhum, caso não esteja
 * no campo, pode ser face down, se estiver virada para baixo, pode ser face up
 * attack ou face up defense.
 * 
 * @author Simão
 */

public class CardState {
	public static final int NONE = 0;
	public static final int FACE_DOWN = 1;
	public static final int FACE_UP_ATTACK = 2;
	public static final int FACE_UP_DEFENSE_POS = 3;
}
