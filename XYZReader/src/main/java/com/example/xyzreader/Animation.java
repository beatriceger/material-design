package com.example.xyzreader;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

public class Animation {
    public static void fadeOutFadeInSimultaneously(ViewGroup viewGroup,
                                                   Runnable runnable) {

        viewGroup.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimationFadeOut = new AlphaAnimation(1.0f, 0f);
        alphaAnimationFadeOut.setDuration(1000);

        viewGroup.startAnimation(alphaAnimationFadeOut);
        viewGroup.setVisibility(View.INVISIBLE);

        runnable.run();

        viewGroup.setVisibility(View.INVISIBLE);
        AlphaAnimation alphaAnimationFadeIn = new AlphaAnimation(0f, 1.0f);
        alphaAnimationFadeIn.setDuration(1000);

        viewGroup.startAnimation(alphaAnimationFadeIn);
        viewGroup.setVisibility(View.VISIBLE);
    }
}
