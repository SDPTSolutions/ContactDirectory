package com.patrick.contactdirectory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class AgreementActivity extends AppCompatActivity {

    Button btnContinue;
    CheckBox chkAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        btnContinue = findViewById(R.id.btnContinue);
        chkAgree = findViewById(R.id.chkAgree);

        btnContinue.setOnClickListener(v -> {

            if(chkAgree.isChecked()){

                SharedPreferences sharedpreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("isFirstTime",0);
                editor.commit();

                Intent i = new Intent(getBaseContext(),ContactsActivity.class);
                startActivity(i);
                finish();



            }else{

                AlertDialog.Builder ad = new AlertDialog.Builder(AgreementActivity.this);
                ad.setMessage("Accept the Agreement First");
                ad.setPositiveButton("OK", null);
                ad.create();
                ad.show();

            }

        });



    }
}