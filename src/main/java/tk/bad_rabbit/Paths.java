package tk.bad_rabbit;

import java.net.URI;

public class Paths {
  /**
   * "Hello World" root resource path.
   */
  public static final String ROOT_PATH = "helloworld";
  
  private static final Integer PORT = 8080;
  private static final String SERVER =  "localhost";
  private static final String CONTEXT = "base";
  
  public static final URI BASE_URI = URI.create("http://" + SERVER + ":" + PORT + "/" + CONTEXT + "/");
}
