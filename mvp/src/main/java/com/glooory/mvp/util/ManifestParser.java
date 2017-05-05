package com.glooory.mvp.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.glooory.mvp.integration.ConfigModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glooory on 17/5/5.
 */

public class ManifestParser {

    private static final String MODULE_VALUE = "ConfigModule";

    private final Context mContext;

    public ManifestParser(Application context) {
        this.mContext = context;
    }

    public List<ConfigModule> parse() {
        List<ConfigModule> configModuleList = new ArrayList<>();
        try {
            ApplicationInfo applicationInfo = mContext.getPackageManager().getApplicationInfo(
                    mContext.getPackageName(), PackageManager.GET_META_DATA
            );
            if (applicationInfo != null) {
                for (String key : applicationInfo.metaData.keySet()) {
                    if (MODULE_VALUE.equals(applicationInfo.metaData.get(key))) {
                        configModuleList.add(parseModule(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse ConfigModule", e);
        }

        return configModuleList;
    }

    private ConfigModule parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find ConfigModule implementation", e);
        }

        Object module;
        try {
            module = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate ConfigModule implementation for " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate ConfigModule implementation for " + clazz, e);
        }

        if (!(module instanceof ConfigModule)) {
            throw new RuntimeException("Expected instanceof ConfigModule, but found: " + module);
        }

        return (ConfigModule) module;
    }
}
