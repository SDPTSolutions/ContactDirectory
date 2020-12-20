package com.patrick.contactdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    DatabaseReference mDatabase;

    RecyclerView listContacts;

    ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        listContacts = findViewById(R.id.listContacts);

        loadContacts();
    }

    void loadContacts(){

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        mDatabase.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                contacts.clear();
                listContacts.setAdapter(null);

                for(DataSnapshot snap:snapshot.getChildren()){
                    Contact contact = snap.getValue(Contact.class);
                    contacts.add(contact);

                    showContacts();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void showContacts(){

        listContacts.setAdapter(null);

        ContactAdapter adapter = new ContactAdapter(contacts,getBaseContext());
        adapter.setClickListener((view, position) -> {
            Contact contact = contacts.get(position);

            Intent i = new Intent(getBaseContext(),InformationActivity.class);

            i.putExtra("name",contact.getName());
            i.putExtra("location",contact.getLocation());
            i.putExtra("number",contact.getNumber());
            i.putExtra("schedule",contact.getSchedule());
            i.putExtra("key",contact.getKey());
            i.putExtra("mapCoordinates",contact.getMapCoordinates());


            startActivity(i);
        });

        listContacts.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listContacts.setAdapter(adapter);

    }
}