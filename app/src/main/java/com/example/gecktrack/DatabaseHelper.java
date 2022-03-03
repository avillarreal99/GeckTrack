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
import android.os.Build;
import android.util.EventLog;

import android.util.EventLog;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.gecktrack.ui.EventDayMap;
import com.example.gecktrack.ui.calendar.EventModel;
import com.example.gecktrack.ui.mygecks.GeckoModel;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    // edit preexisting event in database
    public void editEvent(EventModel event)
    {
        // access database and write query
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // create columns with updated data
        cv.put("EventName", event.getName());
        cv.put("Type", event.getType());
        cv.put("Date", event.getDate());
        cv.put("Time", event.getTime());
        cv.put("Notifications", event.getNotifications());
        cv.put("Notes", event.getNotes());
        cv.put("Geckos", event.getGeckos());

        // update the correct gecko with the new values
        db.update("EVENT", cv, "GeckoID = " + event.getID(), null);
        db.close();
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

    // get the next three upcoming events for Home Page
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<EventDayMap> getTop3Events()
    {
        // initial list of all existing events, past and upcoming
        List<EventModel> allEvents = getTotalEventList();
        // list of narrowed down, upcoming events
        List<EventDayMap> tempEvents = new ArrayList<>();
        // final list, returns top 3 upcoming events
        List<EventDayMap> top3Events = new ArrayList<>();

        // look at all events
        for (EventModel event : allEvents)
        {
            // get each part of event's date to turn into LocalDate
            String m = event.getDate().substring(0,2);
            String d = event.getDate().substring(3,5);
            String y = event.getDate().substring(6,10);

            LocalDate today = LocalDate.now();
            LocalDate date = LocalDate.of(Integer.parseInt(y), Month.of(Integer.parseInt(m)),
                                          Integer.parseInt(d));

            // find time in between today and current event in days
            long timeBetween = ChronoUnit.DAYS.between(today, date);
            int days = (int)timeBetween;

            // event hasn't passed yet, it is an upcoming event
            if (timeBetween >= 0)
            {
                tempEvents.add(new EventDayMap(event, days));
            }
        }

        // user has no events, abort
        if (tempEvents.isEmpty())
        {
            return top3Events;
        }

        // sort all events by days until
        Collections.sort(tempEvents, new Comparator<EventDayMap>()
        {
            @Override
            public int compare(EventDayMap e1, EventDayMap e2)
            {
                return Integer.compare(e1.getDaysUntil(), e2.getDaysUntil());
            }
        });

        // at least three dates, get top 3
        if (tempEvents.size() >= 3)
        {
            // get top 3 dates
            for (int i = 0; i < 3; i++)
            {
                top3Events.add(tempEvents.get(i));
            }
        }
        else // temp events is 1 or 2, add them all to top 3
        {
            top3Events.addAll(tempEvents);
        }

        return top3Events;
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

    // get events in care category for selected gecko
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<EventDayMap> getGeckoEventsInCategory(GeckoModel gecko, String category)
    {
        // list holds all events that match event category
        List<EventModel> tempEvents = new ArrayList<>();

        // list holds all events that match gecko ID AND event category
        List<EventModel> matchingEvents = new ArrayList<>();

        // list holds all matching events sorted by date
        List<EventDayMap> sortedEvents = new ArrayList<>();

        String getEventsQuery = "SELECT * FROM EVENT WHERE Type = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        // cursor is result set from query
        Cursor cursor = db.rawQuery(getEventsQuery, new String[] {category});

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
                EventModel event = new EventModel(ID, name, type, date, time, notifications,
                        notes, geckos);
                tempEvents.add(event);

            } while(cursor.moveToNext());
        }
        else
        {
            // failed, list is probably empty
        }

        // close cursor and db
        cursor.close();
        db.close();

        // cursor succeeded, but found no events in the selected category
        if (tempEvents.isEmpty())
        {
            // return empty list
            return sortedEvents;
        }

        // check each matching event for matching gecko
        for (EventModel e1 : tempEvents)
        {
            // use scanner to get each individual ID from event
            Scanner geckosInEvent = new Scanner(e1.getGeckos()).useDelimiter(" ");

            // loop through separated strings
            while (geckosInEvent.hasNext())
            {
                String geckoID = geckosInEvent.next();

                // filter out events that don't contain selected gecko
                if (geckoID.matches(String.valueOf(gecko.getID())))
                {
                    matchingEvents.add(e1);
                }
            }
        }

        // look at all events
        for (EventModel event : matchingEvents)
        {
            // get each part of event's date to turn into LocalDate
            String m = event.getDate().substring(0,2);
            String d = event.getDate().substring(3,5);
            String y = event.getDate().substring(6,10);

            LocalDate today = LocalDate.now();
            LocalDate date = LocalDate.of(Integer.parseInt(y), Month.of(Integer.parseInt(m)),
                    Integer.parseInt(d));

            // find time in between today and current event in days
            long timeBetween = ChronoUnit.DAYS.between(today, date);
            int days = (int)timeBetween;

            sortedEvents.add(new EventDayMap(event, days));
        }

        // sort all events by days until
        Collections.sort(sortedEvents, new Comparator<EventDayMap>()
        {
            @Override
            public int compare(EventDayMap e1, EventDayMap e2)
            {
                return Integer.compare(e1.getDaysUntil(), e2.getDaysUntil());
            }
        });

        System.out.println("\nFound " + sortedEvents.size() + " event(s) after filtering:");
        System.out.println(sortedEvents);

        return sortedEvents;
    }
}
