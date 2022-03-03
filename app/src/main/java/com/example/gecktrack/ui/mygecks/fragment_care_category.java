// Amanda Villarreal
// January 20, 2022
// fragment_care_category.java
// GeckTrack.app
// Gecko's selected care category page (shows the gecko's events under the selected care category)
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.mygecks;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.example.gecktrack.ui.EventDayMap;
import com.example.gecktrack.ui.SharedViewModel;
import com.example.gecktrack.ui.calendar.EventModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


// -------------------------------------------------------------------------------------------------


public class fragment_care_category extends Fragment
{

    // variables for SharedViewModel
    private SharedViewModel viewModel;
    GeckoModel selectedGecko;
    String selectedEventType;

    // Required empty public constructor
    public fragment_care_category() { }


// CREATION METHODS --------------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // initialize view model for shared data
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_care_category, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // Hide the Bottom Navigation View for this page
        BottomNavigationView navigationBar = getActivity().findViewById(R.id.nav_view);
        navigationBar.setVisibility(View.GONE);

        // initialize page features
        initializeViewModel();
        initializeGeckoTitle();
        initializeBackButton();
        // loadEvents();
    }


// VIEW MODEL METHODS ------------------------------------------------------------------------------


    // get selected gecko and care category from previous fragment
    public void initializeViewModel()
    {
        selectedGecko = viewModel.getGecko().getValue();
        selectedEventType = viewModel.getEventType().getValue();
    }


// GECKO DATA METHODS ------------------------------------------------------------------------------


    // set text of page title to match gecko and category
    public void initializeGeckoTitle()
    {
        // initialize and define title
        TextView geckoTitle = getView().findViewById(R.id.TextView_GeckoTitle);

        // set image if gecko has one
        if (!selectedGecko.getPhotoID().matches("No photo"))
        {
            ImageView photo = getView().findViewById(R.id.GeckoImageCareCategory_ImageView);
            //photo.setImageURI(Uri.parse(selectedGecko.getPhotoID()));
        }

        // format title
        if(selectedEventType.contains("Other"))
        {
            geckoTitle.setText(selectedGecko.getName() + "'s Other Events");
        }
        else if(selectedEventType.contains("Health"))
        {
            geckoTitle.setText(selectedGecko.getName() + "'s Health Events");
        }
        else if(selectedEventType.contains("Breeding"))
        {
            geckoTitle.setText(selectedGecko.getName() + "'s Breeding Events");
        }
        else if(selectedEventType.contains("Shedding"))
        {
            geckoTitle.setText(selectedGecko.getName() + "'s Sheds");
        }
        else
        {
            geckoTitle.setText(selectedGecko.getName() + "'s " + selectedEventType + "s");
        }
    }


// EVENT LIST METHODS ------------------------------------------------------------------------------


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadEvents()
    {
        // connect to database, get events for gecko in selected category
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<EventDayMap> eventsForGecko = db.getGeckoEventsInCategory(selectedGecko, selectedEventType);

        // xml views
        LinearLayout eventHolder = getView().findViewById(R.id.CareCategoryDataHolder_LinearLayout);

        // no events in selected category for selected gecko
        if (eventsForGecko.isEmpty())
        {
            TextView noEventsMessage = new TextView(getContext());
            noEventsMessage.setTextColor(getResources().getColor(R.color.data_input_color));
            noEventsMessage.setTextSize(20);
            noEventsMessage.setGravity(Gravity.CENTER);
            noEventsMessage.setPadding(0,100,0,200);
            noEventsMessage.setText("No events!");
            eventHolder.addView(noEventsMessage);
        }
        else
        {
            for (EventDayMap event : eventsForGecko)
            {
                // clickable linear Layout to hold each event
                LinearLayout oneEvent = new LinearLayout(getContext());
                oneEvent.setOrientation(LinearLayout.VERTICAL);
                oneEvent.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        popUP(event);
                    }
                });

                // Text View for event date and time
                TextView eventInfo = new TextView(getContext());
                eventInfo.setTextColor(getResources().getColor(R.color.data_input_color));
                eventInfo.setTextSize(20);
                eventInfo.setGravity(Gravity.CENTER);
                eventInfo.setTypeface(null, Typeface.BOLD); // bold info
                eventInfo.setPadding(0,20,0,20);
                eventInfo.setBackgroundColor(getResources().getColor(R.color.background2_gray));
                String text = event.getEvent().getDate() + " at " + event.getEvent().getTime();
                eventInfo.setText(text);
                oneEvent.addView(eventInfo);

                // event has notes, include them
                if (!event.getEvent().getNotes().matches("None"))
                {
                    // Text View for event date and time
                    TextView notesText = new TextView(getContext());
                    notesText.setTextColor(getResources().getColor(R.color.data_input_color));
                    notesText.setTextSize(20);
                    notesText.setGravity(Gravity.CENTER);
                    notesText.setPadding(0,20,0,20);
                    notesText.setBackgroundColor(getResources().getColor(R.color.background2_gray));
                    notesText.setText(event.getEvent().getNotes());
                    oneEvent.addView(notesText);
                }

                // separator view
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(50, 50);
                TextView separator = new TextView(getContext());
                separator.setTextColor(getResources().getColor(R.color.faded_black));
                separator.setText(" ");
                separator.setLayoutParams(layoutParams1);
                eventHolder.addView(oneEvent);
                eventHolder.addView(separator);
            }
        }
    }


// TOOL BUTTON METHODS -----------------------------------------------------------------------------


    // initialize back button and set listener, go back to selected gecko's page
    public void initializeBackButton()
    {
        Button backButton = getView().findViewById(R.id.BackToSelectedGecko_Button);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Navigation.findNavController(getView()).navigate(R.id.action_fragment_care_category_to_fragment_selected_gecko);
            }
        });
    }


    public void popUP(EventDayMap event)
    {
        // create alert message to be sure to delete gecko
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(event.getEvent().getName());
        alert.setMessage("Time: " + event.getEvent().getTime());


        // Listener for Yes option
        alert.setPositiveButton("Go to Calendar Day.", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // take user to selected day
                //Navigation.findNavController(getView()).navigate(R.id.action_fragment_selected_gecko_to_gecko_page);

                // close the dialog
                dialog.dismiss();
            }
        });

        // Listener for no option
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
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
        alertMessage.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
    }


}