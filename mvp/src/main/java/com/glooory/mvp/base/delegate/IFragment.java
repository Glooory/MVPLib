package com.glooory.mvp.base.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glooory.mvp.di.component.AppComponent;

/**
 * Created by Glooory on 17/5/5.
 */

public interface IFragment {

    void setupFragmentComponent(AppComponent appComponent);

    View initView(LayoutInflater inflater, ViewGroup container);

    void initData();
}
