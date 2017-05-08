package com.glooory.mvp.base.delegate;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Glooory on 17/5/5.
 */

public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity mActivity;
    private IActivity iActivity;
    private Unbinder mUnbinder;

    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity.setContentView(iActivity.initView());
        mUnbinder = ButterKnife.bind(mActivity);
        iActivity.setupActivityComponent(((App) mActivity.getApplication()).getAppComponent());
        iActivity.initData();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        this.mUnbinder = null;
        this.iActivity = null;
        this.mActivity = null;
    }
}
