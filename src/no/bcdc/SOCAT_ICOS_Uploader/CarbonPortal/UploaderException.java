package no.bcdc.SOCAT_ICOS_Uploader.CarbonPortal;

public class UploaderException extends Exception {
	
	public UploaderException(String message) {
		super(message);
	}
	
	
	public UploaderException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
