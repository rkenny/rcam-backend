package tk.bad_rabbit.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;


public class VideoRecorder implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 6952325964443377011L;
  @JsonProperty
  private Integer duration;
  
  public void setDuration(Integer duration) {
    this.duration = duration;
  }
  
  public Integer getDuration() { return duration; }
  
  @Override
  public String toString() {
    return "ok it worked. video will record for " + duration + " seconds";
  }
  // 

  public void beginRecording() {
    try {
      String line;
      System.out.println("Running cvlc");
      String vlcPath = "/usr/bin/cvlc";
      String[] args = {vlcPath, 
                        "--rc-fake-tty", 
                        "--run-time", duration.toString(), 
                        "v4l2:///dev/video1", 
                        "vlc://quit", 
                        "--sout", "#transcode{vcodec=mp4v,acodec=mpga,vb=800}:std{access=file,mux=ts,dst="+new Date().getTime() +".mpg}"
                      };
      ProcessBuilder pb = new ProcessBuilder(args);
      pb.redirectErrorStream(true);
      Process p = pb.start();
        
      BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
      
      while ((line = input.readLine()) != null) {
//        System.out.println(line);
      }
      input.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println("running cvlc failed");
    }
  }
}
