package com.fz.gb.commutil.text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 剪贴板操作工具类
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/10/16 18:20
 */
public class ClipboardUtil {

    /**
     * 复制文本到剪贴板
     *
     * @author dingpeihua
     * @date 2019/10/16 18:19
     * @version 1.0
     */
    public static void copyText(Context context, CharSequence text) {
        ClipboardManager c = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text label", text);
        c.setPrimaryClip(clipData);
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @author dingpeihua
     * @date 2019/10/16 18:22
     * @version 1.0
     */
    public static CharSequence pasteText(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = cmb.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(context);
        }
        return null;
    }
}
