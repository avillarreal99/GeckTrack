// Amanda Villarreal
// July 20, 2021
// CalendarFragment.java
// GeckTrack.app
// Use this class to program functionality of Calendar Page
// ------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui.calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.gecktrack.R;
import com.example.gecktrack.ui.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// ------------------------------------------------------------------------------------------------

public class CalendarFragment extends Fragment
{

    // variables for SharedViewModel
    private SharedViewModel viewModel;
    private CalendarViewModel notificationsViewModel;


// CREATION METHODS --------------------------------------------------------------------------------


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        notificationsViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // Hide the Bottom Navigation View for this page
        BottomNavigationView navigationBar = getActivity().findViewById(R.id.nav_view);
        navigationBar.setVisibility(View.VISIBLE);

        // initialize page features
        initializeAddButton();
    }


// TOOL BUTTON METHODS -----------------------------------------------------------------------------


    public void initializeAddButton()
    {
        // initialize add button and set listener
        Button addButton = getView().findViewById(R.id.Button_AddEvent);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // event will always be null from add button
                viewModel.setEvent(null);

                // open event data form to add an event
                Navigation.findNavController(getView()).navigate(R.id.action_schedule_page_to_fragment_event_data_form);
            }
        });

    }

}