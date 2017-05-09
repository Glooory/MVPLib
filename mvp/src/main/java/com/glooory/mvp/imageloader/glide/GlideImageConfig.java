package com.glooory.mvp.imageloader.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.Target;
import com.glooory.mvp.imageloader.ImageConfig;

/**
 * Created by Glooory on 17/5/8.
 */

public class GlideImageConfig extends ImageConfig {

    /**
     * 缓存策略
     * 0 对应 DiskCacheStrategy.all
     * 1 对应 DiskCacheStrategy.NONE
     * 2 对应 DiskCacheStrategy.SOURCE
     * 3 对应 DiskCacheStrategy.RESULT
     */
    private int cacheStrategy;
    private BitmapTransformation transformation;
    private Target[] targets;
    private ImageView[] imageviews;
    private boolean isClearMemory; // 清楚内存缓存
    private boolean isClearDiskCache; // 清楚磁盘缓存

    private GlideImageConfig(Builder builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeHolder = builder.placeHolder;
        this.errorImage = builder.errorImage;
        this.cacheStrategy = builder.cacheStrategy;
        this.transformation = builder.transformation;
        this.targets = builder.targets;
        this.imageviews = builder.imageViews;
        this.isClearMemory = builder.isClearMemory;
        this.isClearDiskCache = builder.isClearDiskCache;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getCacheStrategy() {
        return cacheStrategy;
    }

    public BitmapTransformation getTransformation() {
        return transformation;
    }

    public Target[] getTargets() {
        return targets;
    }

    public ImageView[] getImageviews() {
        return imageviews;
    }

    public boolean isClearMemory() {
        return isClearMemory;
    }

    public boolean isClearDiskCache() {
        return isClearDiskCache;
    }

    public static final class Builder {

        private String url;
        private ImageView imageView;
        private int placeHolder;
        private int errorImage;
        private int cacheStrategy;
        private BitmapTransformation transformation;
        private Target[] targets;
        private ImageView[] imageViews;
        private boolean isClearMemory;
        private boolean isClearDiskCache;

        private Builder() {
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder placeHolder(int placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        public Builder errorImage(int errorImage) {
            this.errorImage = errorImage;
            return this;
        }

        public Builder cacheStrategy(int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public Builder transformation(BitmapTransformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public Builder targets(Target... targets) {
            this.targets = targets;
            return this;
        }

        public Builder imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        public Builder isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        public Builder isClearDiskMemory(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }

        public GlideImageConfig build() {
            return new GlideImageConfig(this);
        }
    }

}
