
package nz.net.ultraq.jaxb;

/**
 * Wrapper for any exceptions that may arise during the XML transformating
 * phase.
 * 
 * @author Emanuel Rabina
 */
public class XMLException extends RuntimeException {

	/**
	 * Constructor, generates a new exception with the given message.
	 * 
	 * @param message Exception message to use.
	 */
	protected XMLException(String message) {

		super(message);
	}

	/**
	 * Constructor, wraps the original exception in this unchecked type.
	 * 
	 * @param message Error message to accompany the exception.
	 * @param cause The cause for this exception to be raised.
	 */
	protected XMLException(String message, Exception cause) {

		super(message, cause);
	}
}
