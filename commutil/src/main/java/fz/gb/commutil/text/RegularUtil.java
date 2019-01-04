package fz.gb.commutil.text;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tp on 2018/12/22.
 */

public class RegularUtil {


    /**
     * 校验手机号码是否合格
     *
     * @param phoneNumber
     *            手机号码
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "^[1](([3-9])[0-9])[0-9]{8}$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

   

  

    /**
     * 获取edittext的内容
     *
     * @param et
     * @return
     */
    public static final String getEtContent(EditText et) {
        return et.getText().toString().trim();
    }

    /**
     * 判断edittext内容是否合法
     *
     * @param edt
     * @return
     */
    public static final boolean isEetEmpty(EditText... edt) {
        if (edt != null) {
            for (int i = 0; i < edt.length; i++) {
                EditText editText = edt[i];
                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取edittext的内容
     *
     * @param text
     * @return
     */
    public static final String getTextContent(TextView text) {
        return text.getText().toString().trim();
    }


    /**
     * 判断textview内容是否合法
     *
     * @param text
     * @return
     */
    public static final boolean isTextEmpty(TextView... text) {
        if (text != null) {
            for (int i = 0; i < text.length; i++) {
                TextView textView = text[i];
                if (TextUtils.isEmpty(textView.getText().toString().trim())) {
                    return true;
                }
            }
        }
        return false;
    }


}
