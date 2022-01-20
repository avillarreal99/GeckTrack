// Amanda Villarreal
// January 18, 2022
// SharedViewModel.java
// GeckTrack.app
// Help fragments share data
// -------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gecktrack.ui.dashboard.GeckoModel;

public class SharedViewModel extends ViewModel
{
    private MutableLiveData<GeckoModel> gecko = new MutableLiveData<>();

    // set the stored gecko to be the selected gecko
    public void setGecko(GeckoModel selectedGecko)
    {
        gecko.setValue(selectedGecko);
    }

    // send the stored gecko data back
    public LiveData<GeckoModel> getGecko()
    {
        return gecko;
    }
}
