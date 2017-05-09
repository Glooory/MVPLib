package com.glooory.mvp.mvp;

import com.glooory.mvp.integration.IRepositoryManager;

/**
 * Created by Glooory on 17/5/9.
 */

public class BaseModel implements IModel {

    protected IRepositoryManager mRepositoryManager;

    public BaseModel(IRepositoryManager repositoryManager) {
        mRepositoryManager = repositoryManager;
    }

    @Override
    public void onDestroy() {
        mRepositoryManager = null;
    }

}
