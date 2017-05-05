package com.glooory.mvp.base.delegate;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by Glooory on 17/5/5.
 */

public interface ActivityDelegate extends Serializable {

    String ACTIVITY_DELEGATE = "activity_delegate";

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroy();
}
