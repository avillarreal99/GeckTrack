// Amanda Villarreal
// December 29, 2021
// fragment_gecko_data.java
// GeckTrack.app
// Gecko Data Entry Form (for adding and updating gecko data)
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.mygecks;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

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
import com.example.gecktrack.ui.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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


public class fragment_gecko_data extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener
{
    // viewmodel variables
    SharedViewModel viewModel;
    GeckoModel editGecko;
    private boolean editing;
    private boolean adding;

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
    RadioButton maleSex;
    RadioButton femaleSex;
    RadioButton unknownSex;
    ArrayAdapter<CharSequence> statusAdapter;

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

    // Required empty public constructor
    public fragment_gecko_data() {}


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

        // check if editing a gecko or adding
        editingOrAdding();
    }


// EDITING GECKO METHODS ---------------------------------------------------------------------------


    // check if user is editing or adding a gecko
    public void editingOrAdding()
    {
        // initialize instruction label
        TextView instructions = getView().findViewById(R.id.TextView_GeckoInstructions1);

        // user is adding a new gecko
        if (viewModel.getGecko().getValue() == null)
        {
            adding = true;
            editing = false;
            instructions.setText(R.string.insert_your_gecko_s_information);
        }
        else // user is editing existing gecko
        {
            adding = false;
            editing = true;
            editGecko = viewModel.getGecko().getValue();
            String instructionsString = "Edit " + editGecko.getName() + "'s information!";
            instructions.setText(instructionsString);
            preloadGeckoData();
        }
    }

    // if editing an existing gecko, preload all data to match gecko
    public void preloadGeckoData()
    {
        // set all data to be gecko's data
        name = editGecko.getName();
        species = editGecko.getGeckoSpecies();
        morph = editGecko.getMorph();
        sex = editGecko.getSex();
        birthday = editGecko.getBirthday();

        String editWeight = editGecko.getWeight();
        String formatWeight = editWeight.substring(0, editWeight.length() - 6);
        weight = Double.parseDouble(formatWeight);

        String editTemp = editGecko.getTemperature();
        String formatTemp = editTemp.substring(0, editTemp.length() - 2);
        temperature = Integer.parseInt(formatTemp);

        String editHum = editGecko.getHumidity();
        String formatHum = editHum.substring(0, editHum.length() - 1);
        humidity = Integer.parseInt(formatHum);

        status = editGecko.getStatus();
        seller = editGecko.getSeller();
        photo = editGecko.getPhotoID();

        // preload all user input fields to gecko data
        geckoName.setText(name);
        geckoBirthday.setText(birthday);
        geckoSpecies.setText(species);
        geckoMorph.setText(morph);
        geckoWeight.setText(String.valueOf(weight));
        geckoTemperature.setText(String.valueOf(temperature));
        geckoHumidity.setText(String.valueOf(humidity));
        geckoSeller.setText(seller);

        // preload photo info
        if(!editGecko.getPhotoID().contains("No photo"))
        {
            photoDescription.setText(R.string.photo_selected);
        }

        // preload sex
        if (editGecko.getSex().contains("Female"))
        {
            femaleSex.setChecked(true);
        }
        else if(editGecko.getSex().contains("Male"))
        {
            maleSex.setChecked(true);
        }
        else
        {
            unknownSex.setChecked(true);
        }

        // preset spinner to correct option
        if (editGecko.getStatus().contains("Pet Only"))
        {
            int spinnerPosition = statusAdapter.getPosition("Pet Only");
            geckoStatusOptionSpinner.setSelection(spinnerPosition);
        }
        else if (editGecko.getStatus().contains("Breeder"))
        {
            int spinnerPosition = statusAdapter.getPosition("Breeder");
            geckoStatusOptionSpinner.setSelection(spinnerPosition);
        }
        else
        {
            int spinnerPosition = statusAdapter.getPosition("Special Needs");
            geckoStatusOptionSpinner.setSelection(spinnerPosition);
        }

        // all required data are automatically valid
        validHumidity = true;
        validTemperature = true;
        validName = true;
        validSpecies = true;
        validWeight = true;
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
                break;
            case R.id.TextInput_GeckoBirthday: // birthday field changed
                formatBirthday(geckoBirthday.getText().toString());
                break;
            case R.id.TextInput_GeckoSpecies: // species field changed
                validateSpecies(geckoSpecies.getText().toString());
                break;
            case R.id.TextInput_GeckoMorph:    // morph field changed
                validateMorph(geckoMorph.getText().toString());
                break;
            case R.id.TextInput_GeckoWeight:   // weight field changed
                validateWeight(geckoWeight.getText().toString());
                break;
            case R.id.TextInput_Temperature:   // temperature field changed
                validateTemperature(geckoTemperature.getText().toString());
                break;
            case R.id.TextInput_Humidity:      // humidity field changed
                validateHumidity(geckoHumidity.getText().toString());
                break;
            case R.id.TextInput_PurchasedFrom: // seller field changed
                validateSeller(geckoSeller.getText().toString());
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
        statusAdapter = ArrayAdapter.createFromResource(getContext(),
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
        maleSex = getView().findViewById(R.id.RadioButton_Male);
        femaleSex = getView().findViewById(R.id.RadioButton_Female);
        unknownSex = getView().findViewById(R.id.RadioButton_Unknown);

        // listener for male
        maleSex.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View maleSexOption)
            {
                sex = "Male";
            }
        });

        // listener for female
        femaleSex.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View femaleSexOption)
            {
                sex = "Female";
            }
        });

        // listener for unknown
        unknownSex.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View unknownSexOption)
            {
                sex = "Unknown";
            }
        });
    }


