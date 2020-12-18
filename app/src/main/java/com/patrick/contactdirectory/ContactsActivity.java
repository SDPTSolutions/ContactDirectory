package com.patrick.contactdirectory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

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

        listContacts = findViewById(R.id.listContacts);
        txtSearch = findViewById(R.id.txtSearch);

        btnMenu = findViewById(R.id.btnMenu);
        btnAccount = findViewById(R.id.btnAccount);

        btnMenu.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),MenuActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),LoginActivity.class)));


        firebaseTest();
        dummyData();
        loadContacts();
        searchContacts();
    }

    void firebaseTest(){

        writeNewContact("Patrick Macaraig","Sun","09123456789","Tomorrow");

    }

    private void writeNewContact(String name, String location, String number, String schedule) {
        Contact contact = new Contact(name, location,number,schedule);

        mDatabase.child("contacts").push().setValue(contact);
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

        listContacts.setAdapter(null);

        ContactAdapter adapter = new ContactAdapter(contacts,getBaseContext());
        adapter.setClickListener((view, position) -> {
            Contact contact = contacts.get(position);

            Intent i = new Intent(getBaseContext(),InformationActivity.class);

            i.putExtra("name",contact.getName());
            i.putExtra("location",contact.getLocation());
            i.putExtra("number",contact.getNumber());
            i.putExtra("schedule",contact.getSchedule());


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
                else loadContacts();

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


            startActivity(i);

        });

        listContacts.setAdapter(null);

        listContacts.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listContacts.setAdapter(adapter);

    }

}