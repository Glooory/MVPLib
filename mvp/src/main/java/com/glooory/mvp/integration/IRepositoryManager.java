package com.glooory.mvp.integration;

/**
 * Created by Glooory on 17/5/5.
 */

public interface IRepositoryManager {

    /**
     * 注入 Retrofit Service ，
     * 在 {@link ConfigModule#registerComponents(Context, IRepositoryManager)} 中进行注入
     * @param services
     */
    void injectRetrofitServices(Class<?>... services);

    /**
     * 注入 RxCache Service ，
     * 在 {@link ConfigModule#registerComponents(Context, IRepositoryManager)} 中进行注入
     * @param services
     */
    void injectRxCacheServices(Class<?>... services);

    /**
     * 根据传入的 Class 获取对应的 Retrofit Service
     * @param service
     * @param <T>
     * @return
     */
    <T> T obtainRetrofitService(Class<T> service);

    /**
     * 根据传入的 Class 获取对应的 RxCache Service
     * @param service
     * @param <T>
     * @return
     */
    <T> T obtainRxCacheService(Class<T> service);
}
