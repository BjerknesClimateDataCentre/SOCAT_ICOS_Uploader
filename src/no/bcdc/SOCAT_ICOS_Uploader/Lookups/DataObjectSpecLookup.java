package no.bcdc.SOCAT_ICOS_Uploader.Lookups;

/**
 * Lookup class to determine the data object specification for a file
 * @author zuj007
 *
 */
public class DataObjectSpecLookup {
	
	/**
	 * Get the data object specification name using
	 * the column headers from a file
	 * @param columnHeaders The column headers
	 * @return The data object specification
	 * @throws LookupNotFoundException If the specification cannot be determined
	 */
	public static String getObjectSpec(String columnHeaders) {
		
		boolean hasSalinity = columnHeaders.contains("\tSal\t");
		boolean hasPressure = columnHeaders.contains("\tPPPP [hPa]\t");
		boolean hasPfeil = columnHeaders.contains("Pfeil");
		
		StringBuilder objectSpec = new StringBuilder();
		
		objectSpec.append("socat_");
		
		if (hasSalinity) {
			objectSpec.append("wSal");
		} else {
			objectSpec.append("noSal");
		}
		
		objectSpec.append('_');
		
		if (hasPressure) {
			objectSpec.append("wPPPP");
		} else {
			objectSpec.append("noPPPP");
		}
		
		if (hasPfeil) {
			objectSpec.append("_Pfeil");
		}
		
		objectSpec.append("_DataObject");
		
		return objectSpec.toString();
	}

}
