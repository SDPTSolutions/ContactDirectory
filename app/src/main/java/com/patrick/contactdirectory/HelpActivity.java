package com.patrick.contactdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class HelpActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;

    LinearLayout btnHowToUse, btnReport;

    LinearLayout reportContainer;
    EditText txtReport;
    Button btnSendReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnHowToUse = findViewById(R.id.btnHowToUse);
        btnReport = findViewById(R.id.btnReport);

        reportContainer = findViewById(R.id.reportContainer);
        txtReport = findViewById(R.id.txtReport);
        btnSendReport = findViewById(R.id.btnSendReport);

        btnHowToUse.setOnClickListener(v -> {

            String instructions = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce cursus eros at dolor volutpat, ac ultrices sem rhoncus. Proin a vestibulum erat, quis aliquam risus. Maecenas at diam libero. Mauris orci velit, elementum vel elementum id, suscipit sed lorem. Donec ornare ex eu porttitor auctor. Proin feugiat non lacus sed vehicula. Pellentesque nec porta tellus. Donec lectus tortor, eleifend sed elit vitae, dapibus cursus mi. Integer vel libero sit amet ante congue finibus. Suspendisse bibendum quam arcu, a porta neque porttitor porta. Aliquam porta justo lacinia posuere congue. Cras ut ante tellus. Donec cursus et magna ornare varius. Morbi ac viverra arcu, non laoreet metus.";

            AlertDialog.Builder builder = new AlertDialog.Builder(HelpActivity.this)
                    .setTitle("How To Use")
                    .setIcon(getResources().getDrawable(R.drawable.information))
                    .setMessage(instructions)
                    .setPositiveButton("OK",null);
            builder.create();
            builder.show();

        });

        btnReport.setOnClickListener(v -> {

            FirebaseUser firebaseUser = mAuth.getCurrentUser();

            if(firebaseUser != null) reportContainer.setVisibility(View.VISIBLE);
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpActivity.this)
                        .setTitle("Error")
                        .setIcon(getResources().getDrawable(R.drawable.warning))
                        .setMessage("You must be logged in to be able to Report a Problem.")
                        .setPositiveButton("OK",null);
                builder.create();
                builder.show();
            }
        });

        btnSendReport.setOnClickListener(v -> {



                String reportMessage = txtReport.getText().toString();

                if (reportMessage.trim().length() <= 12) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(HelpActivity.this)
                            .setTitle("ERROR")
                            .setIcon(getResources().getDrawable(R.drawable.warning))
                            .setMessage("Report must be more than 12 Characters.")
                            .setPositiveButton("OK", null);
                    builder.create();
                    builder.show();

                } else {

                    Report report = new Report(reportMessage,mAuth.getCurrentUser().getEmail());

                    ProgressDialog d = new ProgressDialog(HelpActivity.this);

                    d.setMessage("Sending Report...");
                    d.show();
                    d.setCancelable(false);

                    mDatabase.getReference().child("Reports").push().setValue(report).addOnCompleteListener(task -> {
                        d.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(HelpActivity.this)
                                .setTitle("SUCCESS")
                                .setIcon(getResources().getDrawable(R.drawable.information))
                                .setMessage("Report Successfully Sent!")
                                .setPositiveButton("OK", null);
                        builder.create();
                        builder.show();

                        reportContainer.setVisibility(View.GONE);
                        txtReport.setText("");
                    });


                }


        });


    }
}