package pt.ipleiria.imageandvideosample.model;

import java.io.Serializable;

/**
 * Created by joser on 03/11/2017.
 */

public class MediaData implements Serializable {
  private String imagePath;
  private String videoPath;

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public String getVideoPath() {
    return videoPath;
  }

  public void setVideoPath(String videoPath) {
    this.videoPath = videoPath;
  }
}
