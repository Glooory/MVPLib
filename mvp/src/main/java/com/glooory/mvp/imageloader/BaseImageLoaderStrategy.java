package com.glooory.mvp.imageloader;

import android.content.Context;

/**
 * Created by Glooory on 17/5/8.
 */

public interface BaseImageLoaderStrategy<T extends ImageConfig> {

    void loadImage(Context context, T config);

    void clear(Context context, T config);
}
