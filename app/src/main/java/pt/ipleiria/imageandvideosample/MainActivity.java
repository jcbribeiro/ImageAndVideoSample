package pt.ipleiria.imageandvideosample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import pt.ipleiria.imageandvideosample.model.MediaData;

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
  private static final int VIDEO_PERMISSIONS_REQUEST = 3;
  private static final int GALLERY_PERMISSIONS_REQUEST = 4;
  private MediaData mediaData;
  private VideoView videoView;
  private ImageView imageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    videoView = (VideoView) findViewById(R.id.videoView);
    imageView = (ImageView) findViewById(R.id.imageView);

    try {
      FileInputStream fileInputStream = openFileInput("mediaData.bin");
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

      mediaData = (MediaData) objectInputStream.readObject();

      objectInputStream.close();
      fileInputStream.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      Toast.makeText(MainActivity.this, "Could not read from internal storage.", Toast.LENGTH_LONG).show();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      Toast.makeText(MainActivity.this, "Error reading from internal storage.", Toast.LENGTH_LONG).show();
    }

    if (mediaData == null) {
      mediaData = new MediaData();
    } else {
      if (mediaData.getImagePath() != null) {
        Glide.with(this).load(mediaData.getImagePath()).into(imageView);
      }

      if (mediaData.getVideoPath() != null) {
        Uri videoUri = Uri.parse(mediaData.getVideoPath());
        playVideo(videoView, videoUri);
      }
    }
  }

  @Override
  protected void onPause() {
    super.onPause();

    try {
      FileOutputStream fileOutputStream = openFileOutput("mediaData.bin", Context.MODE_PRIVATE);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

      objectOutputStream.writeObject(mediaData);

      objectOutputStream.close();
      fileOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      Toast.makeText(MainActivity.this, "Could not write to internal storage.", Toast.LENGTH_LONG).show();
    }
  }

  public void onClick_chooseVideo(View view) {
    Intent intent = getMyIntent();
    intent.setType("video/*");
    startActivityForResult(intent, REQUEST_CODE_VIDEO);
  }

  public void onClick_ChooseImage(View view) {
    Intent intent = getMyIntent();
    intent.setType("image/*");
    startActivityForResult(intent, REQUEST_CODE_IMAGE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE_VIDEO) {
      if (resultCode == RESULT_OK) {
        Uri videoUri = data.getData();
        this.mediaData.setVideoPath(videoUri.toString());
        playVideo(videoView, Uri.parse(mediaData.getVideoPath()));
      }
    } else if (requestCode == REQUEST_CODE_IMAGE) {
      if (resultCode == RESULT_OK) {
        Uri imageUri = data.getData();
        this.mediaData.setImagePath(imageUri.toString());
        Glide.with(this).load(imageUri).into(imageView);
      }
    }
  }

  private void playVideo(final VideoView videoView, Uri videoUri) {
    videoView.setVideoURI(videoUri);
    videoView.setMediaController(new MediaController(this));
    videoView.requestFocus();
    videoView.start();
  }

  @NonNull
  private Intent getMyIntent() {
    // adapted from: https://stackoverflow.com/a/29588566

    Intent intent;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
      intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
      intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
    } else {
      intent = new Intent(Intent.ACTION_GET_CONTENT);
    }

    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    return intent;
  }
}
