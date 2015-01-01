package tk.bad_rabbit.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tk.bad_rabbit.iface.ApplicationConfiguration;
import tk.bad_rabbit.interfaces.Cleanup;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class VideoRecorder implements Serializable, Cleanup {
  /**
   * Create a VideoRecorder by accessing /record, set duration via JSON
   */
  private static final long serialVersionUID = 6952325964443377011L;
  @JsonProperty
  private Integer duration;
  private List<VideoSource> videoSources;
  ExecutorService executorService;
  CountDownLatch startLatch;
  
  @Autowired
  ApplicationConfiguration applicationConfiguration;
  
  final String currentTimeMs = Long.toString(System.currentTimeMillis());
  final String outputPath = applicationConfiguration.getVideoPath() + currentTimeMs + "/";
  final String videoFilename = "video";
  final String thumbnailFilename = "thumbnail";
  
  public VideoRecorder() {
    videoSources = new ArrayList<VideoSource>();
    executorService = Executors.newFixedThreadPool(5);
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
  
  public void prepareFolders() throws SecurityException {
    File outputFolder = new File(outputPath);
    if(!outputFolder.exists()) {
      outputFolder.mkdir();
    }
    
  }
  
  public void beginRecording(CountDownLatch shutdownLatch) {
    startLatch = new CountDownLatch(1);
    for(VideoSource videoSource : videoSources) {
      executorService.execute(videoSource.createRunnable(duration, startLatch, shutdownLatch));
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
    // the videoSource.record() part can be better encapsulated inside a VideoSources object
    // and still block until the last recording is done, before creating the mosaic
    //then cleaning up themselves, instead of VideoRecorder cleaning them up.
    
    // like this:
    // VideoSources videoSources
    // VideoMosaicer videoMosaicer
    //executorService.execute(videoSources.record());
    //executorService.execute(videoMosaicer.createMosaic());
    //videoSources.cleanup();
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
        new VideoMosaicer(duration, videoSources, outputPath, videoFilename).createMosaic().cleanup();
        createThumbnails();
        cleanup();
      }
    });
  }

  public void createThumbnails() {
    VideoThumbnailler videoThumbNailler = new VideoThumbnailler(outputPath, videoFilename, thumbnailFilename);
    videoThumbNailler.setOutputHeight(200);
    videoThumbNailler.setOutputWidth(320);
    executorService.execute(videoThumbNailler.createRunnable());
  }

  public void cleanup() {
    for(VideoSource videoSource : videoSources) {
      videoSource.cleanup();
    }    
  }
}
