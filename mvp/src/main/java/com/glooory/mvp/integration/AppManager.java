package com.glooory.mvp.integration;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.orhanobut.logger.Logger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Glooory on 17/5/3.
 * Activity 的管理类
 */
@Singleton
public class AppManager {

    private Application mApplication;
    // 管理所有的 Activity
    public List<Activity> mActivityList;
    // 前台 Activity
    private Activity mCurrentActivity;

    @Inject
    public AppManager(Application application) {
        this.mApplication = application;
    }

    /**
     * 让前台的 Activity 启动下一个 Activity
     * @param intent
     */
    public void startActivity(Intent intent) {
        if (getCurrentActivity() == null) {
            Logger.d("Current Activity is null when try to start another Activity");
            // 没有前台 Activity ，使用 new_task 模式启动 Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mApplication.startActivity(intent);
            return;
        }
        getCurrentActivity().startActivity(intent);
    }

    /**
     * 让前台的 Activity 启动下一个 Activity
     * @param activityClass
     */
    public void startActivity(Class activityClass) {
        startActivity(new Intent(mApplication, activityClass));
    }

    /**
     * 将 Activity 实例添加到管理 Activity 的集合中
     * @param activity
     */
    public void addToActivityList(Activity activity) {
        if (mActivityList == null) {
            mActivityList = new LinkedList<>();
        }
        synchronized (AppManager.class) {
            if (!mActivityList.contains(activity)) {
                mActivityList.add(activity);
            }
        }
    }

    /**
     * 将 Activity 从管理集合中移除
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (mActivityList == null) {
            Logger.d("ActivtityList is null when try to remove activity from it");
            return;
        }
        synchronized (AppManager.class) {
            if (mActivityList.contains(activity)) {
                mActivityList.remove(activity);
            }
        }
    }

    /**
     * 关闭某个 Activity
     * @param activityClass
     */
    public void killActivity(Class<?> activityClass) {
        if (mActivityList == null) {
            Logger.d("ActivityList is null when try to kill activity");
            return;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activityClass)) {
                activity.finish();
            }
        }
    }

    /**
     * 判断指定的 Activity 是否存活
     * @param activity
     * @return
     */
    public boolean isActivityInstanchAlive(Activity activity) {
        if (mActivityList == null) {
            return false;
        }
        return mActivityList.contains(activity);
    }

    public boolean isActivityInstanceAlive(Class<?> activityClass) {
        if (mActivityList == null) {
            return false;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activity)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 杀死管理结合中的所有 Activity
     */
    public void killAllActivities() {
        if (mActivityList == null) {
            return;
        }
        Iterator<Activity> iterator = mActivityList.iterator();
        while (iterator.hasNext()) {
            iterator.next().finish();
            iterator.remove();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mActivityList != null) {
            mActivityList.clear();
        }
        mActivityList = null;
        mCurrentActivity = null;
        mApplication = null;
    }

    public void exitApp() {
        killAllActivities();
        release();
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
    }

    /**
     * 获取前台 Activity
     * @return
     */
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    /**
     * 获取存储所有未被销毁的 Activity 的集合
     * @return
     */
    public List<Activity> getActivityList() {
        if (mActivityList == null) {
            mActivityList = new LinkedList<>();
        }
        return mActivityList;
    }
}
