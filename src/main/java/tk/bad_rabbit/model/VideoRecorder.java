package tk.bad_rabbit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tk.bad_rabbit.interfaces.Cleanup;

import com.fasterxml.jackson.annotation.JsonProperty;


public class VideoRecorder implements Serializable, Cleanup {
  /**
   * Create a VideoRecorder by accessing /record, set duration via JSON
   */
  private static final long serialVersionUID = 6952325964443377011L;
  @JsonProperty
  private Integer duration;
  private List<VideoSource> videoSources;
  VideoMosaicer videoMosaicer;
  ExecutorService executorService;
  CountDownLatch startLatch;
  
  public VideoRecorder() {
    videoSources = new ArrayList<VideoSource>();
    executorService = Executors.newFixedThreadPool(5);
  }
  
  public void prepareToRecord(List<VideoSource> videoSources) {
    videoMosaicer = new VideoMosaicer(duration, videoSources);
    this.setVideoSources(videoSources);
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
  
  public void beginRecording(CountDownLatch shutdownLatch) {
    startLatch = new CountDownLatch(1);
    for(VideoSource videoSource : videoSources) {
      executorService.execute(videoSource.createVlcRunnable(duration, startLatch, shutdownLatch));
    }
    try {
      Thread.sleep(200);
      startLatch.countDown();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void createOutputVideo() {
    executorService.execute(new Runnable() {
      public void run() {
        CountDownLatch shutdownLatch = new CountDownLatch(videoSources.size());
        beginRecording(shutdownLatch);
        try {
          shutdownLatch.await();
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        videoMosaicer.createMosaic();
        cleanup();
      }
    });
    
    
  }

  public void cleanup() {
    videoMosaicer.cleanup();
    for(VideoSource videoSource : videoSources) {
      videoSource.cleanup();
    }    
  }
}
