package com.glooory.mvp.di.module;

import android.app.Application;
import android.text.TextUtils;

import com.glooory.mvp.http.HttpRequestHandler;
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

    private GlobalConfigModule(Builder builder) {
        this.mApiBaseUrl = builder.mApiBaseUrl;
        this.mHttpRequestHandler = builder.mHttpRequestHandler;
        this.mInterceptorList = builder.mInterceptorList;
        this.mCacheFile = builder.mCacheFile;
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
        return mHttpRequestHandler == null ? HttpRequestHandler.EMPTY_HTTP_REQUEST_HANDLER
                : mHttpRequestHandler;
    }

    @Singleton
    @Provides
    File provideRxCacheFile(Application application) {
        return mCacheFile == null ? FileUtils.getCacheFile(application) : mCacheFile;
    }

    public static final class Builder {
        private HttpUrl mApiBaseUrl;
        private HttpRequestHandler mHttpRequestHandler;
        private List<Interceptor> mInterceptorList = new ArrayList<>();
        private File mCacheFile;

        private Builder() {

        }

        public Builder baseUrl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new IllegalArgumentException("Base Url can not be empty!");
            }
            this.mApiBaseUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder httpRequestHandler(HttpRequestHandler httpRequestHandler) {
            this.mHttpRequestHandler = httpRequestHandler;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            this.mInterceptorList.add(interceptor);
            return this;
        }

        public Builder cacheFile(File cacheFile) {
            this.mCacheFile = cacheFile;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }
    }
}
