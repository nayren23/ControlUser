package com.example.controluser;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import BDD.DatabaseUser;
import modele.User;

public class InfoUserActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewUser;
    private TextView nomUser;
    private TextView prenomUser;
    private TextView adresseUser;
    private TextView numTelephone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        DatabaseUser dbUser = new DatabaseUser(this);

        Intent intent = getIntent();
        String message = intent.getStringExtra("MESSAGE");
        System.out.println("affichahe vallll"+ message.charAt(0));

        this.imageViewUser = findViewById(R.id.imageViewUser);
        this.nomUser = findViewById(R.id.nomUser);
        this.prenomUser = findViewById(R.id.prenomUser);
        this.adresseUser = findViewById(R.id.adresseUser);
        this.numTelephone = findViewById(R.id.numTelephone);



        int id = Character.getNumericValue(message.charAt(0));
        System.out.println("affichage super id user " + id);

        User user = dbUser.getUser(id);

        String cheminImage = user.getNom()+user.getPrenom();
        System.out.println("chemin Image "  + cheminImage );

        Bitmap imageUser = readImage(cheminImage);

        this.imageViewUser.setImageBitmap(imageUser);
        this.nomUser.setText("Nom Utilisateur : " +user.getNom());
        this.prenomUser.setText("Prenom Utilisateur : " + user.getPrenom());
        this.adresseUser.setText("Adresse Utilisateur : " + user.getAdresse());
        this.numTelephone.setText("Numero Telephone Utilisateur : " + user.getNumeroTelephone());

    }
    @Override
    public void onClick(View view) {


    }
    private Bitmap readImage(String nomFichier) {
        Bitmap bitmap = null;
        try {
            // Open stream to read file.
            FileInputStream in = new FileInputStream(this.getFilesDir()+"/"+nomFichier);

            // Decode file input stream into a bitmap.
            bitmap = BitmapFactory.decodeStream(in);

            // Close the input stream.
            in.close();

        } catch (Exception e) {
            Toast.makeText(this,"Error Impossible c'est une nouvelle instance de l'app:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }
}
