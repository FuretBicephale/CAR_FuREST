package rest.ftp;

import java.io.IOException;
import java.net.SocketException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import rest.exception.FTPBadAnswerException;

@Path("/ftp")
public class RestRequest {

	@GET
	@Path("{uri: .*}")
	public byte[] processGetRequest(@PathParam("uri") String uri) {
		
		GetRestRequestInformation information = new GetRestRequestInformation();
		
		information.setURI(uri);
		
		FTPSession session;
		byte[] result = null;
		try {
			session = new FTPSession();
			session.login();
			result = GetRestRequest.isDirectory(session, uri) ? GetRestRequest.getDirectory(session, information) : GetRestRequest.getFile(session, information);
			
			session.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPBadAnswerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	
	
}
