// Amanda Villarreal
// December 29, 2021
// fragment_gecko_data.java
// GeckTrack.app
// Gecko Data Entry Form (for adding and updating gecko data)
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.dashboard;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gecktrack.DatabaseHelper;
import com.example.gecktrack.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static android.app.Activity.RESULT_OK;


// -------------------------------------------------------------------------------------------------


/*
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_gecko_data#newInstance} factory method to
 * create an instance of this fragment.
 */

public class fragment_gecko_data extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener
{

    // User input widgets from xml code
    EditText geckoName;
    EditText geckoBirthday;
    EditText geckoSpecies;
    EditText geckoMorph;
    EditText geckoWeight;
    EditText geckoTemperature;
    EditText geckoHumidity;
    EditText geckoSeller;
    Spinner  geckoStatusOptionSpinner;

    // Text label widgets from xml code
    TextView nameLabel;
    TextView birthdayLabel;
    TextView speciesLabel;
    TextView weightLabel;
    TextView temperatureLabel;
    TextView humidityLabel;
    TextView statusLabel;
    TextView photoDescription;


    // variables to hold GeckoModel Data:
    String name;
    String sex = "Unknown";
    String birthday = "Unknown";
    int age = -1;
    String species = "Unknown";
    String morph = "Unknown";
    double weight = 0.0;
    int temperature = 0;
    int humidity = 0;
    String status = "No value selected";
    String seller = "Unknown";
    String photo = "No photo";
    Uri imageUri;

    // for data validation
    boolean validBirthday = true;
    boolean validName = false;
    boolean validWeight = false;
    boolean validTemperature = false;
    boolean validHumidity = false;
    boolean validStatus = false;
    boolean validSpecies = false;

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
        // Hide the Bottom Navigation View for this page
        BottomNavigationView navigationBar = getActivity().findViewById(R.id.nav_view);
        navigationBar.setVisibility(View.GONE);

        // initialize page features
        initializeTextLabels();
        initializeSexRadioButtons();
        initializeAllEditTexts();
        initializeSpinners();
        initializePhotoButton();
        initializeSaveButton();
        initializeCancelButton();
    }


