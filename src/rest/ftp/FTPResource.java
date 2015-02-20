package rest.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.apache.commons.net.ftp.*;

@Path("/ftp")
public class FTPResource {
	
	@GET
	@Produces("text/html")
	public String sayHello() {
		return "<h1>Bienvenue.</h1>";
	}
	
	@GET
	@Path("{path: .*}")
	@Produces("application/octet-stream")
	public byte[] resolvePathname( @PathParam("path") String pathname ) {
		System.out.println("PATH="+pathname);
		FTPClient client = new FTPClient();
		
		try {
			client.connect("127.0.0.1", 4223);
			
			client.login("user", "password");
			
			InputStream stream = client.retrieveFileStream(pathname);
			
			int length;
			
			while((length = stream.available()) > 0) {
				byte[] buffer = new byte[length];
 				stream.read(buffer, 0, length);
 				
 				return buffer;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 return null;
	}

}

