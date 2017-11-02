package pt.ipleiria.imageandvideosample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

/* Notes:
 *
 * Add the glide dependency to the build.gradle (Module) file:
 * compile 'com.github.bumptech.glide:glide:3.8.0'
 *
 * Check the following related links:
 * https://guides.codepath.com/android/Displaying-Images-with-the-Glide-Library
 * https://guides.codepath.com/android/Video-Playback-and-Recording
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_VIDEO = 1;
  private static final int REQUEST_CODE_IMAGE = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onClick_chooseVideo(View view) {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("video/*");

    startActivityForResult(intent, REQUEST_CODE_VIDEO);
  }

  public void onClick_ChooseImage(View view) {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");

    startActivityForResult(intent, REQUEST_CODE_IMAGE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE_VIDEO) {
      if (resultCode == RESULT_OK) {
        Uri videoUri = data.getData();
        VideoView videoView = (VideoView) findViewById(R.id.videoView);

        playVideo(videoView, videoUri);
      }
    } else if (requestCode == REQUEST_CODE_IMAGE) {
      if (resultCode == RESULT_OK) {
        Uri imageUri = data.getData();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this).load(imageUri).into(imageView);
      }
    }
  }

  private void playVideo(VideoView videoView, Uri videoUri) {
    videoView.setVideoURI(videoUri);
    videoView.setMediaController(new MediaController(this));
    videoView.requestFocus();
    videoView.start();
  }
}
