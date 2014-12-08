package tk.bad_rabbit.rest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tk.bad_rabbit.App;
import tk.bad_rabbit.model.Video;

@Path(value = "/videos")
public class Videos {
 ObjectMapper mapper = new ObjectMapper();
  
  @GET
  public String sendVideoList() {
    System.out.println("Sending video list to client");
    Video videoTest = new Video();
    String videoJson = "{}";
    try {
      videoJson = mapper.writeValueAsString(videoTest);
    } catch (JsonGenerationException e) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
      System.out.println("json generation exception");
      e.printStackTrace();
    } catch (JsonMappingException e) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
      System.out.println("json mapping exception");
      e.printStackTrace();
    } catch (IOException e) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
      System.out.println("io exception");
      e.printStackTrace();
    }
    
    return videoJson;
  }
}
