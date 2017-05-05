package com.glooory.mvp.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;

/**
 * Created by Glooory on 17/5/5.
 */

public interface FragmentDelegate extends Serializable {

    String FRAGMENT_DELEGATE = "fragment_delegate";

    void onAttach(Context context);

    void onCreate(Bundle savedInstanceState);

    void onCreateView(View view, Bundle savedInstanceState);

    void onActivityCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroyView();

    void onDestroy();

    void onDetach();
}
