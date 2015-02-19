package rest.ftp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/ftp")
public class FTPResource {
	
	@GET
	@Produces("text/html")
	public String sayHello() {
		return "<h1>Bienvenue.</h1>";
	}

}

