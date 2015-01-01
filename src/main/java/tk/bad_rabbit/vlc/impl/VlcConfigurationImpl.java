package tk.bad_rabbit.vlc.impl;


import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import tk.bad_rabbit.vlc.iface.VlcConfiguration;

@Component(value="VlcConfigurationImpl")
public class VlcConfigurationImpl implements VlcConfiguration {
  @Value("${vlc.path}")
  private String path;
  
  public String getPath() {
    return path;
  }
  
  public void setPath(String vlcPath) {
    this.path = vlcPath;
  }

}
