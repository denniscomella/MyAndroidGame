package com.example.android.myandroidgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Dennis on 2/3/2018.
 */

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread myThread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(4000);
                    Intent startGame = new Intent(getApplicationContext(), Game.class);
                    startActivity(startGame);
                    finish();
                }catch(InterruptedException e){e.printStackTrace();}
            } // end run
        }; // end thread
        myThread.start();
    }
}
