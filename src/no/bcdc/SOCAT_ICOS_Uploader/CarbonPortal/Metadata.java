package no.bcdc.SOCAT_ICOS_Uploader.CarbonPortal;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	 * Extract any information required from the actual data
	 * @param dataContents The contents of the data file
	 */
	public void extractFromData(String dataContents) throws MetadataException {
		calculateHashSum(dataContents);
	}
	
	/**
	 * Set the filename
	 * @param filename The filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	/**
	 * Calculate the SHA256 hash sum from the data
	 * @param dataContents The data
	 * @throws NoSuchAlgorithmException 
	 */
	private void calculateHashSum(String dataContents) throws MetadataException {

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(dataContents.getBytes("UTF-8"));
			byte[] digest = md.digest();
			hashSum = String.format("%064x", new BigInteger(1, digest));
		} catch (Exception e) {
			throw new MetadataException("Error while calculating hash sum", e);
		}
	}
}
