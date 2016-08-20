package com.appmobiledeveloper.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

public class SplashScreenActivity  extends Activity {

    private static final String TAG = "SplashScreenActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        try {
            VideoView videoView = (VideoView)this.findViewById(R.id.splash_video);
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.amd);
            videoView.setVideoURI(uri);
            videoView.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    openMainActivity();
                }
            });
            videoView.start();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            openMainActivity();
        }
    }

    protected void openMainActivity() {
        Intent  intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
