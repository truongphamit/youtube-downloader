package org.truong.youtubedownloader.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;

import org.truong.youtubedownloader.fragments.FirstIntroFragment;
import org.truong.youtubedownloader.fragments.SecondIntroFragment;
import org.truong.youtubedownloader.fragments.ThirdIntroFragment;

/**
 * Created by truon on 3/2/17.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        addSlide(FirstIntroFragment.newInstance());
        addSlide(SecondIntroFragment.newInstance());
        addSlide(ThirdIntroFragment.newInstance());

        showSkipButton(false);

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#E62118"));
        setSeparatorColor(Color.parseColor("#E62118"));

        setFadeAnimation();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
