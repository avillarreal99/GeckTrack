// Amanda Villarreal
// January 24, 2022
// EventModel.java
// GeckTrack.app
// Event data entry form (for adding and updating events)
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.calendar;


public class EventModel
{


// PRIVATE INSTANCE VARIABLES ----------------------------------------------------------------------

    private int    ID;
    private String name;
    private String type;
    private String date;
    private String time;
    private String notifications;
    private String notes;
    private String geckos;


// CONSTRUCTOR -------------------------------------------------------------------------------------


    public EventModel(int ID, String name, String type, String date, String time, String notifications,
                      String notes, String geckos)
    {
        this.ID = ID;
        this.name = name;
        this.type = type;
        this.date = date;
        this.time = time;
        this.notifications = notifications;
        this.notes = notes;
        this.geckos = geckos;
    }


// GETTERS AND SETTERS -----------------------------------------------------------------------------


    public int getID() { return ID; }

    public void setID(int ID) { this.ID = ID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getNotifications() { return notifications; }

    public void setNotifications(String notifications) { this.notifications = notifications; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    public String getGeckos() { return geckos; }

    public void setGeckos(String geckos) { this.geckos = geckos; }


// ASSISTING METHODS -------------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return "EventModel\n" +
                "{\n" +
                "ID   = " + ID + "\n" +
                "name = " + name + "\n" +
                "type = " + type + "\n" +
                "date = " + date + "\n" +
                "time = " + time + "\n" +
                "notifications = " + notifications + "\n" +
                "notes = " + notes + "\n" +
                "this event is for geckos with IDs: "  + geckos + "\n" +
                "}\n";
    }
}
