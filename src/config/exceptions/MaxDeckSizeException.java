package config.exceptions;

public class MaxDeckSizeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MaxDeckSizeException(String message) {
		super(message);
	}

}
