package fz.gb.commutil.device;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * @author puhanhui
 * @version 1.0
 * @date 2016/10/11
 * @since 1.0
 */

public class DeviceUtil {
    /**
     * 获取设备的唯一号码，用于区别设备
     *
     * @return ANDROID_ID和serial组合的字符串，基本上可以确保不同的设备字符串不同
     */
    public static String getDeviceID(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings
                .Secure.ANDROID_ID);
        return TextUtils.isEmpty(androidID)?"":androidID + (Build.SERIAL.equals(Build.UNKNOWN)? "" : Build.SERIAL);
    }
}

