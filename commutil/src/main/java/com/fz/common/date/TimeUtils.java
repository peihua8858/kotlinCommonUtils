package com.fz.common.date;

import android.annotation.SuppressLint;
import android.content.Context;

import com.fz.common.utils.ParseUtilKt;
import com.fz.commutil.R;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {
    public static final String TAG = "TimeUtils";
    /**
     * 一秒
     */
    public static final long ONE_SECOND = 1000L;
    /**
     * 一分钟
     */
    public static final long ONE_MINUTE = 60 * ONE_SECOND;
    /**
     * 一个小时
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    /**
     * 一天
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;
    /**
     * 一周
     */
    public static final long ONE_WEEK = 7 * ONE_DAY;
    /**
     * 一个月
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;
    /**
     * 一年
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;
    /**
     * 7个小时
     */
    public static final long TIME_OFFSET = ONE_HOUR * 7L;
    private static final String AGO = "ago";
    private static final String SECOND = "second";
    private static final String SECONDS = "seconds";
    private static final String MINUTE = "minute";
    private static final String MINUTES = "minutes";
    private static final String HOUR = "hour";
    private static final String HOURS = "hours";
    private static final String DAY = "day";
    private static final String WEEK = "week";
    private static final String WEEKS = "weeks";
    private static final String DAYS = "days";
    private static final String MONTH = "month";
    private static final String MONTHS = "months";
    private static final String YEAR = "year";
    private static final String YEARS = "years";
    private static final String YESTERDAY = "yesterday";
    public static final String DATE_FORMAT_AM = "MM/dd/yyyy HH:mm";
    public static final String DATE_FORMAT_M_TO_D = "dd/MM HH:mm";
    public static final String DATE_FORMAT_ALL = "yyyy-MM-dd HH:mm:ss";
    //Jan.04,2019 10:00
    public static final String DATE_FORMAT_FLASH_SALE = "MMM dd,yyyy HH:mm";

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT_US = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT_HOUR = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aaa", Locale.US);
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT_TIME_US = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.US);


    /**
     * 检查当前传入的时间戳是不是毫秒级
     *
     * @author dingpeihua
     * @date 2018/6/5 19:49
     * @version 1.0
     */
    public static long checkTime(long timeInMillis) {
        return checkTime(timeInMillis, 13);
    }

    public static long checkTime(long timeInMillis, int maxBit) {
        StringBuilder time = new StringBuilder(ParseUtilKt.toString(timeInMillis));
        int length = time.length();
        //如果小于maxBit位，则不是毫秒
        if (length < maxBit) {
            int difference = maxBit - length;
            for (int i = 0; i < difference; i++) {
                time.append("0");
            }
            return ParseUtilKt.toLong(time.toString());
        }
        return timeInMillis;
    }

    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        timeInMillis = checkTime(timeInMillis);
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param format
     * @return
     */
    public static String format(long timeInMillis, String format) {
        timeInMillis = checkTime(timeInMillis);
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 根据unix时间戳来计算本地的时间
     *
     * @param timeStamp
     * @return
     * @date 2014-8-9
     * @author 郑伍
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeFromUnixTimeStamp(String timeStamp, String dateFormat) {
        long time = Long.parseLong(timeStamp);
        long localTime = time * 1000 - TIME_OFFSET;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localTime);
        return new SimpleDateFormat(dateFormat).format(calendar.getTime());
    }

    /**
     * 服务器上定义零时区是  获取的 标准 UTC时间上在加了个服务器所有时区的时区(7个小时)
     * 而且服务那边判断的时候是加上一个 8个小时来的，所以我们相应的要减掉
     *
     * @return
     * @date 2014-8-22
     * @author 郑伍
     */
    public static String getTimeOffsetInMillis() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tztz = TimeZone.getTimeZone("GMT");
        calendar.setTimeZone(tztz);
        long offer_time = calendar.getTimeInMillis() / 1000;
        return Long.toString(offer_time);
    }

    /**
     * 将日期时间格式转成时间戳
     *
     * @param dateTime 日期时间字符串
     * @param format   当前时间的格式化字符串
     * @return 解析成功返回正确时间戳，否则返回当前系统时间戳
     * @author dingpeihua
     * @date 2018/12/21 10:41
     * @version 1.0
     */
    public static long format(String dateTime, String format) {
        try {
            Date date = new SimpleDateFormat(format).parse(dateTime);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static String format(String format, long timeInMillis) {
        Locale locale = Locale.getDefault();
        DateFormatSymbols sym = DateFormatSymbols.getInstance(locale);
        sym.setMonths(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        sym.setShortMonths(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        SimpleDateFormat df = new SimpleDateFormat(format, locale);
        df.setDateFormatSymbols(sym);
        timeInMillis = checkTime(timeInMillis);
        return df.format(new Date(timeInMillis));
    }

    /**
     * 秒数转HH:mm:ss
     *
     * @param time 秒数
     * @return HH:mm:ss格式
     */
    public static String secondsToHMS(int time) {
        String timeStr;
        int hour, minute, second;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10) {
            retStr = "0" + i;
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    /**
     * 传入unix时间戳来计算本地的时间
     *
     * @param seconds 日期时间秒数字符串
     * @return
     */
    public static String format(String seconds) {
        try {
            long timeMillis = ParseUtilKt.toLong(seconds);
            return format(timeMillis);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 传入unix时间戳来计算本地的时间
     *
     * @param seconds 日期时间秒数
     * @return
     */
    public static String format(long seconds) {
        return format((Context) null, seconds * 1000);
    }

    public static String format(String seconds, DateFormat format) {
        long timeMillis = ParseUtilKt.toLong(seconds) * 1000;
        return format(timeMillis, format);
    }

    public static String format(long milliseconds, DateFormat format) {
        return format(new Date(milliseconds), format);
    }

    public static String format(Date date, DateFormat format) {
        return format.format(date);
    }

    /**
     * 传入毫秒数获取距离当前时间的间隔数
     *
     * @param context    当前上下文
     * @param timeMillis 日期时间毫秒数
     * @return 如：1天前，1年前等
     */
    public static String format(Context context, long timeMillis) {
        long delta = System.currentTimeMillis() - timeMillis;
        return toFormat(context, delta);
    }

    /**
     * 传入日期{@link Date}获取距离当前时间的间隔数
     *
     * @param date 日期{@link Date}
     * @return 如：1天前，1年前等
     */
    public static String format(Date date) {
        return format(null, date);
    }

    /**
     * 传入日期{@link Date}获取距离当前时间的间隔数
     *
     * @param context 当前上下文
     * @param date    日期{@link Date}
     * @return 如：1天前，1年前等
     */
    public static String format(Context context, Date date) {
        long delta = System.currentTimeMillis() - date.getTime();
        return toFormat(context, delta);
    }

    private static String toFormat(Context context, long delta) {
        StringBuilder dateFormat = new StringBuilder();
        if (delta < ONE_MINUTE) {
            long seconds = toSeconds(delta);
            dateFormat.append((seconds <= 0 ? 1 : seconds));
            dateFormat.append(" ");
            if (context == null) {
                dateFormat.append((seconds <= 1 ? SECOND : SECONDS));
            } else {
                dateFormat.append((seconds <= 1 ? context.getString(R.string.second) : context.getString(R.string.seconds)));
            }
        } else if (delta < ONE_HOUR) {
            long minutes = toMinutes(delta);
            dateFormat.append((minutes <= 0 ? 1 : minutes));
            dateFormat.append(" ");
            if (context == null) {
                dateFormat.append((minutes <= 1 ? MINUTE : MINUTES));
            } else {
                dateFormat.append((minutes <= 1 ? context.getString(R.string.minute) : context.getString(R.string.minutes)));
            }
        } else if (delta < ONE_DAY) {
            long hours = toHours(delta);
            dateFormat.append((hours <= 0 ? 1 : hours));
            dateFormat.append(" ");
            if (context == null) {
                dateFormat.append((hours <= 1 ? HOUR : HOURS));
            } else {
                dateFormat.append((hours <= 1 ? context.getString(R.string.hour) : context.getString(R.string.hours)));
            }
        } else if (delta < ONE_MONTH) {
            long days = toDays(delta);
            dateFormat.append((days <= 0 ? 1 : days));
            dateFormat.append(" ");
            if (context == null) {
                dateFormat.append((days <= 1 ? DAY : DAYS));
            } else {
                dateFormat.append((days <= 1 ? context.getString(R.string.day) : context.getString(R.string.days)));
            }
        } else if (delta < ONE_YEAR) {
            long months = toMonths(delta);
            dateFormat.append((months <= 0 ? 1 : months));
            dateFormat.append(" ");
            if (context == null) {
                dateFormat.append((months <= 1 ? MONTH : MONTHS));
            } else {
                dateFormat.append((months <= 1 ? context.getString(R.string.month) : context.getString(R.string.months)));
            }
        } else {
            long years = toYears(delta);
            dateFormat.append((years <= 0 ? 1 : years));
            dateFormat.append(" ");
            if (context == null) {
                dateFormat.append((years <= 1 ? YEAR : YEARS));
            } else {
                dateFormat.append((years <= 1 ? context.getString(R.string.year) : context.getString(R.string.years)));
            }
        }
        dateFormat.append(" ");
        dateFormat.append(context == null ? AGO : context.getString(R.string.ago));
        return dateFormat.toString();
    }

    private static long toSeconds(long date) {
        return date / ONE_SECOND;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toWeeks(long date) {
        return toDays(date) / 7L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 12L;
    }

    /**
     * 将时间秒数据转成  19:30 Jan. 20 2016格式
     *
     * @author dingpeihua
     * @date 2016/7/11 11:10
     * @version 1.0
     */
    public static String toFormatUS(long delta) {
        delta *= 1000;
        Date date = new Date(delta);
        return DEFAULT_DATE_FORMAT_US.format(date);
    }

    /**
     * 将时间秒数据转成  19:30 Jan. 20 2016格式
     *
     * @author dingpeihua
     * @date 2016/7/11 11:10
     * @version 1.0
     */
    public static String toFormatTimeUS(long delta) {
        delta *= 1000;
        Date date = new Date(delta);
        return DEFAULT_DATE_FORMAT_TIME_US.format(date);
    }

    public static String toFormatUS(String delta) {
        return toFormatUS(ParseUtilKt.toLong(delta));
    }
}
