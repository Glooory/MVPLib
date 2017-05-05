package com.glooory.mvp.integration;

import android.content.Context;

import com.glooory.mvp.util.Preconditions;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 * 用来管理网络请求层，以及数据缓存层，
 * 需要在 {@link ConfigModule} 的实现类中先 inject 需要的服务
 * Created by Glooory on 17/5/5.
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {

    private Retrofit mRetrofit;
    private RxCache mRxCache;
    private final Map<String, Object> mRetrofitServiceCache = new LinkedHashMap<>();
    private final Map<String, Object> mRxCacheServiceCache = new LinkedHashMap<>();

    @Inject
    public RepositoryManager(Retrofit retrofit, RxCache rxCache) {
        this.mRetrofit = retrofit;
        this.mRxCache = rxCache;
    }

    /**
     * 注入 Retrofit Service , 在 {@link ConfigModule#registerComponents(Context, IRepositoryManager)}
     * 中进行注入
     * @param services
     */
    @Override
    public void injectRetrofitServices(Class<?>... services) {
        for (Class<?> service : services) {
            if (mRetrofitServiceCache.containsKey(service.getName())) {
                continue;
            }
            mRetrofitServiceCache.put(service.getName(), mRetrofit.create(service));
        }
    }

    /**
     * 注入 RxCache Service， 在 {@link ConfigModule#registerComponents(Context, IRepositoryManager)}
     * 中进行注入
     * @param services
     */
    @Override
    public void injectRxCacheServices(Class<?>... services) {
        for (Class<?> service : services) {
            if (mRxCacheServiceCache.containsKey(service.getName())) {
                continue;
            }
            mRxCacheServiceCache.put(service.getName(), mRxCache.using(service));
        }
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit Service
     *
     * @param service
     * @param <T>
     * @return
     */
    @Override
    public <T> T obtainRetrofitService(Class<T> service) {
        Preconditions.checkState(mRetrofitServiceCache.containsKey(service.getName()),
                "You must call injectRetrofitService(%s) in ConfigModule before you use it",
                service.getName(), service.getSimpleName());
        return (T) mRxCacheServiceCache.get(service.getName());
    }

    /**
     * 根据传入的 Class 获取对应 RxCache Service
     * @param service
     * @param <T>
     * @return
     */
    @Override
    public <T> T obtainRxCacheService(Class<T> service) {
        Preconditions.checkState(mRxCacheServiceCache.containsKey(service.getName()),
                "You must call injectRetrofitService(%s) in ConfigModule before you use it",
                service.getName(), service.getSimpleName());
        return (T) mRxCacheServiceCache.get(service.getName());
    }
}
