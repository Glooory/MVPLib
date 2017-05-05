package com.glooory.mvp.integration;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.glooory.mvp.base.delegate.AppDelegate;
import com.glooory.mvp.di.module.GlobalConfigModule;

import java.util.List;

/**
 *
 * Created by Glooory on 17/5/5.
 */

public interface ConfigModule {

    /**
     * 使用 {@link GlobalConfigModule.Builder} 给框架配置一些参数
     * @param context
     * @param builder
     */
    void applyOptions(Context context, GlobalConfigModule.Builder builder);

    /**
     * 使用 {@link IRepositoryManager} 给框架注入网络请求和缓存服务等
     *
     * @param context
     * @param repositoryManager
     */
    void registerComponents(Context context, IRepositoryManager repositoryManager);

    void injectAppLifecycle(Context context, List<AppDelegate.Lifecycle> lifecycleList);

    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycleCallbacksesList);

    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycleCallbacksList);
}
