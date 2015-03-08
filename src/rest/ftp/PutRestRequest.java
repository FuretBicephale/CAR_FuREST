package rest.ftp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import rest.exception.FTPBadAnswerException;
import rest.ftp.output.html.HtmlErrorGenerator;

/**
 * @author cachera - falez
 * A class which only contains a static method used to send a STORE Request to a FTPServer
 */
public class PutRestRequest {
	
	/**
	 * Sends a STORE Request to the FTPServer.
	 * It will ask to add a new file to the path uri which will contain contents
	 * @param uri The URI referring the path of the new file
	 * @param contents The contents of the new file
	 * @return
	 */
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
