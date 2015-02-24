package rest.ftp;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Produces;

public class GetRestRequest {
		
	@Produces("application/octet-stream")
	public static byte[] getFile(FTPSession session, GetRestRequestInformation information) throws IOException {
		InputStream stream = session.getFTPClient().retrieveFileStream(information.getURI());
		
		int length;
		byte[] buffer = null;
		
		while((length = stream.available()) > 0) {
			
			int oldLength = buffer != null ? buffer.length : 0;
			
			byte[] bufferInternal = new byte[length+oldLength];
			
			if(buffer != null)
				System.arraycopy(buffer, 0, bufferInternal, 0, buffer.length);

			stream.read(buffer, oldLength, length);
				
			buffer = bufferInternal;
		}
		
		return buffer;
	}

	@Produces("text/html")
	public static byte[] getDirectory(FTPSession session, GetRestRequestInformation information) {
		String htmlResponse = "";
		
		try {
			htmlResponse = HTMLGenerator.generateHeader(information);
			htmlResponse = HTMLGenerator.generateFTPFileList(session.getFTPClient().listFiles());
			htmlResponse += HTMLGenerator.generateFooter(information);
		} catch (IOException e) {
			System.err.println("Unable to connect to FTP Server.");
		}
		
		return htmlResponse.getBytes();
	}

}
