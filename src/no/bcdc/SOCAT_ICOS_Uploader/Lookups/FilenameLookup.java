package no.bcdc.SOCAT_ICOS_Uploader.Lookups;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * Lookup official SOCAT filenames from EXPO codes
 * @author zuj007
 *
 */
public class FilenameLookup {

	/**
	 * The contents of the metadata file, with the header removed
	 */
	private String fileData;
	
	
	public FilenameLookup(File metadataFile) throws IOException {
		String fileContents = FileUtils.readFileToString(metadataFile, StandardCharsets.UTF_8);
		
		int headerEnd = fileContents.indexOf("*/");
		fileData = fileContents.substring(headerEnd + 3);
	}

	/**
	 * Returns the filename for a data set given its EXPO code
	 * @param expoCode The EXPO code
	 * @return The filename
	 * @throws LookupNotFoundException If the EXPO code is not found
	 */
	public String getFilename(String expoCode) throws LookupNotFoundException {
		Pattern pattern = Pattern.compile(".*\\R[0-9]+\t" + expoCode + "[^\t]+\t(" + expoCode + "[^\t]+).*", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(fileData);
		if (!matcher.matches()) {
			throw new LookupNotFoundException("Can't find filename for EXPO code " + expoCode);
		}
		return matcher.group(1) + ".tab";
	}
}
