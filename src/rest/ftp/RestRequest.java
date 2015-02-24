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

		FTPSession session;
		byte[] result = null;
		
		GetRestRequestInformation information = new GetRestRequestInformation();
		information.setURI(uri);
		
		try {
			session = new FTPSession();
			session.login();
			
			if(session.isDirectory(uri) || uri.equals("")) {
				result = GetRestRequest.getDirectory(session, information);
			} else {
				result = GetRestRequest.getFile(session, information);
			}
						
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
