package com.tistory.chebaum.endasapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.VideoView;

/**
 * Created by cheba on 2018-01-10.
 */

public class FullFourScreenPlayActivity extends Activity implements MediaPlayer.OnCompletionListener{

    private VideoView video;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_view_fullscreen_four);

        Intent intent = getIntent();
        String urlPath = intent.getStringExtra("urlPath");

        CharSequence connecting = getText(R.string.connecting);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle(R.string.prepare_to_play_live);
        pDialog.setMessage(connecting);
        pDialog.setIndeterminate(false);
        //pDialog.setCancelable(false);
        pDialog.show();

        video=(VideoView)findViewById(R.id.videoview_fourview1);
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
