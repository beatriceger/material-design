package com.example.xyzreader;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

public class AnimationUtils {
    public static void fadeOutFadeInSimultaneously(ViewGroup viewGroup,
                                                   Runnable runnable,
                                                   int fadeOutDurationInMillis,
                                                   int fadeInDurationInMillis) {

        viewGroup.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimationFadeOut = new AlphaAnimation(1.0f, 0f);
        alphaAnimationFadeOut.setDuration(fadeOutDurationInMillis);

        viewGroup.startAnimation(alphaAnimationFadeOut);
        viewGroup.setVisibility(View.INVISIBLE);

        runnable.run();

        viewGroup.setVisibility(View.INVISIBLE);
        AlphaAnimation alphaAnimationFadeIn = new AlphaAnimation(0f, 1.0f);
        alphaAnimationFadeIn.setDuration(fadeInDurationInMillis);

        viewGroup.startAnimation(alphaAnimationFadeIn);
        viewGroup.setVisibility(View.VISIBLE);
    }
}
