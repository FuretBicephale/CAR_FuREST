package rest.ftp;

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
	
	public static String generateFooter(GetRestRequestInformation information) {
		return "</body></html>";
	}

}
