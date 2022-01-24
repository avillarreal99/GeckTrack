// Amanda Villarreal
// January 13, 2022
// DatabaseHelper.java
// GeckTrack.app
// Handle GeckTrack database operations
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.gecktrack.ui.dashboard.GeckoModel;
import java.util.ArrayList;
import java.util.List;


// -------------------------------------------------------------------------------------------------


public class DatabaseHelper extends SQLiteOpenHelper
{


    // constructor
    public DatabaseHelper(@Nullable Context context)
    {
        super(context, "GeckTrack Database", null, 1);
    }

    // called the first time the database is accessed (code to generate new table)
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTableStatement = "CREATE TABLE GECKO (GeckoID INTEGER PRIMARY KEY AUTOINCREMENT, GeckoName TEXT, Species TEXT, Sex TEXT, Birthday TEXT, Age TEXT, Morph TEXT, Weight TEXT, Temperature TEXT, Humidity TEXT, Status TEXT, Seller TEXT, Photo TEXT)";
        db.execSQL(createTableStatement);
    }

    // called if the version number of the database changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }


// GECKO TABLE METHODS -----------------------------------------------------------------------------

    // add a new gecko to the database
    public boolean addGecko(GeckoModel newGecko)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // create columns and insert data
        cv.put("GeckoName", newGecko.getName());
        cv.put("Species", newGecko.getGeckoSpecies());
        cv.put("Sex", newGecko.getSex());
        cv.put("Birthday", newGecko.getBirthday());
        cv.put("Age", newGecko.getAge());
        cv.put("Morph", newGecko.getMorph());
        cv.put("Weight", newGecko.getWeight());
        cv.put("Temperature", newGecko.getTemperature());
        cv.put("Humidity", newGecko.getHumidity());
        cv.put("Status", newGecko.getStatus());
        cv.put("Seller", newGecko.getSeller());
        cv.put("Photo", newGecko.getPhotoID());

        // add this new record to GECKO table, no null columns allowed
        long insert = db.insert("GECKO", null, cv);

        if (insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // get all of the users inputted geckos
    public List<GeckoModel> getGeckoList()
    {
        List<GeckoModel> returnList = new ArrayList<>();

        String getGeckosQuery = "SELECT * FROM GECKO";
        SQLiteDatabase db = this.getReadableDatabase();

        // cursor is result set from query
        Cursor cursor = db.rawQuery(getGeckosQuery, null);

        if(cursor.moveToFirst())
        {
            // loop through cursor set, get relevant data
            do
            {
                int    ID           = cursor.getInt(0);
                String name         = cursor.getString(1);
                String species      = cursor.getString(2);
                String sex          = cursor.getString(3);
                String birthday     = cursor.getString(4);
                String age          = cursor.getString(5);
                String morph        = cursor.getString(6);
                String weight       = cursor.getString(7);
                String temperature  = cursor.getString(8);
                String humidity     = cursor.getString(9);
                String status       = cursor.getString(10);
                String seller       = cursor.getString(11);
                String photo        = cursor.getString(12);

                // create new gecko, add to returnList
                GeckoModel listGecko = new GeckoModel(ID, name, sex, birthday, age, species, morph,
                                             weight, temperature, humidity, status, seller, photo);
                returnList.add(listGecko);

            } while(cursor.moveToNext());
        }
        else
        {
            // failed, do not add to list
        }

        // close cursor and db
        cursor.close();
        db.close();

        return returnList;
    }

    // remove gecko from database
    public boolean deleteGecko(GeckoModel gecko)
    {
        // access database and write query
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteGeckoQuery = "DELETE FROM GECKO " +
                                  "WHERE GeckoID = " + gecko.getID();

        Cursor cursor = db.rawQuery(deleteGeckoQuery, null);

        // return true if gecko found, should always be true
        if(cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // edit preexisting gecko in database
    public void editGecko(GeckoModel gecko)
    {
        // access database and write query
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // create columns with updated data
        cv.put("GeckoName", gecko.getName());
        cv.put("Species", gecko.getGeckoSpecies());
        cv.put("Sex", gecko.getSex());
        cv.put("Birthday", gecko.getBirthday());
        cv.put("Age", gecko.getAge());
        cv.put("Morph", gecko.getMorph());
        cv.put("Weight", gecko.getWeight());
        cv.put("Temperature", gecko.getTemperature());
        cv.put("Humidity", gecko.getHumidity());
        cv.put("Status", gecko.getStatus());
        cv.put("Seller", gecko.getSeller());
        cv.put("Photo", gecko.getPhotoID());

        // update the correct gecko with the new values
        db.update("GECKO", cv, "GeckoID = " + gecko.getID(), null);
        db.close();
    }

    // update age of gecko
    public void updateAge(GeckoModel gecko)
    {
        // access database and write query
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // create column with updated data
        cv.put("Age", gecko.getAge());

        // update the correct gecko with the new age
        db.update("GECKO", cv, "GeckoID = " + gecko.getID(), null);
        db.close();
    }


// EVENT TABLE METHODS -----------------------------------------------------------------------------

}
