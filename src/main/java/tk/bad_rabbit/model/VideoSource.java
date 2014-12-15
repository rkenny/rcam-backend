package tk.bad_rabbit.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class VideoSource {
  private String videoSource;
  private String destFileName;
  
  public VideoSource(String videoSource, String destFileName) {
    this(videoSource);
    setDestFileName(destFileName);
  }
  
  public VideoSource(String videoSource) {
    this();
    setVideoSource(videoSource);
    destFileName = new Date().getTime() +".mpg";
  }

  public VideoSource() {
  }
  
  public void setDestFileName(String destFileName) {
    this.destFileName = destFileName;
  }

  public String getVideoSource() {
    return videoSource;
  }
  
  public void setVideoSource(String videoSource) {
    this.videoSource = videoSource;
  }
  
  public void beginRecording(Integer duration) {
    final String vlcPath = "/usr/bin/cvlc";
    final String[] args = {vlcPath, 
                      "--rc-fake-tty", 
                      "--run-time", duration.toString(), 
                      videoSource, 
                      "vlc://quit", 
                      "--sout", "#transcode{vcodec=mp4v,acodec=mpga,vb=800}:std{access=file,mux=ts,dst="+destFileName+"}"
                    };
    
    new Thread(new Runnable() {
      public void run() {
        System.out.println("Running cvlc thread. VideoSource:["+videoSource+"] destFile:["+destFileName+"]");
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
