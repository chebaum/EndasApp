package com.tistory.chebaum.endasapp;

import android.os.AsyncTask;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_group);
        // 뒤로가기 버튼을 보이게 하기 위한 코드(뒤로가기 -> 다시 채널/시간 설정 화면으로...)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button getRequestBtn = (Button)findViewById(R.id.getRequestBtn);
        getRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInputs();
                // 함수 실행시 List<ServerChannel> serverStatus 객체에 채널 현황이 저장된다. (if null, failed)
                try{
                    serverStatus = new RequestAsyncTask().execute().get();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(serverStatus==null) {
                    Toast.makeText(getApplicationContext(), "에러! 다시 시도해주십시오", Toast.LENGTH_LONG).show();
                    ((TextView) findViewById(R.id.textview)).setText("error!");
                }
                else {
                    String result="";
                    for (ServerChannel ch : serverStatus) {
                        if (ch.isActive()) {
                            String str = "채널번호: " + Integer.toString(ch.getNumber()) + " / 채널이름: " + ch.getName();
                            result+=str;
                            result+='\n';
                        }
                    }
                    ((TextView)findViewById(R.id.textview)).setText(result);
                    Toast.makeText(getApplicationContext(), "정상적으로 등록되었습니다.", Toast.LENGTH_LONG).show();
                    //TODO 여기서 이제 등록할 채널을 선택할 수 있는 화면으로 넘어가자.
                }
            }
        });
    }

    public void getUserInputs(){
        String head = "http://";
        String url=((EditText)findViewById(R.id.edittext_url)).getText().toString();
        String webPort=":"+((EditText)findViewById(R.id.edittext_webport)).getText().toString();
        String footer="/vb.htm?getrelayenable=all&getchannels";
        String id=((EditText)findViewById(R.id.edittext_id)).getText().toString();
        String pw=((EditText)findViewById(R.id.edittext_pw)).getText().toString();

        basicAuth = "Basic "+ new String(Base64.encode((id+":"+pw).getBytes(),Base64.DEFAULT));
        requestStr = head+url+webPort+footer;
    }

    public class RequestAsyncTask extends AsyncTask<String, Integer, List<ServerChannel>> {
        String serverResponse;
        @Override
        protected List<ServerChannel> doInBackground(String... strings) {
            try {
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
            }
            return serverStatus;
        }
    }


}
