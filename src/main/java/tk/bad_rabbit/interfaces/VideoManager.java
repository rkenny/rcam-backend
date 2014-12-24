package tk.bad_rabbit.interfaces;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tk.bad_rabbit.model.Video;

public class VideoManager {
  final String videoPath = "./videos";
  List<Video> videos;
  
  public VideoManager() {
    populateVideos();
  }
  
  private void populateVideos() {
    videos = new ArrayList<Video>();
    File videoFolder = new File(videoPath);
    File[] videoFilesArray = videoFolder.listFiles();
    for(File videoFile : videoFilesArray) {
      videos.add(new Video(videoFile));
    }
  }
  
  public List<Video> getAllVideos() {
    return videos;
  }
}
