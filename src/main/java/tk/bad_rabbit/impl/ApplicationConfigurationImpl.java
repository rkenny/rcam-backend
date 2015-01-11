package tk.bad_rabbit.impl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
  
  @Value("${videoSource.identifiers}")
  String videoSourceIdentifiersRaw = "";
  
  @Value("${videoSource.URIs}")
  String videoSourceURIsRaw = "";
  
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

  public void setVideoSourceIdentifiers(String videoSourceIdentifiers) {
    this.videoSourceIdentifiersRaw = videoSourceIdentifiers;
  }
  
  public void setVideoSourceURIs(String videoSourceURIs) {
    this.videoSourceURIsRaw = videoSourceURIs;
  }
  
  public String getVideoSourceIdentifiers() {
    return videoSourceIdentifiersRaw;
  }
  
  public String getVideoSourceURIs() {
    return videoSourceURIsRaw;
  }
  

  

  public List<Map.Entry<String, String>> getVideoSources() {
    List<Map.Entry<String, String>> configVideoSources = new ArrayList<Map.Entry<String, String>>();
    
    String[] videoSourceIdentifiers = videoSourceIdentifiersRaw.split(",");
    String[] videoSourceUris = videoSourceURIsRaw.split(",");
    
    for(int i = 0; i < videoSourceUris.length; i++) {
      configVideoSources.add(new AbstractMap.SimpleEntry<String, String>(videoSourceIdentifiers[i], videoSourceUris[i]));
    }
      
    return configVideoSources;
  }
  
}
