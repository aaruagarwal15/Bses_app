package com.example.bses;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        TextView txtZoomIn = findViewById(R.id.welcome);

        Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splashanim);
        txtZoomIn.setVisibility(View.VISIBLE);
        txtZoomIn.startAnimation(animZoomIn);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent homepage = new Intent(MainActivity.this, Homepage.class);
                startActivity(homepage);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
