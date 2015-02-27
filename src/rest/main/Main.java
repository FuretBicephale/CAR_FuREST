package rest.main;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import rest.config.AppConfig;

/**
 * @author cachera - falez
 * Launches a REST server listening on port 2342.
 */
public class Main {

	/**
	 * Creates and launches a REST server listening on port 2342.
	 * The server is accessible at ipAddress:2342/rest with ipAddress the server IP.
	 */
	public static void main(final String[] args) {
		
		System.setProperty( "file.encoding", "UTF-8" );
		
		Server server = new Server(8080);
 		final ServletHolder servletHolder = new ServletHolder(new CXFServlet());
 		final ServletContextHandler context = new ServletContextHandler();	
 		
 		context.setContextPath("/");
 		context.addServlet(servletHolder, "/rest/*"); 	
 		context.addEventListener(new ContextLoaderListener());
 		context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
 		context.setInitParameter("contextConfigLocation", AppConfig.class.getName());
 		 		
        server.setHandler(context);
        
        try {
			server.start();
	        server.join();
		} catch (Exception e) {
			System.err.println("Unable to start the server!");
		}
        
	}

}
