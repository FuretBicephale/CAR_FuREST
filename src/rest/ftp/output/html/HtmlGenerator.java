package rest.ftp.output.html;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Produces;

import org.apache.commons.net.ftp.FTPFile;

import rest.ftp.FTPSession;
import rest.ftp.GetRestRequest;
import rest.ftp.RestRequestInformation;
import rest.ftp.RestToFtpResource;

/**
 * @author cachera - falez
 * Static class which generate strings containing HTML code. 
 */
public class HtmlGenerator {
	
	/**
	 * Generate a HTML header with utf-8 encoding and information's URI as title.
	 * @param information Information of the request asking this header
	 * @return The HTML Code generated in a String
	 */
	public static String generateHeader(String name) {
		return "<!DOCTYPE html>\n"+
				"<html lang=\"fr\">\n"+
					"<head>\n"+
						"<meta charset=\"utf-8\">\n"+
						"<title>"+name+"</title>\n"+
					"</head>\n"+
					"<style>\n"+
						"* { margin: 0px; padding: 0px }\n"+
						"#directory-list { width: 100%; }\n"+
						"#directory-list th { background-color: #ddd }\n"+
						".link { color: #E68A2E}\n"+
					"</style>\n"+
					"<script>\n"+
						"function delete_file(target) {" +
							"var httpRequest = new XMLHttpRequest();\n"+
    						"var username = document.getElementById('username');\n"+
    						"var password = document.getElementById('password');\n"+
    						"httpRequest.open('DELETE', target+'?username='+(username !== undefined ? username.value : 'anonymous')+'&password='+(password !== undefined ? password.value : ''), false);"+
    						"httpRequest.send(null);"+
    						"location.reload();\n"+
    					"}\n"+
    					"function open_ressource(path) {\n" +
    						"var username = document.getElementById('username');\n"+
    						"var password = document.getElementById('password');\n"+
    					 	"location.assign(path+'?username='+(username !== undefined ? username.value : 'anonymous')+'&password='+(password !== undefined ? password.value : ''));\n"+
    					"}\n"+
					"</script>\n"+
				"<body>\n";
	}
	
	public static String generatorLogin(RestRequestInformation information) {
		String[] login = RestToFtpResource.getLoginInformation(information.getUriInfo());
		return "Username : <input type=\"text\" id=\"username\" value=\""+login[0]+"\"/> Password : <input type=\"password\" id=\"password\" value=\""+login[1]+"\" /> <button onclick=\"open_ressource(window.location)\">Recharger</button>";
	}
	
	
	public static String generatorUploadForm(RestRequestInformation information) {
		return "<input id=\"file\" type=\"file\" multiple />\n"+
				"<script>\n"+
				"var input = document.getElementById('file');\n" +
				"input.addEventListener('change', function() {\n"+
					"var ended = 0;\n"+
					"for(var i = 0; i < input.files.length; i++) {\n" +
						"(function (file) {\n"+ 
							"var reader = new FileReader()\n" +
							"reader.addEventListener('load', function() { \n"+
								"var httpRequest = new XMLHttpRequest();\n"+
	    						"var username = document.getElementById('username');\n"+
	    						"var password = document.getElementById('password');\n"+
	    						"console.log(window.location.origin+window.location.pathname+file.name+'?username='+(username !== undefined ? username.value : 'anonymous')+'&password='+(password !== undefined ? password.value : ''));"+
	    						"httpRequest.open('PUT', window.location.origin+window.location.pathname+file.name+'?username='+(username !== undefined ? username.value : 'anonymous')+'&password='+(password !== undefined ? password.value : ''), false);"+
	    						"httpRequest.send(reader.result);"+
	    						"ended++;\n"+
	    						"if(ended ==  input.files.length) {\n"+
	    							"location.reload();\n"+
	    						"}\n"+
	    					"}, false);\n"+
	    					"reader.readAsText(file);\n"+
	    				"})(input.files[i]);\n"+
					"}\n"+
				"}, false)\n"+
				"</script>\n";
	}
	
	/**
	 * Generate a list of links which are files from a FTPServer. 
	 * Each link is a CWD or RETR request for the specified file according it's a folder or a file.
	 * @param ftpFiles The files to generate as a list
	 * @param information Information of the request asking this list
	 * @return The HTML Code generated in a String
	 */
	public static String generateFTPFileList(FTPFile[] ftpFiles, RestRequestInformation information) {
		
		String htmlResponse = "<table id=\"directory-list\">\n"+
				"<tr><th>Nom</th><th>Utilisateur</th><th>Derniere modification</th><th>Action</th></tr>\n";

		Map<String, FTPFile> list = GetRestRequest.getDirectoryList(ftpFiles, information);
		
		for(Entry<String, FTPFile> file : list.entrySet()) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm DD MMM yyyy"); 
			htmlResponse += "<tr>"+
								"<td><a class=\"link\" onclick=\"open_ressource('"+information.getPath()+file.getValue().getName()+"')\">"+file.getKey()+"<a></td>"+
								"<td>"+file.getValue().getUser()+"</td>"+
								"<td>"+(file.getValue().getTimestamp() != null ? format.format(file.getValue().getTimestamp().getTime()) : "")+"</td>"+
								"<td>"+(file. getValue().isDirectory() ? "" : "<button onclick=\"delete_file('"+information.getPath()+file.getValue().getName()+"')\">Supprimer</button>")+"</td>"+
							"</tr>\n";
		}
		
		htmlResponse += "</table>\n";

		return htmlResponse;
	}
	
	/**
	 * Generate a HTML footer in a String
	 * @param information  Information of the request asking this footer
	 * @return The HTML Code generated in a String
	 */
	public static String generateFooter(RestRequestInformation information) {
		return "</body></html>";
	}

	@Produces("text/html")
	public static String generateDirectory(FTPSession session, RestRequestInformation information) {
		String htmlResponse = "";

		try {
			htmlResponse = HtmlGenerator.generateHeader(information.getURI());
			htmlResponse += HtmlGenerator.generatorLogin(information);
			htmlResponse += HtmlGenerator.generateFTPFileList(session.getFTPClient().listFiles(), information);
			htmlResponse += HtmlGenerator.generatorUploadForm(information);
			htmlResponse += HtmlGenerator.generateFooter(information);
		} catch (IOException e) {
			htmlResponse = HtmlErrorGenerator.ftpConnectionFailed(information, session);
		}
		return htmlResponse;
	}

}
