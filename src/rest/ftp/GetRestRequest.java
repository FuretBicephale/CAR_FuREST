package rest.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import rest.exception.FTPBadAnswerException;
import rest.ftp.output.html.HtmlGenerator;
import rest.ftp.output.json.JsonGenerator;

/**
 * A class which only contains two static method used to send a GET or a RETR Request to a FTPServer
 * @author cachera - falez
 */
public class GetRestRequest {

	/**
	 * Send a RETR Request to the FTP Server for the file designed by session uri and return the file as a byte array to be download
	 * @param session The session which send the RETR Request
	 * @param information Information about the FTP Request
	 * @return A byte array containing the file if it's found, null otherwise
	 * @throws FTPBadAnswerException 
	 */

	public static Response getFile(FTPSession session, RestRequestInformation information) throws IOException, FTPBadAnswerException {
		int length;
		byte[] buffer = null;

		InputStream stream = session.getFTPClient().retrieveFileStream(information.getURI());
		
		if(FTPReply.isNegativePermanent(session.getFTPClient().getReplyCode()) || FTPReply.isNegativeTransient(session.getFTPClient().getReplyCode())) {
			throw new FTPBadAnswerException(session.getFTPClient().getReplyCode());
		}
		
		int temporyBufferSize = 1024;
		
		byte[] temporyBuffer = new byte[temporyBufferSize];
		int nbRead;
		
		while((nbRead = stream.read(temporyBuffer, 0, temporyBufferSize)) > 0) {
			int oldLength = buffer != null ? buffer.length : 0;
			
			byte[] buffer2 = new byte[oldLength+nbRead];
			
			if(buffer != null) {
				System.arraycopy(buffer, 0, buffer2, 0, oldLength);
			}
			
			System.arraycopy(temporyBuffer, 0, buffer2, oldLength, nbRead);
			
			buffer = buffer2;
			
		}
		
		stream.close();
		
		return Response.ok(buffer, MediaType.APPLICATION_OCTET_STREAM).build();
		
	}
	
	/**
	 * Send a LIST Request to the FTP Server for the directory designed by session uri and return a HTML page by default or other format as a byte array containing the list of the folder's files
	 * @param session The session which send the LIST Request
	 * @param information Information about the FTP Request
	 * @return A byte array which contains HTML Code to display the directory content
	 * @throws IOException 
	 */
	public static Response getDirectory(FTPSession session, RestRequestInformation information) {
		String output = "html";
		if(information.getUriInfo().getQueryParameters().containsKey("output")) {
			output = information.getUriInfo().getQueryParameters().get("output").get(0);
		}
		
		if(output.equals("json")) {
			return JsonGenerator.generateDirectory(session, information);
		}
		else {
			return HtmlGenerator.generateDirectory(session, information);
		}
	}
	
	/**
	 * Return an ordered list of files contained in a directory asked by the URI.
	 * The first file is the equivalent of "..", the followings are the folders and it finish with the regular files.
	 * @param ftpFiles The list of files to order and return
	 * @param information Information about the request asking the files
	 * @return An ordered map containing the asked files
	 */
	public static Map<String, FTPFile> getDirectoryList(FTPFile[] ftpFiles, RestRequestInformation information) {
		Map<String, FTPFile> listFile = new LinkedHashMap<String, FTPFile>();
		
		String[] folders;
		folders = information.getURI().split("/");

		// Add parent directory link
		if(!information.getURI().equals("")) {
			
			//get parent directory
			String parentDirectory = "/";
			
			for(int i = 0; i < folders.length-1; i++) {
				parentDirectory += folders[i]+"/";
			}
			
			FTPFile parentEntry = new FTPFile();
			parentEntry.setName(parentDirectory);
			parentEntry.setType(FTPFile.DIRECTORY_TYPE);
			
			listFile.put("Parent Directory", parentEntry);
		}
		
		String prefix = (information.getURI().equals("") ? "" : "/"+information.getURI());
		
		//Add directories first
		Map<String, FTPFile> alphaSortedListDir = new TreeMap<String, FTPFile>();
		for(int i = 0; i < ftpFiles.length; i++) {
			if(ftpFiles[i].isDirectory()) {
				String name = ftpFiles[i].getName();
				ftpFiles[i].setName(prefix+(name.startsWith("/") ? name : "/"+name));
				alphaSortedListDir.put(name+"/", ftpFiles[i]);	
			}
		}
		
		// Then regular files
		Map<String, FTPFile> alphaSortedListFiles = new TreeMap<String, FTPFile>();
		for(int i = 0; i < ftpFiles.length; i++) {
			if(!ftpFiles[i].isDirectory()) {
				String name = ftpFiles[i].getName();
				ftpFiles[i].setName(prefix+(name.startsWith("/") ? name : "/"+name));
				alphaSortedListFiles.put(name, ftpFiles[i]);	
			}
		}
		
		listFile.putAll(alphaSortedListDir);
		listFile.putAll(alphaSortedListFiles);
		return listFile;
	}

}
