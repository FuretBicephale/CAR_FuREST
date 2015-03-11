package rest.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import rest.api.JaxRsApiApplication;

/**
 * A REST Application configuration launched in a REST Server.
 * The application is accessible at serverAddress/api with serverAddress the server address.
 * @author cachera - falez
 */
@Configuration
public class AppConfig {	
	
	@Bean(destroyMethod = "shutdown")
	public SpringBus cxf() {
		return new SpringBus();
	}
	
	@Bean @DependsOn("cxf")
	public Server jaxRsServer() {
		
		JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(new JaxRsApiApplication(), JAXRSServerFactoryBean.class);
		
		List<Object> serviceBeans = new ArrayList<Object>();
		serviceBeans.add(new rest.ftp.RestToFtpResource());
		
		factory.setServiceBeans(serviceBeans);
		factory.setAddress("/" + factory.getAddress());
		factory.setProviders(Arrays.<Object>asList(jsonProvider()));
		return factory.create();
		
	}

	@Bean
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}
}
