package com.patrick.contactdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class RegisterActivity extends AppCompatActivity {

    TextView btnSignIn;

    Button btnRegister;

    EditText txtEmailAddress, txtPassword,txtConfirmPassword;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        btnRegister = findViewById(R.id.btnRegister);

        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);

        btnRegister.setOnClickListener(v -> {

            String email = txtEmailAddress.getText().toString();
            String password = txtPassword.getText().toString();
            String confirmPassword = txtConfirmPassword.getText().toString();
            
            if(email.trim().length() <= 0) Toast.makeText(this, "Provide an Email Address.", Toast.LENGTH_SHORT).show();
            else if(password.length() < 8) Toast.makeText(this, "Password must be 8 Characters or above.", Toast.LENGTH_SHORT).show();
            else if(!password.equals(confirmPassword)) Toast.makeText(this, "Password doesn't Match", Toast.LENGTH_SHORT).show();
            else signUp(email,password);

        });

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v -> finish());
    }

    private void signUp(String email, String password) {

        ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("Registering...");
        dialog.show();
        dialog.setCancelable(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if(user != null){
                            dialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                                    .setCancelable(false)
                                    .setMessage(user.getEmail() + "\nRegistered Successfully.")
                                    .setPositiveButton("OK", (dialog1, which) -> finish())
                                    .setTitle("SUCCESS")
                                    .setIcon(getResources().getDrawable(R.drawable.information));

                            builder.create();
                            builder.show();
                        }

                    } else {

                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
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


}