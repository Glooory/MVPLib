package com.glooory.mvp.base;

import com.glooory.mvp.base.delegate.IActivity;
import com.glooory.mvp.mvp.IPresenter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

/**
 * Created by Glooory on 17/5/9.
 */

public abstract class BaseActivity<P extends IPresenter> extends RxAppCompatActivity
        implements IActivity {

    protected final String TAG = this.getClass().getSimpleName();
    @Inject
    P mPresenter;

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mPresenter = null;
        super.onDestroy();
    }

    /**
     * 这个 Activity 是否使用 Fragment，框架会根据这个属性判断是否注册
     * {@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回 false，则意味着这个 Activity 不需要绑定 Fragment，那你再在这个 Activity 中绑定
     * 继承于 {@link com.glooory.mvp.base.BaseFragment} 的 Fragment 将不起任何作用
     * @return
     */
    @Override
    public boolean useFragment() {
        return true;
    }
}
