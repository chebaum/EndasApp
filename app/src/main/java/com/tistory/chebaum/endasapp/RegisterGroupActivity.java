package com.tistory.chebaum.endasapp;

import android.content.DialogInterface;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegisterGroupActivity extends AppCompatActivity {

    private String TAG = "RegisterGroupActivity LOG";

    private List<ServerChannel> serverStatus=null;
    private String basicAuth, requestStr;
    private Group userInput;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_group);
        // 뒤로가기 버튼을 보이게 하기 위한 코드(뒤로가기 -> 다시 채널/시간 설정 화면으로...)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 등록버튼 클릭 시, 연결 시도 -> 연결 성공 시 사용가능한 채널리스트 보여주고 사용자가 선택토록한다.
        ((Button)findViewById(R.id.group_reg_request_btn)).setOnClickListener(new myClickListener());
        // 취소버튼 클릭 시 homeFragment화면으로 돌아간다.
        ((Button)findViewById(R.id.group_reg_quit_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void getUserInputs(){
        String title = ((EditText)findViewById(R.id.group_reg_edittext_title)).getText().toString();
        String url = ((EditText)findViewById(R.id.group_reg_edittext_url)).getText().toString();
        int webPort = Integer.parseInt(((EditText)findViewById(R.id.group_reg_edittext_webport)).getText().toString());
        int videoPort = Integer.parseInt(((EditText)findViewById(R.id.group_reg_edittext_videoport)).getText().toString());
        String id = ((EditText)findViewById(R.id.group_reg_edittext_id)).getText().toString();
        String password = ((EditText)findViewById(R.id.group_reg_edittext_password)).getText().toString();

        //TODO g_id 관련 데이터베이스 속성 값 바꿔야함함
       userInput=new Group(null, 1, title, url, webPort, videoPort, id, password);
    }

    public void createURL(){
        String head = "http://";
        String footer="/vb.htm?getrelayenable=all&getchannels";

        basicAuth = "Basic "+ new String(Base64.encode((userInput.getG_login_id()+":"+userInput.getG_login_pw()).getBytes(),Base64.DEFAULT));
        requestStr = head+ userInput.getG_url()+":"+userInput.getG_web_port()+footer;
    }

    public class RequestAsyncTask extends AsyncTask<String, Integer, List<ServerChannel>> {
        String serverResponse;
        @Override
        protected List<ServerChannel> doInBackground(String... strings) {
            try {
                //TODO 연결중임을 표시하는 진행창 + 시간 오래걸리면 timeoutexception 걸자
                URL obj = new URL(requestStr);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Authorization", basicAuth);
                con.setUseCaches(false);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                //((TextView) findViewById(R.id.textview)).setText(response);
                serverResponse = response.toString();
                Log.d(TAG, response.toString());
            } catch (Exception e) {
                Log.e("TAG", "예외처리됨!!!!!!!!!!!!!!!!");
                e.printStackTrace();
                return null;
            }

            // TODO 체크!... 항상 HTTP/1.0으로 문자열이 끝날까?
            int getChannelsStartingIdx = serverResponse.indexOf("OK getchannels");
            int getChannelsEndingidx = serverResponse.indexOf("HTTP/1.0");

            String getRelayenableResponse;
            String getChannelsResponse;
            getRelayenableResponse = serverResponse.substring(new String("OK getrelayenable=").length(), getChannelsStartingIdx);
            getChannelsResponse = serverResponse.substring(getChannelsStartingIdx + new String("OK getchannels=").length(), getChannelsEndingidx);

            Log.d(TAG, getRelayenableResponse);
            Log.d(TAG, getChannelsResponse);

            String[] firstStrArr = getRelayenableResponse.split(",");
            String[] secondStrArr = getChannelsResponse.split(",");
            if (firstStrArr.length != secondStrArr.length) {
                Toast.makeText(getApplicationContext(), "개수 맞지않음", Toast.LENGTH_LONG).show();
                Log.e(TAG, "response가 비정상임 ******************************************************************");
                return null;
            }

            serverStatus = new ArrayList<>();
            for (int i = 0; i < firstStrArr.length; i++) {
                int num = Integer.parseInt(firstStrArr[i].substring(0, firstStrArr[i].indexOf(":")));
                boolean isActive = (firstStrArr[i].substring(firstStrArr[i].indexOf(":") + 1)).equals("0000") ? false : true;
                serverStatus.add(new ServerChannel(num, isActive, secondStrArr[i]));
                if(isActive) count++; // 사용가능한 채널 개수 파악위해... 배열 길이 초기화 위함임
            }
            return serverStatus;
        }
    }

    private class myClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            getUserInputs();
            createURL();
            // 함수 실행시 List<ServerChannel> serverStatus 객체에 채널 현황이 저장된다. (if null, failed)
            try{
                serverStatus = new RequestAsyncTask().execute().get();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(serverStatus==null) { // 서버에서 응답을 받지 못한경우
                Toast.makeText(getApplicationContext(), "연결 실패", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                Toast.makeText(getApplicationContext(), "연결 성공", Toast.LENGTH_LONG).show();
                // serverStatus에 저장된 정보를 활용한다.(현재 사용가능한 채널목록이 ServerChannel객체들의 리스트 형태로 저장되어있음)
                // for(ServerChannel ch : serverStatus)
                //      if(ch.isActive())
                //            use ch.name/ch.num
                String[] listItems=new String[count];
                final boolean[] checkedItems=new boolean[count];
                for(int i=0, j=0;i<serverStatus.size();i++){
                    if(serverStatus.get(i).isActive()) {
                        listItems[j++] = serverStatus.get(i).getName();
                        checkedItems[j++] = false;
                    }
                    // TODO 지워야한다~
                    if(j!=count)
                        Toast.makeText(getApplicationContext(), "개수 맞지않음 확인해야..", Toast.LENGTH_LONG).show();
                }

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterGroupActivity.this);
                mBuilder.setTitle("사용 가능한 채널")
                        .setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int pos, boolean isChecked) {
                                if(isChecked) checkedItems[pos]=true;
                                else checkedItems[pos]=false;
                            }
                        })
                        .setCancelable(false)
                        .setPositiveButton("선택완료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int j=0;
                                for(ServerChannel channel:serverStatus){
                                    if(channel.isActive()){
                                        channel.setIsSelected(checkedItems[j++]);
                                    }
                                    if(j!=count){
                                        Toast.makeText(getApplicationContext(), "개수 맞지않음 확인해야..222", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    insertSelectedToDatabase();
                                    // TODO 체크! 메인화면으로 돌아오는지...
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
            }
        }
    }

    private void insertSelectedToDatabase(){
        // 일단 userInput객체의 정보를 바탕으로 groupDB.db에 장비 객체 insert한다.
        // 후에 serverStatus의 객체를 하나씩 보면서 isActive&&isSelected 가 true인 객체에 한하여 channelDB.db에 하나씩 삽입한다.
        GroupDBOpenHelper mGroupDBOpenHelper = new GroupDBOpenHelper(this);
        ChannelDBOpenHelper mChannelDBOpenHelper = new ChannelDBOpenHelper(this);
        try{
            mGroupDBOpenHelper.open();
            mChannelDBOpenHelper.open();
        } catch (SQLException e){
            e.printStackTrace();
        }

        long groupID = mGroupDBOpenHelper.insertColumn(userInput);
        //TODO 모든 serverStatus의 객체를 처음부터 훑는 코드가 너무 중복된다..일단은 유지, 후에 한번에 정리
        for(ServerChannel serverChannel:serverStatus){
            if(serverChannel.isActive()&&serverChannel.getIsSelected()){
                mChannelDBOpenHelper.insertColumn(new Channel(serverChannel.getNumber(),serverChannel.getName(),groupID));
            }
        }

    }
}
