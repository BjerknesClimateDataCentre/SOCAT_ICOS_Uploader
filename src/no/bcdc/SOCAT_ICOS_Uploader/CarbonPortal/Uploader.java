package no.bcdc.SOCAT_ICOS_Uploader.CarbonPortal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import no.bcdc.SOCAT_ICOS_Uploader.Config;
import no.bcdc.SOCAT_ICOS_Uploader.SocatPangaea.PangaeaData;

/**
 * Class for handling all data and metadata uploads to the Carbon Portal
 * @author Steve Jones
 *
 */
public class Uploader {
	
	private static final String USER_PARAM = "mail";
	
	private static final String PASSWORD_PARAM = "password";

	private Config config;
	
	private CookieStore cookies = null;
	
	public Uploader(Config config) throws UploaderException {
		this.config = config;
		authenticate();
	}
	
	public void upload(Metadata metadata, PangaeaData data) throws UploaderException {
		if (null == cookies) {
			authenticate();
		}
		
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;

		try {
			System.out.print("Uploading metadata...");
			HttpClientContext context = HttpClientContext.create();
			context.setCookieStore(cookies);
			
			client = HttpClients.createDefault();
			
			HttpPost post = new HttpPost(config.getCPMetadataUrl());
			post.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
			System.out.println(metadata.getJSONString());
			HttpEntity entity = new ByteArrayEntity(metadata.getJSONString().getBytes("UTF-8"));
	        post.setEntity(entity);
	        
	        response = client.execute(post, context);
	        
	        int statusCode = response.getStatusLine().getStatusCode();
			InputStream contentStream = response.getEntity().getContent();
	        if (statusCode != 200) {
	        	System.out.println("Failed (" + statusCode + ")");
	        	System.out.println(IOUtils.toString(contentStream, "UTF-8"));
	        } else {
	        	System.out.println("OK");
				System.out.println(IOUtils.toString(contentStream, "UTF-8"));
	        }
		} catch (Exception e) {
			throw new UploaderException(e);
		} finally {
			closeResponse(response);
			closeClient(client);
		}
	}
	
	private void authenticate() throws UploaderException {
		
		System.out.print("Authenticating at Carbon Portal...");
		
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		
		try {
			HttpClientContext context = HttpClientContext.create();
			client = HttpClients.createDefault();
			
			List <NameValuePair> params = new ArrayList <NameValuePair>();
			params.add(new BasicNameValuePair(USER_PARAM, config.getCPUsername()));
			params.add(new BasicNameValuePair(PASSWORD_PARAM, config.getCPPassword()));

			HttpPost post = new HttpPost(config.getCPAuthUrl());
			post.setEntity(new UrlEncodedFormEntity(params));
			
			response = client.execute(post, context);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() == 200) {
			    cookies = context.getCookieStore();
				System.out.println("OK"); 
			} else {
				System.out.println("Failed (" + status.getStatusCode() + ")");
				InputStream contentStream = response.getEntity().getContent();
				System.out.println(IOUtils.toString(contentStream, "UTF-8"));
				throw new UploaderException("Authentication Failed");
			}
			
		} catch (Exception e) {
			throw new UploaderException(e);
		} finally {
			closeClient(client);
			closeResponse(response);
		}
	}

	private void closeClient(CloseableHttpClient client) {
		if (null != client) {
			try {
				client.close();
			} catch (IOException e) {
				// Meh
			
			}
		}
	}

	private void closeResponse(CloseableHttpResponse response) {
		if (null != response) {
			try {
				response.close();
			} catch (IOException e) {
				// Meh
			
			}
		}
	}
}
