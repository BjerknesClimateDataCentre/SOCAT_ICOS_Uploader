package no.bcdc.SOCAT_ICOS_Uploader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config extends Properties {

	private static final String SOCAT_METADATA = "socat_metadata_table";
	
	private static final String STATION_LOOKUP = "station_lookup_table";
	
	private static final String CP_AUTH_URL = "cp_auth_url";
	
	private static final String CP_METADATA_URL = "cp_metadata_url";
	
	private static final String CP_DATA_URL_STUB = "cp_data_url_stub";
	
	private static final String CP_USER = "cp_user";
	
	private static final String CP_PASSWORD = "cp_password";
	
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
	
	public File getStationLookupFile() throws ConfigException {
		File lookupFile = new File(getProperty(STATION_LOOKUP));
		if (!SOCAT_ICOS_Uploader.checkFile(lookupFile)) {
			throw new ConfigException("Cannot read SOCAT Metadata file");
		}
		
		return lookupFile;
	}
	
	public String getCPUsername() {
		return getProperty(CP_USER);
	}
	
	public String getCPPassword() {
		return getProperty(CP_PASSWORD);
	}
	
	public String getCPAuthUrl() {
		return getProperty(CP_AUTH_URL);
	}
	
	public String getCPMetadataUrl() {
		return getProperty(CP_METADATA_URL);
	}
	
	public String getCPDataUrlStub() {
		return getProperty(CP_DATA_URL_STUB);
	}
}
