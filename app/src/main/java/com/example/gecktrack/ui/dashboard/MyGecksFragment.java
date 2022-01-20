// Amanda Villarreal
// July 20, 2021
// MyGecksFragment.java
// GeckTrack.app
// Use this class to program functionality of Dashboard (My Gecks) Page
// ------------------------------------------------------------------------------------------------


package com.example.gecktrack.ui.dashboard;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.gecktrack.DatabaseHelper;
import com.example.gecktrack.R;
import com.example.gecktrack.ui.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


// ------------------------------------------------------------------------------------------------


public class MyGecksFragment extends Fragment implements View.OnClickListener
{

    // variables for SharedViewModel
    private MyGecksViewModel myGecksViewModel;
    private SharedViewModel viewModel;

    // XML widgets
    Button addGeckoButton;
    LinearLayout geckoListHolder;


// CREATION METHODS --------------------------------------------------------------------------------


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        myGecksViewModel = new ViewModelProvider(this).get(MyGecksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_gecks, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        addGeckoButton = root.findViewById(R.id.Button_AddEvent);
        addGeckoButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // Hide the Bottom Navigation View for this page
        BottomNavigationView navigationBar = getActivity().findViewById(R.id.nav_view);
        navigationBar.setVisibility(View.VISIBLE);

        // initialize tools and features
        updateGeckoList();
    }


// TOOL BUTTONS SECTION ----------------------------------------------------------------------------


    // on click listener for Add button
    @Override
    public void onClick(View view)
    {
        // launches new fragment (add a gecko data form)
        Navigation.findNavController(view).navigate(R.id.action_gecko_page_to_fragment_gecko_data);
    }


// GECKO LIST SECTION ------------------------------------------------------------------------------


    // display the users geckos
    public void updateGeckoList()
    {
        // initialize the layout to hold geckos
        geckoListHolder = getView().findViewById(R.id.LinearLayout_GeckoList);

        // get all geckos from the database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<GeckoModel> geckos = dbHelper.getGeckoList();

        // for each gecko in List geckos
        for (GeckoModel gecko: geckos)
        {
            // create a horizontal linear layout for each gecko.
            LinearLayout geckoInfoCell = new LinearLayout(getContext());
            geckoInfoCell.setOrientation(LinearLayout.HORIZONTAL);
            geckoInfoCell.setClickable(true);
            // geckoInfoCell.setBackgroundColor(Color.);

            // Create image view for each gecko
            ImageView geckoPhoto = new ImageView(getContext());
            geckoPhoto.setBackgroundResource(R.drawable.aftcarebutton);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(300, 300);
            geckoPhoto.setLayoutParams(layoutParams1);

            // gecko has no added photo
            if (gecko.getPhotoID().contains("No photo"))
            {
                geckoPhoto.setBackgroundResource(R.drawable.daycarebutton);
            }
            else // user added a photo, show the photo
            {

            }

            // Create a text view for each gecko name
            TextView geckoName = new TextView(getContext());
            geckoName.setTextColor(getResources().getColor(R.color.white));
            geckoName.setTextSize(20);
            geckoName.setTypeface(null, Typeface.BOLD); // bold name
            geckoName.setText("    " + gecko.getName());

            // Create a text view for each gecko info
            TextView geckoInfo = new TextView(getContext());
            geckoInfo.setTextColor(getResources().getColor(R.color.data_input_color));
            geckoInfo.setTextSize(18);

            String geckoAge;

            // if gecko has no birthday, age is unknown
            if (gecko.getAge().contains("-1 year(s)"))
            {
                geckoAge = "Age Unknown";
            }
            else
            {
                geckoAge = gecko.getAge();
            }

            geckoInfo.setText("    " + gecko.getGeckoSpecies() + "\n" +
                              "    " + gecko.getMorph() + "\n" +
                              "    " + geckoAge + "\n\n");

            // add name and info to its own layout
            LinearLayout geckoTextData = new LinearLayout(getContext());
            geckoTextData.setOrientation(LinearLayout.VERTICAL);
            geckoTextData.addView(geckoName);
            geckoTextData.addView(geckoInfo);

            // create image view for gecko's gender
            ImageView geckoGenderIcon = new ImageView(getContext());

            if(gecko.getSex().contains("Female"))
            {
                geckoGenderIcon.setBackgroundResource(R.drawable.femaleicon);
            }
            else if(gecko.getSex().contains("Male"))
            {
                geckoGenderIcon.setBackgroundResource(R.drawable.maleicon);
            }
            else // gender = unknown
            {
                geckoGenderIcon.setBackgroundResource(R.drawable.unknownicon);
            }

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(50, 50);
            geckoGenderIcon.setLayoutParams(layoutParams2);

            // build gecko data cell to horizontal layout
            geckoInfoCell.addView(geckoPhoto);
            geckoInfoCell.addView(geckoTextData);
            geckoInfoCell.addView(geckoGenderIcon);
            geckoInfoCell.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    geckoSelected(gecko);
                }
            });

            // add data cell to vertical layout
            geckoListHolder.addView(geckoInfoCell);
        }
    }

    // open the selected gecko's information page
    public void geckoSelected(GeckoModel gecko)
    {
        // store gecko for reference in next fragment
        viewModel.setGecko(gecko);
        Navigation.findNavController(getView()).navigate(R.id.action_gecko_page_to_fragment_selected_gecko);
    }

}