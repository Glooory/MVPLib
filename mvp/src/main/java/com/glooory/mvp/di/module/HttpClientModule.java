package com.glooory.mvp.di.module;

import android.app.Application;
import android.content.Context;

import com.glooory.mvp.http.HttpRequestHandler;
import com.glooory.mvp.http.RequestInterceptor;
import com.glooory.mvp.util.FileUtils;

import java.io.File;
import java.io.IOException;
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
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Glooory on 17/5/3.
 */

@Module
public class HttpClientModule {

    private static final int TIME_OUT_DEFAULT = 10;

    @Singleton
    @Provides
    Retrofit provideRetrofit(Application application, RetrofitConfig retrofitConfig,
            Retrofit.Builder builder, OkHttpClient okHttpClient, HttpUrl httpUrl) {
        builder.baseUrl(httpUrl) // base url
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用 RxJava
                .addConverterFactory(GsonConverterFactory.create()); // 使用 Gson
        retrofitConfig.configRetrofit(application, builder);
        return builder.build();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(Application application, OkHttpClientConfig okHttpClientConfig,
            OkHttpClient.Builder builder, Interceptor interceptor, List<Interceptor> interceptorList,
            final HttpRequestHandler httpRequestHandler) {
        builder.connectTimeout(TIME_OUT_DEFAULT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_DEFAULT, TimeUnit.SECONDS)
                .addNetworkInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(httpRequestHandler.
                                onHttpRequestBefore(chain, chain.request()));
                    }
                });
        if (interceptorList != null && interceptorList.size() > 0) {
            for (Interceptor interceptorTemp : interceptorList) {
                builder.addInterceptor(interceptorTemp);
            }
        }
        okHttpClientConfig.configOkHttpClient(application, builder);
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
        return interceptor;
    }

    @Singleton
    @Provides
    RxCache provideRxCache(Application application, RxCacheConfig rxCacheConfig,
            @Named("RxCacheDirectory") File rxCacheDir) {
        RxCache.Builder builder = new RxCache.Builder();
        rxCacheConfig.configRxCache(application, builder);
        return builder.persistence(rxCacheDir, new GsonSpeaker());
    }

    /**
     * 给 RxCache 提供缓存路径
     * @param cacheDir
     * @return
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    File provideRxCacheDirectory(File cacheDir) {
        File cacheDirectory = new File(cacheDir, "RxCache");
        return FileUtils.makeDirs(cacheDirectory);
    }

    public interface RetrofitConfig {

        void configRetrofit(Context context, Retrofit.Builder builder);

        RetrofitConfig EMPTY_CONFIG = new RetrofitConfig() {
            @Override
            public void configRetrofit(Context context, Retrofit.Builder builder) {

            }
        };
    }

    public interface OkHttpClientConfig {

        void configOkHttpClient(Context context, OkHttpClient.Builder builder);

        OkHttpClientConfig EMPTY_CONFIG = new OkHttpClientConfig() {
            @Override
            public void configOkHttpClient(Context context, OkHttpClient.Builder builder) {

            }
        };
    }

    public interface RxCacheConfig {

        void configRxCache(Context context, RxCache.Builder builder);

        RxCacheConfig EMPTY_CONFIG = new RxCacheConfig() {
            @Override
            public void configRxCache(Context context, RxCache.Builder builder) {

            }
        };
    }
}
