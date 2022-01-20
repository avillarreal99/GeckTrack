// Amanda Villarreal
// January 11, 2022
// fragment_selected_gecko.java
// GeckTrack.app
// Gecko information page (shows a selected gecko's data)
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.dashboard;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.gecktrack.R;
import com.example.gecktrack.ui.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


// -------------------------------------------------------------------------------------------------

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_selected_gecko#newInstance} factory method to
 * create an instance of this fragment.
 */



public class fragment_selected_gecko extends Fragment
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
    public fragment_selected_gecko() { }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_selected_gecko.

    // Rename and change types and number of parameters
    public static fragment_selected_gecko newInstance(String param1, String param2) {
        fragment_selected_gecko fragment = new fragment_selected_gecko();
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
        //viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
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
        setDataFields();
    }


// VIEW MODEL METHODS ------------------------------------------------------------------------------


    // get selected gecko from previous fragment
    public void initializeSelectedGecko()
    {
        selectedGecko = viewModel.getGecko().getValue();
        System.out.println("A gecko was loaded: \n" + selectedGecko);
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


}