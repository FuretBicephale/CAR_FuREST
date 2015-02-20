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

/**
 * @author cachera - falez
 * A REST Application Ressource which access to a FTP Server. It defines Ressource pathname and contents.
 * The Ressource is accessible at apiAddress/ftp with apiAddress the Application address.
 */
@Path("/ftp")
public class FTPResource {
	
	/**
	 * Send a LIST Request for the root directory
	 * @return A String which contains HTML Code to display the root directory content
	 */
	@GET
	@Produces("text/html")
	public String sayHello() {
		
		String htmlReturn = "";
		FTPClient client = new FTPClient();
		
		try {
			
			client.connect("127.0.0.1", 4223);
			client.login("user", "password");
			
			FTPFile[] dirContents = client.listFiles();
			
			for(int i = 0; i < dirContents.length; i++) {
				if(dirContents[i].getName().endsWith("~"))
					break;
				htmlReturn += "<a href=\"" + dirContents[i].getName() + "\">" + dirContents[i] + "</a><br/>";						
			}
			
		} catch (IOException e) {
			
			System.err.println("Unable to connect to FTP Server.");
			
		}
		
		return htmlReturn;
		
	}
	
	/**
	 * Send a GET Request to the FTP Server for the file pathname
	 * @param pathname The file to retrieve
	 * @return The file if it's found, null otherwise
	 */

	@GET
	@Path("{path: .*}")
	@Produces("application/html")
	public byte[] resolvePathname( @PathParam("path") String pathname ) {
		
		System.out.println("File = "+pathname);
		FTPClient client = new FTPClient();
		int length;
		
		try {
			
			client.connect("127.0.0.1", 4223);
			client.login("user", "password");
			
			InputStream stream = client.retrieveFileStream(pathname);
			
			while((length = stream.available()) > 0) {
				byte[] buffer = new byte[length];
 				stream.read(buffer, 0, length);
 				return buffer;
			}
			
		} catch (IOException e) {
			
			System.err.println("Unable to connect to FTP Server.");
			
		}
		return null;
		
	}


}
