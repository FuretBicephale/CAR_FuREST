package rest.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author cachera - falez
 * A REST Application definition launched in a REST Server. It defines Application pathname and contents.
 * The application is accessible at serverAddress/api with serverAddress the server address.
 */
@ApplicationPath("api")
public class JaxRsApiApplication extends Application {
	
}
