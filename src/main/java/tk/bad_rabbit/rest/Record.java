package tk.bad_rabbit.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tk.bad_rabbit.iface.ApplicationConfiguration;
import tk.bad_rabbit.model.VideoRecorder;
import tk.bad_rabbit.model.VideoSource;

@Component
@Path("/record")
public class Record {
  
  @Autowired
  ApplicationConfiguration applicationConfiguration;
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response beginRecording(VideoRecorder recorder) throws Exception {
    System.out.println("recording");
    String currentTimeMs = Long.toString(System.currentTimeMillis());
    System.out.println("Client requested a recording at " + currentTimeMs + " for " + recorder.getDuration() + "s");
    
    recorder.setTempPath(applicationConfiguration.getTempPath());
    recorder.setOutputPath(applicationConfiguration.getVideoPath());
    
    recorder.toString();
       
    try {
      recorder.setRecordingSuffix(currentTimeMs);
      recorder.createOutputVideo();
    } catch(Exception e) {
      return Response.status(500).entity("Recording error").build();
    }
    return Response.status(200).entity(recorder.toString()).build();
  }
}
