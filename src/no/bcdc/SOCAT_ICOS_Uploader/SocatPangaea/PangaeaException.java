package no.bcdc.SOCAT_ICOS_Uploader.SocatPangaea;

/**
 * Exception from the Pangaea subsystem
 * @author zuj007
 *
 */
public class PangaeaException extends Exception {

	/**
	 * The Serial Version UID
	 */
	private static final long serialVersionUID = 5528045503780302845L;

	/**
	 * Simple constructor
	 * @param message The error message
	 */
	public PangaeaException(String message) {
		super(message);
	}

	/**
	 * Simple constructor
	 * @param message The error message
	 * @param cause The underlying cause of this error
	 */
	public PangaeaException(String message, Throwable cause) {
		super(message, cause);
	}
}
