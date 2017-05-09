package com.glooory.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glooory.mvp.base.delegate.IFragment;
import com.glooory.mvp.mvp.IPresenter;
import com.trello.rxlifecycle.components.RxFragment;

import javax.inject.Inject;

/**
 * Created by Glooory on 17/5/9.
 */

public abstract class BaseFragment<P extends IPresenter> extends RxFragment implements IFragment {

    protected final String TAG = this.getClass().getSimpleName();
    @Inject
    P mPresenter;

    public BaseFragment() {
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        return initView(inflater, container);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        this.mPresenter = null;
        super.onDestroy();
    }
}
