package com.labrats.destinytracker;

import android.app.Application;

import com.orm.*;

/**
 * Created by felipe on 03/02/2015.
 */
public class DestinyTracker extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
