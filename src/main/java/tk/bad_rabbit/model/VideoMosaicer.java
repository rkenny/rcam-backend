package tk.bad_rabbit.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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
    final String vlcPath = "/usr/bin/cvlc";
    final String vlmPath = "mosaic.vlm.conf";
    final Integer mosaicWidth=960;
    final Integer mosaicHeight=640;
    final Integer mosaicRows=1;
    final Integer mosaicCols=2;
    final Integer mosaicPosition=1;
    final String mosaicOrder="channel1,channel2";
    final Integer imageFps=15;
    final String telnetPassword="password";
    
    final String[] args = {vlcPath, 
                      "--rc-fake-tty", 
                      //"--run-time", duration.toString(), 
                      "--telnet-password", telnetPassword,
                      "--vlm-conf", vlmPath,
                      "--mosaic-width", mosaicWidth.toString(),
                      "--mosaic-height", mosaicHeight.toString(),
                      "--mosaic-keep-picture",
                      "--mosaic-rows", mosaicRows.toString(),
                      "--mosaic-cols", mosaicCols.toString(),
                      "--mosaic-position", mosaicPosition.toString(),
                      "--mosaic-order", mosaicOrder,
                      "--image-fps", imageFps.toString()
                    };
    
    new Thread(new Runnable() {
      public void run() {
        System.out.println("Running cvlc thread. Creating mosaic.");
        String line;
        try {
          ProcessBuilder pb = new ProcessBuilder(args);
          pb.redirectErrorStream(true);
          Process p = pb.start();
            
          BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
          
          while ((line = input.readLine()) != null) {
//            System.out.println(line);
          }
          input.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          System.out.println("running cvlc failed");
        }
      }
    }).start();
  }

}
