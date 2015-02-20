package rest.ftp;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import rest.exception.FTPBadAnswerException;

public class FTPSession {
	
	private FTPClient ftp;
	
	public FTPSession() throws SocketException, IOException, FTPBadAnswerException {
		ftp = new FTPClient();
		ftp.connect("127.0.0.1", 4223);
		
		int reply = ftp.getReplyCode();
		
		if(!FTPReply.isPositiveCompletion(reply)) {
			throw new FTPBadAnswerException(reply);
		}
		
	}
	
	public void login() throws IOException, FTPBadAnswerException {
		ftp.login("user", "password");
		
		int reply = ftp.getReplyCode();
		
		if(!FTPReply.isPositiveCompletion(reply)) {
			throw new FTPBadAnswerException(reply);
		}
	}
	
	public void close() throws IOException {
		ftp.quit();
	}
	
	public FTPClient getFTPClient() {
		return ftp;
	}
	
	

}
