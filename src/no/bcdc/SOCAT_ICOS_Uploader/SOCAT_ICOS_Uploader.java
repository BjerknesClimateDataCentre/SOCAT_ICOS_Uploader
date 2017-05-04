package no.bcdc.SOCAT_ICOS_Uploader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import no.bcdc.SOCAT_ICOS_Uploader.SocatPangaea.PangaeaData;
import no.bcdc.SOCAT_ICOS_Uploader.SocatPangaea.PangaeaException;

/**
 * The SOCAT Data Uploader for uploading SOCAT data
 * from PANGAEA into the ICOS Carbon Portal.
 * 
 * @author zuj007
 *
 */
public class SOCAT_ICOS_Uploader {
	
	/**
	 * Move into the object world. This takes in the command line file,
	 * extracts the IDs and processes each one in turn.
	 * @param idsFile The file containing PANGAEA IDs
	 * @throws Exception If any unhandled errors appear 
	 */
	private SOCAT_ICOS_Uploader(File idsFile) throws Exception {
		List <String> ids = getIdList(idsFile);
		for (String id : ids) {
			processId(id);
		}
	}
	
	/**
	 * Main class!
	 * @param args The command line arguments
	 */
	public static void main(String[] args) {
		
		try {
			if (args.length != 1) {
				System.out.println("Usage: java -jar SOCAT_ICOS_Uploader.jar <file of PANGAEA IDs>");
				System.exit(0);
			}
			
			File idsFile = new File(args[0]);
			
			if (checkFile(idsFile)) {
				new SOCAT_ICOS_Uploader(idsFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check that a file exists, is a file, and is readable
	 * @param filename The filename to check
	 * @return {@code true} if the file passes all checks; {@code false} if it does not.
	 */
	private static boolean checkFile(File file) {
		
		boolean ok = true;
		
		if (!file.exists()) {
			System.out.println(file.getAbsolutePath() + " does not exist");
			ok = false;
		} else if (!file.isFile()) {
			System.out.println(file.getAbsolutePath() + " is not a file");
			ok = false;
		} else if (!file.canRead()) {
			System.out.println(file.getAbsolutePath() + ": read permission denied");
			ok = false;
		}
		
		return ok;
	}
	
	/**
	 * Get a list of IDs from a file. Assumes that there is one ID per line.
	 * @param file The file of IDs
	 * @return The list of IDs
	 * @throws IOException If an error occurs while reading the file
	 */
	private List<String> getIdList(File file) throws IOException {
		String fileContents = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		return Arrays.asList(fileContents.split("\n"));
	}
	
	private void processId(String id) throws PangaeaException {
		System.out.println("Processing ID " + id);
		PangaeaData data = new PangaeaData(id);
	}
}
