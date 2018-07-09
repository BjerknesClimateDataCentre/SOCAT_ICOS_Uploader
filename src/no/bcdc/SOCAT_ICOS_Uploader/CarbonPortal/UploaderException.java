package no.bcdc.SOCAT_ICOS_Uploader.CarbonPortal;

public class UploaderException extends Exception {
	
	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 8232225697992382996L;


	public UploaderException(String message) {
		super(message);
	}
	
	
	public UploaderException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
