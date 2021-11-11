// Amanda Villarreal
// July 20, 2021
// HomeFragment.java
// GeckTrack.app
// Home Page
// -------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui.home;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gecktrack.R;

// -------------------------------------------------------------------------------------------------

public class HomeFragment extends Fragment implements View.OnClickListener
{


// Creation Methods --------------------------------------------------------------------------------


    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // initialize all widgets here:
        initializeImageSlider();
        initializeCareTipButtons();
    }


// VIEW FLIPPER METHODS ----------------------------------------------------------------------------


    // Defines an array of all gecko images to be shown on Home Page
    public void initializeImageSlider()
    {
        // Setting up ViewFlipper to hold an array of images from drawables folder
        int images[] = {R.drawable.fatgecko, R.drawable.crestedgecko, R.drawable.daygecko,
                        R.drawable.leogecko1, R.drawable.knobgecko, R.drawable.leogecko2,
                        R.drawable.tokaygecko};

        // for each image in images[], pass it to setUpImage
        for (int i = 0; i < images.length; i++)
        {
            setUpImage(images[i]); // set up the current image
        }
    }

    // Sets up each image sent from method above into an ImageView for displaying
    public void setUpImage(int image)
    {
        // initialize ViewFlipper that holds gecko images
        ViewFlipper viewFlipperImages = getView().findViewById(R.id.ViewFlipperImages_GeckoImages);

        // set up ImageView parameters
        //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //param.setMargins(30, 30, 30, 30); // set spacing between pics
        //param.gravity = Gravity.CENTER;

        // create an ImageView to hold image, add image to ImageView
        ImageView homePageImageView = new ImageView(getContext());  // may need to adjust context
        //homePageImageView.setLayoutParams(param); // set up parameters
        homePageImageView.setBackgroundResource(image);

        // add ImageView to the ViewFlipper
        viewFlipperImages.addView(homePageImageView);
        viewFlipperImages.setFlipInterval(4000);
        viewFlipperImages.setAutoStart(true);

        // set up animation (how image will move in/out of screen)
        viewFlipperImages.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipperImages.setOutAnimation(getContext(), android.R.anim.slide_out_right);
    }


// CARE TIP BUTTONS METHODS ------------------------------------------------------------------------


    // Initializes and listens for selection of one of the Care Tip Buttons
    private void initializeCareTipButtons( )
    {
        // initialize and define all 9 buttons
        Button leopardCareTipsButton     = getView().findViewById(R.id.Button_LeoTips);
        Button fatTailCareTipsButton     = getView().findViewById(R.id.Button_FatTailTips);
        Button crestedCareTipsButton     = getView().findViewById(R.id.Button_CrestedTips);
        Button dayCareTipsButton         = getView().findViewById(R.id.Button_DayTips);
        Button lechianusCareTipsButton   = getView().findViewById(R.id.Button_LechianusTips);
        Button tokayCareTipsButton       = getView().findViewById(R.id.Button_TokayTips);
        Button gargoyleCareTipsButton    = getView().findViewById(R.id.Button_GargoyleTips);
        Button knobTailCareTipsButton    = getView().findViewById(R.id.Button_KnobTailTips);
        Button chineseCaveCareTipsButton = getView().findViewById(R.id.Button_ChineseCaveTips);

        // listen for a Care Tip Button click on all 9 buttons
        leopardCareTipsButton.setOnClickListener(this);
        fatTailCareTipsButton.setOnClickListener(this);
        crestedCareTipsButton.setOnClickListener(this);
        dayCareTipsButton.setOnClickListener(this);
        lechianusCareTipsButton.setOnClickListener(this);
        tokayCareTipsButton.setOnClickListener(this);
        gargoyleCareTipsButton.setOnClickListener(this);
        knobTailCareTipsButton.setOnClickListener(this);
        chineseCaveCareTipsButton.setOnClickListener(this);
    }

    // Determines the selected Care Tip Button
    @Override
    public void onClick(View selectedCareTipButton)
    {
        // executes case of the selected Care Tip button, launches its web page
        switch (selectedCareTipButton.getId())
        {
            case R.id.Button_LeoTips:
                launchWebLink("https://www.bhbreptiles.com/pages/leopard-gecko-care-sheet");
                break;
            case R.id.Button_FatTailTips:
                launchWebLink("https://www.everythingreptiles.com/african-fat-tailed-gecko/");
                break;
            case R.id.Button_CrestedTips:
                launchWebLink("https://www.joshsfrogs.com/catalog/blog/2018/03/caring-for-crested-geckos/");
                break;
            case R.id.Button_DayTips:
                launchWebLink("https://reptilesmagazine.com/giant-day-gecko-care-sheet/");
                break;
            case R.id.Button_LechianusTips:
                launchWebLink("https://www.everythingreptiles.com/leachie-gecko/");
                break;
            case R.id.Button_TokayTips:
                launchWebLink("https://reptilesmagazine.com/tokay-gecko-care/");
                break;
            case R.id.Button_GargoyleTips:
                launchWebLink("https://www.joshsfrogs.com/catalog/blog/2018/05/gargoyle-gecko-care-sheet/");
                break;
            case R.id.Button_KnobTailTips:
                launchWebLink("https://reptilesmagazine.com/knob-tailed-gecko-care-sheet/");
                break;
            case R.id.Button_ChineseCaveTips:
                launchWebLink("https://www.joshsfrogs.com/catalog/blog/2018/03/chinese-cave-gecko-husbandry-care/");
                break;
        }
    }

    // Opens browser with selected web page
    private void launchWebLink(String link)
    {
        // define Intent, start activity (open browser with selected web page)
        Intent launchLink = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(launchLink);
    }
}