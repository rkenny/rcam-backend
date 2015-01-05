package tk.bad_rabbit.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class VideoSources implements Iterable<VideoSource> {
  private List<VideoSource> videoSources;
  CountDownLatch startLatch;
  ExecutorService executorService;
  String outputFolder;
    
  public VideoSources() {
    videoSources = new ArrayList<VideoSource>();
    
    manuallyInitialize();
    
    executorService = Executors.newFixedThreadPool(5);
  }
  
  public void setOutputFolder(String outputFolder) {
    this.outputFolder = outputFolder;
    for(VideoSource videoSource : videoSources) {
      videoSource.setOutputFolder(outputFolder);
    }
  }
  
  public String getOutputFolder() {
    return outputFolder;
  }
  
  public void setVideoSources(List<VideoSource> videoSources) {
    this.videoSources = videoSources;
  }
  
  public void add(VideoSource videoSource) {
    this.videoSources.add(videoSource);
  }
  
  public void setRecordingSuffix(String suffix) {
    for(VideoSource videoSource : videoSources) {
      videoSource.setRecordingSuffix(suffix);
    }
  }
  
  public void manuallyInitialize() {
    videoSources.add(new VideoSource("v4l2:///dev/video1", "video1"));
    videoSources.add(new VideoSource("v4l2:///dev/video1", "video12"));
    //videoSources.add(new VideoSource("http://192.168.1.2:8080/?action=stream", "ion1"));
  }
  
  
  public void record(Integer duration) {
    startLatch = new CountDownLatch(1);
    CountDownLatch shutdownLatch = new CountDownLatch(videoSources.size());
    
    for(VideoSource videoSource : videoSources) {
      executorService.execute(videoSource.createRecordingRunnable(duration, startLatch, shutdownLatch));
    }
        
    try {
      Thread.sleep(200);
      startLatch.countDown(); // the videoSources are awaiting this
      shutdownLatch.await(); // the videoSources decrement this as they complete
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public List<VlcChannel> getMosaicChannels() {
    List<VlcChannel> vlcChannels = new ArrayList<VlcChannel>();
    for(VideoSource videoSource : videoSources) {
      vlcChannels.add(videoSource.createVlmChannel());
    }
    return vlcChannels;
  }
  
  public Iterator<VideoSource> iterator() {
    // TODO Auto-generated method stub
    return videoSources.iterator();
  }
  
  public void cleanup() {
    for(VideoSource videoSource : videoSources) {
      videoSource.cleanup();
    }    
  }
}
