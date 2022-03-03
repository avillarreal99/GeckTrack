// Amanda Villarreal
// February 20, 2022
// EventDayMap.java
// GeckTrack.app
// Class that bundles an event with its days until occurrence
// -------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui;
import com.example.gecktrack.ui.calendar.EventModel;

public class EventDayMap
{


// PRIVATE INSTANCE VARIABLES ----------------------------------------------------------------------


    private EventModel event;
    private int daysUntil;


// CONSTRUCTOR -------------------------------------------------------------------------------------


    public EventDayMap(EventModel event, int daysUntil)
    {
        this.event = event;
        this.daysUntil = daysUntil;
    }


// GETTERS AND SETTERS -----------------------------------------------------------------------------


    public EventModel getEvent()
    {
        return event;
    }

    public int getDaysUntil()
    {
        return daysUntil;
    }


// TOSTRING ----------------------------------------------------------------------------------------


    @Override
    public String toString()
    {
        return daysUntil + " days until " + event.getName() + "\n";
    }
}
