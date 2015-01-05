package tk.bad_rabbit.model;


import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import tk.bad_rabbit.interfaces.Cleanup;
import tk.bad_rabbit.vlc.VlcRunnable;

@Component
@Configuration
public class VideoMosaicer implements Cleanup {
  List<VlcChannel> vlcChannels;
  
  
  VideoMosaicConfigurator configurator; 
  
  String outputPath;
  String outputFilename;
  
  final Integer mosaicWidth=960;
  final Integer mosaicHeight=640;
  final Integer mosaicRows=1;
  final Integer mosaicCols=2;
  final Integer mosaicPosition=1;
  StringBuilder mosaicOrder = new StringBuilder();
  final Integer imageFps=15;
  final String telnetPassword="password";

  public VideoMosaicer() {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

  }
  
  public void prepareConfigurationForSources(List<VlcChannel> vlcChannels) {
    this.vlcChannels = vlcChannels;
    this.configurator = new VideoMosaicConfigurator(vlcChannels, outputPath, outputFilename);
    configurator.createConfFile();
  }
  
  public VideoMosaicer createMosaic(Integer duration) {
    buildMosaic(duration);
    return this;
  }
  
  public void cleanup() {
    configurator.cleanup();
  }
  

  public void setOutputFolder(String outputFolder) {
    this.outputPath = outputFolder;
  }
  
  public void setOutputFilename(String outputFilename) {
    this.outputFilename = outputFilename;
  }
  
  public void buildMosaic(Integer duration) {
    System.out.println("Building a mosaic for " + vlcChannels.size());
    for(VlcChannel vlcChannel : vlcChannels) {
      mosaicOrder.append(vlcChannel.getChannelId() + ",");
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
