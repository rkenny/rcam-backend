package tk.bad_rabbit.model;


import java.util.ArrayList;
import java.util.List;

import tk.bad_rabbit.interfaces.Cleanup;
import tk.bad_rabbit.vlc.VlcRunnable;

public class VideoMosaicer implements Cleanup {
  
  final String outputPath = "outputs.mpg";
  List<VideoSource> videoSources;
  VideoMosaicConfigurator configurator;
  Integer duration;
  
  final Integer mosaicWidth=960;
  final Integer mosaicHeight=640;
  final Integer mosaicRows=1;
  final Integer mosaicCols=2;
  final Integer mosaicPosition=1;
  StringBuilder mosaicOrder = new StringBuilder();
  final Integer imageFps=15;
  final String telnetPassword="password";
  
  public VideoMosaicer(Integer duration, List<VideoSource> videoSources) {
    this.duration = duration;
    this.videoSources = videoSources;
    this.configurator = new VideoMosaicConfigurator(videoSources, outputPath);
  }
  
  public void createMosaic() {
    configurator.createConfFile();
    buildMosaic();
  }
  
  public void cleanup() {
    configurator.cleanup();
  }
  
  public void buildMosaic() {
    
    for(VideoSource videoSource : videoSources) {
      mosaicOrder.append(videoSource.getChannelId() + ",");
    }
    mosaicOrder.deleteCharAt(mosaicOrder.length() - 1);
    
    List<String> args = new ArrayList<String>();
    args.add("--rc-fake-tty"); 
    args.add("--telnet-password");
    args.add(telnetPassword);
    args.add("--vlm-conf");
    args.add(configurator.getVlmPath());
    args.add("--mosaic-width");
    args.add(mosaicWidth.toString());
    args.add("--mosaic-height");
    args.add(mosaicHeight.toString());
    args.add("--mosaic-keep-picture");
    args.add("--mosaic-rows");
    args.add(mosaicRows.toString());
    args.add("--mosaic-cols");
    args.add(mosaicCols.toString());
    args.add("--mosaic-position");
    args.add(mosaicPosition.toString());
    args.add("--mosaic-order");
    args.add(mosaicOrder.toString());
    args.add("--image-fps");
    args.add(imageFps.toString());
    args.add("vlc://pause:"+duration);
    args.add("vlc://quit");
    final String description = "Creating mosaic";

    new VlcRunnable(description, args).run();    
  }

}
