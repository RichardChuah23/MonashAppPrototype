package com.example.richard.bookrian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by richard on 4/6/17.
 */


//This code is retrieved from https://stackoverflow.com/questions/5486789/how-do-i-make-a-splash-screen. Answered by : Rishabh Dixit


public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        Thread newThread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(1000);
                    Intent newIntent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(newIntent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        newThread.start();
    }
}
