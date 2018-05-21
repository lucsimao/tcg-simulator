package dm.exceptions;

public class MonsterCannotBeSummonedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MonsterCannotBeSummonedException(String message) {
		super(message);
	}
}
