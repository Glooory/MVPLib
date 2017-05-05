package com.glooory.mvp.di.component;

import android.app.Application;

import com.glooory.mvp.base.delegate.AppDelegate;
import com.glooory.mvp.di.module.AppModule;
import com.glooory.mvp.di.module.GlobalConfigModule;
import com.glooory.mvp.di.module.HttpClientModule;
import com.glooory.mvp.integration.AppManager;
import com.glooory.mvp.integration.IRepositoryManager;
import com.google.gson.Gson;

import java.io.File;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by Glooory on 17/5/5.
 */
@Singleton
@Component(modules = {AppModule.class, HttpClientModule.class, GlobalConfigModule.class})
public interface AppComponent {

    Application application();

    // 用于管理网络请求及缓存层
    IRepositoryManager repositoryManager();

    OkHttpClient okHttpClient();

    Gson gson();

    // 缓存文件的根目录
    File rxCacheFile();

    // 用于管理所有的 Activity
    AppManager appManager();

    // 用来存取一些整个 App 公用的数据，切勿大量存放大容量数据
    Map<String, Object> extras();

    void inject(AppDelegate delegate);
}
