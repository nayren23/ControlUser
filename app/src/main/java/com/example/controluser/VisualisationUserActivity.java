package com.example.controluser;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import BDD.DatabaseUser;
import modele.User;

public class VisualisationUserActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisation_user);

        this.listUser = findViewById(R.id.listeUser);
        DatabaseUser dbUser = new DatabaseUser(this);


        List<User> userList = dbUser.getAllUser();

        List<String> listeNom = new ArrayList<String>();

        for (User u: userList){
            listeNom.add("Nom : " + u.getNom()+  "   Prénom: " +  u.getPrenom());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listeNom);

        listUser.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {

    }
}
