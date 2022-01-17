// Amanda Villarreal
// July 20, 2021
// MyGecksFragment.java
// GeckTrack.app
// Use this class to program functionality of Dashboard (My Gecks) Page
// ------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui.dashboard;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
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
import androidx.navigation.Navigation;

import com.example.gecktrack.DatabaseHelper;
import com.example.gecktrack.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


// ------------------------------------------------------------------------------------------------


public class MyGecksFragment extends Fragment implements View.OnClickListener
{

    private MyGecksViewModel myGecksViewModel;
    Button addGeckoButton;
    LinearLayout geckoListHolder;


// CREATION METHODS --------------------------------------------------------------------------------


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        myGecksViewModel = new ViewModelProvider(this).get(MyGecksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_gecks, container, false);

        addGeckoButton = root.findViewById(R.id.Button_AddGecko);
        addGeckoButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // initialize tools and features
        updateGeckoList();
    }

// TOOL BUTTONS SECTION ----------------------------------------------------------------------------


    // on click listener for Add button (for now)
    @Override
    public void onClick(View view)
    {
        // for now, launches new fragment
        Navigation.findNavController(view).navigate(R.id.action_home_page_to_fragment_gecko_data);
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
            String geckoAge;

            // create a horizontal linear layout for each gecko.
            LinearLayout geckoInfoCell = new LinearLayout(getContext());
            geckoInfoCell.setOrientation(LinearLayout.HORIZONTAL);
            geckoInfoCell.setClickable(true);
            // geckoInfoCell.setBackgroundColor(Color.);

            // Create image view for each gecko
            ImageView geckoPhoto = new ImageView(getContext());
            geckoPhoto.setBackgroundResource(R.drawable.aftcarebutton);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
            geckoPhoto.setLayoutParams(layoutParams);

            // Create a text view for each gecko
            TextView geckoInfo = new TextView(getContext());
            geckoInfo.setTextColor(getResources().getColor(R.color.data_input_color));
            geckoInfo.setTextSize(18);

            // if gecko has no birthday, age is unknown
            if (gecko.getAge().contains("-1 year(s)"))
            {
                geckoAge = "Age Unknown";
            }
            else
            {
                geckoAge = gecko.getAge();
            }

            geckoInfo.setText(gecko.getName() + "\n" +
                              gecko.getSex() + "\n" +
                              gecko.getGeckoSpecies() + "\n" +
                              gecko.getMorph() + "\n" +
                              geckoAge + "\n");

            // build gecko data cell to horizontal layout
            geckoInfoCell.addView(geckoPhoto);
            geckoInfoCell.addView(geckoInfo);

            // add data cell to vertical layout
            geckoListHolder.addView(geckoInfoCell);
        }


    }


}