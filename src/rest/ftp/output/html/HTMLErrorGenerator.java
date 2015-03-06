package rest.ftp.output.html;

import java.io.IOException;

import rest.ftp.FTPSession;
import rest.ftp.GetRestRequestInformation;

public class HTMLErrorGenerator {
	
	public static String ftpConnectionFailed(GetRestRequestInformation information, FTPSession session) {
		String htmlResponse = "";
		htmlResponse = HTMLGenerator.generateHeader(information.getURI());
		htmlResponse += "<h1>Unable to connect to ftp server ("+session.getAddress()+":"+session.getPort()+")</h1>";
		htmlResponse += HTMLGenerator.generateFooter(information);
		return htmlResponse;
	}

}
