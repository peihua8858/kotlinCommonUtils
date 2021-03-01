package com.fz.commutils.demo.model;

import com.fz.network.params.VpRequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * 公共基本请求参数
 * 1、支持公共参数与接口参数分离，即通过{@link #hasData}控制将{@link #urlParams}中的参数拼接到data字段中
 * 2、支持一级参数列表，即通过设置{@link #hasData} 为false时，控制将{@link #urlParams}中的参数和
 * {@link #publicParams}中的参数合并组成无data级字段参数列表
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/9/5 15:30
 */
public class RequestParam extends VpRequestParams {
    /**
     * 控制是否将{@link #urlParams}中的参数拼接到data字段中
     */
    private boolean hasData = true;
    final ConcurrentHashMap<String, Object> publicParams = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    public RequestParam() {
        super();
    }

    public RequestParam(String key, Object value) {
        this.publicParams.put(key, value);
    }

    public RequestParam(HashMap<String, String> publicParams) {
        this(true, publicParams);
    }

    public RequestParam(boolean hasData, HashMap<String, String> publicParams) {
        this.hasData = hasData;
        this.publicParams.putAll(publicParams);
    }

    public RequestParam(boolean hasData, boolean isReadCache) {
        super(isReadCache);
        this.hasData = hasData;
    }


    boolean checkValue(Object value) {
        if (value instanceof org.json.JSONArray || value instanceof org.json.JSONObject) {
            throw new IllegalArgumentException("Value can not be org.json.JSONArray or org.json.JSONObject");
        }
        return true;
    }


    private void clearNull() {
        Iterator<String> it = urlParams.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object value = urlParams.get(key);
            if (value == null) {
                urlParams.remove(key);
            }
        }
    }

    @Override
    public MultipartBody createFileRequestBody() {
        urlParams.putAll(publicParams);
        return super.createFileRequestBody();
    }


    public static final RequestBody buildRequestBody(MediaType mediaType, Map<String, Object> map) {
        //补全请求地址
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        Set<String> keys = map.keySet();
        //追加参数
        for (String key : keys) {
            final Object object = map.get(key);
            if (object instanceof File) {
                final File file = (File) object;
                builder.addFormDataPart(key, file.getName(), RequestBody.create(mediaType, file));
            } else {
                builder.addFormDataPart(key, object.toString());
            }
        }
        return builder.build();
    }
}