package com.glooory.mvp.base;

import android.app.Application;

import com.glooory.mvp.base.delegate.App;
import com.glooory.mvp.base.delegate.AppDelegate;
import com.glooory.mvp.di.component.AppComponent;

/**
 * Created by Glooory on 17/5/9.
 */

public class BaseApplication extends Application implements App {

    private AppDelegate mAppDelegate;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppDelegate = new AppDelegate(this);
        this.mAppDelegate.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.mAppDelegate.onTerminate();
    }

    /**
     * 将 AppComponent 返回出去，供其他地方使用
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return mAppDelegate.getAppComponent();
    }

}
