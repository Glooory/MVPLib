package com.glooory.mvp.imageloader;

import android.widget.ImageView;

/**
 * Created by Glooory on 17/5/8.
 * 图片加载配置信息的基类
 */

public class ImageConfig {

    protected String url;
    protected ImageView imageView;
    protected int placeHolder;
    protected int errorImage;

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public int getErrorImage() {
        return errorImage;
    }
}
