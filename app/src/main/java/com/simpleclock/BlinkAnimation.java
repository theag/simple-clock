package com.simpleclock;

import android.view.animation.AlphaAnimation;

/**
 * Created by nbp184 on 2016/02/11.
 */
public class BlinkAnimation extends AlphaAnimation {

    private int lasts;
    private int count;

    public BlinkAnimation(float fromAlpha, float toAlpha) {
        super(fromAlpha, toAlpha);
        lasts = 1;
        setDuration(500);
        setRepeatMode(REVERSE);
    }

    public void setLasts(int lasts) {
        this.lasts = lasts;
        setRepeatCount(lasts*120);
    }

    @Override
    public void reset() {
        super.reset();
        count = 0;
    }

    public void increment() {
        count++;
        if(count == lasts) {
            cancel();
        }
    }

    public boolean isDone() {
        return count == lasts;
    }
}
