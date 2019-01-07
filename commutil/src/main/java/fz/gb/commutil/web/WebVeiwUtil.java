package fz.gb.commutil.web;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 项目名称:Rosegal
 * 创建人：Created by  pengzhijun
 * 创建时间:2019/1/4 10:03
 * 类描述：统一webview配置
 */
public class WebVeiwUtil {

    /**
     *
     * @param context
     * @param progressWebView
     * @param isRelease 是否正式环境
     */
    public static void setWebViewConfig(Context context, WebView progressWebView,boolean isRelease){
        if(progressWebView==null){
            return;
        }
        progressWebView.clearCache(true);
        progressWebView.setBackgroundColor(Color.WHITE);
        progressWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        progressWebView.clearSslPreferences();
        progressWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebSettings webSettings = progressWebView.getSettings();
        //支持缩放
        webSettings.setSupportZoom(true);
        // 缩放按钮
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(context.getDir("appcache", 0).getPath());
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setDisplayZoomControls(false);
        if (!isRelease && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }
}
