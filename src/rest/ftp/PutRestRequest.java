package rest.ftp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import rest.exception.FTPBadAnswerException;
import rest.ftp.output.html.HtmlErrorGenerator;

public class PutRestRequest {
	
	public static byte[] process(String uri, String contents) {
		FTPSession session = new FTPSession();
		
		try {
			session.connect();
			session.login();
			
			InputStream stream = new ByteArrayInputStream(contents.getBytes());

			
			session.getFTPClient().storeFile(uri, stream);
			session.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPBadAnswerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
