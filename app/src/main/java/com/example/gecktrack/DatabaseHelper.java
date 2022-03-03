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
import android.util.EventLog;

import androidx.annotation.Nullable;

import com.example.gecktrack.ui.calendar.EventModel;
import com.example.gecktrack.ui.mygecks.GeckoModel;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


// -------------------------------------------------------------------------------------------------


public class DatabaseHelper extends SQLiteOpenHelper
{


    // constructor
    public DatabaseHelper(@Nullable Context context)
    {
        super(context, "GeckTrack Database", null, 3);
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
        String createEventTable     = "CREATE TABLE EVENT (EventID INTEGER PRIMARY KEY AUTOINCREMENT, EventName TEXT, Type TEXT, Date TEXT, Time TEXT, Notifications TEXT, Notes TEXT, Geckos TEXT)";
        db.execSQL(createEventTable);

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
        // remove geckoID from events it appears in FIRST
        removeGeckoFromEvents(gecko.getID());

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


    // add a new event to the database
    public boolean addEvent(EventModel newEvent)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // create columns and insert data
        cv.put("EventName", newEvent.getName());
        cv.put("Type", newEvent.getType());
        cv.put("Date", newEvent.getDate());
        cv.put("Time", newEvent.getTime());
        cv.put("Notifications", newEvent.getNotifications());
        cv.put("Notes", newEvent.getNotes());
        cv.put("Geckos", newEvent.getGeckos());

        // add this new record to GECKO table, no null columns allowed
        long insert = db.insert("EVENT", null, cv);

        if (insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // remove event from database
    public boolean deleteEvent(EventModel event)
    {
        // access database and write query
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteEventQuery = "DELETE FROM EVENT " +
                                  "WHERE EventID = " + event.getID();

        Cursor cursor = db.rawQuery(deleteEventQuery, null);

        // return true if event found, should always be true
        if(cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // get ALL of the users inputted events
    public List<EventModel> getTotalEventList()
    {
        List<EventModel> returnList = new ArrayList<>();

        String getEventsQuery = "SELECT * FROM EVENT";
        SQLiteDatabase db = this.getReadableDatabase();

        // cursor is result set from query
        Cursor cursor = db.rawQuery(getEventsQuery, null);

        if(cursor.moveToFirst())
        {
            // loop through cursor set, get relevant data
            do
            {
                int    ID             = cursor.getInt(0);
                String name           = cursor.getString(1);
                String type           = cursor.getString(2);
                String date           = cursor.getString(3);
                String time           = cursor.getString(4);
                String notifications  = cursor.getString(5);
                String notes          = cursor.getString(6);
                String geckos         = cursor.getString(7);

                // create new event, add to returnList
                EventModel listEvent = new EventModel(ID, name, type, date, time, notifications,
                                                      notes, geckos);
                returnList.add(listEvent);

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

    // get all dates (ONLY) for marking calendar events
    public ArrayList<String> getAllEventDates()
    {
        ArrayList<String> dates = new ArrayList<>();
        List<EventModel> events = getTotalEventList();

        // get each individual date, add it to dates
        for (EventModel event : events)
        {
            dates.add(event.getDate());
        }

        return dates;
    }

    // get the users inputted events for the selected day
    public List<EventModel> getSelectedDayEventList(String selectedDay)
    {
        List<EventModel> returnList = new ArrayList<>();

        String getEventsQuery = "SELECT * FROM EVENT WHERE Date = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        // cursor is result set from query
        Cursor cursor = db.rawQuery(getEventsQuery, new String[] {selectedDay});

        if(cursor.moveToFirst())
        {
            // loop through cursor set, get relevant data
            do
            {
                int    ID             = cursor.getInt(0);
                String name           = cursor.getString(1);
                String type           = cursor.getString(2);
                String date           = cursor.getString(3);
                String time           = cursor.getString(4);
                String notifications  = cursor.getString(5);
                String notes          = cursor.getString(6);
                String geckos         = cursor.getString(7);

                // create new event, add to returnList
                EventModel listEvent = new EventModel(ID, name, type, date, time, notifications,
                        notes, geckos);
                returnList.add(listEvent);

            } while(cursor.moveToNext());
        }
        else
        {
            // failed, list is probably empty
        }

        // close cursor and db
        cursor.close();
        db.close();

        return returnList;
    }

    // get a String of gecko names that match the ID
    public String getAssociatedGeckos(String IDs)
    {
        // use scanner to get each individual ID from string
        Scanner separator = new Scanner(IDs).useDelimiter(" ");
        StringBuilder nameList = new StringBuilder();

        // loop through separated strings
        while (separator.hasNext())
        {
            String geckoID = String.valueOf(separator.next());
            String geckoName = getGeckoNameByID(geckoID);

            // on the last item
            if (!separator.hasNext())
            {
                nameList.append(geckoName);
            }
            else
            {
                nameList.append(geckoName + ", ");
            }
        }

        nameList.trimToSize();

        return nameList.toString();
    }

    // find the ONE gecko that matches the given ID
    public String getGeckoNameByID(String geckoID)
    {
        String getEventsQuery = "SELECT * FROM GECKO WHERE GeckoID = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        String geckoName = "";

        // cursor is result set from query
        Cursor cursor = db.rawQuery(getEventsQuery, new String[] {geckoID});

        // SHOULD ONLY RETURN ONE GECKO THAT MATCHED ID
        if(cursor.moveToFirst())
        {
            // loop through cursor set, get gecko match
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

                // create new gecko, add to name
                GeckoModel listGecko = new GeckoModel(ID, name, sex, birthday, age, species, morph,
                        weight, temperature, humidity, status, seller, photo);

                geckoName = listGecko.getName();

            } while(cursor.moveToNext());
        }
        else
        {
            // failed, list is probably empty
        }

        // close cursor and db
        cursor.close();
        db.close();

        return geckoName;
    }

    // when gecko is deleted, remove it from all events it appears in
    public void removeGeckoFromEvents(int ID)
    {
        // get all existing events
        List<EventModel> events = getTotalEventList();

        // look at each events gecko list
        for (EventModel event : events)
        {
            // use scanner to get each individual ID from string
            Scanner separator = new Scanner(event.getGeckos()).useDelimiter(" ");

            // new gecko ID list will build in here (all IDs EXCEPT the deleted gecko)
            StringBuilder geckoIDs = new StringBuilder();

            // loop through separated IDs
            while (separator.hasNext())
            {
                String geckoID = String.valueOf(separator.next());

                // compare current ID to search ID
                // ID not found, keep it in String
                if (!geckoID.contains(String.valueOf(ID)))
                {
                    geckoIDs.append(geckoID + " ");
                }
                // means gecko ID was found, skip it (don't add to String)
                else
                {
                    System.out.println("GeckoID #" + geckoID + " removed from event " + event.getName());
                }

                // if gecko ID list comes out empty, delete event
                if(geckoIDs.toString().isEmpty())
                {
                    deleteEvent(event);
                    System.out.println("Deleted Event " + event.getName());
                }
                // else, event still has geckos, update them
                else
                {
                    // access database and write query
                    SQLiteDatabase db = this.getWritableDatabase();
                    ContentValues cv = new ContentValues();

                    // prepare data
                    geckoIDs.trimToSize();
                    cv.put("Geckos", geckoIDs.toString());

                    // update geckoID list for current event
                    db.update("EVENT", cv, "EventID = " + event.getID(), null);
                    db.close();
                }
            }

        }
    }
}
