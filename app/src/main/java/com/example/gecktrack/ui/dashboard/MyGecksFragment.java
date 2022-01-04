// Amanda Villarreal
// July 20, 2021
// MyGecksFragment.java
// GeckTrack.app
// Use this class to program functionality of Dashboard (My Gecks) Page
// ------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui.dashboard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.gecktrack.R;
import com.example.gecktrack.fragment_gecko_data;


// ------------------------------------------------------------------------------------------------


public class MyGecksFragment extends Fragment implements View.OnClickListener
{

    private MyGecksViewModel myGecksViewModel;
    Button addGeckoButton;


// CREATION METHODS --------------------------------------------------------------------------------


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        myGecksViewModel = new ViewModelProvider(this).get(MyGecksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_gecks, container, false);

        addGeckoButton = root.findViewById(R.id.Button_AddGecko);
        addGeckoButton.setOnClickListener(this);

        return root;
    }

// TOOL BUTTONS SECTION ----------------------------------------------------------------------------


    // on click listener for Add button (for now)
    @Override
    public void onClick(View view)
    {
        // for now, launches new fragment
        Navigation.findNavController(view).navigate(R.id.action_home_page_to_fragment_gecko_data);
    }

}