package rest.ftp;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import rest.exception.FTPBadAnswerException;
import rest.ftp.output.html.HtmlErrorGenerator;

/**
 * A REST Application Resource which access to a FTP Server. It defines Resource pathname and contents.
 * The Resource is accessible at apiAddress/ftp with apiAddress the Application address.
 * @author cachera - falez
 */
@Path("/ftp")
public class RestToFtpResource {

	/**
	 * Manages GET Request of the REST application and spreads it to the FTP Server.
	 * The REST Application will send to the FTP Server a LIST Request or a RETR Request depending on the uri.
	 * If the uri refers to the path of a folder, it will be a LIST Request, if it refers to the path of a file, it will be a RETR Request.
	 * @param uri The URI of the GET Request. It affects the kind of request sent to the FTPServer.
	 * @param ui Some basics informations about the URI
	 * @return Either the content of the requested file (RETR) or the requested folder (LIST) as a Response
	 */
	@GET
	@Path("{uri: .*}")
	public Response processGetRequest(@PathParam("uri") String uri, @Context UriInfo ui) {
		
		RestRequestInformation information = new RestRequestInformation();
		information.setURI(uri);
		information.setPath("/rest/api/ftp");
		information.setUriInfo(ui);
		
		String[] login = getLoginInformation(ui);
		FTPSession session = new FTPSession();
		
		try {
			session.connect();
			session.login(login[0], login[1]);
			
			Response result;
			
			if(session.isDirectory(uri) || uri.equals("")) {
				result = GetRestRequest.getDirectory(session, information);
			} else {
				result = GetRestRequest.getFile(session, information);
			}
						
			session.close();
			
			return result;
		} catch (SocketException e) {
			return Response.status(Response.Status.GATEWAY_TIMEOUT).entity(HtmlErrorGenerator.ftpConnectionFailed(information, session)).type(MediaType.TEXT_HTML).build();
		} catch (FTPBadAnswerException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(HtmlErrorGenerator.ftpBadAnswer(information, session, e.getCode())).type(MediaType.TEXT_HTML).build();
		} catch (IOException e) {
			return Response.status(Response.Status.GATEWAY_TIMEOUT).entity(HtmlErrorGenerator.ftpConnectionFailed(information, session)).type(MediaType.TEXT_HTML).build();
		}
	}
	
	/**
	 * Return the couple username/password with the URI information. If no information is found, return anonymous login mode
	 * @param uri The URI information of the request.
	 * @return A String array containing the username and the password of the login
	 */
	public static String[] getLoginInformation(UriInfo ui) {
		String[] login = new String[2];
		if(ui.getQueryParameters().containsKey("username") && ui.getQueryParameters().containsKey("password")) {
			login[0] = ui.getQueryParameters().get("username").get(0);
			login[1] = ui.getQueryParameters().get("password").get(0);
		}
		else {
			login[0] = "anonymous";
			login[1] = "";
		}
		return login;
	}

	/**
	 * Manages DELETE Request of the REST application and spreads it to the FTP Server.
	 * It will send a RM Request to the FTPServer in order to delete the file referred by the uri
	 * @param uri The URI of the DELETE Request. It's the path of the file to delete.
	 * @return A Response object containing informations about the FTP Server response
	 */
	@DELETE
	@Path("{uri: .*}")
	public Response processDeleteRequest(@PathParam("uri") String uri, @Context UriInfo ui) {
		RestRequestInformation information = new RestRequestInformation();
		information.setURI(uri);
		information.setPath("/rest/api/ftp");
		information.setUriInfo(ui);
		return DeleteRestRequest.process(information);
	}
	
	/**
	 * Manages PUT Request of the REST application and spreads it to the FTP Server.
	 * It will send a STORE Request to the FTPServer in order to add a file.
	 * The file will be referred by the uri.
	 * @param contents The content of the file to add.
	 * @param ui Some basics information about the URI.
	 * @return A Response object containing informations about the FTP Server response
	 */
	@PUT
	@Path("{uri: .*}")
	public Response processPutRequest(String contents, @Context UriInfo ui) {
		String path = ui.getPath();
		if(path.startsWith("ftp"))
			path = path.substring(3);
		else if(path.startsWith("/ftp"))
			path = path.substring(4);
		
		RestRequestInformation information = new RestRequestInformation();
		information.setURI(path);
		information.setUriInfo(ui);
		
		return PutRestRequest.process(information, contents);
	}
}
