package com.patrick.contactdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InformationActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;

    TextView txtName, txtLocation, txtNumber, txtSchedule;

    ImageView btnBack, btnMap;

    LinearLayout btnShare, btnFavorite;

    Contact contact;

    TextView txtFavorite;
    boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        txtName = findViewById(R.id.txtName);
        txtLocation = findViewById(R.id.txtLocation);
        txtNumber = findViewById(R.id.txtNumber);
        txtSchedule = findViewById(R.id.txtSchedule);

        btnMap = findViewById(R.id.btnMap);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnFavorite = findViewById(R.id.btnFavorite);
        btnShare = findViewById(R.id.btnShare);

        txtFavorite = findViewById(R.id.txtFavorite);

        contact  = new Contact();

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if(bundle != null) {
                contact.setName(bundle.getString("name"));
                contact.setLocation(bundle.getString("location"));
                contact.setNumber(bundle.getString("number"));
                contact.setSchedule(bundle.getString("schedule"));
                contact.setKey(bundle.getString("key"));
                contact.setMapCoordinates(bundle.getString("mapCoordinates"));
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
        
        
        btnFavorite.setOnClickListener(v -> toggleFavorite());
        btnShare.setOnClickListener(v -> {

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("contactInfo",
                    "Name\n" + contact.getName() + "\n\n" +
                         "Location\n" + contact.getLocation() + "\n\n" +
                         "Number\n" + contact.getNumber() + "\n\n" +
                         "Schedule\n" + contact.getSchedule() + "\n\n" +
                         "Coordinates\n" + contact.getMapCoordinates());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();

        });

        btnMap.setOnClickListener(v -> {
            Uri uri = Uri.parse(contact.getMapCoordinates());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        txtNumber.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this)
                    .setTitle("Confirmation")
                    .setMessage("Call " + contact.getName() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        if(Build.VERSION.SDK_INT > 22) {
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling

                                ActivityCompat.requestPermissions(InformationActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                                return;
                            }
                        }
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+contact.getNumber()));
                        startActivity(callIntent);
                    })
                    .setNegativeButton("Cancel", null);

            builder.create();
            builder.show();


        });

        checkContact();

    }

    private void checkContact() {

        ProgressDialog d = new ProgressDialog(InformationActivity.this);
        d.setMessage("Checking Database...");
        d.show();
        d.setCancelable(false);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser != null) {

            mDatabase.getReference().child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot snap: snapshot.getChildren()){
                        Contact c = snap.getValue(Contact.class);
                        if(c.getKey().equals(contact.getKey())){
                            isFavorite = true;
                            txtFavorite.setText("Unfavorite");
                            break;
                        }
                    }

                    if(!isFavorite) txtFavorite.setText("Favorite");

                    d.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else d.dismiss();


    }

    private void toggleFavorite() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser != null) {

            AlertDialog.Builder builder;

            if(!isFavorite) {
                builder = new AlertDialog.Builder(InformationActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Add this to your Favorites?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            mDatabase.getReference().child("users").child(mAuth.getUid()).child(contact.getKey()).setValue(contact);
                            checkContact();
                        })
                        .setNegativeButton("Cancel", null);
            }else{
                builder = new AlertDialog.Builder(InformationActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Remove this from your Favorites?")
                        .setPositiveButton("Yes", (dialog, which) -> {

                            ProgressDialog d = new ProgressDialog(InformationActivity.this);
                            d.setMessage("Removing from Favorites...");
                            d.show();
                            d.setCancelable(false);

                            isFavorite = false;
                            txtFavorite.setText("Favorite");

                            mDatabase.getReference().child("users").child(mAuth.getUid()).child(contact.getKey()).removeValue().addOnCompleteListener(aVoid -> {
                                d.dismiss();
                            });
                        })
                        .setNegativeButton("Cancel", null);
            }

            builder.create();
            builder.show();

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this)
                    .setTitle("Error")
                    .setIcon(getResources().getDrawable(R.drawable.warning))
                    .setMessage("You must be logged in to be able to add Favorites.")
                    .setPositiveButton("OK",null);
            builder.create();
            builder.show();
        }

    }
}