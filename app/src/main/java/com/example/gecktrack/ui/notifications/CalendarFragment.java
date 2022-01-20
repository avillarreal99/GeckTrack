// Amanda Villarreal
// July 20, 2021
// NotificationsFragment.java
// GeckTrack.app
// Use this class to program functionality of Notifications (Schedule) Page
// ------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui.notifications;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gecktrack.R;

// ------------------------------------------------------------------------------------------------

public class CalendarFragment extends Fragment
{

    private CalendarViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        notificationsViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        return root;
    }
}