package tk.bad_rabbit.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import tk.bad_rabbit.iface.ApplicationConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Component
@Configuration
public class VideoRecorder implements Serializable {
  /**
   * Create a VideoRecorder by accessing /record, set duration via JSON
   */
  private static final long serialVersionUID = 6952325964443377011L;
  
  @JsonProperty("duration")
  private Integer duration;
  
  VideoSources videoSources;
  VideoMosaicer videoMosaicer;
  ExecutorService executorService;

  String tempPath;
  String outputPath;
  
  final String videoFilename = "video";
  final String thumbnailFilename = "thumbnail";
  String suffix;
  
  public void setTempPath(String tempPath) {
    this.tempPath = tempPath;
  }
  
  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }
  
  public VideoRecorder(Integer duration) {
    this();
    setDuration(duration);
  }
  
  public VideoRecorder() {    
    executorService = Executors.newFixedThreadPool(5);
  }
  
  public VideoSources getVideoSources() {
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
  
  public void prepareFolders(String pathToPrepare) throws SecurityException {
    File outputFolder = new File(pathToPrepare);
    if(!outputFolder.exists()) {
      outputFolder.mkdir();
    }
  }
  
  public void setRecordingSuffix(String suffix) {
    this.suffix = suffix;
    videoSources.setRecordingSuffix(suffix);
  }
  
  public void setVideoSources(List<Map.Entry<String, String>> configVideoSources) {
    videoSources = new VideoSources();
    videoSources.setVideoSources(configVideoSources);
  }
  
  
  public String createOutputFolderPath() {
    return outputPath + suffix + "/";
  }
  
  public String createTempFolderPath() {
    return tempPath + suffix + "/";
  }
  
  public void createOutputVideo() {

    executorService.execute(new Runnable() {
      public void run() {
        prepareFolders(createTempFolderPath());
        recordVideos();  // blocks until all recordings are complete
        createMosaic(); // blocks until mosaic complete        
        createThumbnails();
        
        videoSources.cleanup();
        moveFiles(createTempFolderPath(), createOutputFolderPath());
      }
    });
  }
  
  public void moveFiles(String inputFolderPath, String outputFolderPath) {
    try {
      Files.move(Paths.get(inputFolderPath), Paths.get(outputFolderPath), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void createMosaic() {
    VideoMosaicer videoMosaicer = new VideoMosaicer();
    videoMosaicer.setOutputFolder(createTempFolderPath());
    videoMosaicer.setOutputFilename(videoFilename);
    videoMosaicer.prepareConfigurationForSources(videoSources.getMosaicChannels());
    videoMosaicer.createMosaic(duration);
    videoMosaicer.cleanup();
  }
  
  public void recordVideos() {
    videoSources.setOutputFolder(createTempFolderPath());
    videoSources.record(duration);
  }

  public void createThumbnails() {
    VideoThumbnailler videoThumbNailler = new VideoThumbnailler();
    videoThumbNailler.setOutputPath(createTempFolderPath());
    videoThumbNailler.setInputFile(videoFilename);
    videoThumbNailler.setOutputFile(thumbnailFilename);
    videoThumbNailler.setOutputHeight(200);
    videoThumbNailler.setOutputWidth(320);
    videoThumbNailler.createThumbnail();
  }
}
