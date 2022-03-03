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

//import com.applandeo.materialcalendarview.EventDay;
//import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.gecktrack.DatabaseHelper;
import com.example.gecktrack.R;
import com.example.gecktrack.ui.SharedViewModel;
import com.example.gecktrack.ui.mygecks.GeckoModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;


// ------------------------------------------------------------------------------------------------

public class CalendarFragment extends Fragment
{

    private CalendarViewModel calendarViewModel;

    // variables for SharedViewModel
    private SharedViewModel viewModel;

    // widgets
    private MaterialCalendarView calendarView;



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
                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                List<GeckoModel> geckos = dbHelper.getGeckoList();

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
        calendarView = getView().findViewById(R.id.calendarView);
        calendarView.setDateSelected(CalendarDay.today(), true);
        decorateCalendar();

        //listening for clicks on calendar days
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String currentDate, day, month;

                day = String.valueOf(date.getDay());
                month = String.valueOf(date.getMonth());


                //if month number is 1 digit
                if(date.getMonth() < 10){
                    month = "0" + month;
                }

                //if day number is 1 digit
                if(date.getDay() < 10){
                    day = "0" + day;
                }

                currentDate = month + "/" + day + "/" + String.valueOf(date.getYear());
                launchDay(currentDate);
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

    public void decorateCalendar(){

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        calendarView.setLeftArrow(R.drawable.ic_baseline_arrow_back_24);
        calendarView.setRightArrow(R.drawable.ic_baseline_arrow_forward_24);

        //if there are events
        if(!dbHelper.getAllEventDates().isEmpty()) {

            EventDecorator strOfEventDates = new EventDecorator (getResources().getColor(R.color.event_dot), dbHelper.getAllEventDates());

            calendarView.addDecorator(strOfEventDates);
            //calendarView.invalidateDecorators();

        }
    }

}