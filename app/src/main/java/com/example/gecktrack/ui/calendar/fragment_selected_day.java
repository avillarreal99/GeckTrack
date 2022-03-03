// Amanda Villarreal
// January 29, 2022
// fragment_selected_day.java
// GeckTrack.app
// page that shows events from a selected calendar day
// ------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.calendar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.gecktrack.DatabaseHelper;
import com.example.gecktrack.R;
import com.example.gecktrack.ui.SharedViewModel;
import com.example.gecktrack.ui.mygecks.GeckoModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.DateFormatSymbols;
import java.util.List;


// ------------------------------------------------------------------------------------------------


public class fragment_selected_day extends Fragment
{
    // layout
    LinearLayout eventHolder;

    // variables for SharedViewModel
    private SharedViewModel viewModel;
    String eventDate = "";

    // Required empty public constructor
    public fragment_selected_day() {}


// CREATION METHODS --------------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected_day, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // Hide the Bottom Navigation View for this page
        BottomNavigationView navigationBar = getActivity().findViewById(R.id.nav_view);
        navigationBar.setVisibility(View.GONE);

        // initialize layout
        eventHolder = getView().findViewById(R.id.EventHolder_LinearLayout);

        // initialize page features
        initializeDate();
        initializeBackButton();
        initializeAddButton();
        initializeEvents();
    }


// DATE FORMATTING METHODS -------------------------------------------------------------------------


    public void initializeDate()
    {
        // get date value from calendar page
        eventDate = viewModel.getDate().getValue();

        // set date header to selected date
        TextView dateHeader = getView().findViewById(R.id.TextView_DateHeader);
        dateHeader.setText(formatDate());
    }

    // format date into "Month Day, Year"
    public String formatDate()
    {
        // set up new date format
        String month = eventDate.substring(0, 2);
        String day = "";
        String year  = eventDate.substring(6, 10);
        int dayInt  = Integer.parseInt(eventDate.substring(3, 5));

        if (dayInt < 10)
        {
            day = eventDate.substring(4, 5);
        }
        else // day >=10
        {
            day = eventDate.substring(3, 5);
        }

        String monStr = new DateFormatSymbols().getMonths()[Integer.parseInt(month) - 1].toString();

        // return formatted date
        return monStr + " " + day + ", " + year;
    }


