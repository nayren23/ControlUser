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

public class VisualisationUserActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listUser;
    private TextView nombreUser;

    private static final int REQUEST_CODE_INFO_USER_ACTIVITY = 23;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisation_user);

        DatabaseUser dbUser = new DatabaseUser(this);

        this.nombreUser = findViewById(R.id.nombreUser);
        this.listUser = findViewById(R.id.listeUser);

        this.nombreUser.setText("Nombre d'Useurs: " + dbUser.getUserCount() );


        List<User> userList = dbUser.getAllUser();

        List<String> listeNom = new ArrayList<String>();

        for (User u: userList){
            listeNom.add(u.getUserId() +"Nom : " + u.getNom()+  "   Prénom: " +  u.getPrenom());
        }

        // ListView Item Click Listener (Lorsque l'on clique sur la liste)
        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idListView = (String) listUser.getItemAtPosition(position);

                System.out.println("Id de List VIwev"  +  idListView);
                System.out.println("ChartAt 0"  +  idListView.charAt(0));

                String idUser = idListView;

                String tets = "fbezhf" ;
                String message = "Bonjour de la première activité";
                Intent intent = new Intent(VisualisationUserActivity.this, InfoUserActivity.class);
                intent.putExtra("MESSAGE", idUser);


                startActivity(intent);



/*
                Intent i1 = new Intent( VisualisationUserActivity.this, InfoUserActivity.class );
                i1.putExtra(EXTRA_MESSAGE, idUser );
                startActivityForResult(i1, REQUEST_CODE_INFO_USER_ACTIVITY);*/

               // startActivityForResult(new Intent(VisualisationUserActivity.this, InfoUserActivity.class), REQUEST_CODE_INFO_USER_ACTIVITY);

            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listeNom);

        listUser.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {

    }
}
