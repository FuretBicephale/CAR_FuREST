package rest.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.ws.rs.Produces;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.io.SocketOutputStream;

public class GetRestRequest {

	/**
	 * Send a RETR Request to the FTP Server for the file designed by session uri and return the file as a byte array to be download
	 * @param session The session which send the RETR Request
	 * @param information Information about the FTP Request
	 * @return A byte array containing the file if it's found, null otherwise
	 */
	@Produces("application/octet-stream")
	public static byte[] getFile(FTPSession session, GetRestRequestInformation information) throws IOException {
		int length;
		byte[] buffer = null;

		InputStream stream = session.getFTPClient().retrieveFileStream(information.getURI());
		if(stream.available() == 0) {
			buffer = new byte[0];
		}
		
		while((length = stream.available()) > 0) {

			int oldLength = buffer != null ? buffer.length : 0;
			byte[] bufferInternal = new byte[length+oldLength];

			if(buffer != null)
				System.arraycopy(buffer, 0, bufferInternal, 0, buffer.length);

			stream.read(bufferInternal, oldLength, length);
			buffer = bufferInternal;

		}
		
		stream.close();
		
		return buffer;
		
	}

	/**
	 * Send a LIST Request to the FTP Server for the directory designed by session uri and return a HTML page as a byte array containing the list of the folder's files
	 * @param session The session which send the LIST Request
	 * @param information Information about the FTP Request
	 * @return A byte array which contains HTML Code to display the directory content
	 */
	@Produces("text/html")
	public static byte[] getDirectory(FTPSession session, GetRestRequestInformation information) {
		String htmlResponse = "";

		try {
			htmlResponse = HTMLGenerator.generateHeader(information);
			htmlResponse += HTMLGenerator.generateFTPFileList(session.getFTPClient().listFiles(), information);
			htmlResponse += HTMLGenerator.generateFooter(information);
		} catch (IOException e) {
			System.err.println("Unable to connect to FTP Server.");
		}

		return htmlResponse.getBytes();
	}

}