// EVENT LIST METHODS ------------------------------------------------------------------------------


    // generate the list of events for the selected day
    public void initializeEvents()
    {
        // set up database connection, get list of events
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<EventModel> events = db.getSelectedDayEventList(eventDate);

        // user has no inputted events for selected day
        if(events.isEmpty())
        {
            // Create a text view for message, add it to layout
            TextView noEventsMessage1 = new TextView(getContext());
            noEventsMessage1.setTextColor(getResources().getColor(R.color.data_input_color));
            noEventsMessage1.setTextSize(24);
            noEventsMessage1.setGravity(Gravity.CENTER);
            noEventsMessage1.setText(R.string.no_events_message1);
            noEventsMessage1.setPadding(20,450,20,50);

            TextView noEventsMessage2 = new TextView(getContext());
            noEventsMessage2.setTextColor(getResources().getColor(R.color.data_input_color));
            noEventsMessage2.setTextSize(20);
            noEventsMessage2.setGravity(Gravity.CENTER);
            noEventsMessage2.setText(R.string.no_events_message2);
            noEventsMessage2.setPadding(100,0,100,0);

            eventHolder.addView(noEventsMessage1);
            eventHolder.addView(noEventsMessage2);
        }
        // user has events for this day, format them to display
        else
         {
            // loop through retrieved events
            for (EventModel event : events)
            {
                // create a horizontal linear layout for each event.
                LinearLayout eventCell = new LinearLayout(getContext());
                eventCell.setOrientation(LinearLayout.HORIZONTAL);
                eventCell.setPadding(0,50, 0, 0);

                // create colored event dots (revisit to match event color)
                ImageView eventType = new ImageView(getContext());
                eventType.setBackgroundResource(R.drawable.ic_event_type_dot_foreground);
                eventType.setBackgroundColor(getResources().getColor(R.color.white));
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(50, 50);
                eventType.setLayoutParams(layoutParams1);
                eventType.setPadding(25,0,0,0);

                // create a vertical linear layout for event text.
                LinearLayout eventInfoCell = new LinearLayout(getContext());
                eventInfoCell.setOrientation(LinearLayout.VERTICAL);

                // Create a text view for each event name
                TextView eventName = new TextView(getContext());
                eventName.setTextColor(getResources().getColor(R.color.white));
                eventName.setTextSize(20);
                eventName.setTypeface(null, Typeface.BOLD); // bold name
                eventName.setText("    " + event.getName());

                // Create a text view for each event info
                TextView eventInfo = new TextView(getContext());
                eventInfo.setTextColor(getResources().getColor(R.color.data_input_color));
                eventInfo.setTextSize(18);
                String format  = event.getTime() + "\n" +
                                 "Type:  " + event.getType() + "\n" +
                                 "Notifications:  " + event.getNotifications() + "\n" +
                                 "For:  " + db.getAssociatedGeckos(event.getGeckos()) + "\n" +
                                 "Notes:  " + event.getNotes() + "\n\n";
                eventInfo.setText(format);
                eventInfo.setPadding(60,0,0,0);
                eventInfoCell.addView(eventName);
                eventInfoCell.addView(eventInfo);

                // create a horizontal linear layout for event buttons
                LinearLayout eventButtons = new LinearLayout(getContext());
                eventCell.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(215, 75);

                // separator before buttons
                TextView separator1 = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(60, 75);
                separator1.setLayoutParams(layoutParams3);

                // delete button for each event
                Button delete = new Button(getContext());
                delete.setText(R.string.delete);
                delete.setTextSize(12);
                delete.setBackgroundColor(getResources().getColor(R.color.delete_color));
                delete.setPadding(0,0, 0, 0);
                delete.setLayoutParams(layoutParams2);
                delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        promptDelete(event);
                    }
                });

                // separator between buttons
                TextView separator2 = new TextView(getContext());
                separator2.setLayoutParams(layoutParams3);

                // edit button for each event
                Button edit = new Button(getContext());
                edit.setText(R.string.edit);
                edit.setTextSize(12);
                edit.setBackgroundColor(getResources().getColor(R.color.pastel_green));
                edit.setPadding(0, 0,0, 0);
                edit.setLayoutParams(layoutParams2);
                edit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        viewModel.setEvent(event);
                        Navigation.findNavController(getView()).navigate(R.id.action_fragment_selected_day_to_fragment_event_data_form);
                    }
                });

                // separator between events
                TextView separatorEvents = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(0, 75);
                //separatorEvents.setLayoutParams(layoutParams4);

                // add buttons to their layout
                eventButtons.addView(separator1);
                eventButtons.addView(delete);
                eventButtons.addView(separator2);
                eventButtons.addView(edit);
                eventInfoCell.addView(eventButtons);
                eventInfoCell.addView(separatorEvents);

                // add all views to event cell in order to complete formatting
                eventCell.addView(eventType);
                eventCell.addView(eventInfoCell);

                // add event view to the linear scrollview
                eventHolder.addView(eventCell);
            }

        }
    }

    // ensure if the user wants to delete event, actually deletes event if yes
    public void promptDelete(EventModel event)
    {
        // create alert message to be sure to delete event
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Delete " + event.getName() + "?");
        alert.setMessage("Are you sure you want to permanently delete this event?");

        // Listener for Yes option
        alert.setPositiveButton("Yes, Delete.", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                DatabaseHelper db = new DatabaseHelper(getContext());
                // delete event
                db.deleteEvent(event);
                eventHolder.removeAllViews();
                initializeEvents();

                // close the dialog
                dialog.dismiss();
            }
        });

        // Listener for no option
        alert.setNegativeButton("No!", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Do nothing
                dialog.dismiss();
            }
        });

        // configure alert dialog
        AlertDialog alertMessage = alert.create();
        alertMessage.show();
        alertMessage.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertMessage.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }


// TOOL BUTTON METHODS -----------------------------------------------------------------------------


    // initialize and set listener for add button, create a new event for selected date
    public void initializeAddButton()
    {
        Button addButton = getView().findViewById(R.id.Button_AddEvent);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseHelper db = new DatabaseHelper(getContext());
                List<GeckoModel> geckos = db.getGeckoList();
                viewModel.setEvent(null);

                // user has no created geckos, cannot add events
                if(geckos.isEmpty())
                {
                    noGeckosPrompt();
                }
                // else, user has geckos, can create events
                else
                {
                    // open event data form
                    Navigation.findNavController(getView()).navigate(R.id.action_fragment_selected_day_to_fragment_event_data_form);
                }
            }
        });
    }

    // initialize and set listener for back button, go back to calendar page
    public void initializeBackButton()
    {
        Button backButton = getView().findViewById(R.id.Button_BackToCalendar);

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // open selected day page
                Navigation.findNavController(getView()).navigate(R.id.action_fragment_selected_day_to_schedule_page);
            }
        });
    }

    // message for when user has no geckos, cannot create events
    public void noGeckosPrompt()
    {
        // create alert message if no geckos have been created
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("You can't make events yet!");
        alert.setMessage("It looks like you haven't added any geckos. In GeckTrack, events are " +
                "created for your geckos, so you must have at least one gecko to make " +
                "events. Go to the My Gecks page and create a new gecko!");

        // Listener for Yes option
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // close the dialog
                dialog.dismiss();
            }
        });

        // configure alert dialog
        AlertDialog alertMessage = alert.create();
        alertMessage.show();
        alertMessage.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
    }
}