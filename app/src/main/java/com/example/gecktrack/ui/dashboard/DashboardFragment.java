// Amanda Villarreal
// July 20, 2021
// DashboardFragment.java
// GeckTrack.app
// Use this class to program functionality of Dashboard (My Gecks) Page
// ------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui.dashboard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.gecktrack.R;
import com.example.gecktrack.ui.dashboard.DashboardViewModel;

// ------------------------------------------------------------------------------------------------

public class DashboardFragment extends Fragment
{

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_gecks, container, false);

        return root;
    }
}