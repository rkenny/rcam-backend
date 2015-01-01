package tk.bad_rabbit.model;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

import tk.bad_rabbit.Paths;
import tk.bad_rabbit.interfaces.Cleanup;

public class VideoMosaicConfigurator implements Cleanup {
  List<VideoSource> videoSources;
  final String vlmPath = "mosaic.vlm.conf";
  String outputPath;
  
  public void cleanup() {
    try {
      Files.deleteIfExists(FileSystems.getDefault().getPath(".", vlmPath));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  public String getVlmPath() { return vlmPath; }
  
  public VideoMosaicConfigurator(List<VideoSource> videoSources, String outputPath) {
    this.videoSources = videoSources;
    this.outputPath = outputPath; 
  }
  
  private String createBackgroundChannel() {
    System.out.println("adding background channel");
    StringBuilder backgroundChannelSb = new StringBuilder();
    backgroundChannelSb.append("new background broadcast\n");
    backgroundChannelSb.append("setup background input \""+Paths.backgroundImagePath+"\"\n");
    backgroundChannelSb.append("setup background output #transcode{sfilter=mosaic,vcodec=h264,acodec=mp3,samplerate=22050,vb=5000,channels=1}:bridge-in{delay=0}:standard{access=file,mux=ps,dst=\""+outputPath+"\"}\n");
    backgroundChannelSb.append("setup background option image-duration=-1\n");
    backgroundChannelSb.append("setup background option image-fps=15\n");
    backgroundChannelSb.append("setup background option mosaic-keep-picture=1\n");
    backgroundChannelSb.append("setup background option mosaic-order=");
    for(VideoSource videoSource : videoSources) {
      backgroundChannelSb.append(videoSource.getChannelId() + ",");
    }
    backgroundChannelSb.setCharAt(backgroundChannelSb.length() - 1, '\n');
    backgroundChannelSb.append("setup background option mosaic-position=1\n");
    backgroundChannelSb.append("setup background enabled\n");
    
    return backgroundChannelSb.toString();
  }
  
  public String createChannelPlayers() {
    StringBuilder createChannelPlayersSb = new StringBuilder();
    createChannelPlayersSb.append("control background play\n");
    for(VideoSource videoSource : videoSources) {
      createChannelPlayersSb.append("control "+videoSource.getChannelId()+ " play\n");
    }
    
    return createChannelPlayersSb.toString();
  }
  
  public void createConfFile() {
    System.out.println("creating conf file");
    Writer writer = null;
    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(vlmPath), "utf-8"));
      for(VideoSource videoSource : videoSources) {
        writer.write(videoSource.createVlmChannel());
      }
      writer.write(createBackgroundChannel());
      writer.write(createChannelPlayers());
    } catch (IOException ex) {
    // report
    } finally {
      try {
        if(writer != null) {
         writer.close();  
        }
     } catch (Exception ex) {}
    }
    System.out.println("done with conf file");
  }
  
}
