package fz.gb.commutil.uri;

import android.net.Uri;

/**
 * 项目名称:Rosegal
 * 创建人：Created by  pengzhijun
 * 创建时间:2018/8/3 9:07
 * 类描述：
 */
public class UriUtil {

    public static String getUriParameter(String url, String key) {
        try {
            Uri uri = Uri.parse(url);
            return getUriParameter(uri,key);
        } catch (Exception e) {
           return null;
        }
    }

    public static String getUriParameter(Uri uri,String key){
        String parameter=null;
        try {
            parameter = uri.getQueryParameter(key);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return parameter;
    }

    public static String addUriParameter(String url,String key,String value){
        if(url==null){
            return null;
        }
        if(url.contains("?")){
            url+="&"+key+"="+value;
        }else{
            url+="?"+key+"="+value;
        }
        return url;
    }
}
