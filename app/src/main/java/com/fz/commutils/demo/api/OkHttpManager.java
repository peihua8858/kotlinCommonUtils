package com.fz.commutils.demo.api;

import com.fz.commutils.demo.MainApplication;
import com.fz.okhttp.OkHttpWrapper;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class OkHttpManager {
    public synchronized static OkHttpClient imageOkHttpClient() {
        OkHttpWrapper httpClient = OkHttpWrapper.newBuilder(MainApplication.getContext())
                .writeTimeout(45_000, TimeUnit.MILLISECONDS)
                .readTimeout(45_000, TimeUnit.MILLISECONDS)
                .connectTimeout(45_000, TimeUnit.MILLISECONDS)
                .setEnabledHttpLog(true)
                .hostnameVerifier((hostname, session) -> true);
        socketFactory(httpClient, true);
        return httpClient.build();
    }

    public synchronized static OkHttpClient okHttpClient() {
        return buildOkHttpClient();
    }

    public static void socketFactory(OkHttpWrapper httpClient, boolean isIgnore) {
        //Android 5.0 以下或者测试信任所有证书，方便抓取接口
        if (isIgnore ) {
            //定义一个信任所有证书的TrustManager
            final X509TrustManager trustAllCert = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            };
            httpClient.hostnameVerifier((hostname, session) -> true);
        }
    }

    public static OkHttpClient buildOkHttpClient() {
        OkHttpWrapper httpClient = OkHttpWrapper.newBuilder(MainApplication.getContext())
                .writeTimeout(30_000, TimeUnit.MILLISECONDS)
                .readTimeout(30_000, TimeUnit.MILLISECONDS)
                .connectTimeout(30_000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .timeoutInterceptor()
                .setEnabledHttpLog(true);
        socketFactory(httpClient, false);
        return httpClient.build();
    }
}
