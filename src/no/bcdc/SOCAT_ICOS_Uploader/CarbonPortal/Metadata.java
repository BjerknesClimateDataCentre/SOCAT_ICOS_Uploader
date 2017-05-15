package no.bcdc.SOCAT_ICOS_Uploader.CarbonPortal;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
	 * The prefix for the data object specification
	 */
	private static final String DATA_OBJECT_SPEC_PREFIX = "http://meta.icos-cp.eu/resources/cpmeta/";
	
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
	private String station;
	
	/**
	 * The number of rows in the data file
	 */
	private int rows = 0;
	
	/**
	 * The creation date. For SOCAT, we will be using the
	 * SOCAT release date.
	 */
	private ZonedDateTime creationDate = null;
	
	/**
	 * The comment (used to hold the citation text)
	 */
	private String comment = null;
	
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
	 * Get the filename
	 * @return The filename
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * Set the hash sum
	 * @param hashSum The hash sum
	 */
	public void setHashSum(String hashSum) {
		this.hashSum = hashSum;
	}
	
	/**
	 * Set the creation date
	 * @param creationDate The creation date
	 */
	public void setCreationDate(ZonedDateTime creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * Set the station
	 * @param station The station
	 */
	public void setStation(String station) {
		this.station = station;
	}
	
	/**
	 * Set the number of rows
	 * @param rows The row count
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	/**
	 * Set the comment
	 * @param comment The comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Set the object specification
	 * @param objectSpecification The object specification
	 */
	public void setObjectSpecification(String objectSpecification) {
		this.objectSpecification = objectSpecification;
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
		json.append("  \"objectSpecification\": \"" + DATA_OBJECT_SPEC_PREFIX + objectSpecification + "\",\n");
		json.append("  \"specificInfo\": {\n");
		json.append("    \"station\": \"" + STATION_PREFIX + station + "\",\n");
		json.append("    \"nRows\": " + rows + ",\n");
		json.append("    \"production\": {\n");
		json.append("      \"creator\": \"" + HOST_ORGANIZATION + "\",\n");
		json.append("      \"contributors\": [],\n");
		json.append("      \"hostOrganization\": \"" + HOST_ORGANIZATION + "\",\n");
		if (null != comment) {
			json.append("      \"comment\": \"" + comment + "\",\n");
		}
		json.append("      \"creationDate\": \"" + creationDate.format(DateTimeFormatter.ISO_INSTANT) + "\"\n");
		json.append("    }\n");
		json.append("  }\n");
		json.append("}\n");
		
		return json.toString();
	}
}
