package com.glooory.mvp.base.delegate;

import android.app.Application;

import com.glooory.mvp.di.component.AppComponent;
import com.glooory.mvp.di.component.DaggerAppComponent;
import com.glooory.mvp.di.module.AppModule;
import com.glooory.mvp.di.module.GlobalConfigModule;
import com.glooory.mvp.di.module.HttpClientModule;
import com.glooory.mvp.integration.ActivityLifecycle;
import com.glooory.mvp.integration.ConfigModule;
import com.glooory.mvp.util.ManifestParser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Glooory on 17/5/5.
 */

public class AppDelegate implements App {

    private Application mApplication;
    private AppComponent mAppComponent;
    @Inject
    ActivityLifecycle mActivityLifecycle;
    private final List<ConfigModule> mConfigModuleList;
    private List<Lifecycle> mAppLifecycleList = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycleCallbacksList = new ArrayList<>();

    public AppDelegate(Application application) {
        this.mApplication = application;
        this.mConfigModuleList = new ManifestParser(mApplication).parse();
        for (ConfigModule configModule : mConfigModuleList) {
            configModule.injectAppLifecycle(mApplication, mAppLifecycleList);
            configModule.injectActivityLifecycle(mApplication, mActivityLifecycleCallbacksList);
        }
    }

    public void onCreate() {
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(mApplication)) //提供application
                .httpClientModule(new HttpClientModule()) //用于提供okhttp和retrofit的单例
                .globalConfigModule(getGlobalConfigModule(mApplication, mConfigModuleList)) //全局配置
                .build();

        mAppComponent.inject(this);

        mAppComponent.extras().put(ConfigModule.class.getName(), mConfigModuleList);

        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);

        for (ConfigModule configModule : mConfigModuleList) {
            configModule.registerComponents(mApplication, mAppComponent.repositoryManager());
        }

        for (Lifecycle lifecycle : mAppLifecycleList) {
            lifecycle.onCreate(mApplication);
        }
    }

    public void onTerminate() {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }

        if (mActivityLifecycleCallbacksList != null && mActivityLifecycleCallbacksList.size() > 0) {
            for (Application.ActivityLifecycleCallbacks activityLifecycleCallbacks : mActivityLifecycleCallbacksList) {
                mApplication.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
            }
        }

        for (Lifecycle lifecycle : mAppLifecycleList) {
            lifecycle.onTerminate(mApplication);
        }

        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mAppLifecycleList = null;
        this.mAppLifecycleList = null;
        this.mApplication = null;
    }

    /**
     * 将 App 的全局配置信息封装进 module (使用 Dagger 注入到需要配置信息的地方)
     * 需要在 AndroidManifest 文件中声明 {@link ConfigModule} 的实现类，和 Glide 的配置方式相似
     * @param application
     * @param moduleList
     * @return
     */
    private GlobalConfigModule getGlobalConfigModule(Application application, List<ConfigModule> moduleList) {

        GlobalConfigModule.Builder builder = GlobalConfigModule.builder();

        for (ConfigModule configModule : moduleList) {
            configModule.applyOptions(mApplication, builder);
        }

        return builder.build();
    }

    @Override
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public interface Lifecycle {

        void onCreate(Application application);

        void onTerminate(Application application);
    }
}
