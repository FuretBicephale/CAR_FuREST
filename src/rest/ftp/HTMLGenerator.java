package rest.ftp;

import org.apache.commons.net.ftp.FTPFile;

public class HTMLGenerator {
	
	public static String generateHeader(GetRestRequestInformation information) {
		return "<!DOCTYPE html>\n"+
				"<html lang=\"fr\">\n"+
					"<head>\n"+
						"<meta charset=\"utf-8\">\n"+
						"<title>"+information.getURI()+"</title>\n"+
					"</head>\n"+
				"<body>\n";
	}
	
	public static String generateFTPFileList(FTPFile[] ftpFiles) {
		String htmlResponse = "";
		
		for(int i = 0; i < ftpFiles.length; i++) {
			
			if(ftpFiles[i].getName().endsWith("~"))
				break;
			
			htmlResponse += "<a href=\"ftp/" + ftpFiles[i].getName() + "\">" + ftpFiles[i] + "</a><br/>";
			
		}
		
		return htmlResponse;
	}
	
	public static String generateFooter(GetRestRequestInformation information) {
		return "</body></html>";
	}

}
