// Amanda Villarreal
// July 20, 2021
// HomeFragment.java
// GeckTrack.app
// Use this class to program functionality of Home Page
// ------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui.home;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gecktrack.R;

// ------------------------------------------------------------------------------------------------

public class HomeFragment extends Fragment
{

    private HomeViewModel homeViewModel;
    private ViewFlipper viewFlipperImages;  // Holds automatically scrolling images

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // setting up widgets from XML should go here:
        viewFlipperImages = getView().findViewById(R.id.viewFlipperImages);

        // initialize all widgets here:
        initializeImageSlider();
    }


// VIEW FLIPPER METHODS ---------------------------------------------------------------------------


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

}