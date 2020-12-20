package com.patrick.contactdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    DatabaseReference mDatabase;

    ImageView btnMenu, btnAccount;

    RecyclerView listContacts;
    TextView txtSearch;

    ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        listContacts = findViewById(R.id.listContacts);
        txtSearch = findViewById(R.id.txtSearch);

        btnMenu = findViewById(R.id.btnMenu);
        btnAccount = findViewById(R.id.btnAccount);

        btnMenu.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),MenuActivity.class)));
        btnAccount.setOnClickListener(v -> {

            if(mAuth.getCurrentUser() == null) startActivity(new Intent(getBaseContext(),LoginActivity.class));
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this)
                        .setTitle("PROFILE")
                        .setMessage(
                                "Email : " + mAuth.getCurrentUser().getEmail()
                        )
                        .setPositiveButton("OK",null)
                        .setNegativeButton("Log Out", (dialog, which) -> {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(this, "Account Signed Out.", Toast.LENGTH_SHORT).show();
                        });
                builder.create();
                builder.show();

            }

        });


        //dummyData();
        loadContacts();
        searchContacts();
    }

    void dummyData(){

        contacts.add(new Contact("Patrick Macaraig","Sun","09123456789","Tomorrow"));
        contacts.add(new Contact("Sdpt Alenere","Moon","12345","Today"));
        contacts.add(new Contact("Sdpt David","Moon","123456","Wednesday"));
        contacts.add(new Contact("Jaymar Catapang","Earth","1234567","Monday"));
        contacts.add(new Contact("Ricardo Santos","Venus","12345678","Tuesday"));
        contacts.add(new Contact("Hezel Santos","Venus","123456789","Friday"));

    }

    void loadContacts(){

        mDatabase.child("contacts").addValueEventListener(new ValueEventListener() {
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

    void searchContacts(){

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count >0) filterContacts(s.toString().toLowerCase());
                else showContacts();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void filterContacts(String word){

        ArrayList<Contact> filteredContacts = new ArrayList<>();

        for(Contact contact:contacts){
            if(contact.getName().toLowerCase().contains(word)){
                filteredContacts.add(contact);
            }
        }

        ContactAdapter adapter = new ContactAdapter(filteredContacts,getBaseContext());

        adapter.setClickListener((view, position) -> {
            Contact contact = filteredContacts.get(position);

            Intent i = new Intent(getBaseContext(),InformationActivity.class);

            i.putExtra("name",contact.getName());
            i.putExtra("location",contact.getLocation());
            i.putExtra("number",contact.getNumber());
            i.putExtra("schedule",contact.getSchedule());
            i.putExtra("key",contact.getKey());
            i.putExtra("mapCoordinates",contact.getMapCoordinates());


            startActivity(i);

        });

        listContacts.setAdapter(null);

        listContacts.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listContacts.setAdapter(adapter);

    }

}