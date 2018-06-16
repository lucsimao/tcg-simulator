package config.exceptions;

public class ZoneOccupedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ZoneOccupedException(String message) {
		super(message);
	}
}
