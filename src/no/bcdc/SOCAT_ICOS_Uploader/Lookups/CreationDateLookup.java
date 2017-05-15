package no.bcdc.SOCAT_ICOS_Uploader.Lookups;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Class to determine the creation date for a file
 * @author Steve Jones
 *
 */
public class CreationDateLookup {

	/**
	 * Determine the creation date. This is based on the
	 * release date of the SOCAT version in which the file was created,
	 * which is embedded in the file's filename.
	 * 
	 * @param filename The filename
	 * @return The creation date
	 * @throws LookupNotFoundException If the SOCAT version is not recognised
	 */
	public static ZonedDateTime getCreationDate(String filename) throws LookupNotFoundException {
		
		ZonedDateTime result = null;
		
		if (filename.indexOf("SOCATv3") > -1) {
			// 2015-09-07
			result = ZonedDateTime.of(2015, 9, 7, 0, 0, 0, 0, ZoneId.of("Z"));
		} else if (filename.indexOf("SOCATv4") > -1) {
			// 2016-09-01
			result = ZonedDateTime.of(2016, 9, 1, 0, 0, 0, 0, ZoneId.of("Z"));
		} else {
			throw new LookupNotFoundException("Cannot establish creation date for file '" + filename);
		}
		
		return result;
	}
	
}
