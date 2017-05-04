package com.glooory.mvp.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Glooory on 17/5/4.
 */

public class FileUtils {

    public static File getCacheFile(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = null;
            file = context.getExternalCacheDir();
            if (file == null) {
                file = new File(getCacheFilePath(context));
                makeDirs(file);
            }
            return file;
        } else {
            return context.getCacheDir();
        }
    }

    public static String getCacheFilePath(Context context) {
        String packageName = context.getPackageName();
        return "/mnt/sdcard/" + packageName;
    }

    public static File makeDirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

}
