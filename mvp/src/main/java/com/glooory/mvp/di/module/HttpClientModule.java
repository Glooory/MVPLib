package com.glooory.mvp.di.module;

import com.glooory.mvp.base.AppManager;
import com.glooory.mvp.http.RequestInterceptor;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Glooory on 17/5/3.
 */

@Module
public class HttpClientModule {

    private static final int TIME_OUT_DEFAULT = 10;
    private AppManager mAppManager;

    public HttpClientModule(AppManager appManager) {
        this.mAppManager = appManager;
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Retrofit.Builder builder, OkHttpClient httpClient, HttpUrl httpUrl) {
        return builder
                .baseUrl(httpUrl) // BaseUrl
                .client(httpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用 RxJava
                .addConverterFactory(GsonConverterFactory.create()) // 使用 Gson
                .build();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder, Interceptor interceptor,
                                     List<Interceptor> interceptorList) {
        builder.connectTimeout(TIME_OUT_DEFAULT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_DEFAULT, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor);
        if (interceptorList != null && interceptorList.size() > 0) {
            for (Interceptor interceptorTemp : interceptorList) {
                builder.addInterceptor(interceptorTemp);
            }
        }
        return builder.build();
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    Interceptor provideInterceptor(RequestInterceptor interceptor) {
        // Http Request 拦截器
        return interceptor;
    }

    @Singleton
    @Provides
    RxCache provideRxCha(@Named("RxCacheDirectory") File cacheDir) {
        return new RxCache
                .Builder()
                .persistence(cacheDir, new GsonSpeaker());
    }

    @Singleton
    @Provides
    AppManager provideAppManager() {
        return mAppManager;
    }
}
