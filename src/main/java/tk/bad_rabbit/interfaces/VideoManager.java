package tk.bad_rabbit.interfaces;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tk.bad_rabbit.iface.ApplicationConfiguration;
import tk.bad_rabbit.model.Video;
import tk.bad_rabbit.vlc.iface.VlcConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Component
public class VideoManager {
  
  @Autowired
  @Qualifier("ApplicationConfigurationImpl")
  ApplicationConfiguration applicationConfiguration;
  
  String videoPath;
  String videoUrlPrefix;
  String thumbnailUrlPrefix;
  String videoFilename;
  String thumbnailFilename;
  
  
  // why is this working? - Find out.
  @PostConstruct
  public void initVideoManager() {
    videoPath = applicationConfiguration.getVideoPath();
    videoUrlPrefix = "rtmp://localhost/live/";
    thumbnailUrlPrefix = "http://localhost/videos/";
    videoFilename = "video";
    thumbnailFilename = "thumbnail.png";      
  }
   
  public List<Video> getAllVideos() {
    List<Video> videos = new ArrayList<Video>();
    File videoFolders = new File(videoPath);
    File[] videoFoldersArray = videoFolders.listFiles(); // gets the list of video+thumbnails, in a folder named their creationTime
    for(File videoFolder : videoFoldersArray) {
      videos.add(new Video(videoFolder, videoUrlPrefix, thumbnailUrlPrefix, videoFilename, thumbnailFilename));
    }
    
    return videos;
  }
}
