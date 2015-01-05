package tk.bad_rabbit;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import tk.bad_rabbit.impl.ApplicationConfigurationImpl;

public class RCamApplication extends ResourceConfig {
  
  public RCamApplication() {
    packages("tk.bad_rabbit.rest", "com.fasterxml.jackson.jaxrs.json");
    register(RequestContextFilter.class);
    register(ApplicationConfigurationImpl.class);
    register(new ResponseCorsFilter());
  }
}
