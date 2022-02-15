// Amanda Villarreal
// January 11, 2022
// fragment_event_data_form.java
// GeckTrack.app
// Event data entry form (for adding and updating events)
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.calendar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.format.Time;
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
import com.example.gecktrack.ui.SharedViewModel;
import com.example.gecktrack.ui.mygecks.GeckoModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


// -------------------------------------------------------------------------------------------------


public class fragment_event_data_form extends Fragment implements TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener
{
    // viewmodel variables
    SharedViewModel viewModel;
    EventModel editEvent;
    private boolean editing;
    private boolean adding;

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
    TextView eventForGeckosLabel;

    // variables to hold EventModel Data: *** CREATE EVENTMODEL.JAVA***
    String name;
    String type = "no value specified";
    String date = "Unknown";
    String time = "00:00";
    String AMPM = "AM";
    String notifications = "OFF";
    String notes = "None";
    ArrayList<GeckoModel> forGeckos;
    StringBuilder geckoIDs = new StringBuilder();
    String finalGeckoList;

    // for data validation
    boolean validName = false;
    boolean validType = false;
    boolean validDate = false;
    boolean validTime = false;
    boolean validGeckoList = false;
    boolean validNotifications = false;

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
        // initialize view model
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

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

        // check if editing an event or adding
        editingOrAdding();
    }


