package rest.ftp.output.html;

import java.io.IOException;

import rest.ftp.FTPSession;
import rest.ftp.RestRequestInformation;

public class HtmlErrorGenerator {
	
	public static String ftpConnectionFailed(RestRequestInformation information, FTPSession session) {
		String htmlResponse = "";
		htmlResponse = HtmlGenerator.generateHeader(information.getURI());
		htmlResponse += "<h1>Unable to connect to ftp server ("+session.getAddress()+":"+session.getPort()+")</h1>";
		htmlResponse += HtmlGenerator.generateFooter(information);
		return htmlResponse;
	}

}
