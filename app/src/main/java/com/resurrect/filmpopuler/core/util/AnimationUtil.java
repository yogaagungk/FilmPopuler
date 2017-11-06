package com.resurrect.filmpopuler.core.util;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by root on 25/10/17.
 */
public class AnimationUtil {
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
