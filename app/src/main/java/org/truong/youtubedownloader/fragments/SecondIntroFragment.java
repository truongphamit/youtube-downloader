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
public class SecondIntroFragment extends Fragment {

    public static SecondIntroFragment newInstance() {
        return new SecondIntroFragment();
    }

    public SecondIntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second_intro, container, false);
    }

}
