// Amanda Villarreal
// December 29, 2021
// fragment_gecko_data.java
// GeckTrack.app
// Gecko Data Entry Form (for adding and updating gecko data)
// -------------------------------------------------------------------------------------------------

package com.example.gecktrack;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.DateFormat;

// -------------------------------------------------------------------------------------------------

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_gecko_data#newInstance} factory method to
 * create an instance of this fragment.
 */

public class fragment_gecko_data extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener
{

    // widgets from xml code (only this one need be global)
    EditText geckoName;
    EditText geckoBirthday;
    EditText geckoMorph;
    EditText geckoWeight;
    EditText geckoTemperature;
    EditText geckoHumidity;
    EditText geckoSeller;
    Spinner geckoSpeciesOptionSpinner;
    Spinner geckoStatusOptionSpinner;

    // variables to hold GeckoModel Data:
    String name;
    String sex;
    String birthday;
    int age;
    String species;
    String morph;
    double weight = 0.0;
    int temperature = 0;
    int humidity = 0;
    String status;
    String seller;
    String photo;

    /*
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    */

    /*
    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;
     */

    public fragment_gecko_data()
    {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_gecko_data.
     */

    /*
    // Rename and change types and number of parameters
    public static fragment_gecko_data newInstance(String param1, String param2)
    {
        fragment_gecko_data fragment = new fragment_gecko_data();
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
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gecko_data, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // initialize page features
        initializeSexRadioButtons();
        initializeAllEditTexts();
        initializeSpinners();
        initializePhotoButton();
        initializeSaveButton();
        initializeCancelButton();
    }


// EDIT TEXT FIELD METHODS -------------------------------------------------------------------------


    // initialize all EditText fields and set listeners
    public void initializeAllEditTexts()
    {
        // initialize all EditText fields
        geckoName        = getView().findViewById(R.id.TextInput_GeckoName);
        geckoBirthday    = getView().findViewById(R.id.TextInput_GeckoBirthday);
        geckoMorph       = getView().findViewById(R.id.TextInput_GeckoMorph);
        geckoWeight      = getView().findViewById(R.id.TextInput_GeckoWeight);
        geckoTemperature = getView().findViewById(R.id.TextInput_Temperature);
        geckoHumidity    = getView().findViewById(R.id.TextInput_Humidity);
        geckoSeller      = getView().findViewById(R.id.TextInput_PurchasedFrom);

        // set listeners for all EditTexts
        geckoName.setOnEditorActionListener(this);
        geckoBirthday.setOnEditorActionListener(this);
        geckoMorph.setOnEditorActionListener(this);
        geckoWeight.setOnEditorActionListener(this);
        geckoTemperature.setOnEditorActionListener(this);
        geckoHumidity.setOnEditorActionListener(this);
        geckoSeller.setOnEditorActionListener(this);
    }

    // determine which EditText has changed, set new value
    @Override
    public boolean onEditorAction(TextView changedTextView, int actionId, KeyEvent event)
    {
        // determine which EditText field was changed
        switch (changedTextView.getId())
        {
            case R.id.TextInput_GeckoName:     // Name field changed
                name = geckoName.getText().toString();
                break;
            case R.id.TextInput_GeckoBirthday: // birthday field changed
                birthday = geckoBirthday.getText().toString();
                break;
            case R.id.TextInput_GeckoMorph:    // morph field changed
                morph = geckoMorph.getText().toString();
                break;
            case R.id.TextInput_GeckoWeight:   // weight field changed
                weight = Double.parseDouble(geckoWeight.getText().toString());
                break;
            case R.id.TextInput_Temperature:   // temperature field changed
                temperature = Integer.parseInt(geckoTemperature.getText().toString());
                break;
            case R.id.TextInput_Humidity:      // humidity field changed
                humidity = Integer.parseInt(geckoHumidity.getText().toString());
                break;
            case R.id.TextInput_PurchasedFrom: // seller field changed
                seller = geckoSeller.getText().toString();
                break;
        }

        return false;
    }

    // calculate gecko's age based on birthday
    public void calculateAge()
    {
        // logic to calculate age will go here
        // for now:
        age = 5;
    }


// GECKO SPECIES AND STATUS SPINNER METHODS --------------------------------------------------------


    // Sets up the Spinners to display Gecko species and status as options in a drop-down list
    public void initializeSpinners()
    {
        // initialize Species Spinner
        geckoSpeciesOptionSpinner = getView().findViewById(R.id.Spinner_GeckoSpecies);

        // Create an ArrayAdapter using the string array resource and a default spinner layout
        ArrayAdapter<CharSequence> speciesAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.GeckoSpeciesOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        geckoSpeciesOptionSpinner.setAdapter(speciesAdapter);

        // set listener
        geckoSpeciesOptionSpinner.setOnItemSelectedListener(this);



        // initialize Status Spinner
        geckoStatusOptionSpinner = getView().findViewById(R.id.Spinner_GeckoStatusOptions);

        // Create an ArrayAdapter using the string array resource and a default spinner layout
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.GeckoStatusOptions, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        geckoStatusOptionSpinner.setAdapter(statusAdapter);

        // set listener
        geckoStatusOptionSpinner.setOnItemSelectedListener(this);
    }

    // Determine which Spinner was selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View currentSpinner, int position, long id)
    {
        switch (currentSpinner.getId())
        {
            case R.id.Spinner_GeckoSpecies:
                // species = parent.getItemAtPosition(position);
                species = String.valueOf(geckoSpeciesOptionSpinner.getAdapter().getItem(position));
                System.out.println(species); //testing
                break;
            case R.id.Spinner_GeckoStatusOptions:
                status = String.valueOf(geckoStatusOptionSpinner.getAdapter().getItem(position));
                System.out.println(status); // testing
                break;
        }
    }

    // may need to revisit
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }


// SEX RADIO BUTTON METHODS ------------------------------------------------------------------------


    // initialize RadioButton and RadioGroup
    public void initializeSexRadioButtons()
    {
        // initialize RadioGroup and all Radio Buttons
        RadioGroup geckoSex = getView().findViewById(R.id.RadioGroup_SexOptions);
        RadioButton maleSex = getView().findViewById(R.id.RadioButton_Male);
        RadioButton femaleSex = getView().findViewById(R.id.RadioButton_Female);
        RadioButton unknownSex = getView().findViewById(R.id.RadioButton_Unknown);
    }

    // determines which sex was selected
    public void onRadioButtonClicked(View selectedRadioButton)
    {
        // Is a radio button now checked?
        boolean checked = ((RadioButton) selectedRadioButton).isChecked();

        // Check which radio button was clicked
        switch(selectedRadioButton.getId())
        {
            case R.id.RadioButton_Male:    // Male selected
                if (checked)
                    sex = "Male";
                    break;
            case R.id.RadioButton_Female:  // Female selected
                if (checked)
                    sex = "Female";
                    break;
            case R.id.RadioButton_Unknown: // Unknown selected
                if (checked)
                    sex = "Unknown";
        }

        System.out.println(sex); // for testing
    }


// PHOTO BUTTON METHODS ----------------------------------------------------------------------------


    // Set up the add a photo button
    public void initializePhotoButton()
    {
        // initialize photo button
        Button geckoPhoto = getView().findViewById(R.id.Button_Photo);

        // set onClickListener
        geckoPhoto.setOnClickListener(this);

    }

    // define actions for when photo button is clicked
    @Override
    public void onClick(View photoButton)
    {
        // initialize photo description label
        TextView photoDescription = getView().findViewById(R.id.TextView_Photo);

        // temporary code
        photoDescription.setText("Photo Button Works!!");
        photo = "Photo was hypothetically added";
        System.out.println(photo);
    }


// BUTTON SECTION METHODS (SAVE AND CANCEL) --------------------------------------------------------


    // initialize save button and set listener, save data to GeckoModel
    public void initializeSaveButton()
    {
        // initialize save button
        Button saveButton = getView().findViewById(R.id.Button_Save);

        // set listener
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                calculateAge();

                int tempID = 1;

                // Optional data fields (can be empty)

                if (seller.isEmpty())
                {
                    seller = "Unknown";
                }

                if (photo.isEmpty())
                {
                    photo = "No Photo";
                }

                if (birthday.isEmpty())
                {
                    birthday = "Unknown";
                }

                if (morph.isEmpty())
                {
                    morph = "Unknown";
                }

                // Required data fields (must be filled)

                if (name.isEmpty() || species.isEmpty() || sex.isEmpty() || weight == 0||
                    temperature == 0 || humidity == 0)
                {
                    System.out.println("User must fill these out");
                }
                else
                {
                    // create new gecko and print info
                    GeckoModel gecko = new GeckoModel(tempID, name, species, sex, birthday, age,
                            weight, morph, temperature, humidity, seller, status, photo);
                    gecko.toString(); // for testing
                }
            }
        });
    }

    // initialize cancel button and set listener
    public void initializeCancelButton()
    {
        // initialize cancel button
        Button cancelButton = getView().findViewById(R.id.Button_Cancel);

        // set listener
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Code here executes on main thread after user presses button
            }
        });
    }


}