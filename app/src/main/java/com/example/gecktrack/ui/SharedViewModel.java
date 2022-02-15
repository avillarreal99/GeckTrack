// Amanda Villarreal
// January 18, 2022
// SharedViewModel.java
// GeckTrack.app
// Help fragments share data
// -------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gecktrack.ui.mygecks.GeckoModel;
import com.example.gecktrack.ui.calendar.EventModel;

public class SharedViewModel extends ViewModel
{
    private MutableLiveData<GeckoModel> gecko = new MutableLiveData<>(); // gecko
    private MutableLiveData<String> eventType = new MutableLiveData<>(); // event type
    private MutableLiveData<EventModel> event = new MutableLiveData<>(); // event
    private MutableLiveData<String>     date  = new MutableLiveData<>(); // selected calendar day


// GECKO METHODS -----------------------------------------------------------------------------------


    // set the stored gecko to be the selected gecko
    public void setGecko(GeckoModel selectedGecko)
    {
        gecko.setValue(selectedGecko);
    }

    // send the stored gecko data back
    public LiveData<GeckoModel> getGecko()
    {
        return gecko;
    }


// EVENT TYPE METHODS ------------------------------------------------------------------------------


    // set the stored gecko to be the selected gecko
    public void setEventType(String selectedEventType)
    {
        eventType.setValue(selectedEventType);
    }

    // send the stored gecko data back
    public LiveData<String> getEventType()
    {
        return eventType;
    }



// EVENT METHODS -----------------------------------------------------------------------------------


    // set the stored event to be the selected event
    public void setEvent(EventModel selectedEvent)
    {
        event.setValue(selectedEvent);
    }

    // send the stored event data back
    public LiveData<EventModel> getEvent()
    {
        return event;
    }


// SELECTED DAY METHODS ----------------------------------------------------------------------------


    // set the stored day to be the selected day
    public void setDate(String selectedDay)
    {
        date.setValue(selectedDay);
    }

    // send the stored day data back
    public LiveData<String> getDate()
    {
        return date;
    }

}
