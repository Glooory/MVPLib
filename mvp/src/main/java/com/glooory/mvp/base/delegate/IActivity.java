package com.glooory.mvp.base.delegate;

import com.glooory.mvp.di.component.AppComponent;

/**
 * Created by Glooory on 17/5/5.
 */

public interface IActivity {

    void setupActivityComponent(AppComponent appComponent);

    int initView();

    void initData();

    /**
     * 此 Activity 是否会使用 Fragment，
     * 框架会根据这个属性判断是否注册 {@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回 false，则意味着此 Activity 不需要绑定 Fragment，
     * 此时该 Activity 中绑定集继承与 {@link com.glooory.mvp.base.BaseFragment} 的 Fragment将不起任何作用
     * @return
     */
    boolean useFragment();
}
