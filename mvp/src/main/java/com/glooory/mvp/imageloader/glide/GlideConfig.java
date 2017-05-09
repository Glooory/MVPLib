package com.glooory.mvp.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.glooory.mvp.base.delegate.App;
import com.glooory.mvp.di.component.AppComponent;
import com.glooory.mvp.http.OkHttpUrlLoader;
import com.glooory.mvp.util.FileUtils;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Glooory on 17/5/8.
 */

public class GlideConfig implements GlideModule {

    // 图片缓存文件的最大值为 100Mb
    public static final int IMAGE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskCache.Factory() {

            @Override
            public DiskCache build() {
                AppComponent appComponent = ((App) context.getApplicationContext()).getAppComponent();
                return DiskLruCacheWrapper.get(FileUtils.makeDirs(
                        new File(appComponent.rxCacheFile(), "Glide")), IMAGE_DISK_CACHE_MAX_SIZE);
            }
        });

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultBitmapPoolSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // Glide 默认使用 HttpURLConnection 做网络请求，在这里切换成 OkHttp
        AppComponent appComponent = ((App) context.getApplicationContext()).getAppComponent();
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(appComponent.okHttpClient()));
    }

}
