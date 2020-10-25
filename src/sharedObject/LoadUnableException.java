package sharedObject;

public class LoadUnableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoadUnableException(String message) {
		super("Cannot load resource from : " + message);
	}
	
}
