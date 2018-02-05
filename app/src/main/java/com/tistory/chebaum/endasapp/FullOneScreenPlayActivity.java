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

public class FullOneScreenPlayActivity extends Activity implements MediaPlayer.OnCompletionListener{

    private VideoView video;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_view_fullscreen_one);

        Intent intent = getIntent();

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("실시간 영상 재생준비중");
        pDialog.setMessage("Connecting...");
        pDialog.setIndeterminate(false);
        //pDialog.setCancelable(false);
        pDialog.show();

        for(int i=1;i<=1;i++) {
            String urlPath = intent.getStringExtra(Integer.toString(i));
            if(urlPath==null) continue;
            final VideoView video = (VideoView)findViewById(getApplicationContext().getResources().getIdentifier("videoview_oneview"+Integer.toString(i),"id",getApplicationContext().getPackageName()));
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
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        finish();
    }
}
