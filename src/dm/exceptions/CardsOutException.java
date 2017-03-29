package dm.exceptions;

public class CardsOutException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CardsOutException(String message) {
		super(message);
	}
}
