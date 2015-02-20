package rest.ftp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author cachera - falez
 * A REST Application Ressource which access to a FTP Server. It defines Ressource pathname and contents.
 * The Ressource is accessible at apiAddress/ftp with apiAddress the Application address.
 */
@Path("/ftp")
public class FTPResource {
	
	@GET
	@Produces("text/html")
	public String sayHello() {
		return "<h1>Bienvenue.</h1>";
	}

}