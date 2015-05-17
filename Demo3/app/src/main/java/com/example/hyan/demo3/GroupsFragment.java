package com.example.hyan.demo3;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hyan on 2015/4/17.
 */
public class GroupsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.groups_fragment, container, false);
        Location loc = ((MainActivity) this.getActivity()).getLocation();
        GroupsFragmentTask task = new GroupsFragmentTask(this, view, loc);
        task.execute();
        return view;
    }
}