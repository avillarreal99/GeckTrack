// Amanda Villarreal
// January 20, 2022
// fragment_care_category.java
// GeckTrack.app
// Gecko's selected care category page (shows the gecko's events under the selected care category)
// -------------------------------------------------------------------------------------------------


package com.example.gecktrack;
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
import com.example.gecktrack.ui.SharedViewModel;
import com.example.gecktrack.ui.dashboard.GeckoModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


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


}