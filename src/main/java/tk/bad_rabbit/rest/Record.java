package tk.bad_rabbit.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import tk.bad_rabbit.model.VideoRecorder;
import tk.bad_rabbit.model.VideoSource;


@Path(value = "/record")
public class Record {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response beginRecording(VideoRecorder recorder) throws Exception {
    
    List<VideoSource> videoSources = new ArrayList<VideoSource>();
    String currentTimeMs = Long.toString(System.currentTimeMillis());
    
    System.out.println("Client requested a recording at " + currentTimeMs + " for " + recorder.getDuration() + "s");
    
    videoSources.add(new VideoSource("v4l2:///dev/video1", "video1-" + currentTimeMs));
    videoSources.add(new VideoSource("http://192.168.1.2:8080/?action=stream", "ion1-"+currentTimeMs));
    try {
      recorder.setVideoSources(videoSources);
      recorder.prepareFolders();
      recorder.createOutputVideo();
    } catch(Exception e) {
      return Response.status(500).entity("Recording error").build();
    }
    return Response.status(200).entity(recorder.toString()).build();
  }
}
