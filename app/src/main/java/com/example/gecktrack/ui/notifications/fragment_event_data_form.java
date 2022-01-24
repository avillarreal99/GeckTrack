// Amanda Villarreal
// January 11, 2022
// fragment_event_data_form.java
// GeckTrack.app
// Event data entry form (for adding and updating events)
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.notifications;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gecktrack.DatabaseHelper;
import com.example.gecktrack.R;
import com.example.gecktrack.ui.dashboard.GeckoModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// -------------------------------------------------------------------------------------------------


public class fragment_event_data_form extends Fragment implements TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener
{
    // User input widgets from xml code
    EditText eventName;
    Spinner eventTypeSpinner;
    EditText eventDate;
    EditText eventTime;
    RadioButton AM;
    RadioButton PM;
    Switch eventNotifications;
    EditText eventNotes;
    ChipGroup geckoChipGroup;

    // Text label widgets from xml code
    TextView eventNameLabel;
    TextView eventTypeLabel;
    TextView eventDateLabel;
    TextView eventTimeLabel;
    TextView eventNotificationsLabel;
    TextView notificationsStatus;

    // variables to hold EventModel Data: *** CREATE EVENTMODEL.JAVA***
    String name;
    String type = "no value specified";
    String date = "Unknown";
    String time = "00:00";
    String AMPM = "AM";
    String notifications = "OFF";
    String notes = "None";
    ArrayList<GeckoModel> forGeckos;

    // for data validation
    boolean validName = false;
    boolean validType = false;
    boolean validDate = false;
    boolean validTime = false;
    boolean validGeckoList = false;

    // Required empty public constructor
    public fragment_event_data_form() { }


// CREATION METHODS --------------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_data_form, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // Hide the Bottom Navigation View for this page
        BottomNavigationView navigationBar = getActivity().findViewById(R.id.nav_view);
        navigationBar.setVisibility(View.GONE);

        // initialize page features
        initializeTextLabels();
        initializeAllEditTexts();
        initializeSpinners();
        initializeRadioButtons();
        initializeSwitch();
        initializeGeckoChips();
        initializeCancelButton();
        initializeSaveButton();
    }


// EDIT TEXT FIELD METHODS -------------------------------------------------------------------------


    // Initialize all text labels for data validation
    public void initializeTextLabels()
    {
        // initialize all text labels
        eventNameLabel          = getView().findViewById(R.id.TextView_EventName);
        eventTypeLabel          = getView().findViewById(R.id.TextView_EventType);
        eventDateLabel          = getView().findViewById(R.id.TextView_EventDate);
        eventTimeLabel          = getView().findViewById(R.id.TextView_EventTime);
        eventNotificationsLabel = getView().findViewById(R.id.TextView_Notifications);
    }

    // initialize all EditText fields and set listeners
    public void initializeAllEditTexts()
    {
        // initialize all EditText fields
        eventName = getView().findViewById(R.id.TextInput_EventName);
        eventDate = getView().findViewById(R.id.TextInput_EventDate);
        eventTime = getView().findViewById(R.id.TextInput_EventTime);
        eventNotes = getView().findViewById(R.id.TextInput_EventNotes);

        // set listeners
        eventName.setOnEditorActionListener(this);
        eventDate.setOnEditorActionListener(this);
        eventTime.setOnEditorActionListener(this);
        eventNotes.setOnEditorActionListener(this);

    }

    // determine which EditText has changed, set new value
    @Override
    public boolean onEditorAction(TextView changedTextView, int actionId, KeyEvent event)
    {
        switch (changedTextView.getId())
        {
            case R.id.TextInput_EventName: // Name field changed
                validateName(eventName.getText().toString());
                System.out.println("Event name inputted as: " + name );
                break;
            case R.id.TextInput_EventTime: // Time field changed
                validateTime(eventTime.getText().toString());
                System.out.println("Event time inputted as: " + time + AMPM);
                break;
            case R.id.TextInput_EventNotes: // Notes field changed
                validateNotes(eventNotes.getText().toString());
                System.out.println("Event Notes inputted as: " + notes);
                break;
        }

        hideKeyboard();
        return false;
    }

    // ensure a name is inputted
    public void validateName(String inputtedName)
    {
        // set default color of label
        eventNameLabel.setTextColor(getResources().getColor(R.color.data_input_color));

        // user left name blank
        if(inputtedName.isEmpty())
        {
            validName = false;
            eventNameLabel.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Name is required!", Toast.LENGTH_SHORT).show();
        }
        // user inputted a name
        else
        {
            // capitalize name just in case, set name
            name = toTitleCase(inputtedName);
            eventName.setText(name);
            validName = true;
        }
    }

    // validate and format inputted time
    public void validateTime(String inputtedTime)
    {
        // set default color of label
        eventTimeLabel.setTextColor(getResources().getColor(R.color.data_input_color));

        // create the correct time format
        String format1 = "(\\d{2})(:)(\\d{2})"; // hh:mm
        String format2 = "(\\d)(:)(\\d{2})"; // h:mm
        String formattedTime;

        // user did not input time
        if(inputtedTime.isEmpty() || inputtedTime == null)
        {
            eventTimeLabel.setTextColor(Color.RED);
            System.out.println("Must input a time!");
            time = "00:00";
            validTime = false;
            eventTime.setText(time);
        }
        // user inputted a correctly formatted time
        else if(inputtedTime.matches(format2) || inputtedTime.matches(format1))
        {
            // if in format2, alter into format 1
            if (inputtedTime.matches(format2))
            {
                formattedTime = "0" + inputtedTime;
                System.out.println("Time was reformatted into " + formattedTime);
            }

            // time should now match format1
            if(inputtedTime.matches(format1))
            {
                // check if time is valid (exists, military time?)
            }
        }
        // user tried to input time, but wrong format
        else
        {
            eventTimeLabel.setTextColor(Color.RED);
            System.out.println("Time must be in hh:mm format!");
            validTime = false;
        }

        // format the date
        Date dt = new Date(inputtedTime);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String formattedDate = sdf.format(dt);
    }

    // validate inputted notes
    public void validateNotes(String inputtedNotes)
    {
        // user left notes empty
        if(inputtedNotes.isEmpty())
        {
            notes = "None";
            eventNotes.setText(notes);
        }
        // user added notes
        else
        {
            notes = inputtedNotes;
        }
    }


