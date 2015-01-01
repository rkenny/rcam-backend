package tk.bad_rabbit.vlc;

import java.io.BufferedReader;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import tk.bad_rabbit.vlc.iface.VlcConfiguration;

@Service
public class VlcRunnable implements Runnable {
  String threadDescription;
  List<String> args = new ArrayList<String>();
  
  @Autowired
  @Qualifier("VlcConfigurationImpl")
  VlcConfiguration vlcConfiguration;
  
  private CountDownLatch startLatch;
  private CountDownLatch shutdownLatch;
  
  public VlcRunnable(String threadDescription, List<String> args, CountDownLatch startLatch, CountDownLatch shutdownLatch) {
    this(threadDescription, args, startLatch);
    this.shutdownLatch = shutdownLatch;
  }
  
  public VlcRunnable(String threadDescription, List<String> args, CountDownLatch startLatch) {
    this(threadDescription, args);
    this.startLatch = startLatch;
  }
  
  public VlcRunnable(String threadDescription, List<String> args) {
    this(threadDescription);
    this.args.add(vlcConfiguration.getPath());
    this.args.addAll(args);
  }
  
  public VlcRunnable(String threadDescription) {
    this();
    this.threadDescription = threadDescription;
  }
  
  public VlcRunnable() {
    
  }
  
  private void runVlcProcess() {
    String line;

    try {
      System.out.println(args);
      ProcessBuilder pb = new ProcessBuilder(args.toArray(new String[args.size()]));
      pb.redirectErrorStream(true);
      Process p = pb.start();
      BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
    
      while ((line = input.readLine()) != null) {
        //System.out.println(line); // discard debugging output
      }
      input.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println("running cvlc failed - IOException");
    }
  }

  public void run() {
    System.out.println("Running cvlc thread. " + threadDescription);

    try {
      if(startLatch != null) {
        startLatch.await();
      }
      runVlcProcess();
      
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println("running cvlc failed - interrupted");
    } finally {
      if(shutdownLatch != null) {
        shutdownLatch.countDown();
      }
    }
  }
}
