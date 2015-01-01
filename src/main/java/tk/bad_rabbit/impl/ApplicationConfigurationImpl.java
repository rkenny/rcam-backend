package tk.bad_rabbit.impl;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tk.bad_rabbit.iface.ApplicationConfiguration;

@Component(value="ApplicationConfigurationImpl")
public class ApplicationConfigurationImpl implements ApplicationConfiguration {
  @Value("${httpd.videoPath}")
  String videoPath = "";
  
  public String getVideoPath() {
    return videoPath;
  }

  public void setVideoPath(String videoPath) {
    this.videoPath = videoPath;
  }

}
