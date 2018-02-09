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
 * LiveViewFragment에서 1개화면모드가 선택된 상태에서 전체화면 전환 버튼을 누르면 이 액티비티로 intent가 전송되고 즉시 실행된다.
 * intent를 통해 넘겨진 데이터를 분석하여 전체화면으로 해당 영상들을 이어서 보여준다.
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