// EDITING GECKO METHODS ---------------------------------------------------------------------------


    // check if user is editing or adding an event
    public void editingOrAdding()
    {
        // initialize instruction label
        TextView instructions = getView().findViewById(R.id.TextView_EventInstructions1);

        // user is adding a new event
        if (viewModel.getEvent().getValue() == null)
        {
            // user came from a selected day
            if(viewModel.getDate() != null)
            {
                date = viewModel.getDate().getValue();
                eventDate.setText(date);
                validDate = true;
            }

            adding = true;
            editing = false;
            instructions.setText(R.string.insert_event_information);
        }
        else // user is editing existing event
        {
            adding = false;
            editing = true;
            // editGecko = viewModel.getGecko().getValue();
            //String instructionsString = "Edit " + editGecko.getName() + "'s information!";
            //instructions.setText(instructionsString);
            //preloadGeckoData();
            System.out.println("Editing a preexisting event!");
        }
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
        eventForGeckosLabel     = getView().findViewById(R.id.TextView_ForGecko);
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
                break;
            case R.id.TextInput_EventTime: // Time field changed
                validateTime(eventTime.getText().toString());
                break;
            case R.id.TextInput_EventNotes: // Notes field changed
                validateNotes(eventNotes.getText().toString());
                break;
            case R.id.TextInput_EventDate: // Date field changed
                validateDate(eventDate.getText().toString());
                break;
        }

        hideKeyboard();
        return false;
    }

    // format the user inputted birth date
    public void validateDate(String inputtedDate)
    {
        // create the correct  date format
        String format = "(\\d{2})(/)(\\d{2})(/)(\\d{4})";

        // set up today's date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);

        // set default color of label
        eventDateLabel.setTextColor(getResources().getColor(R.color.data_input_color));

        // user did not input date
        if(inputtedDate.isEmpty() || inputtedDate == null)
        {
            date = "Unknown";
            validDate = false;
            eventDateLabel.setTextColor(Color.RED);
            //eventDate.setText(R.string.mm_dd_yyyy);
            Toast.makeText(getContext(), "You must input a date!",
                    Toast.LENGTH_SHORT).show();
        }
        // user inputted correctly formatted date
        else if (inputtedDate.matches(format))
        {
            // is the birthday greater than today's date?
            try
            {
                // compare dates
                Date today = dateFormat.parse(dateFormat.format(cal.getTime()));
                Date userDate = dateFormat.parse(inputtedDate);

                // date happened before today
                // cannot have notifications for a past event
                if (userDate.before(today))
                {
                    date = inputtedDate;
                    validDate = true;
                    eventNotifications.setClickable(false);
                    eventNotifications.setChecked(false);
                    notifications = "OFF";
                    notificationsStatus.setText(R.string.disabled);
                }
                // date exceeds today
                // notifications are allowed
                else
                {
                    date = inputtedDate;
                    validDate = true;
                    eventDateLabel.setTextColor(getResources().getColor(R.color.data_input_color));
                    eventNotifications.setClickable(true);
                    eventNotifications.setChecked(false);
                    notificationsStatus.setText(R.string.off);
                }
            }
            // date does not exist
            catch(ParseException ex)
            {
                ex.printStackTrace();
                validDate = false;
                eventDateLabel.setTextColor(Color.RED);
                Toast.makeText(getContext(), "Entered date does not exist!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        // user tried to input date, but wrong format
        else
        {
            validDate = false;
            eventDateLabel.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Date must be in mm/dd/yyyy format!",
                    Toast.LENGTH_SHORT).show();
        }
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
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm");
        String formattedTime = "";

        // user did not input time
        if(inputtedTime.isEmpty() || inputtedTime == null)
        {
            eventTimeLabel.setTextColor(Color.RED);
            Toast.makeText(getContext(), "You must input a time!",
                    Toast.LENGTH_LONG).show();
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
                eventTime.setText(formattedTime);
            }
            else
            {
                formattedTime = inputtedTime;
            }

            int minutes = Integer.parseInt(formattedTime.substring( 3, 5));
            int hours   = Integer.parseInt(formattedTime.substring( 0, 2));
            System.out.println("hours = " + hours);
            System.out.println("minutes = " + minutes);

            if ((minutes < 59) && (hours >= 1 && hours <= 12))
            {
                time = formattedTime;
                validTime = true;
                eventTime.setText(time);
            }
            else
            {
                eventTimeLabel.setTextColor(Color.RED);
                validTime = false;
                Toast.makeText(getContext(), "Inputted time does not exist",
                        Toast.LENGTH_LONG).show();
                time = "0:00";
            }
        }
        // user tried to input time, but wrong format
        else
        {
            eventTimeLabel.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Time must be in hh:mm format!",
                    Toast.LENGTH_LONG).show();
            validTime = false;
            time = "00:00";
        }
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
        // initialize chip group, geckos will display in here
        geckoChipGroup = getView().findViewById(R.id.ChipGroup_GeckoChips);
        forGeckos = new ArrayList<>();

        // need database connection
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        // get a list of all user's geckos
        List<GeckoModel> geckos = dbHelper.getGeckoList();

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
                    // set default color of label
                    eventForGeckosLabel.setTextColor(getResources().getColor(R.color.data_input_color));

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
                    if(forGeckos.size() == 0)
                    {
                        Toast.makeText(getContext(), "You must select which gecko(s) this event is for!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

            // add new chip to chip group
            geckoChipGroup.addView(geckoChip);
        }
    }

    // turn arrayList of selected geckos into a string of ID's
    public void finalizeGeckos()
    {
        // no geckos were selected from chips, error
        if(forGeckos.isEmpty())
        {
            validGeckoList = false;
            eventForGeckosLabel.setTextColor(getResources().getColor(R.color.error_on_data_color));
        }
        // geckos were selected, format the selection
        else
        {
            validGeckoList = true;
            eventForGeckosLabel.setTextColor(getResources().getColor(R.color.data_input_color));

            // "clear" the StringBuilder
            geckoIDs.setLength(0);

            // add each ID to the String
            for (GeckoModel gecko : forGeckos) {
                geckoIDs.append(gecko.getID() + " ");
            }

            // trim excess white space
            geckoIDs.trimToSize();
            finalGeckoList = geckoIDs.toString();
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
                // if came from selected day, return to selected day
                if (viewModel.getDate().getValue() != null)
                {
                    // go back to selected day
                    Navigation.findNavController(getView()).navigate(R.id.action_fragment_event_data_form_to_fragment_selected_day);
                }
                // go back to calendar page
                else
                {
                    Navigation.findNavController(getView()).navigate(R.id.action_fragment_event_data_form_to_schedule_page);
                }
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

                finalizeGeckos();

                // format final time
                String formattedTime = time + " " + AMPM;

                // Must check the required data fields one more time (in case left empty)
                // data entry form filled out correctly
                if (validName && validType && validDate && validTime &&  validGeckoList)
                {
                    if (editing)
                    {

                    }
                    else if (adding)
                    {
                        // create new event
                        EventModel newEvent = new EventModel(-1, name, type, date, formattedTime,
                                                             notifications, notes, finalGeckoList);

                        // add new gecko to database
                        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                        boolean success = dbHelper.addEvent(newEvent);

                        // if came from selected day, return to selected day
                        if (viewModel.getDate().getValue() != null)
                        {
                            // go back to selected day
                            Navigation.findNavController(getView()).navigate(R.id.action_fragment_event_data_form_to_fragment_selected_day);
                        }
                        // go back to calendar page
                        else
                        {
                            Navigation.findNavController(getView()).navigate(R.id.action_fragment_event_data_form_to_schedule_page);
                        }
                    }
                }
                // at least one required field is wrong, find the invalid ones
                else
                {
                    Toast.makeText(getContext(), "Please correct the red data fields!",
                            Toast.LENGTH_SHORT).show();

                    if (!validName)
                    {
                        eventNameLabel.setTextColor(Color.RED);
                    }
                    if (!validType)
                    {
                        eventTypeLabel.setTextColor(Color.RED);
                    }
                    if (!validDate)
                    {
                        eventDateLabel.setTextColor(Color.RED);
                    }
                    if (!validTime)
                    {
                        eventTimeLabel.setTextColor(Color.RED);
                    }
                    if (!validGeckoList)
                    {
                        eventForGeckosLabel.setTextColor(Color.RED);
                    }
                }
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