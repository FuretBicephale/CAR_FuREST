package rest.ftp;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;

/**
 * @author cachera - falez
 * Static class which generate strings containing HTML code. 
 */
public class HTMLGenerator {
	
	/**
	 * Generate a HTML header with utf-8 encoding and information's URI as title.
	 * @param information Information of the request asking this header
	 * @return The HTML Code generated in a String
	 */
	public static String generateHeader(GetRestRequestInformation information) {
		return "<!DOCTYPE html>\n"+
				"<html lang=\"fr\">\n"+
					"<head>\n"+
						"<meta charset=\"utf-8\">\n"+
						"<title>"+information.getURI()+"</title>\n"+
					"</head>\n"+
				"<body>\n";
	}
	
	/**
	 * Generate a list of links which are files from a FTPServer. 
	 * Each link is a CWD or RETR request for the specified file according it's a folder or a file.
	 * @param ftpFiles The files to generate as a list
	 * @param information Information of the request asking this list
	 * @return The HTML Code generated in a String
	 */
	public static String generateFTPFileList(FTPFile[] ftpFiles, GetRestRequestInformation information) {
		List<FTPFile> listFile = Arrays.asList(ftpFiles);
		
		String htmlResponse = "<table>\n"+
								"<tr><th>Nom</th><th>Utilisateur</th><th>Derniere modification</th></tr>\n";
		
		String[] folders;
		folders = information.getURI().split("/");

		if(!information.getURI().equals("")) {
			
			//get parent directory
			String parentDirectory = "";
			for(int i = 0; i < folders.length-1; i++) {
				parentDirectory += folders+"/";
			}
			
			FTPFile parentEntry = new FTPFile();
			parentEntry.setName("Parent Directory");
			
			listFile.add(parentEntry);
		}
		
		for(int i = 0; i < listFile.size(); i++) {
			
			/* Wut?
			if(ftpFiles[i].getName().endsWith("~"))
				continue;
			*/


			SimpleDateFormat format = new SimpleDateFormat("HH:mm DD MMM yyyy"); 
			htmlResponse += "<tr><td><a href=\""+listFile.get(i).getName()+"\">"+listFile.get(i).getName()+(listFile.get(i).isDirectory() ? "/" : "")+"<a></td><td>"+listFile.get(i).getUser()+"</td><td>"+format.format(listFile.get(i).getTimestamp().getTime())+"</td></tr>\n";
		}
		
		htmlResponse += "</table>\n";
		
		return htmlResponse;
	}
	
	/**
	 * Generate a HTML footer in a String
	 * @param information  Information of the request asking this footer
	 * @return The HTML Code generated in a String
	 */
	public static String generateFooter(GetRestRequestInformation information) {
		return "</body></html>";
	}

}
