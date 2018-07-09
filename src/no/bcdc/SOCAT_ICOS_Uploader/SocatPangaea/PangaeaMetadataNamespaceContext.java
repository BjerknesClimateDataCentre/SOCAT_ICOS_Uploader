package no.bcdc.SOCAT_ICOS_Uploader.SocatPangaea;

import java.util.Collections;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

public class PangaeaMetadataNamespaceContext implements NamespaceContext {

	private static final String PREFIX = "md";
	
	/**
	 * Default constructor - does nothing
	 */
	public PangaeaMetadataNamespaceContext() {
		
	}
	
	@Override
	public String getNamespaceURI(String prefix) {
		return "http://www.pangaea.de/MetaData";
	}

	@Override
	public String getPrefix(String namespaceURI) {
		return PREFIX;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Iterator getPrefixes(String namespaceURI) {
		return Collections.singleton(PREFIX).iterator();
	}
}
