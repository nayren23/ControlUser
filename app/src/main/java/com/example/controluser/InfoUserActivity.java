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

public class InfoUserActivity extends AppCompatActivity {

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
        String message = intent.getStringExtra("idUser");

        //Obtention  les Widgets
        this.imageViewUser = findViewById(R.id.imageViewUser);
        this.nomUser = findViewById(R.id.nomUser);
        this.prenomUser = findViewById(R.id.prenomUser);
        this.adresseUser = findViewById(R.id.adresseUser);
        this.numTelephone = findViewById(R.id.numTelephone);

        int id = Character.getNumericValue(message.charAt(0));
        User user = dbUser.getUser(id);

        String cheminImage = user.getNom()+user.getPrenom();
        Bitmap imageUser = readImage(cheminImage);

        //On set les champs
        this.imageViewUser.setImageBitmap(imageUser);
        this.nomUser.setText("Nom : " +user.getNom());
        this.prenomUser.setText("Prénom : " + user.getPrenom());
        this.adresseUser.setText("Adresse : " + user.getAdresse());
        this.numTelephone.setText("Numéro Téléphone : " + user.getNumeroTelephone());
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
