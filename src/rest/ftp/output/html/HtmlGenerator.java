package rest.ftp.output.html;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.net.ftp.FTPFile;

import rest.ftp.FTPSession;
import rest.ftp.GetRestRequest;
import rest.ftp.RestRequestInformation;
import rest.ftp.RestToFtpResource;

/**
 * Static class which generate strings containing HTML code. 
 * @author cachera - falez
 */
public class HtmlGenerator {
	
	/**
	 * Generate a HTML header with utf-8 encoding.
	 * @param name The title of the page
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
	
	/**
	 * General HTML Code as a String which creates a login form
	 * @param information Information about the request which is asking the form
	 * @return The generated HTML Code as a String
	 */
	public static String generatorLogin(RestRequestInformation information) {
		String[] login = RestToFtpResource.getLoginInformation(information.getUriInfo());
		return "<div style=\"float: left\">Username : <input type=\"text\" id=\"username\" value=\""+login[0]+"\"/> Password : <input type=\"password\" id=\"password\" value=\""+login[1]+"\" /> <button onclick=\"open_ressource(window.location.origin+window.location.pathname)\">Recharger</button></div>"+
				"<div align=right>Envoyer des fichiers : <input id=\"file2\" type=\"file\" multiple /></div>";
	}
	
	/**
	 * General HTML Code as a String which creates a file upload form
	 * @param information Information about the request which is asking the form
	 * @return The generated HTML Code as a String
	 */
	public static String generatorUploadForm(RestRequestInformation information) {
		
		return "<div id=\"file-area\" style=\"width: 100%; padding-top: 50px; padding-bottom: 50px; text-align: center; border: 2px dashed #ddd\">Ajouter un fichier</div>\n"+
		"<script>\n"+
		"var input = document.getElementById('file-area');\n" +
		
		"input.addEventListener('dragenter', function() {\n"+
			"input.style.borderColor = '#d00';\n" +
		"}, false);\n"+
			
		"input.addEventListener('dragover', function(e) {\n"+
	   		"e.preventDefault();\n" +
	   		"e.stopPropagation();\n" +
			"input.style.borderColor = '#d00';\n" +
		"}, false);\n"+
			
		"input.addEventListener('dragleave', function(e) {\n"+
	   		"e.preventDefault();\n" +
	   		"e.stopPropagation();\n" +
			"input.style.borderColor = '#ddd';\n" +
		"}, false);\n"+
			
		"function upload_files(files) {\n"+
			"var ended = 0;\n"+
			"for(var i = 0; i < files.length; i++) {\n" +
				"(function (file) {\n"+ 
					"var reader = new FileReader()\n" +
					"reader.addEventListener('load', function() { \n"+
						"var httpRequest = new XMLHttpRequest();\n"+
        				"var username = document.getElementById('username');\n"+
        				"var password = document.getElementById('password');\n"+
        				"httpRequest.open('PUT', window.location.origin+window.location.pathname+'/'+file.name+'?username='+(username !== undefined ? username.value : 'anonymous')+'&password='+(password !== undefined ? password.value : ''), false);"+
						"httpRequest.send(reader.result);"+
						"ended++;\n"+
						"if(ended == files.length) {\n"+
	    					"location.reload();\n"+
	    				"}\n"+
	    			"}, false);\n"+
	    			"reader.readAsText(file);\n"+
	    		"})(files[i]);\n"+
	    	"}\n"+
		"}\n"+
		
		"input.addEventListener('drop', function(e) {\n"+
	   		"e.preventDefault();\n" +
	   		"e.stopPropagation();\n" +
	        "if(e.dataTransfer){\n" +
	           "if(e.dataTransfer.files.length) {\n" +
	           		"upload_files(e.dataTransfer.files);\n"+
	            "}\n" +
	        "}\n" +
		"}, false);\n"+
	        
		"var input2 = document.getElementById('file2');\n" +
		"input2.addEventListener('change', function() {\n"+
   			"upload_files(input2.files);\n"+
		"}, false);\n"+
	        
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

	/**
	 * Generate HTML Code as a String which creates an entire page which print the content of a directory
	 * @param session The session which is sending the request
	 * @param information Information about the request asking this code
	 * @return The generated HTML Code as a Response
	 */
	public static Response generateDirectory(FTPSession session, RestRequestInformation information) {
		String htmlResponse = "";

		try {
			htmlResponse = HtmlGenerator.generateHeader(information.getURI());
			htmlResponse += HtmlGenerator.generatorLogin(information);
			htmlResponse += HtmlGenerator.generateFTPFileList(session.getFTPClient().listFiles(), information);
			htmlResponse += HtmlGenerator.generatorUploadForm(information);
			htmlResponse += HtmlGenerator.generateFooter(information);
		} catch (IOException e) {
			return Response.status(Response.Status.GATEWAY_TIMEOUT).entity(HtmlErrorGenerator.ftpConnectionFailed(information, session)).type(MediaType.TEXT_HTML).build();
		}
		return Response.ok(htmlResponse, MediaType.TEXT_HTML).build();
	}

}
