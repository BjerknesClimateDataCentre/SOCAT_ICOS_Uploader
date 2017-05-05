package no.bcdc.SOCAT_ICOS_Uploader.CarbonPortal;

/**
 * Handles uploading of metadata to the Carbon Portal
 * @author zuj007
 *
 */
public class Metadata {

	/**
	 * The submitter ID. This is fixed, because it's us.
	 */
	private static final String SUBMITTER_ID = "OTC_SOCAT";
	
	/**
	 * The hash sum of the data file
	 */
	private String hashSum;
	
	/**
	 * The filename of the data file
	 */
	private String filename;
	
	/**
	 * Constructor - does nothing
	 */
	public Metadata() {
		
	}
	
	/**
	 * Set the filename
	 * @param filename The filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void setHashSum(String hashSum) {
		this.hashSum = hashSum;
	}
}
