package no.bcdc.SOCAT_ICOS_Uploader.Lookups;

/**
 * Exception for failed lookups
 * @author zuj007
 *
 */
public class LookupNotFoundException extends Exception {

	/**
	 * The Serial Version UID
	 */
	private static final long serialVersionUID = -6085513367876804165L;

	/**
	 * Simple constructor
	 * @param message The error message
	 */
	public LookupNotFoundException(String message) {
		super(message);
	}
}