// EVENT TYPE SPINNER METHODS ----------------------------------------------------------------------


    // Sets up the Spinner to display event type as options in a drop-down list
    public void initializeSpinners()
    {
        // initialize Event Types Spinner
        eventTypeSpinner = getView().findViewById(R.id.Spinner_EventTypes);

        // Create an ArrayAdapter using the string array resource and a default spinner layout
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.EventTypeOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        eventTypeSpinner.setAdapter(typeAdapter);

        // set listener
        eventTypeSpinner.setOnItemSelectedListener(this);
    }

    // set type to selected option
    @Override
    public void onItemSelected(AdapterView<?> parent, View currentSpinner, int position, long id)
    {
        // Set selected Spinner element to gray (needs to match other data input colors)
        ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
        eventTypeLabel.setTextColor(getResources().getColor(R.color.data_input_color));;

        type = String.valueOf(eventTypeSpinner.getAdapter().getItem(position));
        validType = true;
        System.out.println("Type selected as: " + type ); // testing
    }

    // may need to revisit
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }


// AM PM RADIO BUTTON METHODS ----------------------------------------------------------------------


    // initialize RadioButton and RadioGroup
    public void initializeRadioButtons()
    {
        // initialize RadioGroup and all Radio Buttons
        AM = getView().findViewById(R.id.RadioButton_AM);
        PM = getView().findViewById(R.id.RadioButton_PM);


        // listener for male
        AM.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View maleSexOption)
            {
                AMPM = "AM";
                System.out.println("Time of day selected as: " + AMPM );
            }
        });

        // listener for female
        PM.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View femaleSexOption)
            {
                AMPM = "PM";
                System.out.println("Time of day selected as: " + AMPM );
            }
        });
    }


// NOTIFICATION SWITCH METHODS ---------------------------------------------------------------------


    public void initializeSwitch()
    {
        // initialize Switch
        eventNotifications = getView().findViewById(R.id.Switch_Notifications);
        notificationsStatus = getView().findViewById(R.id.TextView_Off);

        eventNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
               {
                     public void onCheckedChanged(CompoundButton switchSelection, boolean isChecked)
                    {
                        // isChecked will be true if the switch is in the On position
                        if(switchSelection.isChecked())
                        {
                            notifications = "ON";
                        }
                        else
                        {
                            notifications = "OFF";
                        }

                        notificationsStatus.setText(notifications);
                    }
               });
    }


// CHIP BUTTON METHODS -----------------------------------------------------------------------------


    // dynamically display chips, one for each gecko in user's database
    public void initializeGeckoChips()
    {
        // intialize chip group, geckos will display in here
        geckoChipGroup = getView().findViewById(R.id.ChipGroup_GeckoChips);
        forGeckos = new ArrayList<>();

        // need database connection
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        // get a list of all user's geckos
        List<GeckoModel> geckos = dbHelper.getGeckoList();

        // create a new chip, to select all geckos
        Chip allGeckosChip = new Chip(getContext());
        allGeckosChip.setText(R.string.all_geckos);
        allGeckosChip.setCheckable(true);
        allGeckosChip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        geckoChipGroup.addView(allGeckosChip);


        // for each gecko in List geckos
        for (GeckoModel gecko: geckos)
        {
            // create a new chip, set to gecko's name
            Chip geckoChip = new Chip(getContext());
            geckoChip.setText(gecko.getName());

            geckoChip.setCheckable(true);

            geckoChip.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // determine which chip was selected, add it to ArrayList
                    if(geckoChip.isChecked() && !forGeckos.contains(gecko))
                    {
                        forGeckos.add(gecko);
                    }
                    else if (!geckoChip.isChecked())
                    {
                        forGeckos.remove(gecko);
                    }

                    System.out.println(forGeckos.size());
                }
            });

            // add new chip to chip group
            geckoChipGroup.addView(geckoChip);
        }
    }


// TOOL BUTTON METHODS -----------------------------------------------------------------------------


    // initialize and set on click listener for cancel button, go back to calendar
    public void initializeCancelButton()
    {
        Button cancelButton = getView().findViewById(R.id.button_Cancel);
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // go back to calendar page
                Navigation.findNavController(getView()).navigate(R.id.action_fragment_event_data_form_to_schedule_page);
            }
        });
    }

    // initialize and set on click listener for save button
    public void initializeSaveButton()
    {
        Button saveButton = getView().findViewById(R.id.button_Save);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }


// ASSISTING METHODS -------------------------------------------------------------------------------


    // hides the soft keyboard (Modified by Amanda Villarreal, written by a user on GeeksforGeeks)
    public void hideKeyboard()
    {
        // find the current view where the keyboard needs to close
        View view = this.getView();
        if (view != null)
        {
            // now assign the system service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // capitalize each word of a string (currently gets rid of / and -)
    public static String toTitleCase(String givenSpecies)
    {
        // delimiters include "-" , "/" , and " "
        String[] arr = givenSpecies.split("[-/ ]");
        StringBuffer sb = new StringBuffer();

        // uppercase first character after each delimiter
        for (int i = 0; i < arr.length; i++)
        {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }


}