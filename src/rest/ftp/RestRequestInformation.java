package rest.ftp;

import javax.ws.rs.core.UriInfo;
/**
 * @author cachera - falez
 * Contains informations about a GET Request of our REST Application.
 * It contains the URI of the Request and the complete path of the file referred by the uri
 */
public class RestRequestInformation {
	
	private String uri;
	
	private String path;
	private UriInfo uriInfo;
	
	public RestRequestInformation() {
	
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
	
	public void setUriInfo(UriInfo info) {
		this.uriInfo = info;
	}
	
	public UriInfo getUriInfo() {
		return this.uriInfo;
	}
	
	public String getCompleteURI() {
		return this.path+"/"+this.uri;
	}

	public String getPath() {
		return this.path;
	}	

}
