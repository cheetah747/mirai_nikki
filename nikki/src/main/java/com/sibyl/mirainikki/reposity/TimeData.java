package com.sibyl.mirainikki.reposity;

import android.text.TextUtils;

import com.sibyl.mirainikki.activity.MainActivity.helper.PreferHelper;

import java.util.Calendar;

/**
 * Created by Sasuke on 2016/5/10.
 */
public class TimeData {
    final public static String LAST_MONTH = "LAST_MONTH";
    final public static String LAST_WEEK_OF_YEAR = "LAST_WEEK_OF_YEAR";//上次是一年中的第几周
    final public static String LAST_DAY = "LAST_DAY";
    final public static String LAST_YEAR = "LAST_YEAR";
    final public static String LAST_TIME= "LAST_TIME";

//    public static Calendar calendar;
    public static String date;// 2017-03-01-【水】
    public static String time;// hour+"時"+minute+"分"
    public static String weekOfYear;//是一年的第几周（用于比较是否是同一周
    public static String yearMonth;// 如yyyy-MM格式的年月份字符串
    /**
     * 初始化上面各时间值
     */
    public static void initNow(){
//        calendar = Calendar.getInstance();
        date = makeDate();
        time = makeTime();
        weekOfYear = makeWeekOfYear();
    }

    /**
     * 计算日期
     */
    public static String makeDate(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int monthNum = Calendar.getInstance().get(Calendar.MONTH)+1;//月份
        String month = String.format("%02d",monthNum);//使成为两位数，不足的话前面补零
        int dayNum = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);//日期
        String day = String.format("%02d",dayNum);
        int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);//星期

        String weekdayStr = "";
        switch (weekday){
            case Calendar.SUNDAY:
                weekdayStr = "【日】";
                break;
            case Calendar.MONDAY:
                weekdayStr = "月";
                break;
            case Calendar.TUESDAY:
                weekdayStr = "火";
                break;
            case Calendar.WEDNESDAY:
                weekdayStr = "水";
                break;
            case Calendar.THURSDAY:
                weekdayStr = "木";
                break;
            case Calendar.FRIDAY:
                weekdayStr = "金";
                break;
            case Calendar.SATURDAY:
                weekdayStr = "【土】";
                break;
        }

        yearMonth = year+"-"+month;
        return year+"-"+ month +"-"+ day+"-"+weekdayStr;
    }

    /**
     * 计算时刻
     */
    public static String makeTime(){
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);

        return hour+"時"+minute+"分";
    }

    /**
     * 计算日期是一年中的第几周
     * */
    public static String makeWeekOfYear(){
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + "";
    }

    public static String getWeekOfYear() {
        return weekOfYear;
    }

    /**
     * 获取日期字符串
     * @return
     */
    public static String getDate() {
        return date;
    }

    /**
     * 获取当前时刻字符串
     * @return
     */
    public static String getTime() {
        return time;
    }

    /**
     * 获取格式为yyyy-MM的 年月 字符串，用来创建月份文件夹
     * @return
     */
    public static String getYearMonth(){
        if(TextUtils.isEmpty(yearMonth)){
            makeDate();
        }
        return yearMonth;
    }

    /**获取年份*/
    public static String getYear(){
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    /**保存所有日期标记到prefs*/
    public static void saveAllDatePrefs(){
        PreferHelper.getInstance().setString(LAST_DAY,getDate());
        PreferHelper.getInstance().setString(LAST_MONTH,getYearMonth());
        PreferHelper.getInstance().setString(LAST_WEEK_OF_YEAR,getWeekOfYear());
        PreferHelper.getInstance().setString(LAST_YEAR,getYear());
    }

    /**保存所有日期标记到prefs*/
    public static void saveAllDatePrefs(String date,String yearMonth,String weekOfYear,String year,String time){
        PreferHelper.getInstance().setString(LAST_DAY,date);
        PreferHelper.getInstance().setString(LAST_MONTH,yearMonth);
        PreferHelper.getInstance().setString(LAST_WEEK_OF_YEAR,weekOfYear);
        PreferHelper.getInstance().setString(LAST_YEAR,year);
        PreferHelper.getInstance().setString(LAST_TIME,time);
    }

}
