package com.glooory.mvp.base.delegate;

import android.app.Application;

import com.glooory.mvp.base.App;
import com.glooory.mvp.di.component.AppComponent;

import javax.inject.Inject;

/**
 * Created by Glooory on 17/5/5.
 */

public class AppDelegate implements App {

    private Application mApplication;
    private AppComponent mAppComponent;
    @Inject


    @Override
    public AppComponent getAppComponent() {
        return null;
    }

    public interface Lifecycle {

        void onCreate(Application application);

        void onTerminate(Application application);
    }
}
