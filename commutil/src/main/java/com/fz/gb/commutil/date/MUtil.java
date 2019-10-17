package com.fz.gb.commutil.date;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MUtil {

	public static String getStringTime(long date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(date));
    }

    public static String getStringTime(long date, String type){
        return new SimpleDateFormat(type).format(new Date(date));
	}

	public static String getStringTime(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
	}

	public static String getStringTime(String type,Date date) {
		return new SimpleDateFormat(type).format(date);
	}
	
	public static String getNoewStringTime() {  
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
	}
	
	public static String getStringDate(long date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(date));
	}
	public static String getNowDate(String s) {
		return new SimpleDateFormat(s).format(new Date());
	}
 
    public static String getStringMonth(){
        return new SimpleDateFormat("yyyy-MM").format(new Date());
    }
	
	public static String getTime(String s) {  
		return new SimpleDateFormat("MM-dd").format(new Date(s));
	}

	public static String getTimeStamp() {  
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

    public static String format1ToFormat2(String strDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format2date = null;
        try{
            Date date = sdf.parse(strDate);
            format2date = new SimpleDateFormat("yyyy/MM/dd").format(date);
        } catch(ParseException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return format2date;

    }

    public static Date strDate2ObjDate(String format, String strDate){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try{
            date = sdf.parse(strDate);
        } catch(ParseException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }
    Context mContext = null;  
 
}
