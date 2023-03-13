package BDD;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import modele.User;

//Il n'y a qu'une seule bdd dans le téléphone, les new sont la pour instancier la connexion à cette BDD
public class DatabaseUser extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "User_Manager";

    // Table name: User.
    private static final String TABLE_USER = "User";

    //On creer la structure de la table
    private static final String COLUMN_USER_ID ="User_Id";
    private static final String COLUMN_USER_NOM ="User_nom";
    private static final String COLUMN_USER_PRENOM ="User_prenom";
    private static final String COLUMN_USER_ADRESSE ="User_adresse";
    private static final String COLUMN_USER_NUMERO_TELEPHONE ="User_numeroTelephone";
    private static final String COLUMN_USER_PHOTO_PROFIL ="User_photoDeProfil";

    public DatabaseUser(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY," + COLUMN_USER_NOM +
                " TEXT," + COLUMN_USER_PRENOM +
                " TEXT," + COLUMN_USER_ADRESSE +
                " TEXT," +  COLUMN_USER_NUMERO_TELEPHONE +
                " TEXT,"+ COLUMN_USER_PHOTO_PROFIL +
                " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    // If User table has no data
    // default, Insert 2 records.
    public void createDefaultUsersIfNeed()  {
        int count = this.getUserCount();
        if(count ==0 ) {
            User user1 = new User(0 , "Admin","adminApp", "2 rue de l'admin", "0781799878");
            User user2 = new User(1 , "AdminPremium","adminAppPremium", "10 rue de l'admin", "0911223344");
        }
    }

    //On ajoute un User
    public void addUser(User user) {
        Log.i(TAG, "MyDatabaseHelper.addUser ... " + user.getNom()); // affiche un message dans la console android

        SQLiteDatabase db = this.getWritableDatabase();//ouvre une connexion à la base de données en mode écriture

        ContentValues values = new ContentValues(); //stocker des paires clé-valeur de données à insérer ou mettre à jour dans une base de données SQLite

        //on prepare les donnees suivantes
        values.put(COLUMN_USER_NOM, user.getNom());
        values.put(COLUMN_USER_PRENOM, user.getPrenom());
        values.put(COLUMN_USER_ADRESSE, user.getAdresse());
        values.put(COLUMN_USER_NUMERO_TELEPHONE, user.getNumeroTelephone());
        values.put(COLUMN_USER_PHOTO_PROFIL, user.getPhotoDeProfil());

        // Inserting Row
        db.insert(TABLE_USER, null, values);

        // Closing database connection
        db.close();
    }

    public User getUser(int id) {
        Log.i(TAG, "MyDatabaseHelper.getUser ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] { COLUMN_USER_ID,
                        COLUMN_USER_NOM,COLUMN_USER_PRENOM, COLUMN_USER_ADRESSE,COLUMN_USER_NUMERO_TELEPHONE,COLUMN_USER_PHOTO_PROFIL }, COLUMN_USER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return User
        return user;
    }

    public List<User> getAllUser() {
        Log.i(TAG, "MyDatabaseHelper.getAllUsers ... " );

        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserId(Integer.parseInt(cursor.getString(0)));
                user.setNom((cursor.getString(1)));
                user.setPrenom((cursor.getString(2)));
                user.setAdresse((cursor.getString(3)));
                user.setNumeroTelephone((cursor.getString(4)));
                user.setPhotoDeProfil((cursor.getString(5)));

                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return user list
        return userList;
    }

    public int getUserCount() {
        Log.i(TAG, "MyDatabaseHelper.getUsersCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int updateUser(User user) {
        Log.i(TAG, "MyDatabaseHelper.updateUser ... "  + user.getPrenom());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NOM, user.getNom());
        values.put(COLUMN_USER_PRENOM, user.getPrenom());
        values.put(COLUMN_USER_ADRESSE, user.getAdresse());
        values.put(COLUMN_USER_NUMERO_TELEPHONE, user.getNumeroTelephone());
        values.put(COLUMN_USER_PHOTO_PROFIL, user.getPhotoDeProfil());

        // updating row
        return db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
    }

    public void deleteUser(User user) {
        Log.i(TAG, "MyDatabaseHelper.updateUser ... " + user.getNom());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[] { String.valueOf(user.getUserId()) });
        db.close();
    }
}

