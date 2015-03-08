package rest.ftp.output.html;

import java.io.IOException;

import rest.ftp.FTPSession;
import rest.ftp.RestRequestInformation;

/**
 * @author cachera - falez
 * Static class which generate strings containing HTML code for error page. 
 */
public class HtmlErrorGenerator {
	
	/**
	 * Generate HTML for an error page caused by a failed connection.
	 * @param information Information about the failed request
	 * @param session The session which caused the error
	 * @return The HTML Code generated in a String
	 */
	public static String ftpConnectionFailed(RestRequestInformation information, FTPSession session) {
		String htmlResponse = "";
		htmlResponse = HtmlGenerator.generateHeader(information.getURI());
		htmlResponse += "<h1>Unable to connect to ftp server ("+session.getAddress()+":"+session.getPort()+")</h1>";
		htmlResponse += HtmlGenerator.generateFooter(information);
		return htmlResponse;
	}

	/**
	 * Generate HTML for an error caused by an error sent by the FTP Server
	 * @param information Information about the failed request
	 * @param session The session which caused the error
	 * @param code The return error code sent by the FTP Server
	 * @return The HTML Code generated in a String
	 */
	public static String ftpBadAnswer(RestRequestInformation information, FTPSession session, int code) {
		String htmlResponse = "";
		htmlResponse = HtmlGenerator.generateHeader(information.getURI());
		htmlResponse += "<h1>FTP server return bad answer. Code : "+code+"</h1>";
		htmlResponse += HtmlGenerator.generateFooter(information);
		return htmlResponse;
	}

}
