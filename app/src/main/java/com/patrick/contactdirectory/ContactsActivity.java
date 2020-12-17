package com.patrick.contactdirectory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    RecyclerView listContacts;

    TextView txtSearch;

    ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        listContacts = findViewById(R.id.listContacts);
        txtSearch = findViewById(R.id.txtSearch);

        loadContacts();
        searchContacts();
    }

    void loadContacts(){

        contacts.add(new Contact("Patrick Macaraig","Sun","09123456789","Tomorrow"));
        contacts.add(new Contact("Sdpt Alenere","Moon","12345","Today"));
        contacts.add(new Contact("Sdpt David","Moon","123456","Wednesday"));
        contacts.add(new Contact("Jaymar Catapang","Earth","1234567","Monday"));
        contacts.add(new Contact("Ricardo Santos","Venus","12345678","Tuesday"));
        contacts.add(new Contact("Hezel Santos","Venus","123456789","Friday"));

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
                filterContacts(s.toString().toLowerCase());
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
            startActivity(i);

        });

        listContacts.setAdapter(null);

        listContacts.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listContacts.setAdapter(adapter);

    }

}