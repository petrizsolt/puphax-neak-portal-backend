package hu.neak.puphax.syncronizer.exceptions;

public class TooManyTryException extends RuntimeException {
	private static final long serialVersionUID = 7135244076758708799L;

	public TooManyTryException(String location, int limit) {
		super(location + " download failed! too many try! limit: " + limit);
	}
}
