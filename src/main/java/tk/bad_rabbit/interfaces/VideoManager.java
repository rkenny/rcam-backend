package tk.bad_rabbit.interfaces;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tk.bad_rabbit.model.Video;

public class VideoManager {
  final String videoPath = "/usr/share/nginx/html/videos";
  final String videoUrlPrefix = "rtmp://localhost/live/";
  List<Video> videos;
  
  public VideoManager() {
    populateVideos();
  }
  
  private void populateVideos() {
    videos = new ArrayList<Video>();
    File videoFolder = new File(videoPath);
    File[] videoFilesArray = videoFolder.listFiles();
    for(File videoFile : videoFilesArray) {
      videos.add(new Video(videoFile, videoUrlPrefix));
    }
  }
  
  public List<Video> getAllVideos() {
    return videos;
  }
}
