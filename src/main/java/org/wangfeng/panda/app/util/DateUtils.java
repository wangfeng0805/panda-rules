package org.wangfeng.panda.app.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Created by wangfeng.
 */
public class DateUtils {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
    public static final String YYYY_MM_FORMAT = "yyyy-MM";
    public static final String MMM_YY_FORMAT = "MMM-yy";
    public static final String YYYYMMDD = "YYYYMMdd";


    private static final String DAY = "天";
    private static final String HOUR = "小时";
    private static final String MINUTE = "分";
    private static final String SECOND = "秒";

    /**
     * 根据字符串和格式获取对应日期
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static Date getDate(String dateString, String pattern) {
        if (StringUtils.isEmpty(dateString) || StringUtils.isEmpty(pattern) || dateString.length()!=pattern.length()) {
            return null;
        }
        Date date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.UK);
            date = sdf.parse(dateString);
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    /**
     * 根据日期和格式获取对应字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getString(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        String str;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
            str = sdf.format(date);
        } catch (Exception e) {
            str = null;
        }
        return str;
    }

    /**
     * 把默认类型的string专程指定pattern的string
     * @param defaultDateString
     * @param pattern
     * @return
     */
    public static String getStringFromDefault(String defaultDateString, String pattern){
        Date date = getDate(defaultDateString,DEFAULT_FORMAT);
        String str = getString(date,pattern);
        return str;
    }

    /**
     * 根据字符串获取默认格式的日期
     *
     * @param dateString
     * @return
     */
    public static Date getDate(String dateString) {
        return getDate(dateString, DEFAULT_FORMAT);
    }

    /**
     * 根据字符串获取 yyyy-MM-dd 格式的日期
     *
     * @param dateString
     * @return
     */
    public static Date getYMDDate(String dateString) {
        return getDate(dateString, YYYY_MM_DD_FORMAT);
    }

    /**
     * 根据字符串获取 yyyy-MM 格式的日期
     *
     * @param dateString
     * @return
     */
    public static Date getYMDate(String dateString) {
        return getDate(dateString, YYYY_MM_FORMAT);
    }

    /**
     * 根据字符串获取 MMM-yy 格式的日期
     *
     * @param dateString
     * @return
     */
    public static Date getMYDate(String dateString) {
        return getDate(dateString, MMM_YY_FORMAT);
    }

    /**
     * 根据日期获取默认格式的字符串
     *
     * @param date
     * @return
     */
    public static String getString(Date date) {
        return getString(date, DEFAULT_FORMAT);
    }

    /**
     * 根据日期获取 yyyy-MM-dd 格式的字符串
     *
     * @param date
     * @return
     */
    public static String getYMDString(Date date) {
        return getString(date, YYYY_MM_DD_FORMAT);
    }

    /**
     * 根据日期获取 yyyy-MM 格式的字符串
     *
     * @param date
     * @return
     */
    public static String getYMString(Date date) {
        return getString(date, YYYY_MM_FORMAT);
    }

    /**
     * 根据日期获取 MMM-yy 格式的字符串
     *
     * @param date
     * @return
     */
    public static String getMYString(Date date) {
        return getString(date, MMM_YY_FORMAT);
    }

    /**
     * 获得某日期的年份
     *
     * @param date
     * @return
     */
    public static Integer getYear(Date date) {
        if (date==null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获得某日期的月份
     *
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        if (date==null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期为当月的第几天
     *
     * @param date
     * @return
     */
    public static Integer getDay(Date date) {
        if (date==null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 以年为纬度,获取相对日期
     *
     * @param date
     * @param offset
     * @return
     */
    public static Date getRelativeDateByYear(Date date, int offset) {
        if (date==null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, offset);
        return cal.getTime();
    }

    /**
     * 以月为纬度,获取相对日期
     *
     * @param date
     * @param offset
     * @return
     */
    public static Date getRelativeDateByMonth(Date date, int offset) {
        if (date==null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, offset);
        return cal.getTime();
    }

    /**
     * 以天为纬度,获取相对日期
     *
     * @param date
     * @param offset
     * @return
     */
    public static Date getRelativeDateByDay(Date date, int offset) {
        if (date==null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, offset);
        return cal.getTime();
    }


    /**
     * 通过时分秒为纬度,获取相对日期
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date getRelativeDateByHMS(Date date, int hour ,int minute , int second) {
        if (date==null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        cal.add(Calendar.MINUTE, minute);
        cal.add(Calendar.SECOND, second);
        return cal.getTime();
    }

    /**
     * 获取时间差
     *
     * @param front
     * @param back
     * @return
     */
    public static String getBetweenTime(Date front, Date back) {
        if (null == front || null == back) {
            return "";
        }

        long l = -1;
        StringBuilder sb = new StringBuilder();

        if (back.before(front)) {
            l = front.getTime() - back.getTime();
            sb.append("-");
        } else {
            l = back.getTime() - front.getTime();
        }

        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long minute = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (day > 0) {
            sb.append(day).append(DAY);
        }
        if (hour > 0) {
            sb.append(hour).append(HOUR);
        }
        if (minute > 0) {
            sb.append(minute).append(MINUTE);
        }
        if (second > 0) {
            sb.append(second).append(SECOND);
        }

        return sb.toString();
    }


    public static Date getYesterdayLastTime(Date date){
        if(date==null){
            return null;
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 23);
        cal.set(GregorianCalendar.MINUTE, 59);
        cal.set(GregorianCalendar.SECOND, 59);
        cal.set(GregorianCalendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static Date getYesterdayFirstTime(Date date){
        if(date==null){
            return null;
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        return cal.getTime();
    }


    /**
     * 得到指定时间的当天零点
     * @param date
     * @return
     */
    public static Date getDateNoTime(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 得到指定时间的当月第一天零点
     * @param date
     * @return
     */
    public static Date getFirstDateOfMonthNoTime(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        //设置天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public static Date getNextYearFirstDay() {
        Calendar cal = GregorianCalendar.getInstance();
        //设置年份
        cal.add(Calendar.YEAR,1);
        //设置月份
        cal.set(Calendar.MONTH, 0);
        //设置天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getFirstDayThisMonth() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();

    }


    /**
     * 查看两个日期之间相差的天数
     * @return
     */
    public static Integer daysBetween(Date fromDate , Date toDate){
        Date begin = getDateNoTime(fromDate);
        Date end = getDateNoTime(toDate);
        int days = (int) ((end.getTime() - begin.getTime()) / (1000*3600*24));
        return days;
    }

    /**
     * 查看两个日期之间相差的月数
     * @return
     */
    public static Integer monthsBetween(Date fromDate , Date toDate){
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(fromDate);
        aft.setTime(toDate);
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;

        return month + result;
    }


}