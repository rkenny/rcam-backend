package tk.bad_rabbit.model;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import tk.bad_rabbit.interfaces.Cleanup;
import tk.bad_rabbit.vlc.VlcRunnable;

public class VideoSource implements Cleanup {
  private String videoSource;
  private String destFileName;
  private String channelId;
  private Integer videoSourceId;
  private static Integer VideoSourceIdCount = 0;
  public VideoSource(String videoSource, String destFileName) {
    this(videoSource);
    setDestFileName(destFileName);
  }
  
  public VideoSource(String videoSource) {
    this();
    setVideoSource(videoSource);
    destFileName = new Date().getTime() +".mpg";
  }

  public VideoSource() {
    this.videoSourceId = VideoSourceIdCount++;
    this.channelId = "channel" + videoSourceId;
  }
  
  public void setDestFileName(String destFileName) {
    this.destFileName = destFileName;
  }

  public String getVideoSource() {
    return videoSource;
  }
  
  public void setVideoSource(String videoSource) {
    this.videoSource = videoSource;
  }
  
  public String getChannelId() {
    return channelId;
  }
  
  public String createVlmChannel() {
    StringBuilder vlmChannelSb = new StringBuilder();
    
    vlmChannelSb.append("new " + channelId + " broadcast\n");
    vlmChannelSb.append("setup " + channelId + " input \"" + destFileName + "\"\n");
    //vlmChannelSb.append("setup " + channelId + " output #duplicate{dst=mosaic-bridge{id="+videoSourceId+",height=640,width=480},select=video,dst=bridge-out{id="+videoSourceId+"},select=audio}\n"); 
    vlmChannelSb.append("setup " + channelId + " output #duplicate{dst=mosaic-bridge{id="+videoSourceId+",height=640,width=480},select=video,dst=bridge-out{id="+videoSourceId+"},select=audio}\n");
    vlmChannelSb.append("setup " + channelId + " enabled\n");
    return vlmChannelSb.toString();
  }
  
  public VlcRunnable createRunnable(Integer duration, CountDownLatch startLatch, CountDownLatch shutdownLatch) {
    List<String> args = new ArrayList<String>();
    args.add("--rc-fake-tty");
    args.add("--run-time");
    args.add(duration.toString());
    args.add(videoSource);
    // TEMPORARY
    args.add("--input-slave=alsa://plughw:0,0");
    // END TEMPORARY
    args.add("vlc://quit");
    args.add("--sout");
    args.add("#transcode{vcodec=h264,acodec=mp3,samplerate=22050,channels=1}:std{access=file,mux=ps,dst="+destFileName+"}");

    final String description =  "VideoSource:["+videoSource+"] destFile:["+destFileName+"]";
    return new VlcRunnable(description, args, startLatch, shutdownLatch);
  }
  
  public void cleanup() {
    try {
      Files.deleteIfExists(FileSystems.getDefault().getPath(".", destFileName));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
