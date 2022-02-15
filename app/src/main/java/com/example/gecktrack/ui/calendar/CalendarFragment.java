// Amanda Villarreal
// July 20, 2021
// CalendarFragment.java
// GeckTrack.app
// Use this class to program functionality of Calendar Page
// ------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.calendar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.gecktrack.DatabaseHelper;
import com.example.gecktrack.R;
import com.example.gecktrack.ui.SharedViewModel;
import com.example.gecktrack.ui.mygecks.GeckoModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.List;


// ------------------------------------------------------------------------------------------------

public class CalendarFragment extends Fragment
{

    private CalendarViewModel calendarViewModel;

    // variables for SharedViewModel
    private SharedViewModel viewModel;

    // widgets
    private CalendarView calendar;


// CREATION METHODS --------------------------------------------------------------------------------


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.setDate(null);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // show the Bottom Navigation View for this page
        BottomNavigationView navigationBar = getActivity().findViewById(R.id.nav_view);
        navigationBar.setVisibility(View.VISIBLE);

        // initialize page features
        initializeAddButton();
        initializeCalendar();
    }


// TOOL BUTTON METHODS -----------------------------------------------------------------------------


    public void initializeAddButton()
    {
        // initialize add button and set listener
        Button addButton = getView().findViewById(R.id.Button_AddEvent);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseHelper db = new DatabaseHelper(getContext());
                List<GeckoModel> geckos = db.getGeckoList();

                // user has no created geckos, cannot add events
                if(geckos.isEmpty())
                {
                    noGeckosPrompt();
                }
                // else, user has geckos, can create events
                else
                {
                    // event will always be null from add button
                    viewModel.setEvent(null);

                    // open event data form to add an event
                    Navigation.findNavController(getView()).navigate(R.id.action_schedule_page_to_fragment_event_data_form);
                }

            }
        });
    }

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


// CALENDAR METHODS --------------------------------------------------------------------------------


    // initialize calendar features and set listeners
    public void initializeCalendar()
    {
        //connecting to calendar
        calendar  = getView().findViewById(R.id.calendarView);


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {

            // month starts at 0, increment by 1 when using month
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                // format date properly
                String mon = "";
                String day = "";

                if((month + 1) < 10)
                {
                    mon = "0" + String.valueOf(month + 1);
                }
                else
                {
                    mon =String.valueOf(month + 1);
                }

                if(dayOfMonth < 10)
                {
                    day = "0" + String.valueOf(dayOfMonth);
                }
                else
                {
                    day = String.valueOf(dayOfMonth);
                }

                launchDay(mon + "/" + day + "/" + year);
            }
        });

    }


    // set up shared data for new page
    public void launchDay(String selectedDay)
    {
        // set up shared data
        viewModel.setDate(selectedDay);

        // open selected day page
        Navigation.findNavController(getView()).navigate(R.id.action_schedule_page_to_fragment_selected_day);
    }

}