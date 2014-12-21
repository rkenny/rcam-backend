package tk.bad_rabbit;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import tk.bad_rabbit.interfaces.Cleanup;

public class AppResources implements Cleanup {
  File resourcesDir = new File(Paths.resourcesDir);
  File backgroundPng = new File(Paths.backgroundImagePath);
  CopyOption[] options = new CopyOption[]{
      StandardCopyOption.REPLACE_EXISTING  
  }; 
  
  public void unpack() {    
    try {
      resourcesDir.mkdirs();
      Files.copy(this.getClass().getResourceAsStream("/bg.png"), backgroundPng.toPath(), options);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void cleanup() {
    backgroundPng.delete();
    resourcesDir.delete();
  }
}
