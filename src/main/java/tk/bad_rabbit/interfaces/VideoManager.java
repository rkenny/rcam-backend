package tk.bad_rabbit.interfaces;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tk.bad_rabbit.model.Video;

public class VideoManager {
  final String videoPath = "/usr/share/nginx/html/videos";
  final String videoUrlPrefix = "rtmp://localhost/live/";
  final String thumbnailUrlPrefix = "http://localhost/videos/";
  final String videoFilename = "video";
  final String thumbnailFilename = "thumbnail.png";
  
  List<Video> videos;
  
  public VideoManager() {
    populateVideos();
  }
  
  private void populateVideos() {
    videos = new ArrayList<Video>();
    File videoFolders = new File(videoPath);
    File[] videoFoldersArray = videoFolders.listFiles(); // gets the list of video+thumbnails, in a folder named their creationTime
    for(File videoFolder : videoFoldersArray) {
      videos.add(new Video(videoFolder, videoUrlPrefix, thumbnailUrlPrefix, videoFilename, thumbnailFilename));
    }
  }
  
  public List<Video> getAllVideos() {
    return videos;
  }
}
