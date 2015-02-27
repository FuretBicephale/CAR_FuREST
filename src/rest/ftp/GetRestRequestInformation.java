package rest.ftp;

import java.util.regex.Pattern;

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
