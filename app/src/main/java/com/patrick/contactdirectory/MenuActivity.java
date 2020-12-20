package com.patrick.contactdirectory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    LinearLayout btnFavorites,btnAbout,btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        btnFavorites = findViewById(R.id.btnFavorites);
        btnAbout = findViewById(R.id.btnAbout);
        btnHelp = findViewById(R.id.btnHelp);

        btnFavorites.setOnClickListener(v -> {

            if(mUser != null) startActivity(new Intent(getBaseContext(),FavoritesActivity.class));
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this)
                        .setTitle("Error")
                        .setIcon(getResources().getDrawable(R.drawable.warning))
                        .setMessage("You must be logged in to be able to view Favorites.")
                        .setPositiveButton("OK",null);
                builder.create();
                builder.show();
            }
        });
        btnAbout.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),AboutActivity.class)));
        btnHelp.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),HelpActivity.class)));

    }
}