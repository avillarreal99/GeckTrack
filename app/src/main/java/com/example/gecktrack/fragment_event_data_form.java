// Amanda Villarreal
// January 11, 2022
// fragment_event_data_form.java
// GeckTrack.app
// Event data entry form (for adding and updating events)
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


// -------------------------------------------------------------------------------------------------


/*
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_event_data_form#newInstance} factory method to
 * create an instance of this fragment.
 */

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
    // FOR GECKOS SECTION

    // Text label widgets from xml code
    TextView eventNameLabel;
    TextView eventTypeLabel;
    TextView eventDateLabel;
    TextView eventTimeLabel;
    TextView eventNotificationsLabel;

    // variables to hold EventModel Data: *** CREATE EVENTMODEL.JAVA***
    String name;
    String type = "no value specified";
    String date = "Unknown";
    String time = "00:00";
    String AMPM = "AM";
    String notifications = "OFF";
    String notes = "None";
    ArrayList<String> forGeckos;

    // for data validation
    boolean validName = false;
    boolean validType = false;
    boolean validDate = false;
    boolean validTime = false;
    boolean validGeckoList = false;

    /*
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    */

    // Required empty public constructor
    public fragment_event_data_form() { }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_event_data_form.
     */

    /*
    // Rename and change types and number of parameters
    public static fragment_event_data_form newInstance(String param1, String param2) {
        fragment_event_data_form fragment = new fragment_event_data_form();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */


// CREATION METHODS --------------------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
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

                        System.out.println("Notifications are " + notifications);
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