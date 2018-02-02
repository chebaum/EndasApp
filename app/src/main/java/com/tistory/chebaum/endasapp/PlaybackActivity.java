package com.tistory.chebaum.endasapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by cheba on 2018-01-09.
 *
 * 1.  PlaybackFragment로 돌아가는 뒤로가기 버튼필요
 * 2.
 */

public class PlaybackActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Calendar mDatetime;
    private String selected_channel;
    private SimpleDateFormat mToastDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        Intent intent = getIntent();

        // 일단은 채널의 이름만을 string객체로 받는다.
        // 후에는 채널의 모든 속성값을 가지고 있는 직접 작성한 클래스의 객체를 받으면 될듯싶다.
        String channel = getText(R.string.channel).toString();
        mDatetime = (Calendar)intent.getSerializableExtra("startingPoint");
        selected_channel = intent.getStringExtra(channel);

        // 전달받은 값 테스트용...후에 지운다.
        String str = selected_channel;
        str+=" 채널 \n";
        str += mToastDateFormat.format(mDatetime.getTime());
        ((TextView)findViewById(R.id.testView)).setText(str);
        // 여기까지 지운다.

        // 뒤로가기 버튼을 보이게 하기 위한 코드(뒤로가기 -> 다시 채널/시간 설정 화면으로...)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final VideoView video = (VideoView)findViewById(R.id.videoview_oneview1);

        // 버퍼링임을 알려주는 다이얼로그
        CharSequence connecting = getText(R.string.connecting);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle(R.string.prepare_to_play_live);
        pDialog.setMessage(connecting);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        // 주소 설정. 일단은 임의 주소사용한다.
        //String urlPath = "rtsp://192.168.56.1:8554/stream"
        String urlPath = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";

        try{
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(video);
            Uri uri = Uri.parse(urlPath);
            video.setMediaController(mediaController);
            video.setVideoURI(uri);
        }catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        video.requestFocus();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                pDialog.dismiss();
                video.start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            // 액션바의 뒤로가기 버튼 누른경우, MainActivity의 PlaybackFragment 으로 돌아감
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
