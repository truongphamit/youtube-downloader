package org.truong.youtubedownloader.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.truong.youtubedownloader.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstIntroFragment extends Fragment {


    public static FirstIntroFragment newInstance() {
        return new FirstIntroFragment();
    }

    public FirstIntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_intro, container, false);
    }

}
