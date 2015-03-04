package rest.ftp;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import rest.exception.FTPBadAnswerException;
import rest.ftp.output.html.HTMLErrorGenerator;

@Path("/ftp")
public class RestToFtpResource {

	@GET
	@Path("{uri: .*}")
	public byte[] processGetRequest(@PathParam("uri") String uri) {
		
		System.out.println("GET uri="+uri);
		
		FTPSession session = new FTPSession();
		byte[] result = null;
		
		GetRestRequestInformation information = new GetRestRequestInformation();
		information.setURI(uri);
		information.setPath("/rest/api/ftp");
		
		try {
			session.connect();
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
	
	@DELETE
	@Path("{uri: .*}")
	public byte[] processDeleteRequest(@PathParam("uri") String uri) {
		return DeleteRestRequest.process(uri);
	}
	
	@PUT
	@Path("{uri: .*}")
	public byte[] processPutRequest(@PathParam("uri") String uri) {
		return PutRestRequest.process(uri);
	}
}
