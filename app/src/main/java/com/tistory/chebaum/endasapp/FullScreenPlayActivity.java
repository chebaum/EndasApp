package com.tistory.chebaum.endasapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.VideoView;

/**
 * Created by cheba on 2018-01-10.
 */

public class FullScreenPlayActivity extends Activity implements MediaPlayer.OnCompletionListener{

    private VideoView video;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_view_fullscreen);

        Intent intent = getIntent();
        String urlPath = intent.getStringExtra("urlPath");

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("실시간 영상 재생준비중");
        pDialog.setMessage("Connecting...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        video=(VideoView)findViewById(R.id.videoView);
        Uri uri = Uri.parse(urlPath);
        video.setVideoURI(uri);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                pDialog.dismiss();
                video.start();
            }
        });
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        finish();
    }
}
