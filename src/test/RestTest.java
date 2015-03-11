package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class RestTest {

	String filename = "toto";
	String contents = "TOTO";

	public int putFile(boolean anonymous) throws IOException {
		URL url;
		HttpURLConnection connection = null; 

		// Creates connection
		if(anonymous) {
			url = new URL("http://127.0.0.1:8080/rest/api/ftp/" + filename);
		} else {
			url = new URL("http://127.0.0.1:8080/rest/api/ftp/" + filename + "?username=user_test&password=user_test_password");
		}
		connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Content-Length", "" + contents.length());
		connection.setUseCaches(false);
		connection.setDoOutput(true);

		// Creates and sends request
		DataOutputStream fileContents = new DataOutputStream (connection.getOutputStream ());
		fileContents.writeBytes(contents);
		fileContents.flush();
		fileContents.close();

		// Get Response    
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer(); 
		while((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		
		int code = connection.getResponseCode();

		connection.disconnect(); 
		
		return code;
	}
	
	public int deleteFile(boolean anonymous) throws IOException {
		URL url;
		HttpURLConnection connection = null; 
		
		// Creates connection
		if(anonymous) {
			url = new URL("http://127.0.0.1:8080/rest/api/ftp/" + filename);
		} else {
			url = new URL("http://127.0.0.1:8080/rest/api/ftp/" + filename + "?username=user_test&password=user_test_password");
		}
		connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("DELETE");
		connection.setRequestProperty("Content-Length", "0");
		connection.setUseCaches(false);
		connection.setDoOutput(true);

		// Get Response    
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		StringBuffer response = new StringBuffer(); 
		while((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		
		int code = connection.getResponseCode();

		connection.disconnect(); 
		
		return code;
	}

	/**
	 * Test root access
	 * @throws IOException
	 */
	@Test
	public void testRootGetRequest() throws IOException {
		URL requestUrl = new URL("http://127.0.0.1:8080/rest/api/ftp?username=user_test&password=user_test_password");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(requestUrl.openStream()));
		in.close();
	}

	/**
	 * Test CWD Request on the folder TestFolder
	 * @throws IOException
	 */
	@Test
	public void testFolderGetRequest() throws IOException {
		URL requestUrl = new URL("http://127.0.0.1:8080/rest/api/ftp/TestFolder?username=user_test&password=user_test_password");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(requestUrl.openStream()));
		in.close();
	}
	
	/**
	 * Test CWD Request on an incorrect folder
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void testIncorrectFolderGetRequest() throws IOException {
		URL requestUrl = new URL("http://127.0.0.1:8080/rest/api/ftp/IncorrectFolder?username=user_test&password=user_test_password");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(requestUrl.openStream()));
		in.close();
	}

	/**
	 * Test RETR Request on the file toto
	 * @throws IOException
	 */
	@Test
	public void testFileGetRequest() throws IOException {

		putFile(false);

		URL requestUrl = new URL("http://127.0.0.1:8080/rest/api/ftp/" + filename + "?username=user_test&password=user_test_password");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(requestUrl.openStream()));

		String inputLine = in.readLine();

		in.close();

		assertEquals(contents, inputLine);
		
	}
	
	/**
	 * Test RETR Request on an incorrect filename
	 * @throws IOException
	 */
	@Test(expected=IOException.class)
	public void testIncorrectFileGetRequest() throws IOException {
		String incorrectFilename = "incorrectFile";		
		URL requestUrl = new URL("http://127.0.0.1:8080/rest/api/ftp/" + incorrectFilename + "?username=user_test&password=user_test_password");		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(requestUrl.openStream()));
		in.close();
	}

	/**
	 * Test STOR Request
	 * @throws IOException 
	 */
	@Test 
	public void testPutRequest() throws IOException {
		int code = putFile(false);
		assertTrue(code >= 200 && code < 300);
		deleteFile(false);
	}

	/**
	 * Test RM Request
	 * @throws IOException 
	 */
	@Test 
	public void testDeleteRequest() throws IOException {
		putFile(false);			
		int code = deleteFile(false);
		assertTrue(code >= 200 && code < 300);
	} 
	
	/**
	 * Test STOR Request as anonymous
	 * @throws IOException 
	 */
	@Test(expected = IOException.class) 
	public void testAnonymousPutRequest() throws IOException {
		putFile(true);
	}

}
