package tk.bad_rabbit;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

public class ResponseCorsFilter implements ContainerResponseFilter {

  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    // TODO Auto-generated method stub 
    MultivaluedMap<String, Object> responseHeaders = responseContext.getHeaders();
    responseHeaders.add("Access-Control-Allow-Origin", "*");
    responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

    MultivaluedMap<String, String> requestHeaders = requestContext.getHeaders();
    if(null != requestHeaders && (requestHeaders.get("Access-Control-Request-Headers") != null)) {
      responseHeaders.add("Access-Control-Allow-Headers", requestHeaders.get("Access-Control-Request-Headers"));
    }
  }
}