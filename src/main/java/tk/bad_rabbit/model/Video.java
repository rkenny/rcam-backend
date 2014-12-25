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
//    thumbnail = "http://www.gifwave.com/media/355224/panda-animals-playing-stupid-posing_200s.gif";
//    streams.add("http://www.youtube.com/embed/j1Al9KzhQUw?version=3&enablejsapi=1");
//    streams.add("http://www.youtube.com/embed/mmlTByKgX6w?version=3&enablejsapi=1");
//    streams.add("http://www.youtube.com/embed/pHcF7iC984U?version=3&enablejsapi=1");
  }
  
  public Video(File videoFile, String videoUrlPrefix) {
    this();
    thumbnail = "http://www.gifwave.com/media/355224/panda-animals-playing-stupid-posing_200s.gif";
    streams.add(videoUrlPrefix + videoFile.getName());
  }
  
  
  
}
