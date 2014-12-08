package tk.bad_rabbit.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import tk.bad_rabbit.model.VideoRecorder;


@Path(value = "/record")
public class Record {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response beginRecording(VideoRecorder recorder) throws Exception {
    recorder.beginRecording();
    return Response.status(200).entity(recorder.toString()).build();
  }
}
