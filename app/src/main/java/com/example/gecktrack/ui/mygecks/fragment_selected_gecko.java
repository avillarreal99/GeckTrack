// Amanda Villarreal
// January 18, 2022
// fragment_selected_gecko.java
// GeckTrack.app
// Gecko information page (shows a selected gecko's data)
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.mygecks;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.gecktrack.DatabaseHelper;
import com.example.gecktrack.R;
import com.example.gecktrack.ui.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


// -------------------------------------------------------------------------------------------------


public class fragment_selected_gecko extends Fragment implements View.OnClickListener
{
    // variables for SharedViewModel
    private SharedViewModel viewModel;
    GeckoModel selectedGecko;

    // Text label widgets from xml code
    TextView name;
    TextView species;
    TextView morph;
    TextView sex;
    TextView birthday;
    TextView age;
    TextView weight;
    TextView temperature;
    TextView humidity;
    TextView status;
    TextView seller;

    // Required empty public constructor
    public fragment_selected_gecko() { }


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
        return inflater.inflate(R.layout.fragment_selected_gecko, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // Hide the Bottom Navigation View for this page
        BottomNavigationView navigationBar = getActivity().findViewById(R.id.nav_view);
        navigationBar.setVisibility(View.GONE);

        // initialize page features
        initializeSelectedGecko();
        initializeBackButton();
        initializeDeleteButton();
        initializeEditButton();
        initializeCareButtons();
        setDataFields();
    }


// VIEW MODEL METHODS ------------------------------------------------------------------------------


    // get selected gecko from previous fragment
    public void initializeSelectedGecko()
    {
        selectedGecko = viewModel.getGecko().getValue();
    }


// GECKO INFORMATION METHODS -----------------------------------------------------------------------


    // set all data fields to the selected gecko
    public void setDataFields()
    {
        // initialize all text views
        name          = getView().findViewById(R.id.TextView_GeckoName);
        species       = getView().findViewById(R.id.TextView_GeckoSpecies);
        morph         = getView().findViewById(R.id.TextView_GeckoMorph);
        sex           = getView().findViewById(R.id.TextView_GeckoGender);
        birthday      = getView().findViewById(R.id.TextView_GeckoBirthday);
        age           = getView().findViewById(R.id.TextView_GeckoAge);
        weight        = getView().findViewById(R.id.TextView_GeckoWeight);
        temperature   = getView().findViewById(R.id.TextView_GeckoTemperature);
        humidity      = getView().findViewById(R.id.TextView_GeckoHumidity);
        status        = getView().findViewById(R.id.TextView_GeckoStatusOption);
        seller        = getView().findViewById(R.id.TextView_GeckoSeller);

        // set all data fields to display the selected gecko
        name.setText(selectedGecko.getName());
        species.setText("Species: " + selectedGecko.getGeckoSpecies());
        morph.setText("Morph: " + selectedGecko.getMorph());
        sex.setText("Sex: " + selectedGecko.getSex());
        birthday.setText("Birthday: " + selectedGecko.getBirthday());
        weight.setText("Weight: " + selectedGecko.getWeight());
        temperature.setText("Temperature: " + selectedGecko.getTemperature());
        humidity.setText("Humidity: " + selectedGecko.getHumidity());
        status.setText("Status: " + selectedGecko.getStatus());
        seller.setText("Seller: " + selectedGecko.getSeller());

        // determine age if age is unknown
        if(selectedGecko.getAge().contains("-1 year(s)"))
        {
            age.setText("Age: Unknown");
        }
        else // gecko has an age
        {
            age.setText("Age: " + selectedGecko.getAge());
        }
    }


// CARE BUTTONS METHODS ----------------------------------------------------------------------------


    // initialize all 8 care buttons and set listeners
    public void initializeCareButtons()
    {
        // initialize all buttons
        Button feedingButton     = getView().findViewById(R.id.FeedingCategoryCare_Button);
        Button cleaningButton    = getView().findViewById(R.id.CleaningCareCategory_Button);
        Button sheddingButton    = getView().findViewById(R.id.SheddingCareCategory_Button);
        Button healthButton      = getView().findViewById(R.id.HealthCareCategory_Button);
        Button appointmentButton = getView().findViewById(R.id.AppointmentCareCategory_Button);
        Button weighingButton    = getView().findViewById(R.id.WeighingCareCategory_Button);
        Button breedingButton    = getView().findViewById(R.id.BreedingCareCategory_Button);
        Button otherButton       = getView().findViewById(R.id.OtherCareCategory_Button);

        // set listeners
        feedingButton.setOnClickListener(this);
        cleaningButton.setOnClickListener(this);
        sheddingButton.setOnClickListener(this);
        healthButton.setOnClickListener(this);
        appointmentButton.setOnClickListener(this);
        weighingButton.setOnClickListener(this);
        breedingButton.setOnClickListener(this);
        otherButton.setOnClickListener(this);
    }

    // determine which care category was selected
    @Override
    public void onClick(View careButton)
    {
        switch(careButton.getId())
        {
            case R.id.FeedingCategoryCare_Button:
                viewModel.setEventType("Feeding");
                break;
            case R.id.CleaningCareCategory_Button:
                viewModel.setEventType("Cleaning");
                break;
            case R.id.SheddingCareCategory_Button:
                viewModel.setEventType("Shedding");
                break;
            case R.id.HealthCareCategory_Button:
                viewModel.setEventType("Health");
                break;
            case R.id.AppointmentCareCategory_Button:
                viewModel.setEventType("Appointment");
                break;
            case R.id.WeighingCareCategory_Button:
                viewModel.setEventType("Weighing");
                break;
            case R.id.BreedingCareCategory_Button:
                viewModel.setEventType("Breeding");
                break;
            case R.id.OtherCareCategory_Button:
                viewModel.setEventType("Other");
                break;
        }

        // launches new fragment (selected care category page)
        Navigation.findNavController(getView()).navigate(R.id.action_fragment_selected_gecko_to_fragment_care_category);
    }


// TOOL BUTTON METHODS -----------------------------------------------------------------------------


    // initialize back button and set listener, go back to My Gecks Page
    public void initializeBackButton()
    {
        Button backButton = getView().findViewById(R.id.BackToMyGecksPage_Button);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Navigation.findNavController(getView()).navigate(R.id.action_fragment_selected_gecko_to_gecko_page);
            }
        });
    }

    // delete the currently selected gecko
    public void initializeDeleteButton()
    {
        // initialize and set listener
        Button deleteButton = getView().findViewById(R.id.DeleteGecko_Button);

        // need database connection
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // create alert message to be sure to delete gecko
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Delete " + selectedGecko.getName() + "?");
                alert.setMessage("Are you sure you want to permanently delete this gecko? All data and events related to this gecko will also be deleted.");


                // Listener for Yes option
                alert.setPositiveButton("Yes, Delete.", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // delete gecko, go back to My Gecks Page
                        dbHelper.deleteGecko(selectedGecko);
                        Navigation.findNavController(getView()).navigate(R.id.action_fragment_selected_gecko_to_gecko_page);

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
        });
    }

    // edit the currently selected gecko, go to gecko data entry form
    public void initializeEditButton()
    {
        // initialize and set listener
        Button editButton = getView().findViewById(R.id.EditGecko_Button);
        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Navigation.findNavController(getView()).navigate(R.id.action_fragment_selected_gecko_to_fragment_gecko_data);
            }
        });
    }


}