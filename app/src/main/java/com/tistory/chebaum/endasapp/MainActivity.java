package com.tistory.chebaum.endasapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction =   fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.frame_layout, new HomeFragment()).commit();
                    return true;
                case R.id.navigation_live:
                    transaction.replace(R.id.frame_layout, new LiveViewFragment()).commit();
                    return true;
                case R.id.navigation_playback:
                    transaction.replace(R.id.frame_layout, new PlaybackFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Menu menu = navigation.getMenu();
        //MenuItem menuItem = menu.getItem(0);
        //menuItem.setChecked(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction =   fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, new HomeFragment()).commit();
    }

}
