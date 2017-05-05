package no.bcdc.SOCAT_ICOS_Uploader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config extends Properties {

	private static final String SOCAT_METADATA = "socat_metadata_table";
	
	protected Config(String configFilename) throws ConfigException {
		super();
		
		File configFile = new File(configFilename);
		if (!SOCAT_ICOS_Uploader.checkFile(configFile)) {
			throw new ConfigException("Cannot read config file");
		} else {
			try {
				load(new FileReader(configFile));
			} catch (IOException e) {
				throw new ConfigException("Error reading config file", e);
			}
		}	
	}
	
	public File getSocatMetadataFile() throws ConfigException {
		File metadataFile = new File(getProperty(SOCAT_METADATA));
		if (!SOCAT_ICOS_Uploader.checkFile(metadataFile)) {
			throw new ConfigException("Cannot read SOCAT Metadata file");
		}
		
		return metadataFile;
	}
}
