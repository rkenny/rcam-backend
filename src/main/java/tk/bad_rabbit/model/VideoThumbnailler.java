package tk.bad_rabbit.model;

import java.util.ArrayList;
import java.util.List;

import tk.bad_rabbit.vlc.VlcRunnable;

public class VideoThumbnailler {

  String videoPath;
  String videoFilename;
  String thumbnailFilename;
  Integer outputHeight;
  Integer outputWidth;
  
  public VideoThumbnailler(String videoPath, String videoFilename, String thumbnailFilename) {
    this.videoPath = videoPath;
    this.videoFilename = videoFilename;
    this.thumbnailFilename = thumbnailFilename;
    
    outputHeight = 200;
    outputWidth = 320;
  }
  
  public void setOutputHeight(Integer outputHeight) {
    this.outputHeight = outputHeight;
  }
  
  public void setOutputWidth(Integer outputWidth) {
    this.outputWidth = outputWidth;
  }
  
  public void createThumbnail() {
    List<String> args = new ArrayList<String>();
    args.add("--rc-fake-tty"); 
    args.add(videoPath + videoFilename);
    args.add("--video-filter=scene");
    args.add("--scene-format=png");
    args.add("--scene-ratio=10000");
    args.add("--start-time=0");
    args.add("--stop-time=0");
    args.add("--scene-width=" + outputWidth);
    args.add("--scene-height=" + outputHeight);
    args.add("--scene-path=" + videoPath);
    args.add("--scene-prefix=" + thumbnailFilename);
    args.add("--scene-replace");
    args.add("--run-time=1");
    args.add("vlc://quit");
    final String description = "Creating thumbnail";

    new VlcRunnable(description, args).run();    
  }
  
  public Runnable createRunnable() {
    return new Runnable() {
      public void run() {
        createThumbnail();
      }
    };
  }
  
}
