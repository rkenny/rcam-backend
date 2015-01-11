package tk.bad_rabbit.rest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.stereotype.Component;

@Component
@WebFilter(urlPatterns = {"/*"}, description = "Session Checker Filter")
public class ResponseCorsFilter implements ContainerResponseFilter {
  
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
          throws IOException {

      MultivaluedMap<String, Object> headers = responseContext.getHeaders();

      headers.add("Access-Control-Allow-Origin", "*");
      headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");            
      headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type");
  }
}
