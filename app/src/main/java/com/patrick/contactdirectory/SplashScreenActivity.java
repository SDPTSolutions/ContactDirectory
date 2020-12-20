package com.patrick.contactdirectory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedpreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        int firstTime = sharedpreferences.getInt("isFirstTime",1);


        Thread t = new Thread(() -> {

            try {
                Thread.sleep(2000);

                if(firstTime == 1) {
                    Intent i = new Intent(getBaseContext(), GetStartedActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(getBaseContext(), ContactsActivity.class);
                    startActivity(i);
                    finish();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        t.start();

    }
}