package dm.exceptions;

public class EffectMonsterWithNoEffectException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EffectMonsterWithNoEffectException(String message){
		super(message);
	}
}
