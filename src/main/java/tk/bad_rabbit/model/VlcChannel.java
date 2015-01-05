package tk.bad_rabbit.model;

public class VlcChannel {
  String channelId;
  String videoPath;
  Integer videoSourceId;
  
  public VlcChannel(String channelId, Integer videoSourceId, String videoPath) {
    this();
    this.channelId = channelId;
    this.videoSourceId = videoSourceId;
    this.videoPath = videoPath;
  }
  
  public VlcChannel() {
    
  }
  
  public String getChannelId() {
    return channelId;
  }
  
  public String createVlmChannel() {
    StringBuilder vlmChannelSb = new StringBuilder();
    
    vlmChannelSb.append("new " + channelId + " broadcast\n");
    vlmChannelSb.append("setup " + channelId + " input \"" + videoPath + "\"\n");
    //vlmChannelSb.append("setup " + channelId + " output #duplicate{dst=mosaic-bridge{id="+videoSourceId+",height=640,width=480},select=video,dst=bridge-out{id="+videoSourceId+"},select=audio}\n"); 
    vlmChannelSb.append("setup " + channelId + " output #duplicate{dst=mosaic-bridge{id="+videoSourceId+",height=640,width=480},select=video,dst=bridge-out{id="+videoSourceId+"},select=audio}\n");
    vlmChannelSb.append("setup " + channelId + " enabled\n");
    
    System.out.println(vlmChannelSb.toString());
    return vlmChannelSb.toString();
  }
}
