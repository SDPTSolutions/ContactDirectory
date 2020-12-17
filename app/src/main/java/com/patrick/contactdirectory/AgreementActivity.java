package com.patrick.contactdirectory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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