package tk.bad_rabbit.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Video {
  private String thumbnail;
  private List<String> streams;
  
  public String getThumbnail() {
    return thumbnail;
  }
  
  public List<String> getStreams() {
    return streams;
  }
  
  public Video() {
    streams = new ArrayList<String>();

  }
  
  public Video(File videoFile, String videoUrlPrefix, String thumbnailUrlPrefix, String videoName, String thumbnailName) {
    this();
    thumbnail = thumbnailUrlPrefix + videoFile.getName() + "/" + thumbnailName;
    streams.add(videoUrlPrefix + videoFile.getName());
  }
  
  
  
}
