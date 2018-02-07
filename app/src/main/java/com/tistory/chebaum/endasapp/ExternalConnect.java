package com.tistory.chebaum.endasapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class ExternalConnect extends AppCompatActivity implements View.OnClickListener {
    private Button buttonScan,okbtn;
    private EditText editName, editId, editPw;

    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_connect);

        buttonScan = (Button) findViewById(R.id.buttonScan);
        okbtn = (Button) findViewById(R.id.okbtn);
        editName = (EditText) findViewById(R.id.editName);
        editId = (EditText) findViewById(R.id.editId);
        editPw = (EditText) findViewById(R.id.editPw);

        buttonScan.setOnClickListener(this);
        ((Button)findViewById(R.id.cancelbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        qrScan = new IntentIntegrator(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    String channel_name = getText(R.string.channel_name).toString();
                    String id = getText(R.string.id).toString();
                    String pw = getText(R.string.password).toString();
                    editName.setText(obj.getString(channel_name));
                    editId.setText(obj.getString(id));
                    editPw.setText(obj.getString(pw));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        qrScan.initiateScan();
    }
}
