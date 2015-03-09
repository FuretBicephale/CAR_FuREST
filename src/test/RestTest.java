package test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Test;

public class RestTest {
		
	@Test
	public void testRootGetRequest() throws IOException {
		URL requestUrl = new URL("http://127.0.0.1:8080/rest/api/ftp?username=user_test&password=user_test_password");
        BufferedReader in = new BufferedReader(
        		new InputStreamReader(requestUrl.openStream()));
	}
	
	@Test
	public void testFolderGetRequest() throws IOException {
		URL requestUrl = new URL("http://127.0.0.1:8080/rest/api/ftp/TestFolder?username=user_test&password=user_test_password");
        BufferedReader in = new BufferedReader(
        		new InputStreamReader(requestUrl.openStream()));
	}

	@Test
	public void testFileGetRequest() throws IOException {
		URL requestUrl = new URL("http://127.0.0.1:8080/rest/api/ftp/toto?username=user_test&password=user_test_password");
        BufferedReader in = new BufferedReader(
        		new InputStreamReader(requestUrl.openStream()));
        String inputLine = in.readLine();
        in.close();
        assertEquals("TOTO", inputLine);
	}

}
