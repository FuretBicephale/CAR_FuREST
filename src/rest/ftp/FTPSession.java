package rest.ftp;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import rest.exception.FTPBadAnswerException;

public class FTPSession {
	
	private FTPClient ftp;
	
	public FTPSession() throws SocketException, IOException, FTPBadAnswerException {
		this.ftp = new FTPClient();
		this.ftp.connect("127.0.0.1", 4223);
		
		int reply = this.ftp.getReplyCode();
		
		if(!FTPReply.isPositiveCompletion(reply)) {
			throw new FTPBadAnswerException(reply);
		}
		
	}
	
	public void login() throws IOException, FTPBadAnswerException {
		this.ftp.login("user", "password");
		
		int reply = this.ftp.getReplyCode();
		
		if(!FTPReply.isPositiveCompletion(reply)) {
			throw new FTPBadAnswerException(reply);
		}
	}
	
	public void close() throws IOException {
		this.ftp.quit();
	}
	
	public FTPClient getFTPClient() {
		return this.ftp;
	}
	
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
