package rest.ftp;

import java.util.regex.Pattern;

import javax.ws.rs.core.UriInfo;

public class GetRestRequestInformation {
	
	private String uri;
	private String path;
	private UriInfo uriInfo;
	
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
