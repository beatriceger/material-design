package com.example.xyzreader;

import android.support.design.widget.AppBarLayout;

public abstract class AppBarChangeListener implements AppBarLayout.OnOffsetChangedListener {

    private State currentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (currentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            currentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (currentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            currentState = State.COLLAPSED;
        } else {
            if (currentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            currentState = State.IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }
}
