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
public class ThirdIntroFragment extends Fragment {

    public static ThirdIntroFragment newInstance() {
        return new ThirdIntroFragment();
    }

    public ThirdIntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third_intro, container, false);
    }

}
