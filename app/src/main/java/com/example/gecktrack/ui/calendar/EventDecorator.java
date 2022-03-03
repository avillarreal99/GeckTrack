package com.example.gecktrack.ui.calendar;

import com.example.gecktrack.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class EventDecorator implements DayViewDecorator {

    private final int color;
    private List<String> strDates;
    private HashSet<CalendarDay> dates = new HashSet<>();
    private ArrayList<CalendarDay> calendarDays = new ArrayList<>();

    public EventDecorator(int color, Collection<CalendarDay> dates){
        this.color = color;
        this.dates = new HashSet<>(dates);
    }


    //dont think i need second parameter
    public EventDecorator(int color, List<String> strDates){
        this.color = color;
        dates = new HashSet<>(convertData(strDates));
        //this.strDates = strDates;
    }


    //possibly use this in the future
    private List convertData(List<String> Dates){
        int listSize = Dates.size();

        for(int i = 0; i < listSize; i++){
            String strDate = Dates.get(i);


            String monthStr = strDate.substring(0, 2);
            String dayStr = strDate.substring(3, 5);
            String yearStr = strDate.substring(6, 10);

            int dayInt = Integer.parseInt(dayStr);
            int monthInt = Integer.parseInt(monthStr);
            int yearInt = Integer.parseInt(yearStr);

            calendarDays.add(CalendarDay.from(yearInt, monthInt, dayInt)); //wonder if this will work
        }

        return calendarDays;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10, color));
    }
}
