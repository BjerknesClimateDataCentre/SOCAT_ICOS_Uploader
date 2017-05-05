package no.bcdc.SOCAT_ICOS_Uploader;

/**
 * Exceptions from processing metadata
 * @author zuj007
 *
 */
public class ConfigException extends Exception {


	/**
	 * The Serial Version UID
	 */
	private static final long serialVersionUID = -4207144274593713672L;

	/**
	 * Constructor for a message
	 * @param message The error message
	 */
	public ConfigException(String message) {
		super(message);
	}
	
	/**
	 * Constructor for an error with an underlying cause
	 * @param message The error message
	 * @param cause The underlying cause
	 */
	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
