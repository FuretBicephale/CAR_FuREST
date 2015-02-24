package rest.ftp;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import rest.exception.FTPBadAnswerException;

/**
 * @author cachera - falez
 * A connexion between a Client and a FTPServer.
 * Used to connect a client to the FTPServer when the user uses the resource FTPResource.
 * The FTPServer is the server on the address 127.0.0.1 on port 4223.
 */ 
public class FTPSession {
	
	/**
	 * The FTPServer client who will be connected as "user" with the root directory "user_normal".
	 */
	private FTPClient ftp;
	
	/**
	 * Connect the client to the FTPServer 127.0.0.1 on port 4223
	 * @throws SocketException
	 * @throws IOException
	 * @throws FTPBadAnswerException
	 */
	public FTPSession() throws SocketException, IOException, FTPBadAnswerException {
		this.ftp = new FTPClient();
		this.ftp.connect("127.0.0.1", 4223);
		
		int reply = this.ftp.getReplyCode();
		
		if(!FTPReply.isPositiveCompletion(reply)) {
			throw new FTPBadAnswerException(reply);
		}
		
	}
	
	/**
	 * Log in the client into the FTPServer on the "user" account.
	 * @throws IOException
	 * @throws FTPBadAnswerException
	 */
	public void login() throws IOException, FTPBadAnswerException {
		this.ftp.login("user", "password");
		
		int reply = this.ftp.getReplyCode();
		
		if(!FTPReply.isPositiveCompletion(reply)) {
			throw new FTPBadAnswerException(reply);
		}
	}
	
	/**
	 * Make a clean quit between the client and the FTPServer
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.ftp.quit();
	}
	
	public FTPClient getFTPClient() {
		return this.ftp;
	}
	
	/**
	 * Tests if the path uri is a directory and goes into it if it is.
	 * @param uri The path to test
	 * @return True if the path is a directory, false otherwise
	 * @throws IOException
	 */
	public boolean isDirectory(String uri) throws IOException {
		int returnCode;
		
		if(uri.equals("")) {
			return true;
		}
		
		this.ftp.changeWorkingDirectory(uri);
	    returnCode = this.ftp.getReplyCode();
	    
	    if (returnCode == 550) {
	        return false;
	    } else {
		    return true;	    	
	    }
	}
	
	

}
