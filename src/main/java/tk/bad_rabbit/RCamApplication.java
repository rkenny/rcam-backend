package tk.bad_rabbit;

import org.glassfish.jersey.server.ResourceConfig;

public class RCamApplication extends ResourceConfig {
  public RCamApplication() {
    packages("tk.bad_rabbit.rest");
    register(new ResponseCorsFilter());
    
  }
}
