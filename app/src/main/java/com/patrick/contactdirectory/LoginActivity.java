package com.patrick.contactdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextView btnSignUp;

    Button btnLogin;
    EditText txtEmailAddress, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btnLogin);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        txtPassword = findViewById(R.id.txtPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),RegisterActivity.class)));
        if(mAuth.getCurrentUser() != null) Toast.makeText(this, "You are Logged In!", Toast.LENGTH_SHORT).show();

        btnLogin.setOnClickListener(v -> {

            String email = txtEmailAddress.getText().toString();
            String password = txtPassword.getText().toString();

            if(email.trim().length() <= 0) Toast.makeText(this, "Provide an Email Address.", Toast.LENGTH_SHORT).show();
            else if(password.length() < 8) Toast.makeText(this, "Password must be 8 Characters or above.", Toast.LENGTH_SHORT).show();
            else signIn(email,password);

        });

    }

    private void signIn(String email, String password) {

        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Signing In...");
        dialog.show();
        dialog.setCancelable(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if(user != null){
                            dialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                    .setCancelable(false)
                                    .setMessage(user.getEmail() + "\nSigned In Successfully.")
                                    .setPositiveButton("OK", (dialog1, which) -> finish())
                                    .setTitle("SUCCESS")
                                    .setIcon(getResources().getDrawable(R.drawable.information));

                            builder.create();
                            builder.show();
                        }

                    } else {

                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                .setCancelable(false)
                                .setMessage(task.getException().getMessage())
                                .setPositiveButton("OK", null)
                                .setTitle("ERROR")
                                .setIcon(getResources().getDrawable(R.drawable.warning));

                        builder.create();
                        builder.show();

                    }

                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) finish();

    }
}