package com.example.hyan.demo3;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.crypto.SealedObject;


public class EventsFragment extends Fragment implements SearchView.OnQueryTextListener {
    SearchView searchView;
    Spinner eventDateSpinner;
    Spinner tagsSpinner;
    Location location;
    EventsFragment fragment;
    static EventsFragmentTask task;
    View myView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = this;
        final View view = inflater.inflate(R.layout.events_fragment, container,false);
        location = ((MainActivity) this.getActivity()).getLocation();
        initialize(view);
        myView = view;
        return view;
    }

    public void initialize(final View view){
        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        List<String> eventDates = new ArrayList<String>();
        eventDates.add(dateFormat.format(date));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        for(int i = 0; i < 6; ++i){
            calendar.add(calendar.DATE, 1);
            date = calendar.getTime();
            eventDates.add(dateFormat.format(date));
        }
        eventDateSpinner = (Spinner) view.findViewById(R.id.eventDateSpinner);
        ArrayAdapter<String> eventDateSpinnerAdapter = new ArrayAdapter<String>
                (this.getActivity(),R.layout.events_spinner_item,eventDates);
        eventDateSpinner.setAdapter(eventDateSpinnerAdapter);

        tagsSpinner = (Spinner) view.findViewById(R.id.tagsSpinner);
        ArrayAdapter<String> tagsSpinnerAdapter = new ArrayAdapter<String>
                (this.getActivity(), R.layout.events_spinner_item,
                        this.getResources().getStringArray(R.array.options));
        tagsSpinner.setAdapter(tagsSpinnerAdapter);

        eventDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                task = new EventsFragmentTask(fragment,view,location,false);
                task.setEventDate(eventDateSpinner.getSelectedItem().toString());
                task.setTags(tagsSpinner.getSelectedItem().toString());
                task.execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        tagsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                task = new EventsFragmentTask(fragment,view,location,false);
                task.setEventDate(eventDateSpinner.getSelectedItem().toString());
                task.setTags(tagsSpinner.getSelectedItem().toString());
                task.execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        task = new EventsFragmentTask(fragment,myView,location,true);
        task.setKeyword(query);

        task.execute();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
