package tk.bad_rabbit.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import tk.bad_rabbit.vlc.VlcRunnable;

public class VideoMosaicer {
  List<VideoSource> videoSources; // needs to build a VLM for these. Give the VideoSource access to the stream's position?
  public VideoMosaicer(List<VideoSource> videoSources) {
    this.videoSources = videoSources;
  }
  
  // This is the original command line
  //vlc -vvv --color --telnet-password=password --vlm-conf ./mosaic.vlm.conf --mosaic-width=960 --mosaic-height=640 --mosaic-keep-picture --mosaic-rows=1 
  //    --mosaic-cols=2 
  //    --mosaic-position=1 
  //    --mosaic-order=channel1,channel2 
  //    --image-fps=15

  
  public void buildMosaic() {
    final String vlmPath = "mosaic.vlm.conf";
    final Integer mosaicWidth=960;
    final Integer mosaicHeight=640;
    final Integer mosaicRows=1;
    final Integer mosaicCols=2;
    final Integer mosaicPosition=1;
    final String mosaicOrder="channel1,channel2";
    final Integer imageFps=15;
    final String telnetPassword="password";
    
    
    List<String> args = new ArrayList<String>();
    args.add("--rc-fake-tty"); 
//    args.add("--run-time", duration.toString()); 
    args.add("--telnet-password");
    args.add(telnetPassword);
    args.add("--vlm-conf");
    args.add(vlmPath);
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
    args.add(mosaicOrder);
    args.add("--image-fps");
    args.add(imageFps.toString());
    final String description = "Creating mosaic";
    
    Thread vlcThread = new Thread(new VlcRunnable(description, args));
    vlcThread.start(); 
   
  }

}
