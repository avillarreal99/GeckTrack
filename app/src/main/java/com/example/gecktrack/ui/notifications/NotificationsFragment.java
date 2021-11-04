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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.gecktrack.R;
import com.example.gecktrack.ui.notifications.NotificationsViewModel;

// ------------------------------------------------------------------------------------------------

public class NotificationsFragment extends Fragment
{

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        return root;
    }
}