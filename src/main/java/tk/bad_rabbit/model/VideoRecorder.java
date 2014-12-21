package tk.bad_rabbit.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.annotation.JsonProperty;


public class VideoRecorder implements Serializable {
  /**
   * Create a VideoRecorder by accessing /record, set duration via JSON
   */
  private static final long serialVersionUID = 6952325964443377011L;
  @JsonProperty
  private Integer duration;
  private List<VideoSource> videoSources;
  
  public VideoRecorder() {
    videoSources = new ArrayList<VideoSource>();
  }
  
  public void setVideoSources(List<VideoSource> videoSources) {
    this.videoSources = videoSources;
  }
  
  public void addVideoSource(VideoSource videoSource) {
    this.videoSources.add(videoSource);
  }
  
  public void addVideoSource(String videoSource) {
    this.videoSources.add(new VideoSource(videoSource));
  }
  
  public List<VideoSource> getVideoSources() {
    return videoSources;
  }
  
  public void setDuration(Integer duration) {
    this.duration = duration;
  }
  
  public Integer getDuration() { return duration; }
  
  @Override
  public String toString() {
    return "Video will record for " + duration + " seconds";
  }
  // 

  public void beginRecording() throws InterruptedException {
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch shutdownLatch = new CountDownLatch(videoSources.size());
    
    for(VideoSource videoSource : videoSources) {
//      videoSource.beginRecording(duration);
      videoSource.getLatchedRecordingThread(duration, startLatch, shutdownLatch).start();
    }
    Thread.sleep(200);
    startLatch.countDown();
    shutdownLatch.await();    
  }
}
