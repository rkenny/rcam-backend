package tk.bad_rabbit.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import tk.bad_rabbit.iface.ApplicationConfiguration;

@Component
@Configuration
public class VideoSources implements Iterable<VideoSource> {
  private List<VideoSource> videoSources;
  CountDownLatch startLatch;
  ExecutorService executorService;
  String outputFolder;

  
  public VideoSources(String videoSourceIdentifiers, String videoSourceUris) {
    this();
  }
  
  public VideoSources() {
    videoSources = new ArrayList<VideoSource>();
    executorService = Executors.newFixedThreadPool(5);
  }
  
  public void setVideoSources(List<Map.Entry<String, String>> configVideoSources) {
    for(Map.Entry<String, String> configVideoSource : configVideoSources) {
      this.videoSources.add(new VideoSource(configVideoSource.getValue(), configVideoSource.getKey()));
    }
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
  
  public void add(VideoSource videoSource) {
    this.videoSources.add(videoSource);
  }
  
  public void setRecordingSuffix(String suffix) {
    for(VideoSource videoSource : videoSources) {
      videoSource.setRecordingSuffix(suffix);
    }
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
