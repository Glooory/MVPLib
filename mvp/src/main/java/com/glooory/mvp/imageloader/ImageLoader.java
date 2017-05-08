package com.glooory.mvp.imageloader;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Glooory on 17/5/8.
 */
@Singleton
public final class ImageLoader {

    private BaseImageLoaderStrategy mStrategy;

    @Inject
    public ImageLoader(BaseImageLoaderStrategy strategy) {
        mStrategy = strategy;
    }

    public <T extends ImageConfig> void loadImage(Context context, T config) {
        this.mStrategy.loadImage(context, config);
    }

    public <T extends ImageConfig> void clear(Context context, T config) {
        this.mStrategy.clear(context, config);
    }

    public void setImageLoaderStrategy(BaseImageLoaderStrategy strategy) {
        this.mStrategy = strategy;
    }
}
