package tk.bad_rabbit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	/**
	 * Main application entry point.
	 *
	 * @param args application arguments.
	 */
	public static void main(String[] args) {
	  //String configFilename = "rcam.conf";
	  //ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring.xml");;
	  
	  //ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(configFilename);
	  //VlcConfiguration vlcConfiguration = applicationConfiguration.newVlcConfiguration();
	  //HttpdConfiguration httpdConfiguration = applicationConfiguration.newHttpdConfiguration();
	  
		try {
			System.out.println("\"Hello World\" Jersey Example App");
			
			RCamApplication resourceConfig = new RCamApplication();
			AppResources appResources = new AppResources();
			appResources.unpack();
			final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(Paths.BASE_URI, resourceConfig);
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