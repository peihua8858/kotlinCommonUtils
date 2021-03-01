package com.fz.commutils.demo.api;

import com.fz.common.utils.ResponseUtils;
import com.fz.commutils.demo.model.HttpResponse;
import com.fz.commutils.demo.model.HttpResponseAdapter;
import com.fz.network.remote.BasicDataManager;


/**
 * @author tp
 * @date 2018/07/31
 * 管理product所有的接口列表
 */
public class ApiManager extends BasicDataManager {

    protected ApiManager() {
        super(OkHttpManager.okHttpClient(), "");
    }

    static class InnerHelper {
        static ApiManager dataManager = new ApiManager();
    }

    public static ApiManager newInstance() {
        return InnerHelper.dataManager;
    }

    public static void removeAllApi() {
        newInstance().removeAllRetrofit();
        newInstance().removeAllService();
        newInstance().newRetrofit(OkHttpManager.okHttpClient(), "");
    }


    /**
     * 商品模块、用户中心模块及社区模块由于数据不规范需要自定义处理
     *
     * @author dingpeihua
     * @date 2019/1/11 15:52
     * @version 1.0
     */
    @Override
    public <T> T createApi(String host, Class<T> tClass) {
        return createApi(host, tClass, HttpResponse.class, new HttpResponseAdapter());
    }

    @Override
    public <T> T createApi(Class<T> clazz) {
        return createApi("", clazz);
    }

    public static UserApi userApi() {
        return newInstance().createApi(UserApi.class);
    }
}
