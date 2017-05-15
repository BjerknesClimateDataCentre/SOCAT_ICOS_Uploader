package no.bcdc.SOCAT_ICOS_Uploader.CarbonPortal;

import java.time.ZonedDateTime;

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
	 * The hose organisation, which is SOCAT
	 */
	private static final String HOST_ORGANIZATION = "http://meta.icos-cp.eu/resources/organizations/SOCAT";
	
	/**
	 * The prefix to be put in front of station identifiers
	 */
	private static final String STATION_PREFIX = "http://meta.icos-cp.eu/resources/stations/";
	
	/**
	 * The hash sum of the data file
	 */
	private String hashSum;
	
	/**
	 * The filename of the data file
	 */
	private String filename;
	
	/**
	 * The object specification to use for this data
	 */
	private String objectSpecification = "TO DO";
	
	/**
	 * The station identifier
	 */
	private String stationIdentifier = "TO DO";
	
	/**
	 * The number of rows in the data file
	 */
	private int nRows = 0;
	
	/**
	 * The creation date. For SOCAT, we will be using the
	 * SOCAT release date.
	 */
	private ZonedDateTime creationDate = null;
	
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
	
	/**
	 * Set the hash sum
	 * @param hashSum The hash sum
	 */
	public void setHashSum(String hashSum) {
		this.hashSum = hashSum;
	}
	
	/**
	 * Get the JSON string for the metadata, formatted to be human-readable
	 * @return The human-readable JSON string
	 */
	public String getHumanReadableJSONString() {
		StringBuilder json = new StringBuilder();
		
		json.append("{\n");
		json.append("  \"submitterId\": \"" + SUBMITTER_ID + "\",\n");
		json.append("  \"hashSum\": \"" + hashSum + "\",\n");
		json.append("  \"fileName\": \"" + filename + "\",\n");
		json.append("  \"objectSpecification\": \"" + objectSpecification + "\",\n");
		json.append("  \"specificInfo\": {\n");
		json.append("    \"station\": \"" + STATION_PREFIX + stationIdentifier + "\",\n");
		json.append("    \"nRows\": " + nRows + ",\n");
		json.append("    \"production\": {\n");
		json.append("      \"creator\": \"" + HOST_ORGANIZATION + "\",\n");
		json.append("      \"contributors\": [],\n");
		json.append("      \"hostOrganization\": \"" + HOST_ORGANIZATION + "\",\n");
		json.append("      \"creationDate\": \"" + creationDate + "\",\n");
		json.append("    }\n");
		json.append("  }\n");
		json.append("}\n");
		
		return json.toString();
	}
}
