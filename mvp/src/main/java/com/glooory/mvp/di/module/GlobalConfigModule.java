package com.glooory.mvp.di.module;

import android.app.Application;
import android.text.TextUtils;

import com.glooory.mvp.http.HttpRequestHandler;
import com.glooory.mvp.imageloader.BaseImageLoaderStrategy;
import com.glooory.mvp.imageloader.glide.GlideImageLoaderStrategy;
import com.glooory.mvp.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * Created by Glooory on 17/5/4.
 */
@Module
public class GlobalConfigModule {

    private HttpUrl mApiBaseUrl;
    private HttpRequestHandler mHttpRequestHandler;
    private List<Interceptor> mInterceptorList;
    private File mCacheFile;
    private BaseImageLoaderStrategy mImageLoaderStrategy;
    private HttpClientModule.RetrofitConfig mRetrofitConfig;
    private HttpClientModule.OkHttpClientConfig mOkHttpClientConfig;
    private HttpClientModule.RxCacheConfig mRxCacheConfig;
    private AppModule.GsonConfig mGsonConfig;

    private GlobalConfigModule(Builder builder) {
        this.mApiBaseUrl = builder.apiBaseUrl;
        this.mHttpRequestHandler = builder.httpRequestHandler;
        this.mInterceptorList = builder.interceptorList;
        this.mCacheFile = builder.cacheFile;
        this.mImageLoaderStrategy = builder.mImageLoaderStrategy;
        this.mRetrofitConfig = builder.retrofitConfig;
        this.mOkHttpClientConfig = builder.okHttpClientConfig;
        this.mRxCacheConfig = builder.rxCacheConfig;
        this.mGsonConfig = builder.gsonConfig;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Singleton
    @Provides
    List<Interceptor> provideInterceptorList() {
        return mInterceptorList;
    }

    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        return mApiBaseUrl;
    }

    @Singleton
    @Provides
    HttpRequestHandler provideHttpRequestHandler() {
        if (mHttpRequestHandler == null) {
            return HttpRequestHandler.EMPTY_HTTP_REQUEST_HANDLER;
        }
        return mHttpRequestHandler;
    }

    @Singleton
    @Provides
    File provideRxCacheFile(Application application) {
        if (mCacheFile == null) {
            return FileUtils.getCacheFile(application);
        }
        return mCacheFile;
    }

    @Singleton
    @Provides
    BaseImageLoaderStrategy provideImageLoaderStrategy() {
        // 图片加载默认使用 glide
        if (mImageLoaderStrategy == null) {
            return new GlideImageLoaderStrategy();
        }
        return mImageLoaderStrategy;
    }

    @Singleton
    @Provides
    HttpClientModule.RetrofitConfig provideRetrofitConfig() {
        if (mRxCacheConfig == null) {
            return HttpClientModule.RetrofitConfig.EMPTY_CONFIG;
        }
        return mRetrofitConfig;
    }

    @Singleton
    @Provides
    HttpClientModule.OkHttpClientConfig provideOkHttpClientConfig() {
        if (mOkHttpClientConfig == null) {
            return HttpClientModule.OkHttpClientConfig.EMPTY_CONFIG;
        }
        return mOkHttpClientConfig;
    }

    @Singleton
    @Provides
    HttpClientModule.RxCacheConfig provideRxCacheConfig() {
        if (mRxCacheConfig == null) {
            return HttpClientModule.RxCacheConfig.EMPTY_CONFIG;
        }
        return mRxCacheConfig;
    }

    @Singleton
    @Provides
    AppModule.GsonConfig provideGsonConfig() {
        if (mGsonConfig == null) {
            return AppModule.GsonConfig.EMPTY_CONFIG;
        }
        return mGsonConfig;
    }

    public static final class Builder {
        private HttpUrl apiBaseUrl;
        private HttpRequestHandler httpRequestHandler;
        private List<Interceptor> interceptorList = new ArrayList<>();
        private File cacheFile;
        private BaseImageLoaderStrategy mImageLoaderStrategy;
        private HttpClientModule.RetrofitConfig retrofitConfig;
        private HttpClientModule.OkHttpClientConfig okHttpClientConfig;
        private HttpClientModule.RxCacheConfig rxCacheConfig;
        private AppModule.GsonConfig gsonConfig;

        private Builder() {

        }

        public Builder baseUrl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new IllegalArgumentException("Base Url can not be empty!");
            }
            this.apiBaseUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder httpRequestHandler(HttpRequestHandler httpRequestHandler) {
            this.httpRequestHandler = httpRequestHandler;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            this.interceptorList.add(interceptor);
            return this;
        }

        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder retrofitConfig(HttpClientModule.RetrofitConfig retrofitConfig) {
            this.retrofitConfig = retrofitConfig;
            return this;
        }

        public Builder okHttpConfig(HttpClientModule.OkHttpClientConfig clientConfig) {
            this.okHttpClientConfig = clientConfig;
            return this;
        }

        public Builder rxCacheConfig(HttpClientModule.RxCacheConfig cacheConfig) {
            this.rxCacheConfig = cacheConfig;
            return this;
        }

        public Builder gsonConfig(AppModule.GsonConfig gsonConfig) {
            this.gsonConfig = gsonConfig;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }
    }
}
