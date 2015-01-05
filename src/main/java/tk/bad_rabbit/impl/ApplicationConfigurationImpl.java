package tk.bad_rabbit.impl;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tk.bad_rabbit.iface.ApplicationConfiguration;

@Component(value="ApplicationConfigurationImpl")
public class ApplicationConfigurationImpl implements ApplicationConfiguration {
  @Value("${httpd.videoPath}")
  String videoPath = "";
  
  
  @Value("${system.tempPath}")
  String tempPath = "";
  
  public String getTempPath() {
    return tempPath;
  }
  
  public void setTempPath(String tempPath) {
    this.tempPath = tempPath;
  }
  
  public String getVideoPath() {
    return videoPath;
  }

  public void setVideoPath(String videoPath) {
    this.videoPath = videoPath;
  }

}
