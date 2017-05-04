package no.bcdc.SOCAT_ICOS_Uploader.SocatPangaea;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

/**
 * Class for retrieving metadata and data from Pangaea and extracting
 * things from it.
 * @author zuj007
 *
 */
public class PangaeaData {

	/**
	 * The number of times to attempt a download
	 */
	private static final int NETWORK_RETRIES = 10;
	
	/**
	 * The length of time to wait between network retries, in seconds
	 */
	private static final int RETRY_WAIT_TIME = 5;
	
	/**
	 * The PangaVista service endpoint
	 */
	private static final String END_POINT = "https://ws.pangaea.de/ws/services/PangaVista";
	
	/**
	 * The SOAP URI
	 */
	protected static final String OPERATION_NAME_URI = "http://soapinterop.org/";
	
	/**
	 * The command name for registering a new session
	 */
	protected static final String OPERATION_REGISTER_SESSION = "registerSession";
	
	/**
	 * The command name for retrieving a data set's metadata
	 */
	protected static final String OPERATION_METADATA = "metadata";
	
	/**
	 * The error string returned when a session has expired
	 */
	private static final String EXPIRED_SESSION_ERROR = "You must register a valid session first!";
	
	/**
	 * The error string returned when the data set cannot be found
	 */
	private static final String DATASET_NOT_FOUND_ERROR = "This is not a valid PANGAEA DOI or DATASETID";
	
	/**
	 * The PANGAEA ID
	 */
	private String id;
	
	/**
	 * The PANGAEA session ID
	 */
	private static String sessionId = null;
	
	/**
	 * The SOAP service object
	 */
	private Service service = new Service();
	
	/**
	 * The parsed metadata XML
	 */
	private Document metadataXML = null;
	
	/**
	 * The data
	 */
	private String data = null;

	/**
	 * Constructor - downloads the metadata and data ready for processing
	 * @param id The PANGAEA ID
	 * @throws PangaeaException If any unhandleable errors occur
	 */
	public PangaeaData(String id) throws PangaeaException {
		this.id = id;
		initSession();
		downloadMetadata();
		downloadData();
	}
	
	/**
	 * Downloads the metadata from PANGAEA
	 * @throws PangaeaException If any errors occur
	 */
	private void downloadMetadata() throws PangaeaException {
		
		String xml = null;
		
		int retriesLeft = NETWORK_RETRIES;
		
		while (null == xml && retriesLeft > 0) {
			
			boolean sessionOK = false;
			
			while (!sessionOK) {
				
				// Start by assuming we have a valid session
				sessionOK = true;
				
				try {
					System.out.println("Downloading metadata for " + id);
					
					Call call = (Call) service.createCall();
					call.setTargetEndpointAddress(new java.net.URL(END_POINT));
					call.setOperationName(new QName(OPERATION_NAME_URI, OPERATION_METADATA));
					call.setReturnType(org.apache.axis.Constants.XSD_STRING);
					
					call.addParameter("session", Constants.XSD_STRING, ParameterMode.IN);
					call.addParameter("URI", Constants.XSD_STRING, ParameterMode.IN);
			        
			        xml = (String) call.invoke(new Object[] { sessionId, id });
				} catch (Exception e) {
	
					// If the session is invalid, get a new one and try again
					if (e.getMessage().equals(EXPIRED_SESSION_ERROR)) {
						try {
							initSession();
							sessionOK = false;
						} catch (PangaeaException e2) {
							// If that fails, throw the resulting exception
							throw e2;
						}
					} else if (e.getMessage().startsWith(DATASET_NOT_FOUND_ERROR)) {
						throw new PangaeaException("Data set '" + id + "' not found");
					}
				}
			}
			
			if (null == xml) {
				retriesLeft--;
				System.out.println("Data retrieval failed. Retrying in " + RETRY_WAIT_TIME + " seconds (" + retriesLeft + " attempts remaining)");

				int waitCount = RETRY_WAIT_TIME;
				while (waitCount > 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// We don't care
					}
					waitCount--;
				}
				
			}
		}
		
		if (null == xml) {
			throw new PangaeaException("Could not download metadata, despite retries");
		} else {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				metadataXML = builder.parse(IOUtils.toInputStream(xml, StandardCharsets.UTF_8));
			} catch (Exception e) {
				throw new PangaeaException("Error while parsing metadata XML", e);
			}
		}
		
	}
	
	/**
	 * Downloads the data from PANGAEA
	 * @throws PangaeaException If a download error occurs
	 */
	private void downloadData() throws PangaeaException {
		String downloadedData = null;
		
		int retriesLeft = NETWORK_RETRIES;
		
		while (null == downloadedData && retriesLeft > 0) {
			System.out.println("Downloading data for " + id);

			HttpsURLConnection conn = null;
			InputStream stream = null;
			StringWriter writer = null;
			
			try {
				URL url = makeUrl(id);
				conn = (HttpsURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.connect();
				
				stream = conn.getInputStream();
				writer = new StringWriter();
				IOUtils.copy(stream, writer, StandardCharsets.UTF_8);
				downloadedData = writer.toString();
			} catch (FileNotFoundException e) {
				throw new PangaeaException("Data not found on server");
			} catch (Exception e) {
				throw new PangaeaException("Data download failed", e);
			} finally {
				try {
					if (null != writer) {
						writer.close();
					}
					
					if (null != stream) {
						stream.close();
					}
					
					if (null != conn) {
						conn.disconnect();
					}
				} catch (IOException e) {
					// Do nothing - we can say that we tried.
				}
			}
			
			if (null == downloadedData) {
				retriesLeft--;
				
				int waitCount = RETRY_WAIT_TIME;
				while (waitCount > 0) {
					System.out.println("Data retrieval failed. Retrying in " + RETRY_WAIT_TIME + " seconds (" + retriesLeft + " attempts remaining)");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// We don't care
					}
					waitCount--;
				}
			}
		}
		
		this.data = downloadedData;
	}
	
	/**
	 * Make a URL for a data set
	 * @param dataSetId The data set ID
	 * @return The data set URL
	 * @throws MalformedURLException If the generated URL is invalid
	 */
	private URL makeUrl(String dataSetId) throws MalformedURLException {
		StringBuilder url = new StringBuilder("https://doi.pangaea.de/10.1594/PANGAEA.");
		url.append(dataSetId);
		url.append("?format=textfile");
		
		return new URL(url.toString());
	}
	
	/**
	 * Initialise a session on the PANGAEA servers.
	 * If a session already exists, no action is taken.
	 */
	private void initSession() throws PangaeaException {
		if (null == sessionId) {
			try {
				Call call = (Call) service.createCall();
		        call.setTargetEndpointAddress(new java.net.URL(END_POINT));
		        call.setOperationName(new QName(OPERATION_NAME_URI, OPERATION_REGISTER_SESSION));
		        sessionId = (String) call.invoke( new Object[] {} );
			} catch (Exception e) {
				throw new PangaeaException("Unable to get a session ID", e);
			}
			
			// Have a short sleep - trying to access the session too quickly causes problems
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Meh
			}
		}
	}
}
