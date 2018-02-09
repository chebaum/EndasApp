package com.tistory.chebaum.endasapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    boolean selection_mode;
    boolean hasDirtyData;
    private List<Group> groups;
    private List<Group> selected_groups;
    private BottomNavigationView navigation;
    private static String TAG = "MainActivity DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // main activity에서는 하는 일이 크게있지 않습니다.
        // 하단의 네비게이션 메뉴바를 세팅하는 즉시 HomeFragment으로 넘어가며 홈화면을 보여줍니다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate() called");

        navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, new HomeFragment()).commit();
        getSupportActionBar().setTitle(R.string.mng_device);
        hasDirtyData=selection_mode=false;
    }

    // 하단 네비게이션 바 클릭 리스너
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction =   fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.frame_layout, new HomeFragment()).commit();
                    getSupportActionBar().setTitle(R.string.mng_device);
                    return true;
                case R.id.navigation_live:
                    transaction.replace(R.id.frame_layout, new LiveViewFragment()).commit();
                    getSupportActionBar().setTitle(R.string.Live_Video);
                    return true;
                case R.id.navigation_playback:
                    transaction.replace(R.id.frame_layout, new PlaybackFragment()).commit();
                    getSupportActionBar().setTitle(R.string.Record_Video);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    public List<Group> get_group(){
        return groups;
    }
    public List<Group> get_selected_groups(){ return selected_groups; }
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
    public void setSelected_groups(List<Group> selected_groups) {
        this.selected_groups = selected_groups;
    }
    public boolean getHasDirtyData() {
        return hasDirtyData;
    }
    public void setHasDirtyData(boolean hasDirtyData) {
        this.hasDirtyData = hasDirtyData;
    }
    public BottomNavigationView getNavigationView(){ return navigation; }
}
