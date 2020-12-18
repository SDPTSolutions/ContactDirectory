package com.patrick.contactdirectory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InformationActivity extends AppCompatActivity {

    TextView txtName, txtLocation, txtNumber, txtSchedule;

    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        txtName = findViewById(R.id.txtName);
        txtLocation = findViewById(R.id.txtLocation);
        txtNumber = findViewById(R.id.txtNumber);
        txtSchedule = findViewById(R.id.txtSchedule);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        Contact contact = new Contact();

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if(bundle != null) {
                contact.setName(bundle.getString("name"));
                contact.setLocation(bundle.getString("location"));
                contact.setNumber(bundle.getString("number"));
                contact.setSchedule(bundle.getString("schedule"));
            } else {
                Toast.makeText(this, "Bundle is Null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Saved Instance State is not NULL", Toast.LENGTH_SHORT).show();
        }



        txtName.setText(contact.getName());
        txtLocation.setText(contact.getLocation());
        txtNumber.setText(contact.getNumber());
        txtSchedule.setText(contact.getSchedule());

    }
}