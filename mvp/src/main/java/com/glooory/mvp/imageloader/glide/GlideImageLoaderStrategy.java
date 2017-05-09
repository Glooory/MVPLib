package com.glooory.mvp.imageloader.glide;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.glooory.mvp.imageloader.BaseImageLoaderStrategy;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Glooory on 17/5/8.
 */

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<GlideImageConfig> {

    @Override
    public void loadImage(Context context, GlideImageConfig config) {
        if (context == null) {
            throw new IllegalStateException("Context is required");
        }
        if (config == null) {
            throw new IllegalStateException("GlideImageConfig is required");
        }
        if (TextUtils.isEmpty(config.getUrl())) {
            throw new IllegalStateException("GlideImageConfig is required");
        }
        if (config.getImageView() == null) {
            throw new IllegalStateException("ImageView is required");
        }

        RequestManager requestManager;

        requestManager = Glide.with(context);

        DrawableRequestBuilder<String> requestBuilder = requestManager.load(config.getUrl())
                .crossFade()
                .centerCrop();

        switch (config.getCacheStrategy()) {
            case 0:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case 1:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case 2:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE);
                break;
            case 3:
                requestBuilder.diskCacheStrategy(DiskCacheStrategy.RESULT);
                break;
        }

        if (config.getTransformation() != null) {
            requestBuilder.transform(config.getTransformation());
        }

        if (config.getPlaceHolder() != 0) {
            requestBuilder.placeholder(config.getPlaceHolder());
        }

        if (config.getErrorImage() != 0) {
            requestBuilder.error(config.getErrorImage());
        }

        requestBuilder.into(config.getImageView());
    }

    @Override
    public void clear(Context context, GlideImageConfig config) {
        if (context == null) {
            throw new IllegalStateException("Context is required");
        }
        if (config == null) {
            throw new IllegalStateException("GlideImageConfig is required");
        }

        if (config.getImageviews() != null && config.getImageviews().length > 0) {
            for (ImageView imageView : config.getImageviews()) {
                Glide.clear(imageView);
            }
        }

        if (config.getTargets() != null && config.getTargets().length > 0) {
            for (Target target : config.getTargets()) {
                Glide.clear(target);
            }
        }

        if (config.isClearDiskCache()) {
            Observable.just(0)
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            Glide.get(context).clearDiskCache();
                        }
                    });
        }

        if (config.isClearMemory()) {
            Glide.get(context).clearMemory();
        }
    }

}
