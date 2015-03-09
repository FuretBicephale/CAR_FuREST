package rest.ftp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.ws.rs.core.Response;

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
	 * @return A Response object containing informations about the FTP Server response
	 */
	public static Response process(RestRequestInformation information, String contents) {
		FTPSession session = new FTPSession();
		
		String[] login = RestToFtpResource.getLoginInformation(information.getUriInfo());
		
		try {
			session.connect();
			session.login(login[0], login[1]);
			
			InputStream stream = new ByteArrayInputStream(contents.getBytes());

			
			session.getFTPClient().storeFile(information.getURI(), stream);
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
