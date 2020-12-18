package com.patrick.contactdirectory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity {

    LinearLayout btnFavorites,btnAbout,btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnFavorites = findViewById(R.id.btnFavorites);
        btnAbout = findViewById(R.id.btnAbout);
        btnHelp = findViewById(R.id.btnHelp);

        btnFavorites.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),FavoritesActivity.class)));
        btnAbout.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),AboutActivity.class)));
        btnHelp.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),HelpActivity.class)));

    }
}