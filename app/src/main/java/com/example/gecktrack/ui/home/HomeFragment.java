// Amanda Villarreal
// July 20, 2021
// HomeFragment.java
// GeckTrack.app
// Home Page
// -------------------------------------------------------------------------------------------------

package com.example.gecktrack.ui.home;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gecktrack.DatabaseHelper;
import com.example.gecktrack.R;
import com.example.gecktrack.ui.EventDayMap;
import com.example.gecktrack.ui.mygecks.GeckoModel;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Collections;
import java.util.List;

// -------------------------------------------------------------------------------------------------

public class HomeFragment extends Fragment implements View.OnClickListener
{

// CREATION METHODS --------------------------------------------------------------------------------


    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        // initialize page features
        initializeImageSlider();
        initializeCareTipButtons();
        initializeUpcomingEvents();
        updateAge();
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

        // create an ImageView to hold image, add image to ImageView
        ImageView homePageImageView = new ImageView(getContext());
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
    private void initializeCareTipButtons()
    {
        // initialize and define all 9 buttons
        Button leopardCareTipsButton = getView().findViewById(R.id.Button_LeoTips);
        Button fatTailCareTipsButton = getView().findViewById(R.id.Button_FatTailTips);
        Button crestedCareTipsButton = getView().findViewById(R.id.Button_CrestedTips);
        Button dayCareTipsButton = getView().findViewById(R.id.Button_DayTips);
        Button lechianusCareTipsButton = getView().findViewById(R.id.Button_LechianusTips);
        Button tokayCareTipsButton = getView().findViewById(R.id.Button_TokayTips);
        Button gargoyleCareTipsButton = getView().findViewById(R.id.Button_GargoyleTips);
        Button knobTailCareTipsButton = getView().findViewById(R.id.Button_KnobTailTips);
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
        // create alert message to be sure to delete gecko
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Leave GeckTrack?");
        alert.setMessage("This button opens a web link to get care tips for the gecko species you selected. " +
                         "Are you sure you want to leave GeckTrack and open a web link?");

        // Listener for Yes option
        alert.setPositiveButton("Yes!", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // define Intent, start activity (open browser with selected web page)
                Intent launchLink = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(launchLink);

                // close the dialog
                dialog.dismiss();
            }
        });

        // Listener for no option
        alert.setNegativeButton("No, go back.", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Do nothing
                dialog.dismiss();
            }
        });

        // configure alert dialog
        AlertDialog alertMessage = alert.create();
        alertMessage.show();
        alertMessage.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertMessage.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);


    }


// BIRTHDAY METHODS --------------------------------------------------------------------------------


    // update age of geckos whose birthdays have past
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateAge()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<GeckoModel> geckos = dbHelper.getGeckoList();

        // for each gecko in List geckos
        for (GeckoModel gecko: geckos)
        {
            // separate each part of birthday
            String birthMonth = gecko.getBirthday().substring(0,2);
            String birthDay = gecko.getBirthday().substring(3,5);
            String birthYear = gecko.getBirthday().substring(6,10);

            // set birthday and today's date
            LocalDate today = LocalDate.now();
            LocalDate birthday = LocalDate.of(Integer.parseInt(birthYear),
                    Month.of(Integer.parseInt(birthMonth)), Integer.parseInt(birthDay));

            // find age
            Period timeBetween = Period.between(birthday, today);

            // set age
            int age = timeBetween.getYears();
            String formattedAge;

            // format age
            if (age == 1)
            {
                formattedAge = String.valueOf(age) + " year";
            }
            else
            {
                formattedAge = String.valueOf(age) + " years";
            }

            // set the age and update gecko
            gecko.setAge(formattedAge);
            dbHelper.updateAge(gecko);
        }
    }


// UPCOMING EVENTS METHODS -------------------------------------------------------------------------


    // display top 3 upcoming events
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initializeUpcomingEvents()
    {
        // get top 3 events from database
        DatabaseHelper db = new DatabaseHelper(getContext());
        List <EventDayMap> top3Events = db.getTop3Events();

        // xml views
        LinearLayout upcomingEventsHolder = getView().findViewById(R.id.LinearLayout_UpcomingEventsHolder);

        // no events
        if(top3Events.isEmpty())
        {
            // Create text views for message, add it to layout
            TextView noEventsMessage2 = new TextView(getContext());
            noEventsMessage2.setTextColor(getResources().getColor(R.color.data_input_color));
            noEventsMessage2.setTextSize(16);
            noEventsMessage2.setGravity(Gravity.CENTER);
            noEventsMessage2.setText(R.string.no_upcoming_events1);
            noEventsMessage2.setBackgroundColor(getResources().getColor(R.color.background2_gray));
            noEventsMessage2.setPadding(100,50,100,80);
            upcomingEventsHolder.addView(noEventsMessage2);
        }
        // user has events, format them
        else
        {
            // loop through top 3 events
            for (EventDayMap event : top3Events)
            {
                // cell for each event
                LinearLayout eventCell = new LinearLayout(getContext());
                eventCell.setOrientation(LinearLayout.VERTICAL);
                eventCell.setPadding(0,25,0,50);
                eventCell.setBackgroundColor(getResources().getColor(R.color.background2_gray));

                // TextView for each name
                TextView eventName = new TextView(getContext());
                eventName.setTextColor(getResources().getColor(R.color.data_input_color));
                eventName.setTextSize(20);
                eventName.setGravity(Gravity.CENTER);
                eventName.setTypeface(null, Typeface.BOLD); // bold name
                eventName.setText(event.getEvent().getName());

                // TextView for days until event
                TextView eventDays = new TextView(getContext());
                eventDays.setTextColor(getResources().getColor(R.color.data_input_color));
                eventDays.setTextSize(20);
                eventDays.setGravity(Gravity.CENTER);

                int daysUntil = event.getDaysUntil();

                // determine which time measurement to display
                if (daysUntil == 0)
                {
                    eventDays.setText("Today!");
                }
                else if (daysUntil == 1)
                {
                    eventDays.setText("Tomorrow!");
                }
                else
                {
                    String time = String.valueOf(daysUntil) + " days";
                    eventDays.setText(time);
                }

                // add all views to hierarchy
                eventCell.addView(eventName);
                eventCell.addView(eventDays);
                upcomingEventsHolder.addView(eventCell);
            }
        }
    }
}