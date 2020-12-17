package com.patrick.contactdirectory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread t = new Thread(() -> {

            try {
                Thread.sleep(2000);

                Intent i = new Intent(getBaseContext(), GetStartedActivity.class);
                startActivity(i);
                finish();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        t.start();

    }
}