// PHOTO BUTTON METHODS ----------------------------------------------------------------------------


    // Set up the add a photo button
    public void initializePhotoButton()
    {
        // initialize photo button
        Button geckoPhoto = getView().findViewById(R.id.Button_Photo);
        photoDescription = getView().findViewById(R.id.TextView_Photo);

        // set onClickListener
        geckoPhoto.setOnClickListener(this);
    }

    // define actions for when photo button is clicked
    @Override
    public void onClick(View photoButton)
    {
        getPhotoFromGallery();
    }

   // created intent to select a photo from gallery
    public void getPhotoFromGallery()
    {
        // create intent to open the gallery
        Intent openGallery = new Intent();
        openGallery.setType("image/*");
        openGallery.setAction(Intent.ACTION_OPEN_DOCUMENT);

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
            photoDescription.setText(R.string.photo_selected);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                getContext().getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
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
                if (validBirthday && validName && validWeight && validTemperature && validHumidity
                        && validStatus && validSpecies)
                {
                    // only calculate age if a birth date exists
                    if (!birthday.equals("Unknown"))
                    {
                        calculateAge();
                    }

                    // create numbers as strings:
                    String formattedWeight = String.valueOf(weight) + " grams";
                    String formattedTemp = String.valueOf(temperature) + "°F";
                    String formattedHumidity = String.valueOf(humidity) + "%";
                    String formattedAge;
                    if (age == 1)
                    {
                        formattedAge = String.valueOf(age) + " year";
                    }
                    else
                    {
                        formattedAge = String.valueOf(age) + " years";
                    }

                    if (adding)
                    {
                        // create new gecko and print info
                        GeckoModel gecko = new GeckoModel(-1, name, sex, birthday, formattedAge,
                                species, morph, formattedWeight, formattedTemp, formattedHumidity,
                                status, seller, photo);

                        // add new gecko to database
                        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                        boolean success = dbHelper.addGecko(gecko);
                    }
                    else if (editing)
                    {
                        // set new values for gecko
                        editGecko.setName(name);
                        editGecko.setGeckoSpecies(species);
                        editGecko.setMorph(morph);
                        editGecko.setSex(sex);
                        editGecko.setBirthday(birthday);
                        editGecko.setAge(formattedAge);
                        editGecko.setWeight(formattedWeight);
                        editGecko.setTemperature(formattedTemp);
                        editGecko.setHumidity(formattedHumidity);
                        editGecko.setStatus(status);
                        editGecko.setSeller(seller);
                        editGecko.setPhotoID(photo);

                        // edit gecko in database
                        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                        dbHelper.editGecko(editGecko);
                    }
                    Navigation.findNavController(getView()).navigate(R.id.action_fragment_gecko_data_to_gecko_page);
                }
                // at least one required field is wrong, find the invalid ones
                else
                {
                    Toast.makeText(getContext(), "Please correct the red data fields!",
                            Toast.LENGTH_SHORT).show();

                    if (!validName)
                    {
                        nameLabel.setTextColor(Color.RED);
                    }
                    if (!validBirthday)
                    {
                        birthdayLabel.setTextColor(Color.RED);
                    }
                    if (!validSpecies)
                    {
                        speciesLabel.setTextColor(Color.RED);
                    }
                    if (!validStatus)
                    {
                        statusLabel.setTextColor(Color.RED);
                    }
                    if (!validWeight)
                    {
                        weightLabel.setTextColor(Color.RED);
                    }
                    if (!validTemperature)
                    {
                        temperatureLabel.setTextColor(Color.RED);
                    }
                    if (!validHumidity)
                    {
                        humidityLabel.setTextColor(Color.RED);
                    }
                    if (validBirthday)
                    {
                        geckoBirthday.setText(R.string.unknown);
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
                if (adding)  // return to My Gecks page
                {
                    Navigation.findNavController(getView()).navigate(R.id.action_fragment_gecko_data_to_gecko_page);
                }
                else // return to selected gecko's page
                {
                    Navigation.findNavController(getView()).navigate(R.id.action_fragment_gecko_data_to_fragment_selected_gecko);
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