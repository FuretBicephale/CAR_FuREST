package rest.ftp;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import rest.exception.FTPBadAnswerException;
import rest.ftp.output.html.HTMLErrorGenerator;

public class DeleteRestRequest {
	
	public static byte[] process(String uri) {
		FTPSession session = new FTPSession();
		byte[] result = null;
		
		GetRestRequestInformation information = new GetRestRequestInformation();
		information.setURI(uri);
		information.setPath("/rest/api/ftp");
		
		try {
			session.connect();
			session.login();
			
			session.getFTPClient().deleteFile(uri);
						
			session.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPBadAnswerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(SocketTimeoutException e) {
			result = HTMLErrorGenerator.ftpConnectionFailed(information, session).getBytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

}
