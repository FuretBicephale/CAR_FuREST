package rest.ftp.output.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Produces;

import org.apache.commons.net.ftp.FTPFile;

import rest.ftp.FTPSession;
import rest.ftp.GetRestRequest;
import rest.ftp.RestRequestInformation;

/**
 * @author cachera - falez
 * Static class which generate strings containing JSON code. 
 */
public class JsonGenerator {

	@Produces("application/json")
	public static String generateDirectory(FTPSession session, RestRequestInformation information) {
		String response = "";

		try {
			response = JsonGenerator.generateFTPFileList(session.getFTPClient().listFiles(), information);
		} catch (IOException e) {
			response = "[]";
		}
		return response;
	}

	private static String generateFTPFileList(FTPFile[] listFiles,RestRequestInformation information) {
		Map<String, FTPFile> list = GetRestRequest.getDirectoryList(listFiles, information);
		
		String reponse = "[\n";
		
		boolean first = true;
		
		for(Entry<String, FTPFile> file : list.entrySet()) {
			
			if(first) {
				first = false;
				reponse += "\n";
			}
			else {
				reponse += ",\n";
			}
			SimpleDateFormat format = new SimpleDateFormat("HH:mm DD MMM yyyy"); 
			reponse += "{\n"+
								"\"name\": \""+file.getKey()+"\"\n"+
								"\"ressource\": \""+information.getPath()+file.getValue().getName()+"\"\n"+
								"\"user\": \""+file.getValue().getUser()+"\"\n"+
								"\"lastModif\": \""+(file.getValue().getTimestamp() != null ? format.format(file.getValue().getTimestamp().getTime()) : "")+"\"\n"+
						"}";
		}
		reponse += "\n]";
		
		return reponse;
	}
}
