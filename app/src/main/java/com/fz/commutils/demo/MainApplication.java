package com.fz.commutils.demo;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

public class MainApplication extends MultiDexApplication {
    private static MainApplication instance;

    public static synchronized MainApplication getAppInstance() {
        return instance;
    }

    public static Context getContext() {
        return getAppInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
