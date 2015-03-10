package rest.ftp;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.ws.rs.core.Response;

import rest.exception.FTPBadAnswerException;
import rest.ftp.output.html.HtmlErrorGenerator;

/**
 * A class which only contains a static method used to send a RM Request to a FTPServer
 * @author cachera - falez
 */
public class DeleteRestRequest {
	
	/**
	 * Sends a RM Request to the FTPServer. 
	 * It will ask to delete the file referred by the uri 
	 * @param uri The URI referring the file to delete
	 * @return A Response object containing informations about the FTP Server response
	 */
	public static Response process(RestRequestInformation information) {
		FTPSession session = new FTPSession();
		
		String[] login = RestToFtpResource.getLoginInformation(information.getUriInfo());
		
		try {
			session.connect();
			session.login(login[0], login[1]);
			
			session.getFTPClient().deleteFile(information.getURI());
						
			session.close();
		} catch (SocketException e) {
			return Response.status(Response.Status.REQUEST_TIMEOUT).build();
		} catch (FTPBadAnswerException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Ftp server answer "+e.getCode()).build();
		} catch (IOException e) {
			return Response.status(Response.Status.REQUEST_TIMEOUT).build();
		}
		
		return Response.status(Response.Status.ACCEPTED).build();
	}

}
