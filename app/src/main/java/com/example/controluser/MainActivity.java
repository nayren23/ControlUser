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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import BDD.DatabaseUser;
import modele.User;

public class MainActivity extends AppCompatActivity {

    //préfixer les attributs avec la lettre m (pour member en anglais)
    //les variables statiques sont préfixées par la lettre s.

    //Edit Text
    private EditText mMain_champ_nom;
    private EditText mMain_champ_prenom;
    private EditText mMain_champ_adresse;
    private EditText mMain_champ_numero_telephone;

    private Button mButtonImage;
    private ImageView imageView;
    private Button mSauvegarde_compte;
    private Button mVisualisation_users;

    private boolean allFilled = true;// pour verifier si tous les champs sont remplit

    private User mUser;

    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;

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
        this.mButtonImage.setEnabled(false);

        //On crer la BDD user
        DatabaseUser dbUser = new DatabaseUser(this);
        dbUser.createDefaultUsersIfNeed();

        //List<User> userList = dbUser.getAllUser();

        /**
         * Pour détecter que l'utilisateur a cliqué sur le bouton, il est nécessaire
         * d'implémenter un View.OnClickListener
         */
        mSauvegarde_compte.setOnClickListener(new Button.OnClickListener() { // ou view
            @Override
            public void onClick(View v) {
                String nom = mMain_champ_nom.getText().toString();
                String prenom = mMain_champ_prenom.getText().toString();
                String adresse =  mMain_champ_adresse.getText().toString();
                String numeroTelephone =mMain_champ_numero_telephone.getText().toString() ;
                String photoDeProfil = mMain_champ_nom.getText().toString()+ mMain_champ_prenom.getText().toString() ;

                User creationUser = new User(nom,prenom,adresse,numeroTelephone,photoDeProfil);

                try {
                    enregistrementUser(creationUser);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        this.mButtonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        //

        EditText[] editTexts = {mMain_champ_nom, mMain_champ_prenom, mMain_champ_adresse,mMain_champ_numero_telephone}; // Ajoutez tous vos EditText ici

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    allFilled = true;
                    for (EditText editText : editTexts) {
                        String value = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(value)) {
                            allFilled = false;
                        }
                    }
                    if(allFilled){
                        mButtonImage.setEnabled(!s.toString().isEmpty());
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

    }

    private void enregistrementUser(User user) throws IOException {
        DatabaseUser dbUser = new DatabaseUser(this);
        dbUser.addUser(user);
        Toast.makeText(this, "User Sauvegarder avec Succées 😍!", Toast.LENGTH_SHORT).show();
    }

    private void saveImage(Bitmap bp, String nomFichier){
        try  { // use the absolute file path here
            FileOutputStream out = this.openFileOutput(nomFichier, MODE_PRIVATE);
            bp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            out.close();
            Toast.makeText(this,"Image Sauvegarder !",Toast.LENGTH_SHORT).show();
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void captureImage() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Camera
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.imageView.setImageBitmap(bp);

                //Image sauvegarder dans le fichier
                String nomFichierPhotoDeProfil = mMain_champ_nom.getText().toString()+ mMain_champ_prenom.getText().toString() ;
                saveImage(bp,nomFichierPhotoDeProfil);
                mSauvegarde_compte.setEnabled(true);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}