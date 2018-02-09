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
 * LiveViewFragment에서 4개화면모드가 선택된 상태에서 전체화면 전환 버튼을 누르면 이 액티비티로 intent가 전송되고 즉시 실행된다.
 * intent를 통해 넘겨진 데이터를 분석하여 전체화면으로 해당 영상들을 이어서 보여준다.
 */

public class FullFourScreenPlayActivity extends Activity implements MediaPlayer.OnCompletionListener{

    private VideoView video;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_view_fullscreen_four);

        Intent intent = getIntent();

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("실시간 영상 재생준비중");
        pDialog.setMessage("Connecting...");
        pDialog.setIndeterminate(false);
        //pDialog.setCancelable(false);
        pDialog.show();

        // 각 화면에(총4개) 맞는 영상을 재생시킨다.
        // 원래는 intent.getStringExtra("1") 부터 intent.getStringExtra("4") 까지 각자 다른 url경로를 가지고 있어야한다.
        // 지금은 무조건 내장되어있는 h264 영상의 링크로 연결된다.
        for(int i=1;i<=4;i++) {
            String urlPath = intent.getStringExtra(Integer.toString(i));
            if(urlPath==null) continue;
            final VideoView video = (VideoView)findViewById(getApplicationContext().getResources().getIdentifier("videoview_fourview"+Integer.toString(i),"id",getApplicationContext().getPackageName()));
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
