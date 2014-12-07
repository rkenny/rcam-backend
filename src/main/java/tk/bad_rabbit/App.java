package tk.bad_rabbit;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;

/**
 * This is the example entry point, where Jersey application for the example
 * gets populated and published using the Grizzly 2 HTTP container.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public class App {

	/**
	 * Main application entry point.
	 *
	 * @param args application arguments.
	 */
	public static void main(String[] args) {
		try {
			System.out.println("\"Hello World\" Jersey Example App");
			
			RCamApplication resourceConfig = new RCamApplication();
			
			final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(Paths.BASE_URI, resourceConfig);
			System.out.println(
					String.format("Application started.%n"
							+ "Try out %s%s%n"
							+ "Hit enter to stop it...",
							Paths.BASE_URI, Paths.ROOT_PATH));
			System.in.read();
			server.shutdownNow();
		} catch (IOException ex) {
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
		}

	}



}