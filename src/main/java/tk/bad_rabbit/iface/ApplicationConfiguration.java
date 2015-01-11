package tk.bad_rabbit.iface;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ApplicationConfiguration {  
  public String getVideoPath();
  public void setVideoPath(String videoPath);
  public String getTempPath();
  
  public void setVideoSourceIdentifiers(String videoSourceIdentifiers);
  
  public void setVideoSourceURIs(String videoSourceURIs);
  
  public String getVideoSourceIdentifiers();
  
  public String getVideoSourceURIs();
  
  public List<Map.Entry<String, String>> getVideoSources();

}
