package rest.ftp;

import java.util.regex.Pattern;

/**
 * @author cachera - falez
 * Contains informations about a GET Request of our REST Application.
 * It contains the URI of the Request and the complete path of the file referred by the uri
 */
public class GetRestRequestInformation {
	
	private String uri;
	
	private String path;
	
	public GetRestRequestInformation() {
	
	}
	
	public String getURI() {
		return this.uri;
	}
	
	public void setURI(String uri) {
		this.uri = uri;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getCompleteURI() {
		return this.path+"/"+this.uri;
	}

	public String getPath() {
		return this.path;
	}	

}
