package com.example.controluser;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import BDD.DatabaseUser;
import modele.User;

public class VisualisationUserActivity extends AppCompatActivity  {

    private ListView listUser;
    private TextView nombreUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisation_user);

        DatabaseUser dbUser = new DatabaseUser(this);

        //Obtention  les Widgets
        this.nombreUser = findViewById(R.id.nombreUser);
        this.listUser = findViewById(R.id.listeUser);

        this.nombreUser.setText("Nombre d'Useurs: " + dbUser.getUserCount() );

        List<User> userList = dbUser.getAllUser();

        List<String> listeNomPrenom = new ArrayList<String>();

        for (User u: userList){
            listeNomPrenom.add(u.getUserId() +"Nom : " + u.getNom()+  "   Prénom: " +  u.getPrenom());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listeNomPrenom);
        listUser.setAdapter(arrayAdapter);

        // ListView Item Click Listener (Lorsque l'on clique sur la liste)
        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idUser = (String) listUser.getItemAtPosition(position);

                Intent intent = new Intent(VisualisationUserActivity.this, InfoUserActivity.class);
                intent.putExtra("idUser", idUser);
                startActivity(intent);
            }
        });
    }
}
