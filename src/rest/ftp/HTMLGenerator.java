package rest.ftp;

import org.apache.commons.net.ftp.FTPFile;

/**
 * @author cachera - falez
 * Static class which generate strings containing HTML code. 
 */
public class HTMLGenerator {
	
	/**
	 * Generate a HTML header with utf-8 encoding and information's URI as title.
	 * @param information Information of the page containing the header to be generated
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
	 * @return The HTML Code generated in a String
	 */
	public static String generateFTPFileList(FTPFile[] ftpFiles) {
		String htmlResponse = "";
		
		for(int i = 0; i < ftpFiles.length; i++) {
			
			if(ftpFiles[i].getName().endsWith("~"))
				break;
			
			htmlResponse += "<a href=\"ftp/" + ftpFiles[i].getName() + "\">" + ftpFiles[i] + "</a><br/>";
			
		}
		
		return htmlResponse;
	}
	
	/**
	 * Generate a HTML footer in a String
	 * @param information Information of the page containing the footer to be generated
	 * @return The HTML Code generated in a String
	 */
	public static String generateFooter(GetRestRequestInformation information) {
		return "</body></html>";
	}

}
