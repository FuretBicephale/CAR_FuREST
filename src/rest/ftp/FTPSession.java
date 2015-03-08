package rest.ftp;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

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
	 * The address of the FTPServer to connect to
	 */
	private String address;
	
	/**
	 * The port of the FTPServer to connect to
	 */
	private int port;
	
	/**
	 * Connect the client to the FTPServer 127.0.0.1 on port 4223
	 */
	public FTPSession() {
		this.address = "127.0.0.1";
		this.port = 4224;
		
	}
	
	/**
	 * Initialize the FTPClient and connect it to the FTPServer designed by the address and the port
	 * @throws SocketException
	 * @throws IOException
	 * @throws FTPBadAnswerException
	 * @throws SocketTimeoutException
	 */
	public void connect() throws SocketException, IOException, FTPBadAnswerException, SocketTimeoutException {
		this.ftp = new FTPClient();
		this.ftp.setDefaultTimeout(5000);
		this.ftp.connect(this.address, this.port);
		
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
	public void login(String username, String password) throws IOException, FTPBadAnswerException {
		this.ftp.login(username, password);
		
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
	
	public String getAddress() {
		return this.address;
	}
	
	public int getPort() {
		return this.port;
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