// EDIT TEXT FIELD METHODS -------------------------------------------------------------------------


    // Initialize all text labels for data validation
    public void initializeTextLabels()
    {
        // initialize all text labels
        nameLabel        = getView().findViewById(R.id.TextView_Name);
        birthdayLabel    = getView().findViewById(R.id.TextView_Birthday);
        speciesLabel     = getView().findViewById(R.id.TextView_Species);
        weightLabel      = getView().findViewById(R.id.TextView_Weight);
        temperatureLabel = getView().findViewById(R.id.TextView_Temperature);
        humidityLabel    = getView().findViewById(R.id.TextView_Humidity);
        statusLabel      = getView().findViewById(R.id.TextView_GeckoStatus);
    }

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
        geckoSpecies     = getView().findViewById(R.id.TextInput_GeckoSpecies);

        // set listeners for all EditTexts
        geckoName.setOnEditorActionListener(this);
        geckoBirthday.setOnEditorActionListener(this);
        geckoSpecies.setOnEditorActionListener(this);
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
                validateName(geckoName.getText().toString());
                System.out.println("Name inputted as: " + name );
                break;
            case R.id.TextInput_GeckoBirthday: // birthday field changed
                formatBirthday(geckoBirthday.getText().toString());
                System.out.println("Birthday inputted as: " + birthday );
                break;
            case R.id.TextInput_GeckoSpecies: // species field changed
                validateSpecies(geckoSpecies.getText().toString());
                System.out.println("Species inputted as: " + species );
                break;
            case R.id.TextInput_GeckoMorph:    // morph field changed
                validateMorph(geckoMorph.getText().toString());
                System.out.println("Morph inputted as: " + morph );
                break;
            case R.id.TextInput_GeckoWeight:   // weight field changed
                validateWeight(geckoWeight.getText().toString());
                System.out.println("Weight inputted as: " + weight + "g" );
                break;
            case R.id.TextInput_Temperature:   // temperature field changed
                validateTemperature(geckoTemperature.getText().toString());
                System.out.println("Temperature inputted as: " + temperature + "°F");
                break;
            case R.id.TextInput_Humidity:      // humidity field changed
                validateHumidity(geckoHumidity.getText().toString());
                System.out.println("Humidity inputted as: " + humidity + "%" );
                break;
            case R.id.TextInput_PurchasedFrom: // seller field changed
                validateSeller(geckoSeller.getText().toString());
                System.out.println("Seller inputted as: " + seller );
                break;
        }

        hideKeyboard();
        return false;
    }

    // ensure a name is inputted
    public void validateName(String inputtedName)
    {
        // set default color of label
        nameLabel.setTextColor(getResources().getColor(R.color.data_input_color));

        // user left name blank
        if(inputtedName.isEmpty())
        {
            validName = false;
            nameLabel.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Name is required!", Toast.LENGTH_SHORT).show();
        }
        // user inputted a name
        else
        {
            // capitalize name just in case, set name
            name = toTitleCase(inputtedName);
            geckoName.setText(name);
            validName = true;
        }
    }

    // ensure a name is inputted
    public void validateSpecies(String inputtedSpecies)
    {
        // set default color of label
        speciesLabel.setTextColor(getResources().getColor(R.color.data_input_color));

        // user left species blank
        if(inputtedSpecies.isEmpty())
        {
            validSpecies = false;
            speciesLabel.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Species is required!", Toast.LENGTH_SHORT).show();
        }
        // user inputted a species
        else
        {
            // uppercase instance of Gecko
            Pattern keyword = Pattern.compile(" gecko");
            Matcher u = keyword.matcher(inputtedSpecies.toLowerCase());

            // new species string must be all lowercase for comparison
            StringBuilder tempSpecies = new StringBuilder(inputtedSpecies.toLowerCase());

            // if the keyword "gecko" was not included in the string, add it
            if (!u.find())
            {
                tempSpecies = tempSpecies.append(" gecko");
            }

            // set each word in species to be capitalized
            species = toTitleCase(tempSpecies.toString());
            geckoSpecies.setText(species);
            validSpecies = true;
        }
    }

    // validate morph data (Morph is optional)
    public void validateMorph(String inputtedMorph)
    {
        // user left morph blank
        if(inputtedMorph.isEmpty())
        {
            morph = "Unknown";
            geckoMorph.setText("Unknown");
        }
        // user inputted a morph
        else
        {
            // capitalize morph and set new text
            morph = toTitleCase(inputtedMorph);
            geckoMorph.setText(morph);
        }
    }

    // ensure weight is not 0
    public void validateWeight(String inputtedWeight)
    {
        // set default color of label
        weightLabel.setTextColor(getResources().getColor(R.color.data_input_color));

        // user left weight blank
        if(inputtedWeight.isEmpty())
        {
            validWeight = false;
            weightLabel.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Weight is required!", Toast.LENGTH_SHORT).show();
        }
        // user inputted a weight
        else
        {
            weight = Double.parseDouble(inputtedWeight);
            validWeight = true;
        }
    }

    // ensure temperature is not 0
    public void validateTemperature(String inputtedTemp)
    {
        // set default color of label
        temperatureLabel.setTextColor(getResources().getColor(R.color.data_input_color));

        // user left temperature blank
        if(inputtedTemp.isEmpty())
        {
            validTemperature = false;
            temperatureLabel.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Temperature is required!", Toast.LENGTH_SHORT).show();
        }
        // user inputted a temperature
        else
        {
            temperature = Integer.parseInt(inputtedTemp);
            validTemperature = true;
        }
    }

    // ensure humidity is not 0
    public void validateHumidity(String inputtedHumidity)
    {
        // set default color of label
        humidityLabel.setTextColor(getResources().getColor(R.color.data_input_color));

        // user left humidity blank or goes over 100
        if(inputtedHumidity.isEmpty() || Integer.parseInt(inputtedHumidity) > 100)
        {
            validHumidity = false;
            humidityLabel.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Humidity must be between 0-100",
                    Toast.LENGTH_LONG).show();
        }
        // user inputted a humidity
        else
        {
            humidity = Integer.parseInt(inputtedHumidity);
            validHumidity = true;
        }
    }

    // validate seller data (Seller is optional)
    public void validateSeller(String inputtedSeller)
    {
        // user left seller blank
        if(inputtedSeller.isEmpty())
        {
            seller = "Unknown";
            geckoSeller.setText("Unknown");
        }
        // user inputted a seller
        else
        {
            seller = inputtedSeller;
        }
    }

    // format the user inputted birth date
    public void formatBirthday(String userBirthday)
    {
        // create the correct birth date format
        String format = "(\\d{2})(/)(\\d{2})(/)(\\d{4})";

        // set up today's date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        System.out.println("Today's date = " + dateFormat.format(cal.getTime())); // testing

        // set default color of label
        birthdayLabel.setTextColor(getResources().getColor(R.color.data_input_color));

        // user did not input birthday
        if(userBirthday.isEmpty() || userBirthday == null)
        {
            birthday = "Unknown";
            validBirthday = true;
            geckoBirthday.setText("Unknown");
        }
        // user inputted correctly formatted birthday
        else if (userBirthday.matches(format))
        {
            // is the birthday greater than today's date?
            try
            {
                // compare dates
                Date today = dateFormat.parse(dateFormat.format(cal.getTime()));
                Date birth = dateFormat.parse(userBirthday);

                // birthday happened before today or today
                if (birth.before(today) || birth.equals(today))
                {
                    System.out.println("date is acceptable!");
                    birthday = userBirthday;
                    validBirthday = true;
                }
                // birthday exceeds today
                else
                {
                    validBirthday = false;
                    birthdayLabel.setTextColor(Color.RED);
                    Toast.makeText(getContext(), "Birthday cannot exceed today!",
                            Toast.LENGTH_SHORT).show();
                }
            }
            // date does not exist
            catch(ParseException ex)
            {
                ex.printStackTrace();
                validBirthday = false;
                birthdayLabel.setTextColor(Color.RED);
                Toast.makeText(getContext(), "Entered birthday does not exist!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        // user tried to input birthday, but wrong format
        else
        {
            validBirthday = false;
            birthdayLabel.setTextColor(Color.RED);
            Toast.makeText(getContext(), "Birthday must be in mm/dd/yyyy format!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // calculate gecko's age based on birthday
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calculateAge()
    {
        // separate each part of birthday
        String birthMonth = birthday.substring(0,2);
        String birthDay = birthday.substring(3,5);
        String birthYear = birthday.substring(6,10);

        // set birthday and today's date
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(Integer.parseInt(birthYear),
                                          Month.of(Integer.parseInt(birthMonth)),
                                          Integer.parseInt(birthDay));

        // find age
        Period timeBetween = Period.between(birthday, today);

        // set age
        age = timeBetween.getYears();
    }


// GECKO STATUS SPINNER METHODS --------------------------------------------------------------------


    // Sets up the Spinner to display status as options in a drop-down list
    public void initializeSpinners()
    {
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
        // Set selected Spinner element to gray (needs to match other data input colors)
        ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
        statusLabel.setTextColor(getResources().getColor(R.color.data_input_color));;

        status = String.valueOf(geckoStatusOptionSpinner.getAdapter().getItem(position));
        validStatus = true;
        System.out.println("Status selected as: " + status ); // testing
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
        RadioButton maleSex = getView().findViewById(R.id.RadioButton_Male);
        RadioButton femaleSex = getView().findViewById(R.id.RadioButton_Female);
        RadioButton unknownSex = getView().findViewById(R.id.RadioButton_Unknown);

        // listener for male
        maleSex.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View maleSexOption)
            {
                sex = "Male";
                System.out.println("Sex selected as: " + sex );
            }
        });

        // listener for female
        femaleSex.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View femaleSexOption)
            {
                sex = "Female";
                System.out.println("Sex selected as: " + sex );
            }
        });

        // listener for unknown
        unknownSex.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View unknownSexOption)
            {
                sex = "Unknown";
                System.out.println("Sex selected as: " + sex );
            }
        });
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
        photoDescription = getView().findViewById(R.id.TextView_Photo);

        getPhotoFromGallery();
    }

   // created intent to select a photo from gallery
    public void getPhotoFromGallery()
    {
        // create intent to open the gallery
        Intent openGallery = new Intent();
        openGallery.setType("image/*");
        openGallery.setAction(Intent.ACTION_GET_CONTENT);

        // open the gallery and prompt to select a photo
        startActivityForResult(Intent.createChooser(openGallery, "Select a photo"), 1);
    }

    // after photo is selected, get the image URI for database
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            photo = imageUri.toString();
            System.out.println("Image URI = " + imageUri.toString());
            photoDescription.setText("Photo Selected");
        }


        /*
        try
        {
            Bitmap imageBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        */
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v)
            {
                // Must check the required data fields one more time (in case left empty)
                // data entry form filled out correctly
                if (validBirthday == true && validName == true && validWeight == true &&
                    validTemperature == true && validHumidity == true  && validStatus == true &&
                    validSpecies == true )
                {
                    // only calculate age if a birth date exists
                    if (birthday != "Unknown")
                    {
                        calculateAge();
                    }

                    // create numbers as strings:
                    String formattedWeight = String.valueOf(weight) + " grams";
                    String formattedTemp = String.valueOf(temperature) + "°F";
                    String formattedHumidity = String.valueOf(humidity) + "%";
                    String formattedAge = String.valueOf(age) + " year(s)";

                    // create new gecko and print info
                    GeckoModel gecko = new GeckoModel(-1, name, sex, birthday, formattedAge,
                            species, morph, formattedWeight, formattedTemp, formattedHumidity,
                            status, seller, photo);
                    System.out.println(gecko.toString()); // for testing

                    // add new gecko to database
                    DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                    boolean success = dbHelper.addGecko(gecko);

                    Navigation.findNavController(getView()).navigate(R.id.action_fragment_gecko_data_to_gecko_page);

                }
                // at least one required field is wrong, find the invalid ones
                else
                {
                    if (validName == false)
                    {
                        nameLabel.setTextColor(Color.RED);
                        System.out.println("Name is invalid");
                    }
                    if (validBirthday == false)
                    {
                        birthdayLabel.setTextColor(Color.RED);
                        System.out.println("Birthday is invalid");
                    }
                    if (validSpecies == false)
                    {
                        speciesLabel.setTextColor(Color.RED);
                        System.out.println("Species is invalid");
                    }
                    if (validStatus == false)
                    {
                        statusLabel.setTextColor(Color.RED);
                        System.out.println("Species is invalid");
                    }
                    if (validWeight == false)
                    {
                        weightLabel.setTextColor(Color.RED);
                        System.out.println("Weight is invalid");
                    }
                    if (validTemperature == false)
                    {
                        temperatureLabel.setTextColor(Color.RED);
                        System.out.println("Temperature is invalid");
                    }
                    if (validHumidity == false)
                    {
                        humidityLabel.setTextColor(Color.RED);
                        System.out.println("Humidity is invalid");
                    }
                    if (validBirthday == true)
                    {
                        geckoBirthday.setText("Unknown");
                    }
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
                Navigation.findNavController(getView()).navigate(R.id.action_fragment_gecko_data_to_gecko_page);
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