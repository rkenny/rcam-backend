package tk.bad_rabbit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.spring.SpringComponentProvider;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	/**
	 * Main application entry point.
	 *
	 * @param args application arguments.
	 */
	public static void main(String[] args) {
	  //String configFilename = "rcam.conf";
	 
	  //ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(configFilename);
	  //VlcConfiguration vlcConfiguration = applicationConfiguration.newVlcConfiguration();
	  //HttpdConfiguration httpdConfiguration = applicationConfiguration.newHttpdConfiguration();
	  
		try {
			System.out.println("\"Hello World\" Jersey Example App");
			
			//RCamApplication resourceConfig = new RCamApplication();
			AppResources appResources = new AppResources();
			appResources.unpack();
			
	
			
			WebappContext ctx = new WebappContext("ctx", "/base");
			ctx.addContextInitParameter("contextConfigLocation", "classpath:/applicationContext.xml");
			ctx.addListener("org.springframework.web.context.ContextLoaderListener");
			ctx.addListener("org.springframework.web.context.request.RequestContextListener");
			
			 // Create a servlet registration for the web application in order to wire up Spring managed collaborators to Jersey resources.
			ServletRegistration servletRegistration = ctx.addServlet("jersey-servlet", ServletContainer.class);
			servletRegistration.setInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters", "com.sun.jersey.api.container.filter.LoggingFilter");
			servletRegistration.setInitParameter("com.sun.jersey.spi.container.ContainerRequestFilters", "com.sun.jersey.api.container.filter.LoggingFilter");
			servletRegistration.setInitParameter("javax.ws.rs.Application", "tk.bad_rabbit.RCamApplication");
			servletRegistration.setInitParameter("com.sun.jersey.config.property.packages", "com.test");
			servletRegistration.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
			servletRegistration.addMapping("/*");
			
			
			 // Initialize Grizzly HttpServer
      HttpServer server = new HttpServer();
      NetworkListener listener = new NetworkListener("grizzly2", "localhost", 8080);
      server.addListener(listener);
			
			ctx.deploy(server);
			
			server.start();
			
			
			//final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(Paths.BASE_URI, resourceConfig);
			System.out.println(
					String.format("Application started.%n"
							+ "Try out %s%s%n"
							+ "Hit enter to stop it...",
							Paths.BASE_URI, Paths.ROOT_PATH));
			System.in.read();
			server.shutdownNow();
			appResources.cleanup();
		} catch (IOException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}

	}



}