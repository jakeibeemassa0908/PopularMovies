package com.example.theotherside.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by theotherside on 5/27/16.
 */
public class SettingActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content,new SettingFragment())
                    .commit();
        }
    }
}
