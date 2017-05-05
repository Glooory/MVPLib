package com.glooory.mvp.di.module;

import android.app.Application;
import android.content.Context;
import android.util.ArrayMap;

import com.glooory.mvp.integration.IRepositoryManager;
import com.glooory.mvp.integration.RepositoryManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Glooory on 17/5/3.
 */

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    public Gson provideGson(Application application, GsonConfig gsonConfig) {
        GsonBuilder builder = new GsonBuilder();
        gsonConfig.configGson(application, builder);
        return builder.create();
    }

    @Singleton
    @Provides
    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    @Singleton
    @Provides
    public Map<String, Object> provideExtras() {
        return new ArrayMap<>();
    }

    public interface GsonConfig {

        void configGson(Context context, GsonBuilder builder);

        GsonConfig EMPTY_CONFIG = new GsonConfig() {
            @Override
            public void configGson(Context context, GsonBuilder builder) {

            }
        };
    }
}
