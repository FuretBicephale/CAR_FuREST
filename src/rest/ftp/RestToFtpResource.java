package rest.ftp;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import rest.exception.FTPBadAnswerException;
import rest.ftp.output.html.HtmlErrorGenerator;

/**
 * @author cachera - falez
 * A REST Application Resource which access to a FTP Server. It defines Resource pathname and contents.
 * The Resource is accessible at apiAddress/ftp with apiAddress the Application address.
 */
@Path("/ftp")
public class RestToFtpResource {

	/**
	 * Manages GET Request of the REST application and spreads it to the FTP Server.
	 * The REST Application will send to the FTP Server a LIST Request or a RETR Request depending on the uri.
	 * If the uri refers to the path of a folder, it will be a LIST Request, if it refers to the path of a file, it will be a RETR Request.
	 * @param uri The URI of the GET Request. It affects the kind of request sent to the FTPServer.
	 * @param ui Some basics informations about the URI
	 * @return Either the content of the requested file (RETR) or the requested folder (LIST) as a byte array
	 */
	@GET
	@Path("{uri: .*}")
	public byte[] processGetRequest(@PathParam("uri") String uri, @Context UriInfo ui) {
		
		FTPSession session = new FTPSession();
		byte[] result = null;
		
		GetRestRequestInformation information = new GetRestRequestInformation();
		information.setURI(uri);
		information.setPath(ui.getAbsolutePath().getPath());
		information.setUriInfo(ui);
		
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
			result = HtmlErrorGenerator.ftpConnectionFailed(information, session).getBytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Manages DELETE Request of the REST application and spreads it to the FTP Server.
	 * It will send a RM Request to the FTPServer in order to delete the file referred by the uri
	 * @param uri The URI of the DELETE Request. It's the path of the file to delete.
	 * @return
	 */
	@DELETE
	@Path("{uri: .*}")
	public byte[] processDeleteRequest(@PathParam("uri") String uri) {
		return DeleteRestRequest.process(uri);
	}
	
	/**
	 * Manages PUT Request of the REST application and spreads it to the FTP Server.
	 * It will send a STORE Request to the FTPServer in order to add a file.
	 * The file will be referred by the uri.
	 * @param contents The content of the file to add.
	 * @param ui Some basics information about the URI.
	 * @return
	 */
	@PUT
	@Path("{uri: .*}")
	public byte[] processPutRequest(String contents, @Context UriInfo ui) {
		String path = ui.getPath();
		if(path.startsWith("ftp"))
			path = path.substring(3);
		else if(path.startsWith("/ftp"))
			path = path.substring(4);
		return PutRestRequest.process(path, contents);
	}
}
