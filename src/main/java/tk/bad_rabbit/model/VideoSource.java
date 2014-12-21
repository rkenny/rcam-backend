package tk.bad_rabbit.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import tk.bad_rabbit.vlc.VlcRunnable;
import tk.bad_rabbit.vlc.VlcThread;

public class VideoSource {
  private String videoSource;
  private String destFileName;
  
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
  
  public VlcRunnable createVlcRunnable(Integer duration, CountDownLatch startLatch, CountDownLatch shutdownLatch) {
    List<String> args = new ArrayList<String>();
    args.add("--rc-fake-tty");
    args.add("--run-time");
    args.add(duration.toString());
    args.add(videoSource);
    args.add("vlc://quit");
    args.add("--sout");
    args.add("#transcode{vcodec=mp4v,acodec=mpga,vb=800}:std{access=file,mux=ts,dst="+destFileName+"}");

    final String description =  "VideoSource:["+videoSource+"] destFile:["+destFileName+"]";
    return new VlcRunnable(description, args, startLatch, shutdownLatch);
  }
  
  public Thread getLatchedRecordingThread(Integer duration, CountDownLatch startLatch, CountDownLatch shutdownLatch) {
    return new Thread(createVlcRunnable(duration, startLatch, shutdownLatch));
  }
  
//  public void beginRecording(Integer duration) {
//    Thread vlcThread = new Thread(createVlcRunnable(duration));
//    vlcThread.start();
//    
//  }
}
