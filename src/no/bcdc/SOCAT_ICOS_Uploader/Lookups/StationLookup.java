package no.bcdc.SOCAT_ICOS_Uploader.Lookups;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * Class for looking up ICOS stations from EXPO codes
 * @author zuj007
 *
 */
public class StationLookup {

	private Map<String, String> lookupTable;
	
	/**
	 * Constructor - load the lookups from a CSV file
	 * @param lookupFile The lookup file
	 * @throws FileNotFoundException If the file does not exist
	 * @throws IOException If the file cannot be read
	 */
	public StationLookup(File lookupFile) throws FileNotFoundException, IOException {
		
		lookupTable = new HashMap<String, String>();
		
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(new FileReader(lookupFile));
		for (CSVRecord record : records) {
			lookupTable.put(record.get(0), record.get(1));
		}
	}
	
	public String getIcosStation(String expoCode) throws LookupNotFoundException {
		
		String station = null;
		
		for (Map.Entry<String, String> entry : lookupTable.entrySet()) {
			if (expoCode.startsWith(entry.getKey())) {
				station = entry.getValue();
				break;
			}
		}
		
		if (null == station) {
			throw new LookupNotFoundException("Cannot find station for EXPO Code " + expoCode);
		}
		
		return station;
	}
	
}
