package com.simmorsal.devrantnative.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simmorsal.devrantnative.R;


public class StoriesFragment extends Fragment {

    public StoriesFragment() {}

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stories, container, false);


        return view;
    }
}
