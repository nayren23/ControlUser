package com.example.controluser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.Notification;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import BDD.DatabaseUser;
import modele.User;

public class MainActivity extends AppCompatActivity {

    //préfixer les attributs avec la lettre m (pour member en anglais)
    //les variables statiques sont préfixées par la lettre s.

    private EditText mMain_champ_nom;
    private EditText mMain_champ_prenom;
    private EditText mMain_champ_adresse;
    private EditText mMain_champ_numero_telephone;

    private Button mButtonImage;
    private ImageView imageView;

    private Button mSauvegarde_compte;
    private Button mVisualisation_users;


    private User mUser;

    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_CODE_VISUALISATION_ACTIVITY = 42;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//permet de déterminer quel fichier layout utiliser. R.layout.nom_du_fichier (sans l'extension XML)
        /**
         * On ne peut utiliser la méthode findViewById() qu’après avoir utilisé la
         * méthode setContentView().
         *
         * Pour obtenir les Widgets dans l’Activity, la méthode à appeler
         * est findViewById().
         *
         * Un Widget doit avoir un attribut id dans le layout pour être référençable dans
         * l’Activity.
         */
        this.mMain_champ_nom = findViewById(R.id.main_champ_nom);
        this.mMain_champ_prenom = findViewById(R.id.main_champ_prenom);
        this.mMain_champ_adresse = findViewById(R.id.main_champ_adresse);
        this.mMain_champ_numero_telephone = this.findViewById(R.id.main_champ_numero_telephone);
        this.mButtonImage = this.findViewById(R.id.button_image);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        this.mSauvegarde_compte = this.findViewById(R.id.sauvegarde_compte);
        this.mVisualisation_users = this.findViewById(R.id.visualisation_users);

        this.mSauvegarde_compte.setEnabled(false);

        mUser = new User();
        //On crer la BDD user
        DatabaseUser dbUser = new DatabaseUser(this);
        dbUser.createDefaultUsersIfNeed();
        dbUser.addUser(mUser);
        //List<User> userList = dbUser.getAllUser();

        //Notification
        this.editTextTitle = (EditText) this.findViewById(R.id.editText_title);
        this.editTextMessage = (EditText) this.findViewById(R.id.editText_message);


        /**
         * il faut pouvoir être notifié lorsque l'utilisateur commence à saisir du texte
         * dans le champ EditText correspondant
         */
        mNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                i = 0;
                i1 = 0;
                i2 = 0;
            }

            /**
             * This is where we'll check the user input
             * La méthode à utiliser pour détecter un changement de texte dans une EditText
             * est afterTextChanged().
             */
            @Override
            public void afterTextChanged(Editable s) {
                mPlayButton.setEnabled(!s.toString().isEmpty());
            }
        });


        /**
         * Pour détecter que l'utilisateur a cliqué sur le bouton, il est nécessaire
         * d'implémenter un View.OnClickListener
         */
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstName(mNameEditText.getText().toString()); //On change le prénom du joueur
                dbUser.updateUser(mUser);

                // The user just clicked
                getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                        .edit()
                        .putString(SHARED_PREF_USER_INFO_NAME, mNameEditText.getText().toString())
                        .apply();
                startActivityForResult(new Intent(MainActivity.this, GameActivity.class), REQUEST_CODE_GAME_ACTIVITY);
            }
        });
        greetUser();//pour que meme si on ferme l'app on garde en cache les infos de l'useur



        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        //save fichier
        this.saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }



    private void captureImage() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }


/*
    @SuppressLint("StringFormatInvalid")
    private void greetUser() {
        //Avec les getSharedPreferences
        // String firstName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);
        // int score = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_SCORE, -1); // -1 pour verifier si la case n'est pas null

        //Avec recuperation des infis de la BDD
        DatabaseUser dbUser = new DatabaseUser(this);
        User userBDD =  dbUser.getUser(this.mUser.getUserId());
        String firstName = userBDD.getFirstName();
        int score = userBDD.getScoreJoueur();

        if (firstName != null) {
            if (score != -1) {
                readData();
                mGreetingTextView.setText(getString(R.string.welcome_back_with_score) +" "+ firstName + " " + score);
                readImage();
            } else {
                mGreetingTextView.setText(getString(R.string.welcome_back) + " " + firstName);
            }
            mNameEditText.setText(firstName);
        }
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_GAME_ACTIVITY == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);

            //on change la valeur dans les shared preferences
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                    .edit()
                    .putInt(SHARED_PREF_USER_INFO_SCORE, score)
                    .apply();
            greetUser();
        }

        //Camera
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.imageView.setImageBitmap(bp);
                String nomFichier =  saveData();
                saveImage(bp,nomFichier);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

}