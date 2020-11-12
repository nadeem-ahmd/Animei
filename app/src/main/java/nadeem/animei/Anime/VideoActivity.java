package nadeem.animei.Anime;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import nadeem.animei.R;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        String link = intent.getStringExtra("url");

        try {
            videoView = findViewById(R.id.videoView);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            mediaController.setMediaPlayer(videoView);

            Uri video = Uri.parse(link);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.start();

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    videoView.requestFocus();
                }

            });

        } catch (Exception e) {
            Toast.makeText(this, "Error: Failed to stream video", Toast.LENGTH_SHORT).show();
        }
    }
}
